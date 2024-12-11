package com.capstone.maggotin.ui.login

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.capstone.maggotin.R
import com.capstone.maggotin.data.remote.response.LoginResponse
import com.capstone.maggotin.databinding.ActivityLoginBinding
import com.capstone.maggotin.ui.main.MainActivity
import com.capstone.maggotin.ui.signup.SignupActivity
import com.dicoding.picodiploma.loginwithanimation.view.ViewModelFactory

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private var isErrorDialogShown = false

    private val viewModel by viewModels<LoginViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.isLoading.observe(this) { isLoading ->
            if (isLoading) {
                binding.progressBar.visibility = View.VISIBLE
            } else {
                binding.progressBar.visibility = View.GONE
            }
        }

        setupView()
        setupAction()
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    private fun setupAction() {
        viewModel.loginResult.observe(this) { result ->
            result?.let {
                handleLoginResult(it)
            }
        }

        binding.btnLogin.setOnClickListener {
            val email = binding.emailEditText.text.toString().trim()
            val password = binding.passwordEditText.text.toString().trim()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                viewModel.clearLoginResult()
                viewModel.login(email, password)
            } else {
                binding.emailEditTextLayout.error = if (email.isEmpty()) getString(R.string.input_email) else null
                binding.passwordEditTextLayout.error = if (password.isEmpty()) getString(R.string.input_password) else null
            }
        }

        binding.btnSignup.setOnClickListener {
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
        }
    }


    private fun handleLoginResult(result: Result<LoginResponse>) {
        result.onSuccess { response ->
            if (response.error == true) {
                showErrorDialog(getString(R.string.login_failed), response.message ?: getString(R.string.login_failed_message))
            } else {
                showSuccessDialog(getString(R.string.login_success),
                    getString(R.string.login_success_message, response.loginResult?.name))
            }
        }
        result.onFailure { throwable ->
            showErrorDialog(getString(R.string.login_failed), throwable.message ?: getString(R.string.login_failed_message))
        }
    }

    private fun showErrorDialog(title: String, message: String) {
        isErrorDialogShown = true
        AlertDialog.Builder(this).apply {
            setTitle(title)
            setMessage(message)
            setPositiveButton(getString(R.string.try_again)) { _, _ ->
                isErrorDialogShown = false
            }
            setOnDismissListener {
                isErrorDialogShown = false
            }
            create()
            show()
        }
    }

    private fun showSuccessDialog(title: String, message: String) {
        AlertDialog.Builder(this).apply {
            setTitle(title)
            setMessage(message)
            setPositiveButton(getString(R.string.next)) { _, _ ->
                navigateToMain()
            }
            create()
            show()
        }
    }

    private fun navigateToMain() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        finish()
    }
}