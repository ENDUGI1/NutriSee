package com.bangkit.nutrisee.ui.signin

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.bangkit.nutrisee.MainActivity
import com.bangkit.nutrisee.R
import com.bangkit.nutrisee.data.user.UserPreferences
import com.bangkit.nutrisee.data.user.userPreferencesDataStore
import com.bangkit.nutrisee.ui.forgotpassword.ForgotPasswordActivity
import com.bangkit.nutrisee.ui.signup.SignupActivity
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.launch

class SigninActivity : AppCompatActivity() {
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button
    private lateinit var signupLink: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var userPreferences: UserPreferences
    private lateinit var viewModel: SigninViewModel
    private lateinit var forgotPassword: TextView

    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signin)
        supportActionBar?.hide()

        emailEditText = findViewById(R.id.et_email)
        passwordEditText = findViewById(R.id.et_password)
        loginButton = findViewById(R.id.btn_login)
        signupLink = findViewById(R.id.signupLink)
        progressBar = findViewById(R.id.progressBar)
        forgotPassword = findViewById(R.id.forgotPasswordLink)

        viewModel = ViewModelProvider(this)[SigninViewModel::class.java]
        userPreferences = UserPreferences.getInstance(applicationContext.userPreferencesDataStore)

        signupLink.setOnClickListener {
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
        }
        forgotPassword.setOnClickListener {
            val intent = Intent(this, ForgotPasswordActivity::class.java)
            startActivity(intent)
        }

        loginButton.setOnClickListener {
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                showLoading(true)
                viewModel.loginUser(email, password)
            } else {
                Toast.makeText(this, "Email and password cannot be empty!", Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.loginResult.observe(this) { result ->
            showLoading(false)

            result.onSuccess { loginResponse ->
                Log.d("LoginResponse", "refresh token: ${loginResponse.refreshToken.token}")
                lifecycleScope.launch {
                    userPreferences.saveLoginData(
                        loginResponse.accessToken,
                        loginResponse.refreshToken.token,
                        loginResponse.refreshToken.expiresIn
                    )
                }

                Toast.makeText(this, "Login Succeeded!", Toast.LENGTH_SHORT).show()
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