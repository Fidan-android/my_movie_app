package com.example.my_movie_app.ui.films

import android.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import com.example.my_movie_app.MainActivity
import com.example.my_movie_app.api.ApiManager
import com.example.my_movie_app.api.IInternetConnected
import com.example.my_movie_app.api.models.FilmsModel
import com.example.my_movie_app.conventions.RenderViewType
import com.example.my_movie_app.databinding.FragmentFilmsBinding
import com.example.my_movie_app.ui.adapters.RenderAdapter
import com.google.android.material.snackbar.Snackbar

class FilmsFragment : Fragment() {

    private var _binding: FragmentFilmsBinding? = null
    private val binding get() = _binding!!

    private val viewModel by lazy {
        ViewModelProvider(this)[FilmsViewModel::class.java]
    }
    private val adapter: RenderAdapter<FilmsModel> by lazy {
        RenderAdapter(
            RenderViewType.FilmsViewType.viewType,
            object : RenderAdapter.IItemClickListener {
                override fun onClick(position: Int) {
                    NavHostFragment.findNavController(this@FilmsFragment)
                        .navigate(
                            FilmsFragmentDirections.actionNavFilmsToFilmPageFragment(
                                viewModel.onGetData().value?.get(position)?.id!!
                            ),
                            NavOptions.Builder().setRestoreState(true).build()
                        )
                }
            })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFilmsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvFilms.adapter = adapter
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
            adapter.onUpdateRenderList(it)
        }
        binding.swipeRefresh.setOnRefreshListener {
            binding.swipeRefresh.isRefreshing = false
            viewModel.onLoadData()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}