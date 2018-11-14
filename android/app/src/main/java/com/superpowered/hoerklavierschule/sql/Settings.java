package com.superpowered.hoerklavierschule.sql;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;

/**
 * settings
 */
public class Settings {
    private Context context;
    SharedPreferences sharedPref;

    public static final String ONSTART_SHOW_AUDIO = "ONSTART_SHOW_AUDIO";
    public static final String ONSTART_SHOW_LIST = "ONSTART_SHOW_LIST";

    public Settings(Context context) {
        this.context = context;
        sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public boolean onStart_showAudio() {
        boolean showAudio = sharedPref.getBoolean(ONSTART_SHOW_AUDIO, false);

        return showAudio;
    }

    public boolean onStart_showList() {
        boolean showList = sharedPref.getBoolean(ONSTART_SHOW_LIST, false);

        return showList;
    }

    public void set_onStart_showAudio(boolean val) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(ONSTART_SHOW_AUDIO, val);

        editor.commit();
    }

    public void set_onStart_showList(boolean val) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(ONSTART_SHOW_LIST, val);

        editor.commit();
    }

    public void clearSavedFragment() {
        set_onStart_showAudio(false);
        set_onStart_showList(false);
    }
}
