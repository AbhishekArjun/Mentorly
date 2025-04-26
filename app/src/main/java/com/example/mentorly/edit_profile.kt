package com.example.mentorly

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException

private const val REQUEST_IMAGE_PICK = 100
private const val REQUEST_STORAGE_PERMISSION = 101
private const val PROFILE_IMAGE_FILENAME = "profile_image.jpg"
private const val PREFS_NAME = "UserProfilePrefs"
private const val KEY_FULL_NAME = "full_name"
private const val KEY_AGE = "age"
private const val KEY_PROFESSION = "profession"
private const val KEY_EXPERIENCE = "experience"
private const val KEY_BIO = "bio"

class edit_profile : AppCompatActivity() {

    private lateinit var profileImageButton: ImageButton
    private lateinit var uploadPhotoTextView: TextView
    private var selectedImageUri: Uri? = null
    private lateinit var fullNameEditText: EditText
    private lateinit var ageEditText: EditText
    private lateinit var professionEditText: EditText
    private lateinit var experienceEditText: EditText
    private lateinit var bioEditText: EditText
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_edit_profile)

        profileImageButton = findViewById(R.id.profileImage)
        uploadPhotoTextView = findViewById(R.id.uploadPhotoText)
        fullNameEditText = findViewById(R.id.editFullName)
        ageEditText = findViewById(R.id.age)
        professionEditText = findViewById(R.id.profession)
        experienceEditText = findViewById(R.id.experience)
        bioEditText = findViewById(R.id.editBio)
        sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val saveProfileButton = findViewById<Button>(R.id.btnSaveprofile)
        val logoutButton = findViewById<androidx.appcompat.widget.AppCompatButton>(R.id.btnlogout)

        profileImageButton.setOnClickListener {
            checkStoragePermissionAndPickImage()
        }

        saveProfileButton.setOnClickListener {
            saveProfileData()
        }

        logoutButton.setOnClickListener {
            // Perform your logout logic here
            // For example, clear user session, navigate to login activity
            Toast.makeText(this, "Logging out...", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, LoginActivity::class.java) // Replace LoginActivity with your actual login screen
            startActivity(intent)
            finish() // Close the current activity
        }

        loadProfileData()
        loadImageFromInternalStorage() // Load any previously saved image
    }

    private fun checkStoragePermissionAndPickImage() {
        val permission = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            Manifest.permission.READ_MEDIA_IMAGES
        } else {
            Manifest.permission.READ_EXTERNAL_STORAGE
        }

        if (ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED) {
            pickImageFromGallery()
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(permission), REQUEST_STORAGE_PERMISSION)
        }
    }

    private fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, REQUEST_IMAGE_PICK)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_STORAGE_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                pickImageFromGallery()
            } else {
                Toast.makeText(
                    this,
                    "Permission is required to upload a photo.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_PICK && resultCode == RESULT_OK && data != null) {
            selectedImageUri = data.data
            profileImageButton.setImageURI(selectedImageUri) // Display the selected image
            saveImageToInternalStorage(selectedImageUri)
        }
    }

    private fun saveImageToInternalStorage(imageUri: Uri?) {
        imageUri?.let { uri ->
            try {
                val inputStream = contentResolver.openInputStream(uri)
                val bitmap = BitmapFactory.decodeStream(inputStream)
                inputStream?.close()

                bitmap?.let {
                    val directory = getDir("profile_images", MODE_PRIVATE)
                    val file = File(directory, PROFILE_IMAGE_FILENAME)
                    val outputStream = FileOutputStream(file)
                    it.compress(Bitmap.CompressFormat.JPEG, 90, outputStream) // Compress the image
                    outputStream.close()
                    Toast.makeText(this, "Image saved!", Toast.LENGTH_SHORT).show()
                    loadImageFromInternalStorage() // Load the saved image
                }
            } catch (e: IOException) {
                e.printStackTrace()
                Toast.makeText(this, "Failed to save image.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loadImageFromInternalStorage() {
        try {
            val directory = getDir("profile_images", MODE_PRIVATE)
            val file = File(directory, PROFILE_IMAGE_FILENAME)
            if (file.exists()) {
                val inputStream = FileInputStream(file)
                val bitmap = BitmapFactory.decodeStream(inputStream)
                inputStream?.close()
                profileImageButton.setImageBitmap(bitmap)
            }
        } catch (e: IOException) {
            e.printStackTrace()
            Toast.makeText(this, "Failed to load image.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun saveProfileData() {
        val editor = sharedPreferences.edit()
        editor.putString(KEY_FULL_NAME, fullNameEditText.text.toString())
        editor.putString(KEY_AGE, ageEditText.text.toString())
        editor.putString(KEY_PROFESSION, professionEditText.text.toString())
        editor.putString(KEY_EXPERIENCE, experienceEditText.text.toString())
        editor.putString(KEY_BIO, bioEditText.text.toString())
        editor.apply()
        Toast.makeText(this, "Profile data saved!", Toast.LENGTH_SHORT).show()
    }

    private fun loadProfileData() {
        fullNameEditText.setText(sharedPreferences.getString(KEY_FULL_NAME, ""))
        ageEditText.setText(sharedPreferences.getString(KEY_AGE, ""))
        professionEditText.setText(sharedPreferences.getString(KEY_PROFESSION, ""))
        experienceEditText.setText(sharedPreferences.getString(KEY_EXPERIENCE, ""))
        bioEditText.setText(sharedPreferences.getString(KEY_BIO, ""))
    }

    override fun onPause() {
        super.onPause()
        saveProfileData() // Save data when the activity is no longer in the foreground
    }
}