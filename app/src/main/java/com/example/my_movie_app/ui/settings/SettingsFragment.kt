package com.example.my_movie_app.ui.settings

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.my_movie_app.App
import com.example.my_movie_app.R
import com.example.my_movie_app.databinding.FragmentSettingsBinding
import com.example.my_movie_app.login.LoginActivity

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SettingsViewModel by lazy {
        ViewModelProvider(this)[SettingsViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        binding.logOut.setOnClickListener {
            val ed: SharedPreferences.Editor =
                App.appContext.getSharedPreferences(
                    getString(R.string.app_name),
                    AppCompatActivity.MODE_PRIVATE
                )
                    .edit()
            ed.remove(getString(R.string.token))
            ed.apply()
            startActivity(Intent(requireActivity(), LoginActivity::class.java))
            requireActivity().finish()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}