package com.example.my_movie_app.ui.films

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.my_movie_app.MainActivity
import com.example.my_movie_app.Properties
import com.example.my_movie_app.R
import com.example.my_movie_app.api.ApiManager
import com.example.my_movie_app.api.IInternetConnected
import com.example.my_movie_app.api.models.FilmModel
import com.example.my_movie_app.conventions.RenderViewType
import com.example.my_movie_app.databinding.FragmentFilmsBinding
import com.example.my_movie_app.ui.adapters.RenderAdapter
import com.example.my_movie_app.ui.dialogs.ISortingFilmsDialog
import com.example.my_movie_app.ui.dialogs.SortingFilmsDialog
import com.google.android.material.snackbar.Snackbar


class FilmsFragment : Fragment() {

    private var _binding: FragmentFilmsBinding? = null
    private val binding get() = _binding!!
    private var searchView: SearchView? = null
    private var queryTextListener: SearchView.OnQueryTextListener? = null
    private var paginationListener: PaginationListener? = null

    private val viewModel by lazy {
        ViewModelProvider(this)[FilmsViewModel::class.java]
    }
    private val adapter: RenderAdapter<FilmModel> by lazy {
        RenderAdapter(
            RenderViewType.FilmsViewType.viewType,
            object : RenderAdapter.IItemClickListener {
                override fun onClick(position: Int) {
                    NavHostFragment.findNavController(this@FilmsFragment)
                        .navigate(
                            FilmsFragmentDirections.actionFilmsFragmentToFilmPageFragment(position),
                            NavOptions.Builder().setRestoreState(true).build()
                        )
                }
            })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.search_menu, menu)
        val searchItem = menu.findItem(R.id.action_search)
        val searchManager =
            requireActivity().getSystemService(Context.SEARCH_SERVICE) as SearchManager

        if (searchItem != null) {
            searchView = searchItem.actionView as SearchView
        }
        if (searchView != null) {
            searchView!!.setSearchableInfo(searchManager.getSearchableInfo(requireActivity().componentName))
            queryTextListener = object : SearchView.OnQueryTextListener {
                override fun onQueryTextChange(newText: String): Boolean {
                    binding.rvFilms.removeOnScrollListener(paginationListener!!)
                    viewModel.onFindData(newText)
                    return true
                }

                override fun onQueryTextSubmit(query: String) = true
            }
            searchView!!.setOnQueryTextListener(queryTextListener)
        }
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_search -> {
                return false
            }
            R.id.action_sort -> {
                SortingFilmsDialog(object : ISortingFilmsDialog {
                    override fun onChangeSortingParam(param: Int) {
                        Log.d("param", param.toString())
                        viewModel.onSortData(param)
                    }

                }).show(parentFragmentManager, SortingFilmsDialog::class.java.name)
                return false
            }
        }
        searchView?.setOnQueryTextListener(queryTextListener)
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFilmsBinding.inflate(layoutInflater)
        (requireActivity() as MainActivity).setSupportActionBar(binding.toolbarFilms)
        (requireActivity() as MainActivity).supportActionBar?.setTitle(R.string.title_films)
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

        val edit = requireContext().getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE).edit()
        edit.remove(getString(R.string.sort_type))
        edit.apply()
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
            viewModel.onRefreshData()

            val edit = requireContext().getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE).edit()
            edit.remove(getString(R.string.sort_type))
            edit.apply()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}