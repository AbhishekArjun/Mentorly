package com.example.mentorly

import android.os.Bundle
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.view.setMargins
import androidx.core.view.setPadding

class viewFeedback : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_view_feedback)

        val feedbackContainer: LinearLayout = findViewById(R.id.feedbackContainer)
        val backButton: AppCompatButton = findViewById(R.id.backBtn)
        val textColorBlack = ContextCompat.getColor(this, R.color.black)
        val textColorSecondary = ContextCompat.getColor(this, android.R.color.darker_gray)

        val sharedPreferences = getSharedPreferences("MentorFeedbacks", MODE_PRIVATE)
        val allEntries = sharedPreferences.all

        for ((mentorName, feedback) in allEntries) {
            if (mentorName != "Select Mentor") {
                // CardView for each feedback entry
                val cardView = CardView(this)
                val cardViewParams = LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT)
                cardViewParams.setMargins(8)
                cardView.layoutParams = cardViewParams
                cardView.cardElevation = 2f
                cardView.setBackgroundResource(R.drawable.login_rounded_button) // Set the drawable here
                feedbackContainer.addView(cardView)

                val innerLinearLayout = LinearLayout(this)
                innerLinearLayout.orientation = LinearLayout.VERTICAL
                innerLinearLayout.setPadding(16)
                cardView.addView(innerLinearLayout)

                // Mentor Name TextView
                val mentorTextView = TextView(this)
                mentorTextView.text = "$mentorName:"
                mentorTextView.textSize = 18f
                mentorTextView.setTextColor(textColorBlack)
                innerLinearLayout.addView(mentorTextView)


                // Feedback TextView
                val feedbackParts = feedback.toString().trim().split(" - ", limit = 2)
                val rating = feedbackParts.getOrNull(0) ?: ""
                val comment = feedbackParts.getOrNull(1) ?: ""

                val feedbackTextView = TextView(this)
                feedbackTextView.text = "$rating  $comment" // Combine rating and comment
                feedbackTextView.textSize = 16f
                feedbackTextView.setTextColor(textColorSecondary) // Less prominent color
                innerLinearLayout.addView(feedbackTextView)
            }
        }

        backButton.setOnClickListener {
            finish()
        }
    }
}