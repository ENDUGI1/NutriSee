package com.bangkit.nutrisee.ui.forgotpassword

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.bangkit.nutrisee.R
import com.bangkit.nutrisee.ui.welcome.WelcomeActivity

class ForgotPasswordActivity : AppCompatActivity() {
    private lateinit var emailEditText: EditText
    private lateinit var forgotPasswordButton: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var viewModel: ForgotPasswordViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)
        supportActionBar?.hide()

        // Initialize UI components
        emailEditText = findViewById(R.id.et_email)
        forgotPasswordButton = findViewById(R.id.btn_login)
        progressBar = findViewById(R.id.progressBar)

        // Initialize ViewModel
        viewModel = ViewModelProvider(this)[ForgotPasswordViewModel::class.java]

        // Set onClickListener for Forgot Password button
        forgotPasswordButton.setOnClickListener {
            val email = emailEditText.text.toString().trim()

            if (email.isNotEmpty()) {
                // Show loading
                showLoading(true)
                viewModel.forgotPassword(email)
            } else {
                Toast.makeText(this, "Email tidak boleh kosong!", Toast.LENGTH_SHORT).show()
            }
        }

        // Observe forgot password result
        viewModel.forgotPasswordResult.observe(this) { result ->
            // Hide loading
            showLoading(false)

            result.onSuccess { response ->
                Toast.makeText(this, response.message, Toast.LENGTH_SHORT).show()
                // Navigate to WelcomeActivity
                val intent = Intent(this, WelcomeActivity::class.java)
                startActivity(intent)
                finish()
            }.onFailure { exception ->
                Toast.makeText(this, exception.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        forgotPasswordButton.isEnabled = !isLoading
    }
}