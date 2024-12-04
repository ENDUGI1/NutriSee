package com.bangkit.nutrisee.ui.signin

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bangkit.nutrisee.MainActivity
import com.bangkit.nutrisee.R
import com.bangkit.nutrisee.data.user.UserPreferences
import com.bangkit.nutrisee.data.user.userPreferencesDataStore
import com.bangkit.nutrisee.ui.signup.SignupActivity
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class SigninActivity : AppCompatActivity() {
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button
    private lateinit var signupLink: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var userPreferences: UserPreferences
    private lateinit var viewModel: SigninViewModel

    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signin)
        supportActionBar?.hide()

        // Initialize UI components
        emailEditText = findViewById(R.id.et_email)
        passwordEditText = findViewById(R.id.et_password)
        loginButton = findViewById(R.id.btn_login)
        signupLink = findViewById(R.id.signupLink)
        progressBar = findViewById(R.id.progressBar)

        // Initialize ViewModel and UserPreferences
        viewModel = ViewModelProvider(this)[SigninViewModel::class.java]
        userPreferences = UserPreferences.getInstance(applicationContext.userPreferencesDataStore)

        // Navigate to SignupActivity
        signupLink.setOnClickListener {
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
        }

        // Set onClickListener for login button
        loginButton.setOnClickListener {
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                // Show loading
                showLoading(true)
                viewModel.loginUser(email, password)
            } else {
                Toast.makeText(this, "Email dan password tidak boleh kosong!", Toast.LENGTH_SHORT).show()
            }
        }

        // Observe login result
        viewModel.loginResult.observe(this) { result ->
            // Hide loading
            showLoading(false)

            result.onSuccess { loginResponse ->
                // Save login data
                GlobalScope.launch(Dispatchers.IO) {
                    userPreferences.saveLoginData(
                        loginResponse.accessToken,
                        loginResponse.refreshToken.token,
                        loginResponse.refreshToken.expiresIn
                    )
                }

                Toast.makeText(this, "Login berhasil!", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }.onFailure { exception ->
                Toast.makeText(this, exception.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        loginButton.isEnabled = !isLoading
    }
}