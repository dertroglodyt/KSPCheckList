package de.hdc.kspchecklist

import android.os.Bundle
import android.preference.PreferenceActivity

/**
 * Created by DerTroglodyt on 2017-02-17 12:30.
 * Email dertroglodyt@gmail.com
 * Copyright by HDC, Germany
 */

class SettingsActivity : PreferenceActivity() { // implements Preference.OnPreferenceChangeListener {

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Display the fragment as the main content.
        fragmentManager.beginTransaction()
            .replace(android.R.id.content, SettingsFragment())
            .commit()
        //        Preference aktienlistePref = findPreference(getString(R.string.preference_theme_key));
        //        aktienlistePref.setOnPreferenceChangeListener(this);
    }

    //    @Override
    //    public boolean onPreferenceChange(Preference preference, Object o) {
    //        return false;
    //    }
}


