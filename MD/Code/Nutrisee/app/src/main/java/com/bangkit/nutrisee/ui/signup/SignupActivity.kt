package com.bangkit.nutrisee.ui.signup

import SignupViewModel
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bangkit.nutrisee.R
import com.bangkit.nutrisee.ui.signin.SigninActivity

class SignupActivity : AppCompatActivity() {
    private lateinit var viewModel: SignupViewModel
    private lateinit var progressBar: ProgressBar
    private lateinit var signupButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_signup)
        supportActionBar?.hide()

        viewModel = ViewModelProvider(this)[SignupViewModel::class.java]

        val usernameInput = findViewById<EditText>(R.id.usernameInput)
        val emailInput = findViewById<EditText>(R.id.emailInput)
        val passwordInput = findViewById<EditText>(R.id.passwordInput)
        val confirmPasswordInput = findViewById<EditText>(R.id.confirmPasswordInput)
        signupButton = findViewById(R.id.signupButton)
        val loginLink = findViewById<TextView>(R.id.loginLink)
        progressBar = findViewById(R.id.progressBar)

        // Login link navigation
        loginLink.setOnClickListener {
            val intent = Intent(this, SigninActivity::class.java)
            startActivity(intent)
        }

        // Observe registration result
        viewModel.registrationResult.observe(this) { result ->
            // Hide loading
            showLoading(false)

            result.onSuccess { message ->
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                finish() // Close SignupActivity
            }.onFailure { exception ->
                Toast.makeText(this, exception.message, Toast.LENGTH_SHORT).show()
            }
        }

        // Handle signup button
        signupButton.setOnClickListener {
            val username = usernameInput.text.toString().trim()
            val email = emailInput.text.toString().trim()
            val password = passwordInput.text.toString().trim()
            val confirmPassword = confirmPasswordInput.text.toString().trim()

            // Show loading
            showLoading(true)
            viewModel.registerUser(username, email, password, confirmPassword)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        signupButton.isEnabled = !isLoading
    }
}