import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bangkit.nutrisee.data.user.ApiUserConfig
import com.bangkit.nutrisee.data.user.RegisterRequest
import com.bangkit.nutrisee.data.user.RegisterResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignupViewModel : ViewModel() {
    private val _registrationResult = MutableLiveData<Result<String>>()
    val registrationResult: LiveData<Result<String>> = _registrationResult

    fun registerUser(username: String, email: String, password: String, confirmPassword: String) {
        if (username.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            _registrationResult.value = Result.failure(Exception("Semua field harus diisi!"))
            return
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _registrationResult.value = Result.failure(Exception("Format email tidak valid!"))
            return
        }

        if (password != confirmPassword) {
            _registrationResult.value = Result.failure(Exception("Password dan Konfirmasi Password tidak cocok!"))
            return
        }

        val request = RegisterRequest(username, email, password, confirmPassword)
        ApiUserConfig.getApiService().register(request)
            .enqueue(object : Callback<RegisterResponse> {
                override fun onResponse(
                    call: Call<RegisterResponse>,
                    response: Response<RegisterResponse>
                ) {
                    if (response.isSuccessful) {
                        val registerResponse = response.body()
                        val message = registerResponse?.message ?: "Registrasi berhasil!"

                        if (message == "Register berhasil") {
                            _registrationResult.value = Result.success(message)
                        } else {
                            _registrationResult.value = Result.failure(Exception(message))
                        }
                    } else {
                        _registrationResult.value = Result.failure(
                            Exception("Registrasi gagal: ${response.errorBody()?.string()}")
                        )
                    }
                }

                override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                    _registrationResult.value = Result.failure(
                        Exception("Gagal terhubung ke server: ${t.message}")
                    )
                }
            })
    }
}