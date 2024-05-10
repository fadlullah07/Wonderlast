package com.pmm.wonderlast

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityOptionsCompat
import com.google.firebase.auth.FirebaseAuth

class UserdataActivity : AppCompatActivity() {

    private lateinit var backBtn: ImageView
    private lateinit var nameInput : EditText
    private lateinit var emailInput : EditText
    private lateinit var phoneInput : EditText
    private lateinit var ediBtn : Button
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.userdata_activity)
        backBtn = findViewById(R.id.arrow_back)
        backBtn.setOnClickListener{
            intent = Intent(this, ProfileActivity::class.java)
            val options = ActivityOptionsCompat.makeCustomAnimation(
                this,
                R.anim.enter_animation_reverse,
                R.anim.exit_animation_reverse
            )
            ActivityCompat.startActivity(this, intent , options.toBundle())
            finish()
        }
        nameInput = findViewById(R.id.name_input)
        emailInput = findViewById(R.id.email_input)
        phoneInput = findViewById(R.id.phone_input)
//        ediBtn = findViewById(R.id.editbtn)
        nameInput.isFocusable = false // Tidak dapat di-fokus
        emailInput.isFocusable = false // Dapat di-fokus
        phoneInput.isFocusable = false // Dapat di-fokus

        auth = FirebaseAuth.getInstance()

        val currentUser = auth.currentUser
        if (currentUser != null) {
                nameInput.setText(currentUser.displayName)
                emailInput.setText(currentUser.email)
        }
//        ediBtn.setOnClickListener {
//            val builder = AlertDialog.Builder(this)
//            builder.setTitle("Password Required")
//            builder.setMessage("Please enter your password to continue editing")
//
//            // Tambahkan field teks untuk memasukkan password
//            val input = EditText(this)
//            input.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
//            builder.setView(input)
//
//            builder.setPositiveButton("OK") { dialog, which ->
//                val password = input.text.toString()
//                val credential = EmailAuthProvider.getCredential(currentUser!!.email!!, password)
//                currentUser.reauthenticate(credential)
//                        .addOnCompleteListener { reauthTask ->
//                            if (reauthTask.isSuccessful) {
//                                Toast.makeText(
//                                    this,
//                                    "Re-authentication successful",
//                                    Toast.LENGTH_SHORT
//                                ).show()
//                                nameInput.isEnabled = true // Tidak dapat diedit
//                                emailInput.isEnabled = true // Dapat diedit
//                                phoneInput.isEnabled = true // Dapat diedit
//                                nameInput.requestFocus()
//
//                                if (currentUser.displayName == "") {
//                                    nameInput.text.clear()
//                                }
//                            } else {
//                                // Re-authentication gagal, tampilkan pesan kesalahan
//                                Toast.makeText(
//                                    this,
//                                    "Re-authentication failed: ${reauthTask.exception?.message}",
//                                    Toast.LENGTH_SHORT
//                                ).show()
//                            }
//
//                        }
//            }
//
//            builder.setNegativeButton("Cancel") { dialog, which ->
//                dialog.dismiss() // Tutup alert dialog jika tombol "Cancel" ditekan
//            }
//
//            val dialog = builder.create()
//            dialog.show()
//        }

    }


}