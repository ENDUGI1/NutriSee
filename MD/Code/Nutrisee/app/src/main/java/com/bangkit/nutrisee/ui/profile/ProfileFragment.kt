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
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.bangkit.nutrisee.data.product.ProductStorage
import com.bangkit.nutrisee.data.user.UserPreferences
import com.bangkit.nutrisee.data.user.userPreferencesDataStore
import com.bangkit.nutrisee.databinding.FragmentProfileBinding
import com.bangkit.nutrisee.ui.welcome.WelcomeActivity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

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
            val name = userPreferences.getName().first()
            val email = userPreferences.getEmail().first()

            if (name.isNullOrEmpty() || email.isNullOrEmpty()) {
                val accessToken = userPreferences.getAccessToken().first()
                profileViewModel.getUserProfile(accessToken)

                profileViewModel.profileResult.observe(viewLifecycleOwner, { result ->
                    result.onSuccess { profileResponse ->
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
                binding.textUsername.text = name
                binding.textEmail.text = email
            }
        }
        productStorage = ProductStorage(requireContext())

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