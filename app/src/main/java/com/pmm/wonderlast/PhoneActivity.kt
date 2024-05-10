package com.pmm.wonderlast

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthMissingActivityForRecaptchaException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit

class PhoneActivity:AppCompatActivity() {
    private lateinit var auth : FirebaseAuth
    private lateinit var storedVerificationId : String
    private lateinit var resendToken : PhoneAuthProvider.ForceResendingToken
    private lateinit var inputPhone : EditText
    private lateinit var inputOtp : EditText
    private lateinit var sendOtpBtn : TextView
    private lateinit var confirmBtn : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.phone_activity)

        auth = FirebaseAuth.getInstance()
        inputPhone = findViewById(R.id.input_phone)
        inputOtp = findViewById(R.id.input_otp)
        sendOtpBtn = findViewById(R.id.send_otp)
        confirmBtn = findViewById(R.id.confirm_button)
        val user = FirebaseAuth.getInstance().currentUser


        sendOtpBtn.setOnClickListener {
            auth.setLanguageCode("id")
            val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                    // This callback will be invoked in two situations:
                    // 1 - Instant verification. In some cases the phone number can be instantly
                    //     verified without needing to send or enter a verification code.
                    // 2 - Auto-retrieval. On some devices Google Play services can automatically
                    //     detect the incoming verification SMS and perform verification without
                    //     user action.
                    Log.d(TAG, "onVerificationCompleted:$credential")
                    linkPhoneNumber(user!!, credential)
                }

                override fun onVerificationFailed(e: FirebaseException) {
                    // This callback is invoked in an invalid request for verification is made,
                    // for instance if the the phone number format is not valid.
                    Log.w(TAG, "onVerificationFailed", e)

                    if (e is FirebaseAuthInvalidCredentialsException) {
                        // Invalid request
                    } else if (e is FirebaseTooManyRequestsException) {
                        // The SMS quota for the project has been exceeded
                    } else if (e is FirebaseAuthMissingActivityForRecaptchaException) {
                        // reCAPTCHA verification attempted with null Activity
                    }

                    // Show a message and update the UI
                }

                override fun onCodeSent(
                    verificationId: String,
                    token: PhoneAuthProvider.ForceResendingToken,
                ) {
                    // The SMS verification code has been sent to the provided phone number, we
                    // now need to ask the user to enter the code and then construct a credential
                    // by combining the code with a verification ID.
                    Log.d(TAG, "onCodeSent:$verificationId")

                    // Save verification ID and resending token so we can use them later
                    storedVerificationId = verificationId
                    resendToken = token
                }
            }
            Log.d("TAG","Its Click")
            val phoneNumber = inputPhone.text.toString()
            if (phoneNumber.isEmpty()){
                Toast.makeText(this,"Isi nomor Telepon terlebih dahulu",Toast.LENGTH_LONG).show()
            }
            val options = PhoneAuthOptions.newBuilder(auth)
                .setPhoneNumber("+62$phoneNumber") // Phone number to verify
                .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                .setActivity(this) // Activity (for callback binding)
                .setCallbacks(callbacks) // OnVerificationStateChangedCallbacks
                .build()
            PhoneAuthProvider.verifyPhoneNumber(options)
        }
        confirmBtn.setOnClickListener{
            if(!::storedVerificationId.isInitialized){
                Toast.makeText(this,"Kode OTP belum dikirim",Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            val code = inputOtp.text.toString()
            val credential = PhoneAuthProvider.getCredential(storedVerificationId, code)
            if (user != null) {
                linkPhoneNumber(user, credential)
            }
        }


    }
    private fun linkPhoneNumber(user: FirebaseUser, credential: PhoneAuthCredential) {
        // Hubungkan nomor telepon ke pengguna saat ini
        user.updatePhoneNumber(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Nomor telepon berhasil dihubungkan
                    Log.d(TAG, "Phone number linked")
                    Toast.makeText(this, "Phone number linked successfully", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, ProfileActivity::class.java))
                    finish()
                } else {
                    // Gagal menghubungkan nomor telepon
                    Log.w(TAG, "Linking phone number failed", task.exception)
                    Toast.makeText(this, "Failed to link phone number: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }


}