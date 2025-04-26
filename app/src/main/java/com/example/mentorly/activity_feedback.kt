package com.example.mentorly

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton

class activity_feedback : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feedback)

        val mentorSpinner: Spinner = findViewById(R.id.mentorSpinner)

        val mentors = arrayOf(
            "Select Mentor", "Anupam Mittal", "Aman Gupta", "Ashneer Grover",
            "Peyush Bansal", "Vineeta Singh", "Namita Thapar",
            "Ghazal Alagh", "Ritesh Agarwal", "Dipender Goyal", "Amit Jain"
        )

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, mentors)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        mentorSpinner.adapter = adapter

        val feedbackInput = findViewById<EditText>(R.id.feedbackInput)
        val ratingBar = findViewById<RatingBar>(R.id.ratingBar)
        val submitBtn = findViewById<AppCompatButton>(R.id.submitFeedbackBtn)
        val viewFeedbackBtn = findViewById<AppCompatButton>(R.id.viewFeedbackBtn)

        submitBtn.setOnClickListener {
            val selectedMentor = mentorSpinner.selectedItem.toString()
            val feedback = feedbackInput.text.toString().trim()
            val rating = ratingBar.rating

            if (selectedMentor == "Select Mentor") {
                Toast.makeText(this, "Please select a mentor!", Toast.LENGTH_SHORT).show()
            } else if (feedback.isEmpty()) {
                Toast.makeText(this, "Please enter your feedback.", Toast.LENGTH_SHORT).show()
            } else {
                val sharedPreferences = getSharedPreferences("MentorFeedbacks", MODE_PRIVATE)
                val existingFeedback = sharedPreferences.getString(selectedMentor, "")
                val newFeedback = "$existingFeedback\n⭐ $rating - $feedback"
                sharedPreferences.edit().putString(selectedMentor, newFeedback).apply()

                Toast.makeText(this, "Feedback submitted for $selectedMentor.", Toast.LENGTH_SHORT).show()
                feedbackInput.text.clear()
                ratingBar.rating = 0f
                mentorSpinner.setSelection(0)
            }
        }

        viewFeedbackBtn.setOnClickListener {
            startActivity(Intent(this, viewFeedback::class.java))
        }
    }
}
