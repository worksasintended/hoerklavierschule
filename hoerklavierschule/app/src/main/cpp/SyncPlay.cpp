#include "SyncPlay.h"
#include <SuperpoweredSimple.h>
#include <SuperpoweredCPU.h>
#include <jni.h>
#include <stdio.h>
#include <stdlib.h>
#include <SLES/OpenSLES.h>
#include <SLES/OpenSLES_AndroidConfiguration.h>

#define log_print __android_log_print

// This is called by player A upon successful load.
static void playerEventCallbackA (
	void *clientData,   // &playerA
	SuperpoweredAdvancedAudioPlayerEvent event,
	void * __unused value
) {
    if (event == SuperpoweredAdvancedAudioPlayerEvent_LoadSuccess) {
        // The pointer to the player is passed to the event callback via the custom clientData pointer.
    	SuperpoweredAdvancedAudioPlayer *playerA = *((SuperpoweredAdvancedAudioPlayer **)clientData);
    };
}

static void playerEventCallbackB(
	void *clientData,   // &playerB
	SuperpoweredAdvancedAudioPlayerEvent event,
	void * __unused value
) {
    if (event == SuperpoweredAdvancedAudioPlayerEvent_LoadSuccess) {
       	SuperpoweredAdvancedAudioPlayer *playerB = *((SuperpoweredAdvancedAudioPlayer **)clientData);
    };
}

// Audio callback function. Called by the audio engine.
static bool audioProcessing (
	void *clientdata,		    // A custom pointer your callback receives.
	short int *audioIO,		    // 16-bit stereo interleaved audio input and/or output.
	int numFrames,			    // The number of frames received and/or requested.
	int __unused samplerate	    // The current sample rate in Hz.
) {
	return ((SyncPlay *)clientdata)->process(audioIO, (unsigned int)numFrames);
}

//Initialize players and audio engine
SyncPlay::SyncPlay (
		unsigned int samplerate,    // sampling rate
		unsigned int buffersize,    // buffer size
        const char *path,           // path to APK package
		int fileAoffset,            // offset of file A in APK
		int fileAlength,            // length of file A
		int fileBoffset,            // offset of file B in APK
		int fileBlength             // length of file B
) : crossValue(0.0f), volB(.5f *headroom), volA(.5f * headroom)
{
    // Allocate aligned memory for floating point buffer.
    stereoBuffer = (float *)memalign(16, buffersize * sizeof(float) * 2);

    // Initialize players and open audio files.
    playerA = new SuperpoweredAdvancedAudioPlayer(&playerA, playerEventCallbackA, samplerate, 0);
    playerA->open(path, fileAoffset, fileAlength);
    playerB = new SuperpoweredAdvancedAudioPlayer(&playerB, playerEventCallbackB, samplerate, 0);
    playerB->open(path, fileBoffset, fileBlength);

    playerA->syncMode = playerB->syncMode = SuperpoweredAdvancedAudioPlayerSyncMode_TempoAndBeat;


    // Initialize audio engine and pass callback function.
    audioSystem = new SuperpoweredAndroidAudioIO (
			samplerate,                     // sampling rate
			buffersize,                     // buffer size
			false,                          // enableInput
			true,                           // enableOutput
			audioProcessing,                // audio callback function
			this,                           // clientData
			-1,                             // inputStreamType (-1 = default)
			SL_ANDROID_STREAM_MEDIA,        // outputStreamType (-1 = default)
			buffersize * 2                  // latency (frames)
	);
}

// Destructor. Free resources.
SyncPlay::~SyncPlay() {
    delete audioSystem;
    delete playerA;
    delete playerB;

    free(stereoBuffer);
}

// onPlayPause - Toggle playback state of players.
void SyncPlay::onPlayPause(bool play) {
    if (!play) {
        playerA->pause();
        playerB->pause();
    } else {
        bool masterIsA = (crossValue <= 0.5f);
        playerA->play(!masterIsA);
        playerB->play(masterIsA);
    };
    SuperpoweredCPU::setSustainedPerformanceMode(play); // <-- Important to prevent audio dropouts.
}

