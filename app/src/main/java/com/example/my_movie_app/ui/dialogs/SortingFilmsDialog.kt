package com.example.my_movie_app.ui.dialogs

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.my_movie_app.R
import com.example.my_movie_app.databinding.SortingFilmsDialogBinding

interface ISortingFilmsDialog {
    fun onChangeSortingParam(param: Int)
}

class SortingFilmsDialog(private val delegate: ISortingFilmsDialog) : DialogFragment() {

    private var _binding: SortingFilmsDialogBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = SortingFilmsDialogBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onStart() {
        super.onStart()

        val prefs = requireContext().getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE)
        binding.sortParams.check(prefs.getInt(getString(R.string.sort_type), -1))

        binding.sortParams.setOnCheckedChangeListener { _, i ->
            val editor = prefs.edit()
            editor.putInt("sort_type", i)
            editor.apply()

            delegate.onChangeSortingParam(i)
            dismissAllowingStateLoss()
        }

        binding.clearSorting.setOnClickListener {
            binding.sortParams.clearCheck()

            val editor = prefs.edit()
            editor.remove("sort_type")
            editor.apply()

            delegate.onChangeSortingParam(-1)
            dismissAllowingStateLoss()
        }
    }
}