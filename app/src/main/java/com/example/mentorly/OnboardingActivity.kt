package com.example.mentorly

import OnboardingAdapter
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2

class OnboardingActivity : AppCompatActivity() {

    private lateinit var viewPager: ViewPager2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Check if onboarding was already completed
        val sharedPref = getSharedPreferences("app_prefs", Context.MODE_PRIVATE) // Use "app_prefs" here
        if (sharedPref.getBoolean("onboarding_completed", false)) {
            navigateToNext() // Renamed for clarity
            return
        }

        setContentView(R.layout.activity_onboarding)
        viewPager = findViewById(R.id.viewPager)

        val items = listOf(
            OnboardingItem(R.drawable.onboard1),
            OnboardingItem(R.drawable.onboard2),
            OnboardingItem(R.drawable.onboard3),
            OnboardingItem(R.drawable.onboard4),
            OnboardingItem(R.drawable.onboard5)
        )

        // Callback when onboarding completes
        val adapter = OnboardingAdapter(items) {
            sharedPref.edit().putBoolean("onboarding_completed", true).apply()
            navigateToNext() // Renamed for clarity
        }

        viewPager.adapter = adapter
    }

    private fun navigateToNext() {
        startActivity(Intent(this, WelcomeActivity::class.java)) // Or LoginActivity if that's the next step
        finish()
    }
}