@file:Suppress("DEPRECATION")

package com.pmm.wonderlast



import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.RelativeLayout
import android.widget.Toast
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
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider


@Suppress("DEPRECATION")
class SignupActivity :AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var etEmail : EditText
    private lateinit var etPassword: EditText
    private lateinit var etConfirmPassword : EditText
    private lateinit var btnSignup : Button
    private lateinit var googleSignInClient: GoogleSignInClient
    private val RC_SIGN_IN = 1
    private lateinit var btnGoogleSignIn : RelativeLayout

    private lateinit var loginbtn : RelativeLayout
    companion object{
        private const val TAG = "Email Verification"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContentView(R.layout.signup_activity)

        auth = FirebaseAuth.getInstance()
        etEmail = findViewById(R.id.input_email)
        etPassword = findViewById(R.id.input_password)
        etConfirmPassword = findViewById(R.id.input_password_confirm)
        btnSignup = findViewById(R.id.signupbtn)
        btnGoogleSignIn = findViewById((R.id.google_btn))


        btnSignup.setOnClickListener {
            val email = etEmail.text.toString()
            val password = etPassword.text.toString()
            val confirmPassword = etConfirmPassword.text.toString()
            // Validasi email dan password
            if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() ) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (password != confirmPassword){
                Toast.makeText(this, "The Confirmed Password is wrong", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            signUpUser(email,password)
        }
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)
        // Setup tombol Google Sign-In
        btnGoogleSignIn.setOnClickListener {
            signInWithGoogle()
        }

        loginbtn = findViewById(R.id.loginbtn)

        loginbtn.setOnClickListener{
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent) // Memulai activity baru
            val options = ActivityOptionsCompat.makeCustomAnimation(
                this,
                R.anim.enter_animation,
                R.anim.exit_animation
            )
            ActivityCompat.startActivity(this, intent, options.toBundle())
            finish()
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
    private fun signUpUser(email: String, password: String) {
        // Lakukan pendaftaran pengguna di Firebase Authentication
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Registrasi berhasil, kirim email verifikasi
                    sendEmailVerification(task.result?.user)
                } else {
                    // Registrasi gagal, tampilkan pesan kesalahan
                    Toast.makeText(this, "Gagal mendaftar: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun sendEmailVerification(user: FirebaseUser?) {
        user?.sendEmailVerification()?.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // Email verifikasi berhasil dikirim
                Toast.makeText(this, "Email verifikasi telah dikirim", Toast.LENGTH_SHORT).show()

                // Tidak ada tindakan khusus yang diperlukan di sini
                // Data pengguna akan disimpan setelah email diverifikasi
            } else {
                // Gagal mengirim email verifikasi
                Toast.makeText(this, "Gagal mengirim email verifikasi", Toast.LENGTH_SHORT).show()
            }
        }
    }

}