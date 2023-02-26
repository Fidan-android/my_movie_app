package com.example.my_movie_app.ui.films.film_page

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import com.example.my_movie_app.MainActivity
import com.example.my_movie_app.R
import com.example.my_movie_app.api.ApiManager
import com.example.my_movie_app.api.IInternetConnected
import com.example.my_movie_app.databinding.FragmentFilmPageBinding
import com.google.android.material.snackbar.Snackbar

class FilmPageFragment : Fragment() {

    private var _binding: FragmentFilmPageBinding? = null
    private val binding get() = _binding!!
    private val args: FilmPageFragmentArgs by navArgs()

    private val viewModel by lazy {
        ViewModelProvider(this)[FilmPageViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = FragmentFilmPageBinding.inflate(layoutInflater)
        with((requireActivity() as MainActivity)) {
            supportActionBar?.hide()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.onSaveFilmId(args.filmId)
    }

    override fun onStart() {
        ApiManager.setConnectCallback(requireContext(), object : IInternetConnected {
            override fun onConnect() {
                viewModel.onLoadData()
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
        viewModel.onGetData().observe(viewLifecycleOwner) {
            Log.d("bg_image", it.bgImage.toString())
            Glide
                .with(requireContext())
                .load(
                    GlideUrl(
                        "http://n931333e.beget.tech/api/v3/img/" + it.bgImage,
                        LazyHeaders.Builder()
                            .addHeader("User-Agent", "Mozilla/5.0")
                            .build()
                    )
                )
                .fitCenter()
                .error(R.drawable.logo)
                .into(binding.filmImage)
            Glide
                .with(requireContext())
                .load(
                    GlideUrl(
                        "http://n931333e.beget.tech/api/v3/img/" + it.image,
                        LazyHeaders.Builder()
                            .addHeader("User-Agent", "Mozilla/5.0")
                            .build()
                    )
                )
                .centerCrop()
                .error(R.drawable.logo)
                .into(binding.logoFilm)

            binding.titleFilm.text = it.title
            binding.authorFilm.text = it.author
            binding.timeFilm.text =
                if (it.time!! < 60) it.time.toString().plus("min") else (it.time / 60).toString()
                    .plus("h ") + (it.time % 60).toString().plus("min")
            binding.description.text = it.description
        }
        viewModel.onProgressBar().observe(this) {
            if (it) {
                binding.contentLayout.visibility = View.GONE
                binding.progressContent.visibility = View.VISIBLE
            } else {
                binding.contentLayout.visibility = View.VISIBLE
                binding.progressContent.visibility = View.GONE
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}