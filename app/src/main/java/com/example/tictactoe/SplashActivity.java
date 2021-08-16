package com.example.tictactoe;


import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        PreferenceManager.setDefaultValues(this, R.xml.root_preferences, false);
        Utils.setNightModeOnOffFromPreferenceValue(getApplicationContext(), getString(R.string.night_mode_key));



        startActivity (new Intent(getApplicationContext (), MainActivity.class));
        finish ();
    }
}