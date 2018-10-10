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

    public Settings(Context context) {
        this.context = context;
        sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public boolean onStart_showAudio() {


        return true;
    }

    public void test() {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean("TEST", true);
        editor.commit();
    }
}
