package com.example.my_movie_app.ui.genres

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import com.example.my_movie_app.MainActivity
import com.example.my_movie_app.R
import com.example.my_movie_app.api.models.GenreModel
import com.example.my_movie_app.conventions.RenderViewType
import com.example.my_movie_app.databinding.FragmentGenreBinding
import com.example.my_movie_app.ui.adapters.RenderAdapter
import com.google.android.material.snackbar.Snackbar

class GenreFragment : Fragment() {

    private var _binding: FragmentGenreBinding? = null
    private val binding get() = _binding!!
    private val viewModel by lazy {
        ViewModelProvider(this)[GenreViewModel::class.java]
    }

    private val adapter: RenderAdapter<GenreModel> by lazy {
        RenderAdapter(
            RenderViewType.GenresViewType.viewType,
            object : RenderAdapter.IItemClickListener {
                override fun onClick(position: Int) {
                    NavHostFragment.findNavController(this@GenreFragment)
                        .navigate(
                            GenreFragmentDirections.actionGenreFragmentToFilteredFilmsFragment(position),
                            NavOptions.Builder().setRestoreState(true).build()
                        )
                }
            })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGenreBinding.inflate(inflater, container, false)
        (requireActivity() as MainActivity).setSupportActionBar(binding.toolbarCinema)
        (requireActivity() as MainActivity).supportActionBar?.setTitle(R.string.title_genre)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvGenres.adapter = adapter
    }

    override fun onStart() {
        super.onStart()
        viewModel.onGetErrorMessage().observe(viewLifecycleOwner) {
            Snackbar.make(binding.root, it, Snackbar.LENGTH_SHORT).show()
        }
        viewModel.onGetData().observe(viewLifecycleOwner) {
            adapter.onUpdateItems(it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}