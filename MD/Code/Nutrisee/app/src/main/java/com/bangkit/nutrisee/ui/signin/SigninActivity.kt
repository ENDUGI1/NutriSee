package com.bangkit.nutrisee.ui.signin


import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
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
                viewModel.loginUser(email, password)
            } else {
                Toast.makeText(this, "Email dan password tidak boleh kosong!", Toast.LENGTH_SHORT).show()
            }
        }

        // Observe login result
        viewModel.loginResult.observe(this) { result ->
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