// onCrossfader - Handle crossfader and adjust volume levels accordingly.
void SyncPlay::onCrossfader(int value) {
    crossValue = float(value) * 0.01f;
    if (crossValue < 0.01f) {
        volA = 1.0f * headroom;
        volB = 0.0f;
    } else if (crossValue > 0.99f) {
        volA = 0.0f;
        volB = 1.0f * headroom;
    } else { // constant power curve
        volA = cosf(float(M_PI_2) * crossValue) * headroom;
        volB = cosf(float(M_PI_2) * (1.0f - crossValue)) * headroom;
    };
}



#define MINFREQ 60.0f
#define MAXFREQ 20000.0f

static inline float floatToFrequency(float value) {
    if (value > 0.97f) return MAXFREQ;
    if (value < 0.03f) return MINFREQ;
    value = powf(10.0f, (value + ((0.4f - fabsf(value - 0.4f)) * 0.3f)) * log10f(MAXFREQ - MINFREQ)) + MINFREQ;
    return value < MAXFREQ ? value : MAXFREQ;
}


// Main process function where audio is generated.
bool SyncPlay::process (
        short int *output,         // buffer to receive output samples
        unsigned int numFrames     // number of frames requested
) {
    bool masterIsA = (crossValue <= 0.5f);
    double masterBpm = masterIsA ? playerA->currentBpm : playerB->currentBpm;
    // When playerB needs it, playerA has already stepped this value, so save it now.
    double msElapsedSinceLastBeatA = playerA->msElapsedSinceLastBeat;

    // Request audio from player A.
    bool silence = !playerA->process (
            stereoBuffer,  // 32-bit interleaved stereo output buffer.
            false,         // bufferAdd - true: add to buffer / false: overwrite buffer
            numFrames,     // The number of frames to provide.
            volA,          // volume - 0.0f is silence, 1.0f is "original volume"
            masterBpm,     // BPM value to sync with.
            playerB->msElapsedSinceLastBeat // ms elapsed since the last beat on the other track.
    );

    // Request audio from player B.
    if (playerB->process(
            stereoBuffer,  // 32-bit interleaved stereo output buffer.
            !silence,      // bufferAdd - true: add to buffer / false: overwrite buffer
            numFrames,     // The number of frames to provide.
            volB,          // volume - 0.0f is silence, 1.0f is "original volume"
            masterBpm,     // BPM value to sync with.
            msElapsedSinceLastBeatA   // ms elapsed since the last beat on the other track.
    )) silence = false;


    // The stereoBuffer is ready now, let's write the finished audio into the requested buffers.
    if (!silence) SuperpoweredFloatToShortInt(stereoBuffer, output, numFrames);
    return !silence;
}

static SyncPlay *piano = NULL;

extern "C" JNIEXPORT void
Java_com_superpowered_hoerklavierschule_MainActivity_SyncPlay (
		JNIEnv *env,
		jobject __unused obj,
		jint samplerate,        // sampling rate
		jint buffersize,        // buffer size
		jstring apkPath,        // path to APK package
		jint fileAoffset,       // offset of file A in APK
		jint fileAlength,       // length of file A
		jint fileBoffset,       // offset of file B in APK
		jint fileBlength        // length of file B
) {
    const char *path = env->GetStringUTFChars(apkPath, JNI_FALSE);
    piano = new SyncPlay((unsigned int)samplerate, (unsigned int)buffersize,
			path, fileAoffset, fileAlength, fileBoffset, fileBlength);
    env->ReleaseStringUTFChars(apkPath, path);
}

// onPlayPause - Toggle playback state of player.
extern "C" JNIEXPORT void
Java_com_superpowered_hoerklavierschule_MainActivity_onPlayPause (
        JNIEnv * __unused env,
        jobject __unused obj,
        jboolean play
) {
	piano->onPlayPause(play);
}

// onCrossfader - Handle crossfader events.
extern "C" JNIEXPORT void
Java_com_superpowered_hoerklavierschule_MainActivity_onCrossfader (
        JNIEnv * __unused env,
        jobject __unused obj,
        jint value
) {
	piano->onCrossfader(value);
}


