// book_session.kt
package com.example.mentorly

import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.CalendarView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import java.text.SimpleDateFormat
import java.util.*

private const val PREFS_NAME = "UserProfilePrefs"
private const val PREFS_SESSION_DATE_TIME = "session_date_time"
private const val PREFS_SELECTED_MENTOR = "selected_mentor"

class book_session : AppCompatActivity() {

    private lateinit var calendarView: CalendarView
    private lateinit var sessionInfo: TextView
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var mentorSpinner: Spinner
    private lateinit var saveSessionButton: AppCompatButton
    private var selectedDate: Long = 0L
    private var selectedTimeInMillis: Long = 0L
    private var selectedMentor: String = "Select Mentor"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_session)

        // Initialize views
        calendarView = findViewById(R.id.datepicker)
        sessionInfo = findViewById(R.id.sessionInfo)
        mentorSpinner = findViewById(R.id.mentorSpinnerSession)
        saveSessionButton = findViewById(R.id.btnsavesession)
        sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

        // Mentor Spinner setup
        val mentors = arrayOf(
            "Select Mentor", "Anupam Mittal", "Aman Gupta", "Ashneer Grover",
            "Peyush Bansal", "Vineeta Singh", "Namita Thapar",
            "Ghazal Alagh", "Ritesh Agarwal", "Dipender Goyal", "Amit Jain"
        )
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, mentors)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        mentorSpinner.adapter = adapter

        mentorSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedMentor = mentors[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Another item was selected or the selection cleared.
                // You can leave this empty if you don't need to handle this case.
            }
        }

        // Handle date selection
        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val calendar = Calendar.getInstance()
            calendar.set(year, month, dayOfMonth, 0, 0, 0) // Set time to start of day
            selectedDate = calendar.timeInMillis
            showTimePicker()
        }

        // Handle save session button click
        saveSessionButton.setOnClickListener {
            if (selectedMentor == "Select Mentor") {
                Toast.makeText(this, "Please select a mentor", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (selectedDate == 0L || selectedTimeInMillis == 0L) {
                Toast.makeText(this, "Please select a date and time", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val sessionDateTime = "$selectedMentor on ${formatDateTime(selectedDate + selectedTimeInMillis)}"
            saveScheduledSession(sessionDateTime, selectedMentor)
            Toast.makeText(this, "Session scheduled!", Toast.LENGTH_SHORT).show()
            finish() // Go back to dashboard
        }

        // Load previously selected mentor if any
        val savedMentor = sharedPreferences.getString(PREFS_SELECTED_MENTOR, "Select Mentor") ?: "Select Mentor"
        val mentorPosition = mentors.indexOf(savedMentor)
        if (mentorPosition != -1) {
            mentorSpinner.setSelection(mentorPosition)
            selectedMentor = savedMentor
        }
    }

    private fun showTimePicker() {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(
            this,
            { _, selectedHour, selectedMinute ->
                val cal = Calendar.getInstance()
                cal.set(Calendar.HOUR_OF_DAY, selectedHour)
                cal.set(Calendar.MINUTE, selectedMinute)
                cal.set(Calendar.SECOND, 0)
                cal.set(Calendar.MILLISECOND, 0)
                selectedTimeInMillis = cal.timeInMillis - (cal.timeInMillis % 60000) // Round to nearest minute

                val sessionDateTime = "$selectedMentor on ${formatDateTime(selectedDate + selectedTimeInMillis)}"
                sessionInfo.text = "Session Scheduled: $sessionDateTime"
            },
            hour,
            minute,
            false
        )
        timePickerDialog.show()
    }

    private fun formatDateTime(timeInMillis: Long): String {
        val formatter = SimpleDateFormat("dd/MM/yyyy 'at' hh:mm a", Locale.getDefault())
        return formatter.format(Date(timeInMillis))
    }

    private fun saveScheduledSession(dateTime: String, mentor: String) {
        sharedPreferences.edit()
            .putString(PREFS_SESSION_DATE_TIME, dateTime)
            .putString(PREFS_SELECTED_MENTOR, mentor)
            .apply()
    }
}