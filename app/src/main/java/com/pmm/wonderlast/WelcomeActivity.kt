package com.pmm.wonderlast

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityOptionsCompat
import com.google.firebase.auth.FirebaseAuth

class WelcomeActivity :AppCompatActivity() {
    private lateinit var auth :FirebaseAuth
    private lateinit var loginbtn : Button
    private lateinit var signupbtn: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.first_activity)
        auth = FirebaseAuth.getInstance()
        loginbtn = findViewById(R.id.login_btn)
        signupbtn = findViewById(R.id.Sign_btn)
        signupbtn.setOnClickListener{
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent) // Memulai activity baru
            val options = ActivityOptionsCompat.makeCustomAnimation(
                this,
                R.anim.enter_animation,
                R.anim.exit_animation
            )
            ActivityCompat.startActivity(this, intent, options.toBundle())
        }
        loginbtn.setOnClickListener{
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent) // Memulai activity baru
            val options = ActivityOptionsCompat.makeCustomAnimation(
                this,
                R.anim.enter_animation,
                R.anim.exit_animation
            )
            ActivityCompat.startActivity(this, intent, options.toBundle())
        }

    }


}