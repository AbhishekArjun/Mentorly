package com.example.mentorly

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class mentordetailspage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_mentordetailspage)

        val mentorImageView: ImageView = findViewById(R.id.mentor_image)
        val mentorNameTextView: TextView = findViewById(R.id.mentor_name)
        val mentorTitleTextView: TextView = findViewById(R.id.mentor_title) // New ID
        val mentorDescriptionTextView: TextView = findViewById(R.id.mentor_description)



        // Retrieve the mentor data passed from the Intent
        val mentorName = intent.getStringExtra("mentor_name")
        val mentorImageResource = intent.getIntExtra("mentor_image", 0) // 0 is a default value
        val mentorTitle = intent.getStringExtra("mentor_title")
        val mentorDescription = intent.getStringExtra("mentor_description")
        val mentorReviewsArray = intent.getStringArrayExtra("mentor_reviews")

        // Set the data to the views
        mentorNameTextView.text = mentorName
        mentorTitleTextView.text = mentorTitle // Set the title
        if (mentorImageResource != 0) {
            mentorImageView.setImageResource(mentorImageResource)
        }
        mentorDescriptionTextView.text = mentorDescription


    }
}