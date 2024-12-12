package com.bangkit.nutrisee.ui.welcome

import android.content.Intent
import android.os.Bundle
import android.widget.Button
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

        userPreferences = UserPreferences.getInstance(applicationContext.userPreferencesDataStore)

        lifecycleScope.launch {
            val accessToken = userPreferences.getAccessToken().first()
            if (accessToken.isNotEmpty()) {
                startActivity(Intent(this@WelcomeActivity, MainActivity::class.java))
                finish()
                return@launch
            } else {
                setContentView(R.layout.activity_welcome)
                setUpEdgeToEdgeLayout()
                setUpButtons()
            }
        }
    }

    private fun setUpEdgeToEdgeLayout() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun setUpButtons() {
        val signInButton = findViewById<Button>(R.id.signInButton)
        val signUpButton = findViewById<Button>(R.id.signUpButton)

        signInButton.setOnClickListener {
            val intent = Intent(this, SigninActivity::class.java)
            startActivity(intent)
        }

        signUpButton.setOnClickListener {
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
        }
    }
}