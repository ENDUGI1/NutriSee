package com.bangkit.nutrisee.ui.signin

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bangkit.nutrisee.MainActivity
import com.bangkit.nutrisee.R
import com.bangkit.nutrisee.data.user.ApiUserConfig
import com.bangkit.nutrisee.data.user.LoginRequest
import com.bangkit.nutrisee.data.user.LoginResponse
import com.bangkit.nutrisee.data.user.UserPreferences
import com.bangkit.nutrisee.data.user.userPreferencesDataStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SigninActivity : AppCompatActivity() {

    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button
    private lateinit var userPreferences: UserPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signin)

        // Initialize UI components
        emailEditText = findViewById(R.id.et_email)
        passwordEditText = findViewById(R.id.et_password)
        loginButton = findViewById(R.id.btn_login)

        // Initialize UserPreferences
        userPreferences = UserPreferences.getInstance(applicationContext.userPreferencesDataStore)

        // Set onClickListener for login button
        loginButton.setOnClickListener {
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                loginUser(email, password)
            } else {
                Toast.makeText(this, "Email dan password tidak boleh kosong!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loginUser(email: String, password: String) {
        val request = LoginRequest(email, password)
        ApiUserConfig.getApiService().login(request)
            .enqueue(object : Callback<LoginResponse> {
                override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                    if (response.isSuccessful) {
                        val loginResponse = response.body()
                        if (loginResponse != null) {
                            // Save user data using DataStore
                            val accessToken = loginResponse.accessToken
                            val refreshToken = loginResponse.refreshToken.token
                            val expiresIn = loginResponse.refreshToken.expiresIn

                            // Save login data using UserPreferences
                            GlobalScope.launch(Dispatchers.IO) {
                                userPreferences.saveLoginData(accessToken, refreshToken, expiresIn)
                            }

                            Log.d("SigninActivity", "LoginResponse accessToken: $accessToken")
                            Log.d("SigninActivity", "LoginResponse RefreshToken: $refreshToken")
                            Log.d("SigninActivity", "LoginResponse Pesan: ${loginResponse.message}")
                            Toast.makeText(this@SigninActivity, "Login berhasil!", Toast.LENGTH_SHORT).show()

                            // Start MainActivity after successful login
                            startActivity(Intent(this@SigninActivity, MainActivity::class.java))
                            finish()
                        }
                    } else {
                        Toast.makeText(this@SigninActivity, "Login gagal, periksa email dan password!", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    Log.e("SigninActivity", "Failure: ${t.message}")
                    Toast.makeText(this@SigninActivity, "Gagal terhubung ke server!", Toast.LENGTH_SHORT).show()
                }
            })
    }

    // Function to save user data using UserPreferences (DataStore)
    private suspend fun saveUserData(accessToken: String, refreshToken: String, expiresIn: String) {
        userPreferences.saveLoginData(accessToken, refreshToken, expiresIn)
    }
}

















//class SigninActivity : AppCompatActivity() {
//
//    private lateinit var emailEditText: EditText
//    private lateinit var passwordEditText: EditText
//    private lateinit var loginButton: Button
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_signin)
//
//        // Initialize UI components
//        emailEditText = findViewById(R.id.et_email)
//        passwordEditText = findViewById(R.id.et_password)
//        loginButton = findViewById(R.id.btn_login)
//
//        // Set onClickListener for login button
//        loginButton.setOnClickListener {
//            val email = emailEditText.text.toString().trim()
//            val password = passwordEditText.text.toString().trim()
//
//            if (email.isNotEmpty() && password.isNotEmpty()) {
//                loginUser(email, password)
//            } else {
//                Toast.makeText(this, "Email dan password tidak boleh kosong!", Toast.LENGTH_SHORT).show()
//            }
//        }
//    }
//
//    private fun loginUser(email: String, password: String) {
//        val request = LoginRequest(email, password)
//        ApiUserConfig.getApiService().login(request)
//            .enqueue(object : Callback<LoginResponse> {
//                override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
//                    if (response.isSuccessful) {
//                        val loginResponse = response.body()
//                        if (loginResponse != null) {
//                            saveUserData(loginResponse.accessToken, loginResponse.refreshToken.token)
//                            Log.d("SigninActivity", "LoginResponse accessToken: ${loginResponse.accessToken}")
//                            Log.d("SigninActivity", "LoginResponse RefereshToken: ${loginResponse.refreshToken}")
//                            Log.d("SigninActivity", "LoginResponse Pesan: ${loginResponse.message}")
//                            Toast.makeText(this@SigninActivity, "Login berhasil!", Toast.LENGTH_SHORT).show()
//                            startActivity(Intent(this@SigninActivity, MainActivity::class.java))
//                            finish()
//                        }
//                    } else {
//                        Toast.makeText(this@SigninActivity, "Login gagal, periksa email dan password!", Toast.LENGTH_SHORT).show()
//                    }
//                }
//
//                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
//                    Log.e("SigninActivity", "Failure: ${t.message}")
//                    Toast.makeText(this@SigninActivity, "Gagal terhubung ke server!", Toast.LENGTH_SHORT).show()
//                }
//            })
//    }
//
//    private fun saveUserData(accessToken: String, refreshToken: String) {
//        val sharedPreferences = getSharedPreferences("user_preferences", Context.MODE_PRIVATE)
//        val editor = sharedPreferences.edit()
//        editor.putString("ACCESS_TOKEN", accessToken)
//        editor.putString("REFRESH_TOKEN", refreshToken)
//        editor.apply()
//    }
//}





//class SigninActivity : AppCompatActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
//        setContentView(R.layout.activity_signin)
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }
//    }
//}