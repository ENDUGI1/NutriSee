package com.bangkit.nutrisee.ui.profile

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.bangkit.nutrisee.data.product.ProductStorage
import com.bangkit.nutrisee.data.user.UserPreferences
import com.bangkit.nutrisee.data.user.userPreferencesDataStore
import com.bangkit.nutrisee.databinding.FragmentProfileBinding
import com.bangkit.nutrisee.ui.welcome.WelcomeActivity
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlin.math.log

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private lateinit var userPreferences: UserPreferences
    private lateinit var productStorage: ProductStorage

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val profileViewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)

        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root

        userPreferences = UserPreferences.getInstance(requireContext().userPreferencesDataStore)

        val logoutButton: Button = binding.buttonLogout
        val xButton: ImageButton = binding.buttonX
        val instagramButton: ImageButton = binding.buttonInstagram

        lifecycleScope.launch {
            // Cek apakah data nama dan email sudah ada di SharedPreferences
            val name = userPreferences.getName().first()
            val email = userPreferences.getEmail().first()

            if (name.isNullOrEmpty() || email.isNullOrEmpty()) {
                // Jika data nama dan email belum ada, ambil data dari API
                val accessToken = userPreferences.getAccessToken().first()
                profileViewModel.getUserProfile(accessToken)

                profileViewModel.profileResult.observe(viewLifecycleOwner, { result ->
                    result.onSuccess { profileResponse ->
                        // Menampilkan data profil
                        Log.d("UserProfile", "Username: ${profileResponse.data.username}, Email: ${profileResponse.data.email}")
                        lifecycleScope.launch {
                            userPreferences.updateUserDetails(
                                profileResponse.data.username,
                                profileResponse.data.email
                            )
                        }
                        lifecycleScope.launch {
                            binding.textUsername.text = profileResponse.data.username
                            binding.textEmail.text = profileResponse.data.email
                        }
                    }
                    result.onFailure {
                        Log.e("UserProfileError", it.message ?: "Error fetching profile")
                    }
                })
            } else {
                // Jika data nama dan email sudah ada, langsung set ke TextView
                binding.textUsername.text = name
                binding.textEmail.text = email
            }
        }

        logoutButton.setOnClickListener {
            lifecycleScope.launch {
                userPreferences.clearLoginData()
                productStorage.clearAllProducts()
                Toast.makeText(requireContext(), "Logged out", Toast.LENGTH_SHORT).show()

                val intent = Intent(requireContext(), WelcomeActivity::class.java)
                startActivity(intent)
                requireActivity().finish()
            }
        }

        xButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://x.com/nutrisee242137"))
            startActivity(intent)
        }

        instagramButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://instagram.com/nutrisee"))
            startActivity(intent)
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}




