package com.example.my_movie_app.ui.films.film_page

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import com.example.my_movie_app.MainActivity
import com.example.my_movie_app.Properties
import com.example.my_movie_app.R
import com.example.my_movie_app.api.ApiManager
import com.example.my_movie_app.api.IInternetConnected
import com.example.my_movie_app.api.models.GenreModel
import com.example.my_movie_app.api.models.StaffModel
import com.example.my_movie_app.conventions.RenderViewType
import com.example.my_movie_app.databinding.FragmentFilmPageBinding
import com.example.my_movie_app.ui.adapters.RenderAdapter
import com.example.my_movie_app.ui.dialogs.AddCommentDialog
import com.example.my_movie_app.ui.dialogs.IAddCommentDialog
import com.google.android.material.snackbar.Snackbar
import java.text.SimpleDateFormat
import java.util.*
import kotlin.properties.Delegates

class FilmPageFragment : Fragment() {

    private var _binding: FragmentFilmPageBinding? = null
    private val binding get() = _binding!!
    private val args: FilmPageFragmentArgs by navArgs()
    private var isChecked: Boolean = false

    private val viewModel by lazy {
        ViewModelProvider(this)[FilmPageViewModel::class.java]
    }
    private val adapter: RenderAdapter<StaffModel> by lazy {
        RenderAdapter(
            RenderViewType.StaffViewType.viewType,
            object : RenderAdapter.IItemClickListener {
                override fun onClick(position: Int) {

                }
            })
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

        if (Properties.favouriteMovies.value?.any { item -> item.kinopoiskId == args.filmId || item.filmId == args.filmId } == true) {
            isChecked = !isChecked
            binding.likesFilm.isActivated = isChecked
        }
        binding.rvStaff.adapter = adapter
    }

    @SuppressLint("SetJavaScriptEnabled", "SimpleDateFormat")
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
            Glide
                .with(requireContext())
                .load(
                    GlideUrl(
                        it.posterUrlPreview,
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
                        it.posterUrl,
                        LazyHeaders.Builder()
                            .addHeader("User-Agent", "Mozilla/5.0")
                            .build()
                    )
                )
                .centerCrop()
                .error(R.drawable.logo)
                .into(binding.logoFilm)

            binding.titleFilm.text = it.nameRu
            binding.titleFilm.isSelected = true
            binding.authorFilm.text =
                it.countries?.joinToString(", ") { country -> country.country }
                    ?.plus(", " + it.year)

            if (it.filmLength != null) {
                val minutes = if (!it.filmLength.contains(':')) {
                    it.filmLength.toInt()
                } else {
                    SimpleDateFormat("mm").format(Date(SimpleDateFormat("HH:mm").parse(it.filmLength.toString())!!.time))
                        .toInt()
                }

                binding.timeFilm.text =
                    when {
                        minutes < 60 -> minutes.toString()
                            .plus("мин")
                        else -> (minutes / 60).toString()
                            .plus("ч ") + (minutes % 60).toString().plus("мин")
                    }
            }
            binding.description.text = it.description
            binding.ratingFilm.text =
                it.ratingKinopoisk?.toString() ?: it.ratingImdb?.toString() ?: "0.0"
        }
        viewModel.onGetStaff().observe(viewLifecycleOwner) {
            adapter.onUpdateItems(it)
        }
        viewModel.onGetVideos().observe(viewLifecycleOwner) {
            binding.webView.isGone = it.videoItems.isEmpty()
            binding.videoView.isGone = it.videoItems.isEmpty()

            if (it.videoItems.isNotEmpty()) {
                binding.videoLayout.isVisible = true

                val url = it.videoItems.first().url
                if (url.startsWith("https://www.youtube.com/")) {
                    binding.webView.isVisible = true
                    binding.videoView.isVisible = false
                    binding.webView.apply {
                        settings.javaScriptEnabled = true
                        loadData(
                            initIframeVideo("https://www.youtube.com/embed/${url.substringAfter("v/")}?autoplay=1&vq=small"),
                            "text/html",
                            "utf-8"
                        )
                        webChromeClient = WebChromeClient()
                    }
                } else {
                    binding.webView.isVisible = true
                    binding.videoView.isVisible = false
                    binding.webView.apply {
                        settings.javaScriptEnabled = true
                        loadUrl(url)
                        webChromeClient = WebChromeClient()
                    }
                }
            }
        }
        viewModel.onProgressBar().observe(viewLifecycleOwner) {
            if (it) {
                binding.contentLayout.visibility = View.GONE
                binding.progressContent.visibility = View.VISIBLE
            } else {
                binding.contentLayout.visibility = View.VISIBLE
                binding.progressContent.visibility = View.GONE
            }
        }

        binding.likesFilm.setOnClickListener {
            isChecked = !isChecked
            binding.likesFilm.isActivated = isChecked
            viewModel.onFavouriteMovie(isChecked)
        }

        binding.commentFilm.setOnClickListener {
            AddCommentDialog(object : IAddCommentDialog {
                override fun onAccept(stars: Int, comment: String) {
                    viewModel.onCreateComment(stars, comment)
                }
            }).show(parentFragmentManager, AddCommentDialog::class.java.name)
        }

        binding.btnBack.setOnClickListener {
            NavHostFragment.findNavController(this@FilmPageFragment).popBackStack()
        }
    }

    private fun initIframeVideo(videoUrl: String) =
        "<iframe width=\"100%\" height=\"100%\" src=\"$videoUrl\" " +
                "frameborder=\"0\" allow=\"accelerometer; autoplay;" +
                " encrypted-media; gyroscope; picture-in-picture\" allowfullscreen></iframe>"


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}