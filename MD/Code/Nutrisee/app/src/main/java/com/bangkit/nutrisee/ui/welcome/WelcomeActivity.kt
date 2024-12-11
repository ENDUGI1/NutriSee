package com.bangkit.nutrisee.ui.welcome

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.bangkit.nutrisee.MainActivity
import com.bangkit.nutrisee.R
import com.bangkit.nutrisee.data.user.UserPreferences
import com.bangkit.nutrisee.data.user.userPreferencesDataStore
import com.bangkit.nutrisee.ui.signin.SigninActivity
import com.bangkit.nutrisee.ui.signup.SignupActivity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class WelcomeActivity : AppCompatActivity() {

    private lateinit var userPreferences: UserPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()

        // Inisialisasi UserPreferences dengan DataStore
        userPreferences = UserPreferences.getInstance(applicationContext.userPreferencesDataStore)

        // Mengecek apakah accessToken sudah ada di DataStore
        lifecycleScope.launch {
            val accessToken = userPreferences.getAccessToken().first()
            if (accessToken.isNotEmpty()) {
                // Jika accessToken ditemukan, langsung menuju MainActivity
                startActivity(Intent(this@WelcomeActivity, MainActivity::class.java))
                finish() // Pastikan WelcomeActivity tidak ada dalam stack
                return@launch // Hentikan eksekusi lebih lanjut
            } else {
                // Jika tidak ada accessToken, tampilkan WelcomeActivity
                setContentView(R.layout.activity_welcome)
                setUpEdgeToEdgeLayout()
                setUpButtons()
            }
        }
    }

    // Set up padding untuk edge-to-edge layout
    private fun setUpEdgeToEdgeLayout() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    // Set up tombol Sign In dan Sign Up
    private fun setUpButtons() {
        val signInButton = findViewById<Button>(R.id.signInButton)
        val signUpButton = findViewById<Button>(R.id.signUpButton)

        // Set OnClickListeners untuk tombol-tombol tersebut
        signInButton.setOnClickListener {
            // Arahkan ke Sign In Activity
            val intent = Intent(this, SigninActivity::class.java)
            startActivity(intent)
        }

        signUpButton.setOnClickListener {
            // Arahkan ke Sign Up Activity
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
        }
    }
}