//class ProfileFragment : Fragment() {
//
//    private var _binding: FragmentProfileBinding? = null
//    private val binding get() = _binding!!
//
//    private lateinit var userPreferences: UserPreferences
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        val profileViewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)
//
//        _binding = FragmentProfileBinding.inflate(inflater, container, false)
//        val root: View = binding.root
//
//        userPreferences = UserPreferences.getInstance(requireContext().userPreferencesDataStore)
//
//        val logoutButton: Button = binding.buttonLogout
//        val xButton: ImageButton = binding.buttonX
//        val instagramButton: ImageButton = binding.buttonInstagram
//
//        lifecycleScope.launch {
//            val accessToken = userPreferences.getAccessToken().first()
//            profileViewModel.getUserProfile(accessToken)
//
//            profileViewModel.profileResult.observe(viewLifecycleOwner, { result ->
//                result.onSuccess { profileResponse ->
//                    // Menampilkan data profil
//                    Log.d("UserProfile", "Username: ${profileResponse.data.username}, Email: ${profileResponse.data.email}")
//                    lifecycleScope.launch {
//                        userPreferences.updateUserDetails(
//                            profileResponse.data.username,
//                            profileResponse.data.email
//                        )
//                    }
//                    lifecycleScope.launch {
//                        val name = async { userPreferences.getName().first() }
//                        val email = async { userPreferences.getEmail().first() }
//
//                        // Set text to TextViews
//                        binding.textUsername.text = name.await()
//                        binding.textEmail.text = email.await()
//                    }
//
//                }
//                result.onFailure {
//                    Log.e("UserProfileError", it.message ?: "Error fetching profile")
////                    Toast.makeText(requireContext(), "Gagal mengambil data profil", Toast.LENGTH_SHORT).show()
//                }
//            })
//        }
//
//        lifecycleScope.launch {
//            // Mengumpulkan semua data sekaligus dan menampilkannya dalam satu log
//            val accessToken = async { userPreferences.getAccessToken().first() }
//            val refreshToken = async { userPreferences.getRefreshToken().first() }
//            val name = async { userPreferences.getName().first() }
//            val email = async { userPreferences.getEmail().first() }
//            val userData = async { userPreferences.getLoginData().first() }
//
//            // Tunggu hingga semua data selesai dikumpulkan
//            val allData = listOf(
//                "Access Token: ${accessToken.await()}",
//                "Refresh Token: ${refreshToken.await()}",
//                "Name: ${name.await()}",
//                "Email: ${email.await()}",
//                "User Data: $userData"
//            )
//
//            // Menampilkan semua data dalam satu log
//            Log.d("Userdetail", allData.joinToString("\n"))
//        }
//
//        logoutButton.setOnClickListener {
//            lifecycleScope.launch {
//                userPreferences.clearLoginData()
//                Toast.makeText(requireContext(), "Logged out", Toast.LENGTH_SHORT).show()
//
//                val intent = Intent(requireContext(), WelcomeActivity::class.java)
//                startActivity(intent)
//                requireActivity().finish()
//            }
//        }
//
//        xButton.setOnClickListener {
//            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://x.com/nutrisee242137"))
//            startActivity(intent)
//        }
//
//        instagramButton.setOnClickListener {
//            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://instagram.com/nutrisee"))
//            startActivity(intent)
//        }
//
//        return root
//    }
//
//    override fun onDestroyView() {
//        super.onDestroyView()
//        _binding = null
//    }
//}



//class ProfileFragment : Fragment() {
//
//    private var _binding: FragmentProfileBinding? = null
//    private val binding get() = _binding!!
//
//    private lateinit var userPreferences: UserPreferences
//
//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View {
//        val profileViewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)
//
//        _binding = FragmentProfileBinding.inflate(inflater, container, false)
//        val root: View = binding.root
//
//        userPreferences = UserPreferences.getInstance(requireContext().userPreferencesDataStore)
//
//        val logoutButton: Button = binding.buttonLogout
//        val xButton: ImageButton = binding.buttonX
//        val instagramButton: ImageButton = binding.buttonInstagram
//
//        lifecycleScope.launch {
//            // Mengumpulkan semua data sekaligus dan menampilkannya dalam satu log
//            val accessToken = async { userPreferences.getAccessToken().first() }
//            val refreshToken = async { userPreferences.getRefreshToken().first() }
//            val name = async { userPreferences.getName().first() }
//            val email = async { userPreferences.getEmail().first() }
//            val userData = async { userPreferences.getLoginData().first() }
//
//            // Tunggu hingga semua data selesai dikumpulkan
//            val allData = listOf(
//                "Access Token: ${accessToken.await()}",
//                "Refresh Token: ${refreshToken.await()}",
//                "Name: ${name.await()}",
//                "Email: ${email.await()}",
//                "User Data: $userData"
//            )
//
//            // Menampilkan semua data dalam satu log
//            Log.d("Userdetail", allData.joinToString("\n"))
//        }
//
//
//        logoutButton.setOnClickListener {
//            lifecycleScope.launch {
//                userPreferences.clearLoginData()
//                Toast.makeText(requireContext(), "Logged out", Toast.LENGTH_SHORT).show()
//
//                val intent = Intent(requireContext(), WelcomeActivity::class.java)
//                startActivity(intent)
//                requireActivity().finish()
//            }
//        }
//
//        xButton.setOnClickListener {
//            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://x.com/nutrisee242137"))
//            startActivity(intent)
//        }
//
//        instagramButton.setOnClickListener {
//            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://instagram.com/nutrisee"))
//            startActivity(intent)
//        }
//
//        return root
//    }
//
//    override fun onDestroyView() {
//        super.onDestroyView()
//        _binding = null
//    }
//}
