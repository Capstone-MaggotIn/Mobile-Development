package com.capstone.maggotin.ui.signup

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.capstone.maggotin.data.UserRepository
import com.capstone.maggotin.data.remote.retrofit.ApiClient
import com.capstone.maggotin.data.pref.UserPreference
import com.capstone.maggotin.data.pref.dataStore
import com.capstone.maggotin.databinding.ActivitySignupBinding

class SignupActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignupBinding

    private val userRepository by lazy {
        UserRepository.getInstance(
            UserPreference.getInstance(this.dataStore),
            ApiClient.instance
        )
    }

    private val viewModel: SignupViewModel by viewModels {
        SignupViewModelFactory(userRepository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        binding.btnSignup.setOnClickListener {
            val name = binding.nameEditText.text.toString().trim()
            val email = binding.emailEditText.text.toString().trim()
            val password = binding.passwordEditText.text.toString().trim()
            val confirmPassword = binding.passworConfirmdEditText.text.toString().trim()

            when {
                name.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() -> {
                    showAlertDialog("Error", "Semua kolom harus diisi!")
                }
                !checkPasswordMatch(password, confirmPassword) -> {
                    Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    viewModel.register(name, email, password, confirmPassword)
                    observeRegistrationResponse()
                }
            }
        }
    }

    private fun observeRegistrationResponse() {
        viewModel.registrationResponse.observe(this) { response ->
            if (response?.error == false) {
                showAlertDialog("Berhasil!", "Registrasi berhasil. Silakan login.") {
                    finish()
                }
            } else {
                showAlertDialog("Error", response?.message ?: "Something went wrong")
            }
        }
    }

    private fun showAlertDialog(title: String, message: String, onPositiveClick: (() -> Unit)? = null) {
        AlertDialog.Builder(this).apply {
            setTitle(title)
            setMessage(message)
            setPositiveButton("OK") { _, _ -> onPositiveClick?.invoke() }
            create()
            show()
        }
    }

    private fun checkPasswordMatch(password: CharSequence?, confirmPassword: CharSequence?): Boolean {
        return password == confirmPassword
    }
}