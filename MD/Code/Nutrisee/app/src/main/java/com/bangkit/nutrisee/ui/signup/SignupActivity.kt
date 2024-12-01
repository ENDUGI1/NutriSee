package com.bangkit.nutrisee.ui.signup

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bangkit.nutrisee.R
import com.bangkit.nutrisee.data.user.ApiUserConfig
import com.bangkit.nutrisee.data.user.RegisterRequest
import com.bangkit.nutrisee.data.user.RegisterResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignupActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_signup)
        supportActionBar?.hide()

        // Set up window insets for edge-to-edge display


        val usernameInput = findViewById<EditText>(R.id.usernameInput)
        val emailInput = findViewById<EditText>(R.id.emailInput)
        val passwordInput = findViewById<EditText>(R.id.passwordInput)
        val confirmPasswordInput = findViewById<EditText>(R.id.confirmPasswordInput)
        val signupButton = findViewById<Button>(R.id.signupButton)

        // Handle button click for registration
        signupButton.setOnClickListener {
            val username = usernameInput.text.toString().trim()
            val email = emailInput.text.toString().trim()
            val password = passwordInput.text.toString().trim()
            val confirmPassword = confirmPasswordInput.text.toString().trim()

            // Basic validation
            if (username.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(this, "Semua field harus diisi!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Email format validation
            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(this, "Format email tidak valid!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password != confirmPassword) {
                Toast.makeText(this, "Password dan Konfirmasi Password tidak cocok!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Make registration request
            registerUser(username, email, password, confirmPassword)
        }
    }

    private fun registerUser(username: String, email: String, password: String, confirmPassword: String) {
        val request = RegisterRequest(username, email, password, confirmPassword)

        ApiUserConfig.getApiService().register(request)
            .enqueue(object : Callback<RegisterResponse> {
                override fun onResponse(
                    call: Call<RegisterResponse>,
                    response: Response<RegisterResponse>
                ) {
                    if (response.isSuccessful) {
                        // Mendapatkan message dari respons body
                        val registerResponse = response.body()
                        val message = registerResponse?.message ?: "Registrasi berhasil!"

                        // Menampilkan pesan sesuai respons dari server
                        Toast.makeText(this@SignupActivity, message, Toast.LENGTH_SHORT).show()

                        if (message == "Register berhasil") {
                            finish() // Close SignupActivity jika registrasi berhasil
                        }
                    } else {
                        // Menangani error ketika response tidak sukses
                        Log.e("SignupActivity", "Error: ${response.errorBody()?.string()}")
                        Toast.makeText(this@SignupActivity, "Registrasi gagal!", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                    // Menangani kegagalan jaringan atau masalah lainnya
                    Log.e("SignupActivity", "Failure: ${t.message}")
                    Toast.makeText(this@SignupActivity, "Gagal terhubung ke server!", Toast.LENGTH_SHORT).show()
                }
            })
    }

}










//
//class SignupActivity : AppCompatActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
//        setContentView(R.layout.activity_signup)
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }
//    }
//}