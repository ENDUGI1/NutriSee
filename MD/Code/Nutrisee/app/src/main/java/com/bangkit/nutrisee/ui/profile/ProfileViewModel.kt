package com.bangkit.nutrisee.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bangkit.nutrisee.data.user.ApiUserConfig
import com.bangkit.nutrisee.data.user.UserProfileResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileViewModel : ViewModel() {
    private val _profileResult = MutableLiveData<Result<UserProfileResponse>>()
    val profileResult: LiveData<Result<UserProfileResponse>> = _profileResult
    private val apiService = ApiUserConfig.getApiService()

     fun getUserProfile(token: String) {
        apiService.getProfile("Bearer $token")
            .enqueue(object : Callback<UserProfileResponse> {
                override fun onResponse(
                    call: Call<UserProfileResponse>,
                    response: Response<UserProfileResponse>
                ) {
                    if (response.isSuccessful) {
                        val profileResponse = response.body()
                        if (profileResponse != null) {
                            _profileResult.value = Result.success(profileResponse)
                        } else {
                            _profileResult.value = Result.failure(Exception("Data profil kosong!"))
                        }
                    } else {
                        _profileResult.value = Result.failure(Exception("Gagal mengambil profil, periksa koneksi!"))
                    }
                }

                override fun onFailure(call: Call<UserProfileResponse>, t: Throwable) {
                    _profileResult.value = Result.failure(Exception("Gagal terhubung ke server: ${t.message}"))
                }
            })
    }
}