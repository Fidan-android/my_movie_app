package com.example.my_movie_app.ui.dialogs

import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.my_movie_app.api.models.ProfileResponse
import com.example.my_movie_app.databinding.EditProfileDialogBinding

interface IEditProfileDialog {
    fun onAccept(firstName: String, middleName: String, lastName: String)
}

class EditProfileDialog(
    private val model: ProfileResponse,
    private val delegate: IEditProfileDialog
) : DialogFragment() {
    private var _binding: EditProfileDialogBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = EditProfileDialogBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.etFirstName.text = Editable.Factory.getInstance().newEditable(model.firstName)
        binding.etMiddleName.text = Editable.Factory.getInstance().newEditable(model.name)
        binding.etLastName.text = Editable.Factory.getInstance().newEditable(model.lastName ?: "")
    }

    override fun onStart() {
        super.onStart()

        binding.btnSaveChanges.setOnClickListener {
            if (binding.etFirstName.text.isNullOrEmpty() || binding.etMiddleName.text.isNullOrEmpty() || binding.etLastName.text.isNullOrEmpty()) {
                binding.lastNameLayout.error = "Заполните все поля"
            } else {
                delegate.onAccept(
                    binding.etFirstName.text.toString(),
                    binding.etMiddleName.text.toString(),
                    binding.etLastName.text.toString()
                )
                dismissAllowingStateLoss()
            }
        }
    }
}