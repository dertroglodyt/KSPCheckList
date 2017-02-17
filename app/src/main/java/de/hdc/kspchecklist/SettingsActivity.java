package de.hdc.kspchecklist;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.support.annotation.Nullable;

/**
 * Created by DerTroglodyt on 2017-02-17 12:30.
 * Email dertroglodyt@gmail.com
 * Copyright by HDC, Germany
 */

public class SettingsActivity extends PreferenceActivity implements Preference.OnPreferenceChangeListener {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.settings);
//        Preference aktienlistePref = findPreference(getString(R.string.preference_theme_key));
//        aktienlistePref.setOnPreferenceChangeListener(this);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object o) {
        return false;
    }
}


