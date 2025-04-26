package com.example.mentorly

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.util.Calendar

private const val PREFS_NAME = "UserProfilePrefs"
private const val KEY_FULL_NAME = "full_name"
private const val KEY_AGE = "age"
private const val KEY_PROFESSION = "profession"
private const val PROFILE_IMAGE_FILENAME = "profile_image.jpg"
private const val PREFS_QUOTE_INDEX = "quote_index"
private const val PREFS_SESSION_DATE_TIME = "session_date_time"

class dashboard : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var greetingTextView: TextView
    private lateinit var profileNameTextView: TextView
    private lateinit var ageTextView: TextView
    private lateinit var professionTextView: TextView
    private lateinit var profileImageView: ImageButton // Using ImageButton here
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var tipTextView: TextView
    private lateinit var notificationTextView: TextView
    private val motivationalQuotes = arrayOf(
        "The journey of a thousand miles begins with a single step. - Lao Tzu",
        "Believe in yourself and all that you are. Know that there is something inside you that is greater than any obstacle. - Christian D. Larson",
        "The future belongs to those who believe in the beauty of their dreams. - Eleanor Roosevelt",
        "Success is not final, failure is not fatal: it is the courage to continue that counts. - Winston S. Churchill",
        "Strive not to be a success, but rather to be of value. - Albert Einstein",
        "The only way to do great work is to love what you do. - Steve Jobs",
        "Don't watch the clock; do what it does. Keep going. - Sam Levenson"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_dashboard)

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()
        val currentUser: FirebaseUser? = auth.currentUser

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

        // Find the greeting TextView
        greetingTextView = findViewById(R.id.tvGreeting)

        // Find the profile info TextViews
        profileNameTextView = findViewById(R.id.profilename)
        ageTextView = findViewById(R.id.age)
        professionTextView = findViewById(R.id.Profession) // Note the capital 'P'

        // Find the profile ImageView
        profileImageView = findViewById(R.id.profile)

        // Find the tip of the day TextView
        tipTextView = findViewById(R.id.tip)

        // Find the notification TextView
        notificationTextView = findViewById(R.id.notificationText)

        // Set the user's name in the greeting
        if (currentUser != null && currentUser.displayName != null) {
            val displayName = currentUser.displayName
            greetingTextView.text = "Hello, $displayName"
        } else {
            // If no display name is available, use a default
            greetingTextView.text = "Hello!"
        }

        // Load and display profile data
        loadProfileInfo()

        // Load and display profile image
        loadImageFromInternalStorage()

        // Display the quote for the current day
        displayDailyQuote()

        // Load and display the scheduled session time
        loadScheduledSession()

        // Top Profile ImageButton
        profileImageView.setOnClickListener { // Use the found ImageView here
            val intent = Intent(this, edit_profile::class.java)
            startActivity(intent)
        }

        // Notes
        val notesButton = findViewById<ImageButton>(R.id.btnNotes) // Corrected ID
        notesButton.setOnClickListener {
            val intent = Intent(
                this, activity_notes::class.java
            ) // Replace NotesActivity with your actual Notes activity
            startActivity(intent)
        }

        // Feedback
        val feedbackButton = findViewById<ImageButton>(R.id.btnFeedback)
        feedbackButton.setOnClickListener {
            val intent = Intent(
                this, activity_feedback::class.java
            ) // Replace FeedbackActivity with your actual Feedback activity
            startActivity(intent)
        }

        // Schedule Session Button
        val scheduleSessionButton = findViewById<ImageButton>(R.id.btnSession)
        scheduleSessionButton.setOnClickListener {
            val intent = Intent(this, book_session::class.java)
            startActivity(intent)
        }

        // Find your Mentor Button
        val findMentorButton = findViewById<ImageButton>(R.id.btnMentor)
        findMentorButton.setOnClickListener {
            val intent = Intent(this, mentor_list::class.java)
            startActivity(intent)
        }

    }

    private fun loadProfileInfo() {
        val fullName = sharedPreferences.getString(KEY_FULL_NAME, "")
        val age = sharedPreferences.getString(KEY_AGE, "")
        val profession = sharedPreferences.getString(KEY_PROFESSION, "")

        profileNameTextView.text = fullName
        ageTextView.text = "Age: $age"
        professionTextView.text = "Profession: $profession"
    }

    private fun loadImageFromInternalStorage() {
        try {
            val directory = getDir("profile_images", MODE_PRIVATE)
            val file = File(directory, PROFILE_IMAGE_FILENAME)
            if (file.exists()) {
                val inputStream = FileInputStream(file)
                val bitmap = BitmapFactory.decodeStream(inputStream)
                inputStream?.close()
                profileImageView.setImageBitmap(bitmap)
            }
        } catch (e: IOException) {
            e.printStackTrace()
            // Handle the error if the image doesn't exist or cannot be loaded
        }
    }

    private fun displayDailyQuote() {
        val calendar = Calendar.getInstance()
        val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) // Sunday is 1, Monday is 2, etc.
        val quoteIndex = (dayOfWeek - 1) % motivationalQuotes.size // Map day to quote index

        tipTextView.text = "Tip of the day: \"${motivationalQuotes[quoteIndex]}\""
        tipTextView.textSize = 16f // Adjust text size for better readability
        tipTextView.setTextColor(ContextCompat.getColor(this, R.color.deep_blue))
    }

    private fun loadScheduledSession() {
        val dateTime = sharedPreferences.getString(PREFS_SESSION_DATE_TIME, null)
        if (dateTime != null) {
            notificationTextView.text = "Scheduled Session: $dateTime"
        } else {
            notificationTextView.text = "No session scheduled yet."
        }
    }

    override fun onResume() {
        super.onResume()
        // Reload the scheduled session time when the dashboard becomes active again
        loadScheduledSession()
        // Reload the profile info here to reflect changes
        loadProfileInfo()

    }
}