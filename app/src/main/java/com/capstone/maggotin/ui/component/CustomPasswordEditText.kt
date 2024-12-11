package com.capstone.maggotin.ui.component

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getString
import com.capstone.maggotin.R

class CustomPasswordEditText @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
) : TextInputEditText(context, attrs) {

    companion object {
        private const val MIN_PASSWORD_LENGTH = 8
    }

    init {
        setupValidation()
    }

    private fun setupValidation() {
        this.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                validatePassword(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun validatePassword(password: String) {
        val parentLayout = parent.parent as? TextInputLayout
        if (password.length < MIN_PASSWORD_LENGTH) {
            parentLayout?.isErrorEnabled = true
            parentLayout?.error = context.getString(R.string.password_min_length_error, MIN_PASSWORD_LENGTH)
            parentLayout?.setBoxStrokeColor(ContextCompat.getColor(context, android.R.color.holo_red_light))
        } else {
            parentLayout?.isErrorEnabled = false
            parentLayout?.error = null
            parentLayout?.setBoxStrokeColor(ContextCompat.getColor(context, android.R.color.holo_green_light))
        }
    }
}