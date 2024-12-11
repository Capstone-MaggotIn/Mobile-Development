package com.capstone.maggotin.ui.onboarding

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.capstone.maggotin.R
import com.capstone.maggotin.databinding.ActivityOnboardingBinding
import com.capstone.maggotin.ui.login.LoginActivity

class OnboardingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOnboardingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnboardingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        val items = listOf(
            OnboardingItem(
                R.drawable.ic_onboarding_1,
                getString(R.string.onboarding_title_1),
                getString(R.string.onboarding_desc_1)
            ),
            OnboardingItem(
                R.drawable.ic_onboarding_2,
                getString(R.string.onboarding_title_2),
                getString(R.string.onboarding_desc_2)
            ),
            OnboardingItem(
                R.drawable.ic_onboarding_3,
                getString(R.string.onboarding_title_3),
                getString(R.string.onboarding_desc_3)
            )
        )

        val adapter = OnboardingAdapter(items)
        binding.viewPager.adapter = adapter
        binding.dotsIndicator.setViewPager2(binding.viewPager)

        binding.finishButton.setOnClickListener {
            val sharedPreferences = getSharedPreferences("onboarding_prefs", MODE_PRIVATE)
            sharedPreferences.edit().putBoolean("onboarding_completed", true).apply()

            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
}