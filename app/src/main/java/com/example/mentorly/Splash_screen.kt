package com.example.mentorly

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class Splash_screen : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        // Check if onboarding is completed
        val sharedPref = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        val isOnboardingCompleted = sharedPref.getBoolean("onboarding_completed", false)
        Log.d("SplashScreen", "Onboarding Completed: $isOnboardingCompleted")

        // Check current Firebase user
        val currentUser = auth.currentUser
        Log.d("SplashScreen", "Current Firebase User: $currentUser")

        Handler(Looper.getMainLooper()).postDelayed({
            if (!isOnboardingCompleted) {
                Log.d("SplashScreen", "Navigating to OnboardingActivity")
                startActivity(Intent(this, OnboardingActivity::class.java))
            } else {
                if (currentUser != null) {
                    Log.d("SplashScreen", "User logged in, navigating to DashboardActivity")
                    startActivity(Intent(this, dashboard::class.java))
                } else {
                    Log.d("SplashScreen", "User not logged in, navigating to LoginActivity")
                    startActivity(Intent(this, LoginActivity::class.java))
                }
            }
            finish() // Prevent going back to Splash Screen
        }, 2500) // 2.5 seconds delay
    }
}