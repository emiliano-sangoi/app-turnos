package com.example.emiliano.appturnos.config;

import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.example.emiliano.appturnos.R;

@SuppressWarnings("deprecation")
public class PreferenciasActivity extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferencias);


    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_OK);
        finish();

        super.onBackPressed();
    }
}
