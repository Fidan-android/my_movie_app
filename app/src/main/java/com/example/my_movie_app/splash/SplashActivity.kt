package com.example.my_movie_app.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.example.my_movie_app.App
import com.example.my_movie_app.MainActivity
import com.example.my_movie_app.R
import com.example.my_movie_app.databinding.ActivitySplashBinding
import com.example.my_movie_app.login.LoginActivity

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivitySplashBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        Handler(Looper.getMainLooper()).postDelayed({
            if (App.appContext.getSharedPreferences(
                    App.appContext.getString(R.string.app_name),
                    MODE_PRIVATE
                ).getString("token", "") != ""
            ) {
                startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                finish()
            } else {
                startActivity(Intent(this@SplashActivity, LoginActivity::class.java))
                finish()
            }
        }, 2000)
    }
}