package com.pmm.wonderlast

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityOptionsCompat
import androidx.viewpager2.widget.ViewPager2
import com.google.firebase.auth.FirebaseAuth

class WelcomeActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var loginbtn: Button
    private lateinit var signupbtn: Button
    private lateinit var viewPager: ViewPager2
    private lateinit var sliderAdapter: SliderAdapter
    private val NUM_PAGES = 5 // Jumlah halaman slider
    private val images = listOf(
        R.drawable.placeholder_image,
        R.drawable.image_slider2,
        R.drawable.image_slider3,
        R.drawable.image_slider4,
        R.drawable.image_slider5
        // Tambahkan gambar lainnya di sini
    )
    private val handler : Handler
    private val delay = 3000L // Delay dalam milidetik (misalnya, 3000L = 3 detik)
    private var currentPage = 0

    init {
        val handlerThread = HandlerThread("SliderHandlerThread")
        handlerThread.start()
        handler = Handler(handlerThread.looper)
    }


    private val runnable = object : Runnable {
        override fun run() {
            if (currentPage == NUM_PAGES - 1) {
                currentPage = 0
            } else {
                currentPage++
            }
            viewPager.setCurrentItem(currentPage, true)
            handler.postDelayed(this, delay)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.first_activity)

        viewPager = findViewById(R.id.viewPagerSlider)
        sliderAdapter = SliderAdapter(this, images)
        viewPager.adapter = sliderAdapter

        auth = FirebaseAuth.getInstance()
        loginbtn = findViewById(R.id.login_btn)
        signupbtn = findViewById(R.id.Sign_btn)

        signupbtn.setOnClickListener {
            val intent = Intent(this, SignupActivity::class.java)
            val options = ActivityOptionsCompat.makeCustomAnimation(
                this,
                R.anim.enter_animation,
                R.anim.exit_animation
            )
            ActivityCompat.startActivity(this, intent, options.toBundle())
        }

        loginbtn.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            val options = ActivityOptionsCompat.makeCustomAnimation(
                this,
                R.anim.enter_animation,
                R.anim.exit_animation
            )
            ActivityCompat.startActivity(this, intent, options.toBundle())
        }
    }

    override fun onResume() {
        super.onResume()
        startAutoSlider()
    }

    override fun onPause() {
        super.onPause()
        stopAutoSlider()
    }

    private fun startAutoSlider() {
        handler.postDelayed(runnable, delay)
    }

    private fun stopAutoSlider() {
        handler.removeCallbacks(runnable)
    }
}

