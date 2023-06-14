package com.example.my_movie_app.ui.genres.filter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.example.my_movie_app.api.ApiManager
import com.example.my_movie_app.api.IInternetConnected
import com.example.my_movie_app.api.models.FilmModel
import com.example.my_movie_app.conventions.RenderViewType
import com.example.my_movie_app.databinding.FragmentFilmsBinding
import com.example.my_movie_app.ui.adapters.PaginationListener
import com.example.my_movie_app.ui.adapters.RenderAdapter
import com.google.android.material.snackbar.Snackbar


class FilteredFilmsFragment : Fragment() {

    private var _binding: FragmentFilmsBinding? = null
    private val binding get() = _binding!!
    private var paginationListener: PaginationListener? = null
    private val args: FilteredFilmsFragmentArgs by navArgs()

    private val viewModel by lazy {
        ViewModelProvider(this)[FilteredFilmsViewModel::class.java]
    }
    private val adapter: RenderAdapter<FilmModel> by lazy {
        RenderAdapter(
            RenderViewType.FilmsViewType.viewType,
            object : RenderAdapter.IItemClickListener {
                override fun onClick(position: Int) {
                    NavHostFragment.findNavController(this@FilteredFilmsFragment)
                        .navigate(
                            FilteredFilmsFragmentDirections.actionFilteredFilmsFragmentToFilmPageFragment(position),
                            NavOptions.Builder().setRestoreState(true).build()
                        )
                }
            })
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFilmsBinding.inflate(layoutInflater)
        binding.toolbarFilms.visibility = View.GONE
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvFilms.itemAnimator = null
        paginationListener = object :
            PaginationListener(binding.rvFilms.layoutManager as GridLayoutManager) {
            override fun getTotalCount(): Int {
                return viewModel.onGetTotalCount()
            }

            override fun isLoaded(): Boolean {
                return viewModel.onIsLoaded()
            }

            override fun loadMoreItems() {
                viewModel.onLoadData()
            }
        }
        binding.rvFilms.addOnScrollListener(paginationListener!!)
        binding.rvFilms.adapter = adapter

        viewModel.onSetGenreId(args.genreId)
    }

    override fun onStart() {
        ApiManager.setConnectCallback(requireContext(), object : IInternetConnected {
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
        viewModel.onGetErrorMessage().observe(viewLifecycleOwner) {
            Snackbar.make(binding.root, it, Snackbar.LENGTH_SHORT).show()
        }
        viewModel.onGetData().observe(viewLifecycleOwner) {
            adapter.onUpdateItems(it)
        }

        binding.swipeRefresh.setOnRefreshListener {
            binding.swipeRefresh.isRefreshing = false
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}