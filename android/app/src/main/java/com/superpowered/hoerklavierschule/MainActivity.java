package com.superpowered.hoerklavierschule;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.media.AudioManager;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.pm.PackageManager;

import java.io.File;
import java.io.IOException;
import android.os.Build;
import android.support.annotation.NonNull;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Button;
import android.widget.Toast;
import android.Manifest;

public class MainActivity extends AppCompatActivity {
    private boolean playing = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Checking permissions.
        String[] permissions = {
                Manifest.permission.MODIFY_AUDIO_SETTINGS
        };
        for (String s:permissions) {
            if (ContextCompat.checkSelfPermission(this, s) != PackageManager.PERMISSION_GRANTED) {
                // Some permissions are not granted, ask the user.
                ActivityCompat.requestPermissions(this, permissions, 0);
                return;
            }
        }
        // Got all permissions, initialize.
        initialize();

    }

    //open file selector
    //TODO does not work in KitKat?
    public void performFileSearch(View button) {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("audio/*");
        startActivityForResult(intent, 42);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode,
                                 Intent resultData) {
        if (requestCode == 42 && resultCode == Activity.RESULT_OK) {
            Uri uri = null;
            if (resultData != null) {
                uri = resultData.getData();
                //TODO handle file
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        // Called when the user answers to the permission dialogs.
        if ((requestCode != 0) || (grantResults.length < 1) || (grantResults.length != permissions.length)) return;
        boolean hasAllPermissions = true;

        for (int grantResult:grantResults) if (grantResult != PackageManager.PERMISSION_GRANTED) {
            hasAllPermissions = false;
            Toast.makeText(getApplicationContext(), "Please allow all permissions for the app.", Toast.LENGTH_LONG).show();
        }

        if (hasAllPermissions) initialize();
    }

    private void initialize() {
        // Get the device's sample rate and buffer size to enable
		// low-latency Android audio output, if available.
        String samplerateString = null, buffersizeString = null;
        if (Build.VERSION.SDK_INT >= 17) {
            AudioManager audioManager = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
            if (audioManager != null) {
                samplerateString = audioManager.getProperty(AudioManager.PROPERTY_OUTPUT_SAMPLE_RATE);
                buffersizeString = audioManager.getProperty(AudioManager.PROPERTY_OUTPUT_FRAMES_PER_BUFFER);
            }
        }
        if (samplerateString == null) samplerateString = "48000";
        if (buffersizeString == null) buffersizeString = "480";
        int samplerate = Integer.parseInt(samplerateString);
        int buffersize = Integer.parseInt(buffersizeString);

        // Files under res/raw are not zipped, just copied into the APK.
		// Get the offset and length to know where our files are located.
        String apkPath = getPackageResourcePath();
        AssetFileDescriptor fd0 = getResources().openRawResourceFd(R.raw.links);
		AssetFileDescriptor fd1 = getResources().openRawResourceFd(R.raw.rechts);

        int fileAoffset = (int)fd0.getStartOffset();
		int fileAlength = (int)fd0.getLength();
		int fileBoffset = (int)fd1.getStartOffset();
		int fileBlength = (int)fd1.getLength();

        try {
            fd0.getParcelFileDescriptor().close();
            fd1.getParcelFileDescriptor().close();
        } catch (IOException e) {
            android.util.Log.d("", "Close error.");
        }

        // Initialize the players and effects, and start the audio engine.
        System.loadLibrary("SyncPlay");
        SyncPlay(
                samplerate,     // sampling rate
                buffersize,     // buffer size
                apkPath,        // path to .apk package
			    fileAoffset,    // offset (start) of file A in the APK
			    fileAlength,    // length of file A
			    fileBoffset,    // offset (start) of file B in the APK
			    fileBlength     // length of file B
        );

        // Setup crossfader events
        final SeekBar crossfader = findViewById(R.id.crossFader);
        crossfader.setProgress(50);
        if (crossfader != null) crossfader.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                onCrossfader(progress);
            }
            public void onStartTrackingTouch(SeekBar seekBar) {}
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });



    }

    // PlayPause - Toggle playback state of the player.
    public void Toggle_PlayPause(View button) {
        playing = !playing;
        onPlayPause(playing);
        Button b = findViewById(R.id.playPause);
        if (b != null) b.setText(playing ? "Pause" : "Play");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //stop audio playback when app is in background
    @Override
    protected void onPause() {
        super.onPause();
        playing=false;
        onPlayPause(playing);
        Button b = findViewById(R.id.playPause);
        if (b != null) b.setText("Play");
    }
 /*
    //resume audio when app is in foreground
    @Override
    protected void onResume() {
        super.onResume();
        onPlayPause(true);
    }
*/
    // Functions implemented in the native library.
    private native void SyncPlay(int samplerate, int buffersize, String apkPath, int fileAoffset, int fileAlength, int fileBoffset, int fileBlength);
    private native void onPlayPause(boolean play);
    private native void onCrossfader(int value);
}
