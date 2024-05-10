package com.pmm.wonderlast

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityOptionsCompat
import com.google.firebase.auth.FirebaseAuth

class ResetPassActivity: Activity() {


    private lateinit var emailinput:EditText
    private lateinit var sendbtn: Button
    private lateinit var backbtn: ImageView
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.reset_pass_activity)
        auth = FirebaseAuth.getInstance()


        emailinput = findViewById(R.id.input_email)
        sendbtn = findViewById(R.id.sendbtn)
        backbtn = findViewById(R.id.arrow_back)

        backbtn.setOnClickListener{
            intent = Intent(this, LoginActivity::class.java)
            val options = ActivityOptionsCompat.makeCustomAnimation(
                this,
                R.anim.enter_animation_reverse,
                R.anim.exit_animation_reverse
            )
            ActivityCompat.startActivity(this, intent , options.toBundle())
            finish()
        }
        sendbtn.setOnClickListener{
            val emailAddress = emailinput.text.toString()
            val user = auth.currentUser
            if (emailAddress.isEmpty()) {
                Toast.makeText(this,"Please fill the fields",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if(user == null){
                Toast.makeText(this,"Email is not registered",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            auth.sendPasswordResetEmail(emailAddress)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Email reset password berhasil dikirim
                   Toast.makeText( this,"Email reset password berhasil dikirim",Toast.LENGTH_SHORT).show()
                    // Tampilkan pesan kepada pengguna bahwa email reset password telah dikirim
                } else {
                    // Terjadi kesalahan saat mengirim email reset password
                    Toast.makeText( this,"Gagal mengirim email reset password: ${task.exception}",Toast.LENGTH_SHORT).show()
                    // Tampilkan pesan kesalahan kepada pengguna
                }
            }
        }

    }

}