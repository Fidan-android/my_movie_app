package com.example.my_movie_app

import android.text.Editable
import android.text.TextWatcher
import com.google.android.material.textfield.TextInputLayout

class BaseTextWatcher(
    private val inputLayout: TextInputLayout,
    private val delegate: (text: String) -> Unit
) : TextWatcher {

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        inputLayout.error = ""
    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

    override fun afterTextChanged(text: Editable?) {
        delegate(text.toString())
    }
}