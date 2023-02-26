package com.example.my_movie_app.registration

import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.my_movie_app.BaseTextWatcher
import com.example.my_movie_app.api.ApiManager
import com.example.my_movie_app.api.IInternetConnected
import com.example.my_movie_app.databinding.ActivityRegistrationBinding
import com.google.android.material.snackbar.Snackbar

class RegistrationActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityRegistrationBinding.inflate(layoutInflater)
    }
    private val viewModel by lazy {
        ViewModelProvider(this)[RegistrationViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }

    override fun onStart() {
        ApiManager.setConnectCallback(this, object : IInternetConnected {
            override fun onConnect() {

            }

            override fun onLost() {
                Snackbar.make(
                    binding.root,
                    "Отсутствует интернет-соединение",
                    Snackbar.LENGTH_SHORT
                ).show()
            }

        })
        super.onStart()
        viewModel.onGetData().observe(this) {
            Snackbar.make(binding.root, it, Snackbar.LENGTH_SHORT).show()
            onBackPressed()
        }
        viewModel.onGetErrorMessage().observe(this) {
            binding.repeatPasswordLayout.error = it
        }
        viewModel.onProgressBar().observe(this) {
            binding.btnRegistration.visibility = if (it) {
                GONE
            } else {
                VISIBLE
            }
        }

        binding.etFirstName.addTextChangedListener(
            BaseTextWatcher(
                binding.repeatPasswordLayout,
                viewModel::onSaveFirstNameString
            )
        )
        binding.etMiddleName.addTextChangedListener(
            BaseTextWatcher(
                binding.repeatPasswordLayout,
                viewModel::onSaveNameString
            )
        )
        binding.etLogin.addTextChangedListener(
            BaseTextWatcher(
                binding.repeatPasswordLayout,
                viewModel::onSaveEmailString
            )
        )
        binding.etPassword.addTextChangedListener(
            BaseTextWatcher(
                binding.repeatPasswordLayout,
                viewModel::onSavePasswordString
            )
        )
        binding.etRepeatPassword.addTextChangedListener(
            BaseTextWatcher(
                binding.repeatPasswordLayout,
                viewModel::onSaveRepeatPasswordString
            )
        )
        binding.btnRegistration.setOnClickListener { viewModel.onLoadData() }
        binding.btnBack.setOnClickListener { onBackPressed() }
    }
}