package com.bangkit.nutrisee.ui.signin

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bangkit.nutrisee.data.user.ApiUserConfig
import com.bangkit.nutrisee.data.user.LoginRequest
import com.bangkit.nutrisee.data.user.LoginResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response



class SigninViewModel : ViewModel() {
    private val _loginResult = MutableLiveData<Result<LoginResponse>>()
    val loginResult: LiveData<Result<LoginResponse>> = _loginResult

    fun loginUser(email: String, password: String) {
        // Input validation
        if (email.isEmpty() || password.isEmpty()) {
            _loginResult.value = Result.failure(Exception("Email dan password tidak boleh kosong!"))
            return
        }

        // Create login request
        val request = LoginRequest(email, password)
        ApiUserConfig.getApiService().login(request)
            .enqueue(object : Callback<LoginResponse> {
                override fun onResponse(
                    call: Call<LoginResponse>,
                    response: Response<LoginResponse>
                ) {
                    if (response.isSuccessful) {
                        val loginResponse = response.body()
                        if (loginResponse != null) {
                            _loginResult.value = Result.success(loginResponse)
                        } else {
                            _loginResult.value = Result.failure(
                                Exception("Login gagal, respons kosong!")
                            )
                        }
                    } else {
                        _loginResult.value = Result.failure(
                            Exception("Login gagal, periksa email dan password!")
                        )
                    }
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    _loginResult.value = Result.failure(
                        Exception("Gagal terhubung ke server: ${t.message}")
                    )
                }
            })
    }
}