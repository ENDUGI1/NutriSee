package com.bangkit.nutrisee.ui.splashscreen

import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bangkit.nutrisee.R

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.bangkit.nutrisee.MainActivity
import com.bangkit.nutrisee.ui.welcome.WelcomeActivity

class SplashActivity : AppCompatActivity() {

    private val SPLASH_DELAY: Long = 3000 // Delay dalam milidetik (3 detik)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // Handler untuk menunda perpindahan ke MainActivity
        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, WelcomeActivity::class.java))
            finish()
        }, SPLASH_DELAY)
    }
}
