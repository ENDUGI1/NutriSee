package com.bangkit.nutrisee.ui.profile


import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bangkit.nutrisee.R
import com.bangkit.nutrisee.data.user.UserPreferences
import com.bangkit.nutrisee.data.user.userPreferencesDataStore
import com.bangkit.nutrisee.databinding.FragmentProfileBinding
import com.bangkit.nutrisee.ui.welcome.WelcomeActivity
import kotlinx.coroutines.launch

//class ProfileFragment : Fragment() {
//
//    private var _binding: FragmentProfileBinding? = null
//    private val binding get() = _binding!!
//
//    // Mengambil instance dari UserPreferences
//    private lateinit var userPreferences: UserPreferences
//
//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    )
//    :
//            View? {
//        _binding = FragmentProfileBinding.inflate(inflater, container, false)
//        val root: View = binding.root
//
//        // Inisialisasi userPreferences dengan DataStore yang sesuai
//        userPreferences = UserPreferences.getInstance(requireContext().userPreferencesDataStore)
//
//        // Mengambil data user dari UserPreferences
//        lifecycleScope.launch {
//            userPreferences.getLoginData().collect { userData ->
//                // Menampilkan semua data user pada TextView
//                binding.textProfile.text = """
//                    AccessToken: ${userData.accessToken}
//                    RefreshToken: ${userData.refreshToken}
//                    Name: ${userData.name}
//                    Email: ${userData.email}
//                    ExpiresIn: ${userData.expiresIn}
//                """.trimIndent()
//
//                // Menampilkan log data untuk debugging
//                Log.d("ProfileFragment", "AccessToken: ${userData.accessToken}")
//                Log.d("ProfileFragment", "RefreshToken: ${userData.refreshToken}")
//                Log.d("ProfileFragment", "Name: ${userData.name}")
//                Log.d("ProfileFragment", "Email: ${userData.email}")
//                Log.d("ProfileFragment", "ExpiresIn: ${userData.expiresIn}")
//            }
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







class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private lateinit var userPreferences: UserPreferences

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val profileViewModel =
            ViewModelProvider(this).get(ProfileViewModel::class.java)

        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Inisialisasi UserPreferences
        userPreferences = UserPreferences.getInstance(requireContext().userPreferencesDataStore)

        val textView: TextView = binding.textProfile
        val logoutButton: Button = binding.buttonLogout

        profileViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }

        logoutButton.setOnClickListener {
            // Logika untuk logout
            lifecycleScope.launch {
                userPreferences.clearLoginData()
                Toast.makeText(requireContext(), "Logged out", Toast.LENGTH_SHORT).show()

                // Pindah ke WelcomeActivity
                val intent = Intent(requireContext(), WelcomeActivity::class.java)
                startActivity(intent)
                // Jika Anda ingin menutup aktivitas ini (ProfileFragment)
                requireActivity().finish()
            }
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}