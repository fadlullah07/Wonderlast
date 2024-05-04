package com.pmm.wonderlast

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth


class HomeActivity :AppCompatActivity() {

    private lateinit var tvUserEmail : TextView
    private lateinit var btnLogout : Button
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_activity)
        tvUserEmail = findViewById(R.id.welcome)
        btnLogout = findViewById(R.id.logoutbtn)
        auth = FirebaseAuth.getInstance()

        val currentUser = auth.currentUser
        tvUserEmail.text = "Welcome, ${currentUser?.displayName}"
        btnLogout.setOnClickListener {
            logout()
        }
    }
    private fun logout() {
        auth.signOut()
        Toast.makeText(this, "Logged out", Toast.LENGTH_SHORT).show()

        // Arahkan ke halaman login
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }
}