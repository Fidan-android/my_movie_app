package com.example.my_movie_app.login

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.my_movie_app.BaseTextWatcher
import com.example.my_movie_app.MainActivity
import com.example.my_movie_app.Properties.RESULT_AUTH
import com.example.my_movie_app.R
import com.example.my_movie_app.api.ApiManager
import com.example.my_movie_app.api.IInternetConnected
import com.example.my_movie_app.databinding.ActivityLoginBinding
import com.example.my_movie_app.registration.RegistrationActivity
import com.google.android.material.snackbar.Snackbar

class LoginActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }
    private val viewModel by lazy {
        ViewModelProvider(this)[LoginViewModel::class.java]
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
                    "Отсутствует интернет соединение",
                    Snackbar.LENGTH_SHORT
                ).show()
            }

        })
        super.onStart()
        viewModel.onGetData().observe(this) {
            val ed: SharedPreferences.Editor =
                getSharedPreferences(getString(R.string.app_name), MODE_PRIVATE)
                    .edit()
            ed.putString(getString(R.string.token), it)
            ed.apply()
            startActivity(Intent(this, MainActivity::class.java))
            setResult(RESULT_AUTH)
            finish()
        }
        viewModel.onGetErrorMessage().observe(this) {
            binding.passwordLayout.error = it
        }
        viewModel.onProgressBar().observe(this) {
            binding.btnAuthorize.visibility = if (it) {
                View.GONE
            } else {
                View.VISIBLE
            }
        }

        binding.etLogin.addTextChangedListener(
            BaseTextWatcher(
                binding.passwordLayout,
                viewModel::onSaveLoginString
            )
        )
        binding.etPassword.addTextChangedListener(
            BaseTextWatcher(
                binding.passwordLayout,
                viewModel::onSavePasswordString
            )
        )
        binding.btnAuthorize.setOnClickListener {
            viewModel.onLoadData()
        }
        binding.registerNow.setOnClickListener {
            startActivity(Intent(this, RegistrationActivity::class.java))
        }
    }
}