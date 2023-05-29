package com.example.my_movie_app.ui.dialogs

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.RatingBar.OnRatingBarChangeListener
import androidx.fragment.app.DialogFragment
import com.example.my_movie_app.R
import com.example.my_movie_app.databinding.CommnetDialogBinding
import com.example.my_movie_app.databinding.SortingFilmsDialogBinding

interface IAddCommentDialog {
    fun onAccept(stars: Int, comment: String)
}

class AddCommentDialog(private val delegate: IAddCommentDialog) : DialogFragment() {

    private var _binding: CommnetDialogBinding? = null
    private val binding get() = _binding!!
    private var countStars: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = CommnetDialogBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onStart() {
        super.onStart()

        binding.starsBar.onRatingBarChangeListener =
            OnRatingBarChangeListener { _, stars, _ ->
                countStars = stars.toInt()
            }

        binding.btnNext.setOnClickListener {
            binding.starsLayout.visibility = View.GONE
            binding.commentForm.visibility = View.VISIBLE
        }

        binding.btnComment.setOnClickListener {
            if (binding.etComment.text.toString().isNotEmpty()) {
                delegate.onAccept(countStars, binding.etComment.text.toString())
                dismissAllowingStateLoss()
            }
        }
    }
}