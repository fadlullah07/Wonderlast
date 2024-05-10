package com.pmm.wonderlast

import android.content.Intent
import android.os.Bundle
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class ProfileActivity :AppCompatActivity() {

    private lateinit var tvUserEmail : TextView
    private lateinit var btnLogout : RelativeLayout
    private lateinit var auth: FirebaseAuth
    private lateinit var tvnama : TextView
    private lateinit var tvPhone : TextView
    private lateinit var personalData : RelativeLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.profile_activity)
        tvUserEmail = findViewById(R.id.user_email)
        tvnama = findViewById(R.id.user_name)
        btnLogout = findViewById(R.id.log_out)
        auth = FirebaseAuth.getInstance()
        personalData = findViewById(R.id.personal_data)
        tvPhone = findViewById(R.id.user_phonenumber)

        val currentUser = auth.currentUser
        if (currentUser != null) {
            if(currentUser.displayName == ""){
                "${currentUser.email}".also { tvUserEmail.text = it }
                "Nama belum di set".also { tvnama.text = it }
                "${currentUser.phoneNumber}".also { tvPhone.text = it }
            }else{
                "${currentUser.email}".also { tvUserEmail.text = it }
                "${currentUser.displayName}".also { tvnama.text = it }
                "${currentUser.phoneNumber}".also { tvPhone.text = it }
            }
        }

        personalData.setOnClickListener{
            startActivity(Intent(this, UserdataActivity::class.java))

        }

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