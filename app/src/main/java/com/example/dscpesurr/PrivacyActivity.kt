package com.example.dscpesurr

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import kotlinx.android.synthetic.main.activity_privacy.*

class PrivacyActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_privacy)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        link_playservices.setOnClickListener {
            val intent= Intent(this,GooglePlayServicesActivity::class.java)
            startActivity(intent)
        }

        link_firebase.setOnClickListener {
            val intent= Intent(this,FirebasePrivacyActivity::class.java)
            startActivity(intent)
        }
    }
}