package com.example.dscpesurr

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebViewClient
import kotlinx.android.synthetic.main.activity_google_play_services.*

class FirebasePrivacyActivity : AppCompatActivity() {
    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_firebase_privacy)

        webView.webViewClient= WebViewClient()
        webView.apply{
            loadUrl("https://firebase.google.com/policies/analytics")
            settings.javaScriptEnabled=true
            settings.safeBrowsingEnabled=true
        }
    }
}