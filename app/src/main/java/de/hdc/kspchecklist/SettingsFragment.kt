package de.hdc.kspchecklist

import android.os.Bundle
import android.preference.PreferenceFragment

/**
 * Created by DerTroglodyt on 2018-01-16 10:47.
 * Email dertroglodyt@gmail.com
 * Copyright by HDC, Germany
 */

class SettingsFragment : PreferenceFragment() { // implements Preference.OnPreferenceChangeListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        addPreferencesFromResource(R.xml.settings)
        //        Preference aktienlistePref = findPreference(getString(R.string.preference_theme_key));
        //        aktienlistePref.setOnPreferenceChangeListener(this);
    }

    //    @Override
    //    public boolean onPreferenceChange(Preference preference, Object o) {
    //        return false;
    //    }
}
