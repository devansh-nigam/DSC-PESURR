package com.example.dscpesurr

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Toast
import android.widget.VideoView
import androidx.appcompat.app.AppCompatDelegate
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.firestoreSettings
import com.google.firebase.ktx.Firebase

@Suppress("DEPRECATION")
class SplashScreenActivity : AppCompatActivity()
{
    // declaring a null variable for VideoView
    var simpleVideoView: VideoView? = null
    private lateinit var mAuth:FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        supportActionBar?.hide()

        simpleVideoView = findViewById<View>(R.id.videoView) as VideoView

        // set the absolute path of the video file which is going to be played
        simpleVideoView!!.setVideoURI(
            Uri.parse("android.resource://"
                + packageName + "/" + R.raw.dscpesu))

        // starting the video
        simpleVideoView!!.start()

        mAuth= FirebaseAuth.getInstance();
        val user=mAuth.currentUser;

        val db = Firebase.firestore
        val settings = firestoreSettings {
            isPersistenceEnabled = true
        }
        db.firestoreSettings = settings

        Handler().postDelayed({

            if(user!=null)
            {
                db.collection("Users").document(user.uid).get()
                    .addOnSuccessListener { document ->
                        val filledAllDetails=document.get("filledAllDetails")
                        if(filledAllDetails==true){
                            val dashboardIntent= Intent(this@SplashScreenActivity,DashboardActivity::class.java)
                            startActivity(dashboardIntent)
                            overridePendingTransition(R.anim.slide_out_bottom,R.anim.slide_in_bottom)
                            finish()
                        }else{
                            val personInfoIntent= Intent(this@SplashScreenActivity,PersonInfoActivity::class.java)
                            startActivity(personInfoIntent)
                            overridePendingTransition(R.anim.slide_out_bottom,R.anim.slide_in_bottom)
                            finish()
                        }
                    }
            }else{
                val signInIntent= Intent(this@SplashScreenActivity,SignInActivity::class.java)
                startActivity(signInIntent)
                overridePendingTransition(R.anim.slide_out_bottom,R.anim.slide_in_bottom)
                finish()
            }
        },3600)
    }
}