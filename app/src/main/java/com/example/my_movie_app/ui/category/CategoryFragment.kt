package com.example.my_movie_app.ui.category

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.my_movie_app.R
import com.example.my_movie_app.api.ApiManager
import com.example.my_movie_app.api.IInternetConnected
import com.example.my_movie_app.api.models.CategoryModel
import com.example.my_movie_app.conventions.RenderViewType
import com.example.my_movie_app.databinding.FragmentCategoryBinding
import com.example.my_movie_app.ui.adapters.RenderAdapter
import com.google.android.material.snackbar.Snackbar

class CategoryFragment : Fragment() {

    private var _binding: FragmentCategoryBinding? = null
    private val binding get() = _binding!!
    private val viewModel by lazy {
        ViewModelProvider(this)[CategoryViewModel::class.java]
    }
    private val adapter: RenderAdapter<CategoryModel> by lazy {
        RenderAdapter(
            RenderViewType.CategoryViewType.viewType,
            object : RenderAdapter.IItemClickListener {
                override fun onClick(position: Int) {
                    val ed: SharedPreferences.Editor =
                        requireActivity().getSharedPreferences(
                            getString(R.string.app_name),
                            AppCompatActivity.MODE_PRIVATE
                        )
                            .edit()
                    ed.putInt(
                        getString(R.string.category_id),
                        viewModel.onGetData().value?.get(position)?.id!!
                    )
                    ed.apply()
                }
            })
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCategoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvCategories.adapter = adapter
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