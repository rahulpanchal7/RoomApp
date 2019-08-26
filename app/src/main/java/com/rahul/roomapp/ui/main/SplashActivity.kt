package com.rahul.roomapp.ui.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.rahul.roomapp.R
import com.rahul.roomapp.ui.userauth.UserRegistration

class SplashActivity : AppCompatActivity() {
    private var mDelayHandler : Handler? = null
    private val SPLASH_DELAY : Long = 1500 //1.5 seconds

    private val mRunnable : Runnable = Runnable {
        if (!isFinishing) {
            val intent = Intent(applicationContext, UserRegistration::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        //Initialize the Handler
        mDelayHandler = Handler()
        //Navigate with delay
        mDelayHandler?.postDelayed(mRunnable, SPLASH_DELAY)

    }

    public override fun onDestroy() {
        super.onDestroy()
        if (mDelayHandler != null) {
            mDelayHandler?.removeCallbacks(mRunnable)
        }
    }
}