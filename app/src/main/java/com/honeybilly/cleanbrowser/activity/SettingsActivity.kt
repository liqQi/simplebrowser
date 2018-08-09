package com.honeybilly.cleanbrowser.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.honeybilly.cleanbrowser.R
import kotlinx.android.synthetic.main.activity_setting.*

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)
        back.setOnClickListener { onBackPressed() }
        fragmentManager.beginTransaction().replace(R.id.container, SettingFragment()).commitAllowingStateLoss()
    }


}
