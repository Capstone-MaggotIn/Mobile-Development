package com.capstone.maggotin.ui.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.capstone.maggotin.databinding.ActivitySplashBinding
import com.capstone.maggotin.ui.main.MainActivity
import com.capstone.maggotin.ui.onboarding.OnboardingActivity

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        val sharedPreferences = getSharedPreferences("onboarding_prefs", MODE_PRIVATE)
        val isOnboardingCompleted = sharedPreferences.getBoolean("onboarding_completed", false)

        Handler(Looper.getMainLooper()).postDelayed({
            if (isOnboardingCompleted) {
                startActivity(Intent(this, MainActivity::class.java))
            } else {
                startActivity(Intent(this, OnboardingActivity::class.java))
            }
            finish()
        }, 3000)
    }
}