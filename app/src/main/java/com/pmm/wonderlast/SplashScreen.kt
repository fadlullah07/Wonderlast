package com.pmm.wonderlast

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity


@SuppressLint("CustomSplashScreen")
class SplashScreen :AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // Proceed to the next activity after a delay
        @Suppress("DEPRECATION")
        Handler().postDelayed({
            // Start your main activity
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            // Finish the splash activity
            finish()
        }, 3000) // 3000 milliseconds = 3 seconds
    }
}