package com.honeybilly.cleanbrowser.activity

import android.os.Bundle
import android.preference.PreferenceFragment
import com.honeybilly.cleanbrowser.R

/**
 * Created by liqi on 10:34.
 */
class SettingFragment : PreferenceFragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addPreferencesFromResource(R.xml.preference)
        preferenceManager.findPreference(getString(R.string.key_home_page_url)).summary = WebViewFragment.HOME_URL
    }
}

