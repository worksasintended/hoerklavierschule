package com.superpowered.hoerklavierschule;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.superpowered.hoerklavierschule.sql.Piece;
import com.superpowered.hoerklavierschule.sql.Settings;

import java.io.IOException;

public class MainActivity extends AppCompatActivity implements AudioPlayFragment.OnFragmentInteractionListener, AudioItemFragment.OnListFragmentInteractionListener, NavigationView.OnNavigationItemSelectedListener {
    public DrawerLayout drawerLayout;
    public NavigationView navigationView;
    public Toolbar toolbar;

    public Context context;
    public Settings settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        restoreTheme();
        super.onCreate(savedInstanceState);

        context = getApplicationContext();
        settings = new Settings(context);

        setView();
        restoreSettings();
        checkPermissions();
        initView();
        setToolbar();
        setListener();

        initialize();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        // Called when the user answers to the permission dialogs.
        if ((requestCode != 0) || (grantResults.length < 1) || (grantResults.length != permissions.length))
            return;
        boolean hasAllPermissions = true;

        for (int grantResult : grantResults)
            if (grantResult != PackageManager.PERMISSION_GRANTED) {
                hasAllPermissions = false;
                Toast.makeText(getApplicationContext(), "Please allow all permissions for the app.", Toast.LENGTH_LONG).show();
            }

        // if (hasAllPermissions) initialize();
    }

    /**
     * handle a click on the navigation menu
     * @param item
     * @return
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        switch (id) {

            case R.id.nav_pieces:
                show_AudioItem_Fragment(true);
                break;

             case R.id.nav_piece:
                show_AudioPlay_Fragment(true);
                    break;
        }

        //* close navigation drawer
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * onStart call, get settings like saved instance
     */
    public void restoreSettings () {
        settings.clearSavedFragment();
    }

    private void setView() {
        setContentView(R.layout.activity_main);

        if(settings.onStart_showAudio()) {
            show_AudioPlay_Fragment(true);
        }
        else if (settings.onStart_showList()) {
            show_AudioItem_Fragment(true);
        }
    }

    /**
     * check for all required permissions
     */
    private void checkPermissions() {
        String[] permissions = {
                Manifest.permission.MODIFY_AUDIO_SETTINGS
        };
        for (String s : permissions) {
            if (ContextCompat.checkSelfPermission(this, s) != PackageManager.PERMISSION_GRANTED) {
                // Some permissions are not granted, ask the user.
                ActivityCompat.requestPermissions(this, permissions, 0);
                return;
            }
        }
    }

    private void initialize() {
        // Get the device's sample rate and buffer size to enable
        // low-latency Android audio output, if available.
        String samplerateString = null, buffersizeString = null;
        if (Build.VERSION.SDK_INT >= 17) {
            AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
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
        int fileAoffset = (int) fd0.getStartOffset();
        int fileAlength = (int) fd0.getLength();
        int fileBoffset = (int) fd1.getStartOffset();
        int fileBlength = (int) fd1.getLength();
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
    }

    private void restoreTheme() {
        setTheme(R.style.Theme_AppCompat_Light_NoActionBar);
    }

    private void initView() {
        drawerLayout = findViewById(R.id.main_drawer_layout);
        navigationView = findViewById(R.id.nav_main);
        toolbar = findViewById(R.id.toolbar_main);
    }

    private void setToolbar() {
        setSupportActionBar(toolbar);

        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
    }

    private void setListener() {
         navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    /**
     * handle the clicks on the menu
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);

                return true;
            default:

                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void show_AudioPlay_Fragment(Boolean firstTime) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();

        AudioPlayFragment fragment = new AudioPlayFragment();

        if (firstTime) {
            transaction.add(R.id.activity_main, fragment, "AUDIO_PLAY_FRAGMENT");
        }
        else {
            transaction.replace(R.id.activity_main, fragment, "AUDIO_PLAY_FRAGMENT");
        }

        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void show_AudioItem_Fragment(Boolean firstTime) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();

        Fragment fragment = new AudioItemFragment();

        if (firstTime) {
            transaction.add(R.id.activity_main, fragment, "");
        }
        else {
            transaction.replace(R.id.activity_main, fragment, "");
        }

        transaction.addToBackStack(null);
        transaction.commit();
    }


    @Override
    public void onListFragmentInteraction(Piece piece) {

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onPause() {
        AudioPlayFragment audioPlayFragment = (AudioPlayFragment)getSupportFragmentManager().findFragmentByTag("AUDIO_PLAY_FRAGMENT");
        AudioItemFragment audioItemFragment = (AudioItemFragment)getSupportFragmentManager().findFragmentByTag("AUDIO_ITEM_FRAGMENT");

        Log.d("onPause", "saving current state...");

        if (audioPlayFragment != null && audioPlayFragment.isVisible()) {
            Log.d("Fragment", "AudioPlayFragment is visible: true");n

            settings.set_onStart_showAudio(true);
        }
        else if (audioItemFragment != null && audioItemFragment.isVisible()) {
            Log.d("Fragment", "AudioItemFragment is visible: true");

            settings.set_onStart_showList(true);
        }

        Log.d("onPause", "saving current state... [OK]");

        super.onPause();
    }

    private native void SyncPlay(int samplerate, int buffersize, String apkPath, int fileAoffset, int fileAlength, int fileBoffset, int fileBlength);
}
