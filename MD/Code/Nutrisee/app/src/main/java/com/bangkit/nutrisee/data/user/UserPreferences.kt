package com.bangkit.nutrisee.data.user

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Extension untuk DataStore
val Context.userPreferencesDataStore:
        DataStore<Preferences> by preferencesDataStore(name = "user_preferences")

// Data class untuk menyimpan data user
data class UserData(
    val accessToken: String,
    val refreshToken: String,
    val name: String,
    val email: String,
    val expiresIn: String
)

// UserPreferences Class
class UserPreferences private constructor(private val dataStore: DataStore<Preferences>) {

    // Fungsi untuk mendapatkan AccessToken
    fun getAccessToken(): Flow<String> = dataStore.data.map { preferences ->
        preferences[ACCESS_TOKEN_KEY] ?: ""
    }

    // Fungsi untuk mendapatkan RefreshToken
    fun getRefreshToken(): Flow<String> = dataStore.data.map { preferences ->
        preferences[REFRESH_TOKEN_KEY] ?: ""
    }

    // Fungsi untuk mendapatkan nama pengguna
    fun getName(): Flow<String> = dataStore.data.map { preferences ->
        preferences[NAME_KEY] ?: ""
    }

    // Fungsi untuk mendapatkan email pengguna
    fun getEmail(): Flow<String> = dataStore.data.map { preferences ->
        preferences[EMAIL_KEY] ?: ""
    }

    // Fungsi untuk menyimpan data dari respons login
    suspend fun saveLoginData(
        accessToken: String,
        refreshToken: String,
        expiresIn: String
    ) {
        dataStore.edit { preferences ->
            preferences[ACCESS_TOKEN_KEY] = accessToken
            preferences[REFRESH_TOKEN_KEY] = refreshToken
            preferences[EXPIRES_IN_KEY] = expiresIn
        }
    }

    // Fungsi untuk memperbarui nama dan email pengguna
    suspend fun updateUserDetails(name: String, email: String) {
        dataStore.edit { preferences ->
            preferences[NAME_KEY] = name
            preferences[EMAIL_KEY] = email
        }
    }

    // Fungsi untuk mendapatkan semua data login dan user
    fun getLoginData(): Flow<UserData> {
        return dataStore.data.map { preferences ->
            UserData(
                accessToken = preferences[ACCESS_TOKEN_KEY] ?: "",
                refreshToken = preferences[REFRESH_TOKEN_KEY] ?: "",
                name = preferences[NAME_KEY] ?: "",
                email = preferences[EMAIL_KEY] ?: "",
                expiresIn = preferences[EXPIRES_IN_KEY] ?: ""
            )
        }
    }

    // Fungsi untuk menghapus data pengguna (logout)
    suspend fun clearLoginData() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: UserPreferences? = null

        private val ACCESS_TOKEN_KEY = stringPreferencesKey("access_token")
        private val REFRESH_TOKEN_KEY = stringPreferencesKey("refresh_token")
        private val NAME_KEY = stringPreferencesKey("name")
        private val EMAIL_KEY = stringPreferencesKey("email")
        private val EXPIRES_IN_KEY = stringPreferencesKey("expires_in")

        // Singleton untuk UserPreferences
        fun getInstance(dataStore: DataStore<Preferences>): UserPreferences {
            return INSTANCE ?: synchronized(this) {
                val instance = UserPreferences(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}
