package com.superpowered.hoerklavierschule.sql;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

/**
 * settings
 */
public class Settings {
    private Context context;
    SharedPreferences sharedPref;

    public static final String ONSTART_SHOW_FRAGMENT = "ONSTART_SHOW_FRAGMENT";
    public static final String AUDIO_PLAY_FRAGMENT = "AUDIO_PLAY_FRAGMENT";
    public static final String AUDIO_ITEM_FRAGMENT = "AUDIO_ITEM_FRAGMENT";

    public Settings(Context context) {
        this.context = context;
        sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
    }

    /**
     * get the string representation of last called fragment
     * @return
     */
    public String onStart_getFragment() {
        String fragmentToShow = sharedPref.getString(ONSTART_SHOW_FRAGMENT, "NONE");

        return fragmentToShow;
    }

    public void set_onStart_showAudioPlayFragment() {
        Log.d("Settings", "setting AudioPlayFragment as last called fragment");

        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(ONSTART_SHOW_FRAGMENT, AUDIO_PLAY_FRAGMENT);

        editor.apply();
    }

    public void set_onStart_showAudioItemFragment() {
        Log.d("Settings", "setting AudioItemFragment as last called fragment");

        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(ONSTART_SHOW_FRAGMENT, AUDIO_ITEM_FRAGMENT);

        editor.apply();
    }
}
