package com.pmm.wonderlast

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        auth = Firebase.auth
        val user = FirebaseAuth.getInstance().currentUser
        Log.d("Tag","MainActivity")
        if (user != null) {
            if (user.isEmailVerified) {
                // Email sudah diverifikasi, cek nomor telepon terhubung
                if (user.phoneNumber != "") {
                    // Nomor telepon sudah terhubung
                    startActivity(Intent(this, HomeActivity::class.java))
                    finish()
                } else {
                    // Nomor telepon belum terhubung
                    startActivity(Intent(this, PhoneActivity::class.java))
                    finish()
                }
            } else {
                // Email belum diverifikasi
                Toast.makeText(this, "Silakan verifikasi alamat email Anda terlebih dahulu", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
        }else{
            startActivity(Intent(this, WelcomeActivity::class.java))
            finish()
        }
    }
}

