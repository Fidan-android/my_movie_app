package com.example.my_movie_app

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.*
import com.example.my_movie_app.api.ApiHelper
import com.example.my_movie_app.api.ApiManager
import com.example.my_movie_app.api.IInternetConnected
import com.example.my_movie_app.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!
    private var selectedItem = -1
    private val navHostFragment by lazy {
        supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        try {
            MainScope().launch(Dispatchers.IO) {
                val response = ApiHelper.getFavouriteMovies().execute()

                if (response.isSuccessful) {
                    Properties.favouriteMovies.postValue(response.body()?.favourites ?: mutableListOf())
                }
            }
        } catch (ex: Exception) {
            Log.e("Error", ex.printStackTrace().toString())
        }

        navHostFragment.navController.graph =
            navHostFragment.navController.navInflater.inflate(R.navigation.nav_graph)
        NavigationUI.setupWithNavController(binding.bottomMenu, navHostFragment.navController)
        binding.bottomMenu.setOnItemSelectedListener { item ->
            if (selectedItem == -1 || item.itemId != selectedItem) {
                when (item.itemId) {
                    R.id.filmsFragment -> {
                        navHostFragment.navController.navigate(
                            R.id.filmsFragment,
                            Bundle(),
                            NavOptions.Builder()
                                .setPopUpTo(R.id.filmsFragment, true)
                                .build()
                        )
                    }
                    R.id.genreFragment -> {
                        navHostFragment.navController.navigate(
                            R.id.genreFragment,
                            Bundle(),
                            NavOptions.Builder()
                                .setPopUpTo(R.id.genreFragment, true)
                                .build()
                        )
                    }
                    R.id.profileFragment -> {
                        navHostFragment.navController.navigate(
                            R.id.profileFragment,
                            Bundle(),
                            NavOptions.Builder()
                                .setPopUpTo(R.id.profileFragment, true)
                                .build()
                        )
                    }
                }
                selectedItem = item.itemId
                return@setOnItemSelectedListener true
            } else {
                return@setOnItemSelectedListener false
            }
        }
    }

    override fun onStart() {
        ApiManager.setConnectCallback(this, object : IInternetConnected {
            override fun onConnect() {

            }

            override fun onLost() {
                Snackbar.make(
                    _binding?.root!!,
                    getString(R.string.not_network_connect),
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        })
        super.onStart()
        supportFragmentManager.addOnBackStackChangedListener {
            binding.bottomMenu.menu.getItem(0).isChecked = true
        }

        navHostFragment.navController.addOnDestinationChangedListener { _, destination, _ ->
            if(destination.id == R.id.filmPageFragment) {
                binding.bottomMenu.isGone = true
            } else {
                binding.bottomMenu.isVisible = true
            }
        }
    }

    override fun onStop() {
        ApiManager.unsetConnectCallback()
        super.onStop()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        selectedItem = -1
    }
}