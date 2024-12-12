package com.bangkit.nutrisee.ui.forgotpassword

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bangkit.nutrisee.data.user.ApiUserConfig
import com.bangkit.nutrisee.data.user.ForgotPasswordRequest
import com.bangkit.nutrisee.data.user.ForgotPasswordResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ForgotPasswordViewModel : ViewModel() {
    private val _forgotPasswordResult = MutableLiveData<Result<ForgotPasswordResponse>>()
    val forgotPasswordResult: LiveData<Result<ForgotPasswordResponse>> = _forgotPasswordResult

    fun forgotPassword(email: String) {
        val request = ForgotPasswordRequest(email)
        ApiUserConfig.getApiService().forgotPassword(request)
            .enqueue(object : Callback<ForgotPasswordResponse> {
                override fun onResponse(
                    call: Call<ForgotPasswordResponse>,
                    response: Response<ForgotPasswordResponse>
                ) {
                    if (response.isSuccessful) {
                        val forgotPasswordResponse = response.body()
                        if (forgotPasswordResponse != null) {
                            _forgotPasswordResult.value = Result.success(forgotPasswordResponse)
                        } else {
                            _forgotPasswordResult.value = Result.failure(
                                Exception("Empty response from the server!")
                            )
                        }
                    } else {
                        _forgotPasswordResult.value = Result.failure(
                            Exception("Failing that, check your email!")
                        )
                    }
                }

                override fun onFailure(call: Call<ForgotPasswordResponse>, t: Throwable) {
                    _forgotPasswordResult.value = Result.failure(
                        Exception("Failed to connect to the server: ${t.message}")
                    )
                }
            })
    }
}