package com.example.my_movie_app.ui.cinema

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavHost
import androidx.navigation.fragment.NavHostFragment
import com.example.my_movie_app.MainActivity
import com.example.my_movie_app.R
import com.example.my_movie_app.api.ApiManager
import com.example.my_movie_app.api.IInternetConnected
import com.example.my_movie_app.api.models.CinemaModel
import com.example.my_movie_app.conventions.RenderViewType
import com.example.my_movie_app.databinding.FragmentCinemaBinding
import com.example.my_movie_app.ui.adapters.RenderAdapter
import com.google.android.material.snackbar.Snackbar

class CinemaFragment : Fragment() {

    private var _binding: FragmentCinemaBinding? = null
    private val binding get() = _binding!!
    private val viewModel by lazy {
        ViewModelProvider(this)[CinemaViewModel::class.java]
    }

    private val adapter: RenderAdapter<CinemaModel> by lazy {
        RenderAdapter(
            RenderViewType.CinemaViewType.viewType,
            object : RenderAdapter.IItemClickListener {
                override fun onClick(position: Int) {
                    
                }
            })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.cinema_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_map -> {
                NavHostFragment.findNavController(this@CinemaFragment)
                    .navigate(CinemaFragmentDirections.actionCinemaFragmentToMapFragment())
                return false
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCinemaBinding.inflate(inflater, container, false)
        (requireActivity() as MainActivity).setSupportActionBar(binding.toolbarCinema)
        (requireActivity() as MainActivity).supportActionBar?.setTitle(R.string.title_cinema)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvCinemas.adapter = adapter
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
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}