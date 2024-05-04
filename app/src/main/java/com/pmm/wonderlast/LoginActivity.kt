@file:Suppress("DEPRECATION")

package com.pmm.wonderlast
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityOptionsCompat
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.GoogleSignInStatusCodes
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class LoginActivity :AppCompatActivity() {
    private lateinit var eye : ImageView
    private var eyeStatus = false
    private lateinit var password : EditText
    private lateinit var btnLogin : Button
    private lateinit var etEmail : EditText
    private lateinit var auth : FirebaseAuth
    private lateinit var signupbtn : RelativeLayout
    private lateinit var googleSignInClient: GoogleSignInClient
    private val RC_SIGN_IN = 2
    private lateinit var btnGoogleLogin : RelativeLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity)
        auth = FirebaseAuth.getInstance()

        eye = findViewById(R.id.eye_icon)
        password = findViewById(R.id.input_password)
        etEmail = findViewById((R.id.input_email))
        btnLogin = findViewById(R.id.LoginBtn)
        btnGoogleLogin = findViewById(R.id.Ggl_btn)
        // Ketika tombol Login ditekan
        btnLogin.setOnClickListener {
            val email = etEmail.text.toString()
            val password = password.text.toString()

            // Validasi email dan password
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val user = auth.currentUser
                        if (user != null) {
                            if (user.isEmailVerified) {
                                // Login berhasil, arahkan ke halaman utama atau halaman selanjutnya
                                Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT)
                                    .show()
                                startActivity(Intent(this, HomeActivity::class.java))
                                finish()
                            }
                            else {
                                // Email belum diverifikasi, tampilkan pesan
                                Toast.makeText(this, "Verify our email firs", Toast.LENGTH_SHORT).show()
                            }
                    }else{
                            Toast.makeText(this,"User not found", Toast.LENGTH_SHORT).show()
                        }

                }else{
                        Toast.makeText(this,"${task.exception?.message}", Toast.LENGTH_SHORT).show()
                    }
            }

        }
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)
        // Setup tombol Google Sign-In
        btnGoogleLogin.setOnClickListener {
            signInWithGoogle()
            Log.d("TAG", "Is Click")
        }

        eye.setOnClickListener {
            // Di sini Anda bisa mengubah ikon
            if (!eyeStatus){
                eye.setImageResource(R.drawable.icon_eye_close)
                password.inputType = InputType.TYPE_CLASS_TEXT
            }
            else{
                eye.setImageResource(R.drawable.icon_eye)
                password.inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD or InputType.TYPE_CLASS_TEXT
            }
            eyeStatus = !eyeStatus
        }
        signupbtn = findViewById(R.id.signupbtn)

        signupbtn.setOnClickListener{
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent) // Memulai activity baru
            val options = ActivityOptionsCompat.makeCustomAnimation(
                this,
                R.anim.enter_animation_reverse,
                R.anim.exit_animation_reverse
            )
            ActivityCompat.startActivity(this, intent, options.toBundle())
        }

    }
    private fun signInWithGoogle() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }
    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)!!
                firebaseAuthWithGoogle(account)
            } catch (e: ApiException) {
                val errorMessage = when (e.statusCode) {
                    GoogleSignInStatusCodes.SIGN_IN_CANCELLED -> "Sign in cancelled"
                    GoogleSignInStatusCodes.SIGN_IN_FAILED -> "Sign in failed"
                    GoogleSignInStatusCodes.SIGN_IN_REQUIRED -> "Sign in required"
                    else -> "Error code: ${e.statusCode}"
                }
                Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()

            }
        }
    }
    private fun firebaseAuthWithGoogle(account: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    val user = auth.currentUser
                    Toast.makeText(this, "Welcome, ${user?.displayName}", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, HomeActivity::class.java))
                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(this, "Authentication failed", Toast.LENGTH_SHORT).show()
                }
            }
        }


}