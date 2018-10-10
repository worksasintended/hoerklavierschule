package com.superpowered.hoerklavierschule;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SeekBar;

public class AudioPlayFragment extends Fragment {

    public DrawerLayout drawerLayout;

    public WaveFormView waveFormView_first;
    public WaveFormView waveFormView_second;

    public SeekBar seekBar_crossfader;
    public SeekBar seekBar_tempo;
    public SeekBar seekBar_pitch;
    public SeekBar seekBar_pitchCents;

    public ImageButton imageButton_audio_play;

    private boolean playing = false;

    private OnFragmentInteractionListener mListener;

    public AudioPlayFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_audio_play, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        Toolbar toolbar = getView().findViewById(R.id.toolbar_audio_play);
        drawerLayout = getView().findViewById(R.id.drawer_layout);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        ActionBar actionbar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);

        // WaveFormView
        waveFormView_first = getView().findViewById(R.id.waveFormView_first);
        waveFormView_second = getView().findViewById(R.id.waveFormView_second);

        for (int i = 0; i < 10; i++) {
            waveFormView_first.addAmplitude((float) 300);
            waveFormView_second.addAmplitude((float) 300);
        }
        for (int i = 0; i < 10; i++) {
            waveFormView_first.addAmplitude((float) 3000);
            waveFormView_second.addAmplitude((float) 300);
        }

        waveFormView_first.invalidate();
        waveFormView_first.postInvalidate();
        waveFormView_second.invalidate();
        waveFormView_second.postInvalidate();

        // Buttons
        imageButton_audio_play = getView().findViewById(R.id.imageButton_audio_play);
        imageButton_audio_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClick_audio_play(v);
            }
        });


        setupCrossfader(view);
        setupTempo(view);
        setupPitch(view);
        setupPitchCents(view);
    }

    /**setup the View for the crossfader
     *
     * @param view
     */
    private void setupCrossfader(View view) {
        seekBar_crossfader = view.findViewById(R.id.seekBar_crossfader);

        seekBar_crossfader.setProgress(50);
        seekBar_crossfader.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                onCrossfader(progress);
            }
            public void onStartTrackingTouch(SeekBar seekBar) {}
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    }

    /**setup the View for tempo
     *
     * @param view
     */
    private void setupTempo(View view) {
        seekBar_tempo = view.findViewById(R.id.seekBar_tempo);
        seekBar_tempo.setProgress(40);
        if (seekBar_tempo != null) seekBar_tempo.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                setTempo(progress);
            }
            public void onStartTrackingTouch(SeekBar seekBar) {}
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    }

    /**setup the View for pitch halfs
     *
     * @param view
     */
    private void setupPitch(View view) {
        seekBar_pitch = view.findViewById(R.id.seekBar_pitch);
        seekBar_pitch.setProgress(11);

        if (seekBar_pitch != null) seekBar_pitch.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                pitch(progress - 11);
            }
            public void onStartTrackingTouch(SeekBar seekBar) {}
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    }

    /**setup the View for pitchCents
     *
     * @param view
     */
    private void setupPitchCents(View view) {
        seekBar_pitchCents = view.findViewById(R.id.seekBar_pitchCents);
        seekBar_pitchCents.setProgress(50);
        if (seekBar_pitchCents != null)
            seekBar_pitchCents.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    pitchCents(progress);
                }

                public void onStartTrackingTouch(SeekBar seekBar) {
                }

                public void onStopTrackingTouch(SeekBar seekBar) {
                }
            });
    }

    // PlayPause - Toggle playback state of the player.
    public void onClick_audio_play(View button) {
        playing = !playing;
        onPlayPause(playing);

        if (imageButton_audio_play != null) {

        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);

                return true;
            default:

                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /** Functions implemented in the native library.
     *
     */
    private native void onPlayPause(boolean play);
    private native void onCrossfader(int value);
    private native void setTempo(int tempo);
    private native void pitchCents(int pitchCents);
    private native void pitch(int pitch);

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
