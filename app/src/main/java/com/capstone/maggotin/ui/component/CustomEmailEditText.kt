package com.capstone.maggotin.ui.component

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import androidx.core.content.ContextCompat
import com.capstone.maggotin.R
import java.util.regex.Pattern

class CustomEmailEditText @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
) : TextInputEditText(context, attrs) {

    init {
        setupValidation()
    }

    private fun setupValidation() {
        this.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                validateEmail(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun validateEmail(email: String) {
        val parentLayout = parent.parent as? TextInputLayout
        if (!isValidEmail(email)) {
            parentLayout?.isErrorEnabled = true
            parentLayout?.error = context.getString(R.string.email_format_error)
            parentLayout?.setBoxStrokeColor(ContextCompat.getColor(context, android.R.color.holo_red_light))
        } else {
            parentLayout?.isErrorEnabled = false
            parentLayout?.error = null
            parentLayout?.setBoxStrokeColor(ContextCompat.getColor(context, android.R.color.holo_green_light))
        }
    }

    private fun isValidEmail(email: String): Boolean {
        val emailPattern = Pattern.compile(
            "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
        )
        return emailPattern.matcher(email).matches()
    }
}
