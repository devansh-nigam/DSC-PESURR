package com.example.dscpesurr

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebViewClient
import kotlinx.android.synthetic.main.activity_google_play_services.*

class GooglePlayServicesActivity : AppCompatActivity() {
    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_google_play_services)

        webView.webViewClient= WebViewClient()
        webView.apply{
            loadUrl("https://www.google.com/policies/privacy/")
            settings.javaScriptEnabled=true
            settings.safeBrowsingEnabled=true
        }

    }
}