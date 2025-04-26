package com.example.mentorly

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

private const val PREFS_NOTES = "MyNotesPrefs"
private const val KEY_NOTES_LIST = "notes_list"
data class Note(val text: String)

class activity_notes : AppCompatActivity() {

    private lateinit var noteEditText: EditText
    private lateinit var addNoteButton: Button
    private lateinit var notesContainer: LinearLayout
    private lateinit var sharedPreferences: SharedPreferences
    private val notesList = mutableListOf<Note>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notes)

        // Initialize views
        noteEditText = findViewById(R.id.noteEditText)
        addNoteButton = findViewById(R.id.btnAddNote)
        notesContainer = findViewById(R.id.notesContainer)
        sharedPreferences = getSharedPreferences(PREFS_NOTES, Context.MODE_PRIVATE)

        // Load saved notes
        loadNotes()
        displayNotes()

        // Add Note button logic
        addNoteButton.setOnClickListener {
            val noteText = noteEditText.text.toString().trim()

            if (noteText.isNotEmpty()) {
                val newNote = Note(noteText)
                notesList.add(newNote)
                saveNotes()
                displayNote(newNote)
                noteEditText.text.clear()
            } else {
                Toast.makeText(this, "Please enter a note.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loadNotes() {
        val gson = Gson()
        val json = sharedPreferences.getString(KEY_NOTES_LIST, null)
        // Specify the full package name for TypeToken from Gson
        val type = object : com.google.gson.reflect.TypeToken<List<Note>>() {}.type
        val savedNotes = gson.fromJson<List<Note>>(json, type)
        savedNotes?.let {
            notesList.addAll(it)
        }
    }

    private fun saveNotes() {
        val gson = Gson()
        val json = gson.toJson(notesList)
        sharedPreferences.edit().putString(KEY_NOTES_LIST, json).apply()
    }

    private fun displayNotes() {
        notesList.forEach { note ->
            displayNote(note)
        }
    }

    private fun displayNote(note: Note) {
        val noteView = TextView(this).apply {
            text = note.text
            setPadding(16, 16, 16, 16)
            setTextColor(ContextCompat.getColor(context, R.color.black))
            textSize = 16f
            background = ContextCompat.getDrawable(context, R.drawable.login_rounded_button)

            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            params.setMargins(0, 8, 0, 8)
            layoutParams = params

            setOnClickListener {
                showNoteDialog(note)
            }
        }

        val deleteButton = Button(this).apply {
            text = "Delete"
            setTextColor(ContextCompat.getColor(context, android.R.color.white))
            setBackgroundColor(ContextCompat.getColor(context, android.R.color.holo_red_dark))
            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            params.setMargins(8, 8, 0, 8)
            layoutParams = params
            setOnClickListener {
                deleteNote(note, noteView)
            }
        }

        val noteLayout = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            addView(noteView, LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f))
            addView(deleteButton)
        }

        notesContainer.addView(noteLayout)
    }

    private fun showNoteDialog(note: Note) {
        AlertDialog.Builder(this)
            .setTitle("Note")
            .setMessage(note.text)
            .setPositiveButton("OK", null)
            .show()
    }

    private fun deleteNote(noteToDelete: Note, noteLayoutToDelete: View) {
        AlertDialog.Builder(this)
            .setTitle("Delete Note")
            .setMessage("Are you sure you want to delete this note?")
            .setPositiveButton("Yes") { _, _ ->
                notesList.remove(noteToDelete)
                saveNotes()
                notesContainer.removeView(noteLayoutToDelete)
                Toast.makeText(this, "Note deleted.", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("No", null)
            .show()
    }
}