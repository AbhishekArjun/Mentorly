package com.example.mentorly

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest

class SignupActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var editTextSignupName: TextInputEditText // For Full Name
    private lateinit var editTextSignupEmail: TextInputEditText
    private lateinit var editTextSignupPassword: TextInputEditText
    private lateinit var editTextConfirmPassword: TextInputEditText
    private lateinit var buttonSignup: Button
    private lateinit var loginNavButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        // Initialize UI elements using the IDs from your layout
        val fullNameTextInputLayout = findViewById<TextInputLayout>(R.id.fullNameLayout)
        val emailTextInputLayout = findViewById<TextInputLayout>(R.id.emailLayout)
        val passwordTextInputLayout = findViewById<TextInputLayout>(R.id.passwordLayout)
        val confirmPasswordTextInputLayout = findViewById<TextInputLayout>(R.id.confirmPasswordLayout)

        editTextSignupName = fullNameTextInputLayout.findViewById(R.id.editTextSignupName) // Get the Name EditText
        editTextSignupEmail = emailTextInputLayout.findViewById(R.id.textInputEditTextEmail) // Use the specific ID
        editTextSignupPassword = passwordTextInputLayout.findViewById(R.id.textInputEditTextPassword) // Use the specific ID
        editTextConfirmPassword = confirmPasswordTextInputLayout.findViewById(R.id.textInputEditTextConfirmPassword) // Use the specific ID
        buttonSignup = findViewById(R.id.signupBtn)
        loginNavButton = findViewById(R.id.loginBtn)

        buttonSignup.setOnClickListener {
            val name = editTextSignupName.text.toString().trim() // Get the name
            val email = editTextSignupEmail.text.toString().trim()
            val password = editTextSignupPassword.text.toString().trim()
            val confirmPassword = editTextConfirmPassword.text.toString().trim()

            // Basic input validation for Name
            if (name.isEmpty()) {
                findViewById<TextInputLayout>(R.id.fullNameLayout).error = "Full Name is required"
                editTextSignupName.requestFocus()
                return@setOnClickListener
            } else {
                findViewById<TextInputLayout>(R.id.fullNameLayout).error = null
            }

            // Basic input validation for Email
            if (email.isEmpty()) {
                findViewById<TextInputLayout>(R.id.emailLayout).error = "Email is required"
                editTextSignupEmail.requestFocus()
                return@setOnClickListener
            } else {
                findViewById<TextInputLayout>(R.id.emailLayout).error = null
            }

            // Basic input validation for Password
            if (password.isEmpty()) {
                findViewById<TextInputLayout>(R.id.passwordLayout).error = "Password is required"
                editTextSignupPassword.requestFocus()
                return@setOnClickListener
            } else if (password.length < 6) {
                findViewById<TextInputLayout>(R.id.passwordLayout).error = "Password must be at least 6 characters"
                editTextSignupPassword.requestFocus()
                return@setOnClickListener
            } else {
                findViewById<TextInputLayout>(R.id.passwordLayout).error = null
            }

            // Basic input validation for Confirm Password
            if (confirmPassword.isEmpty()) {
                findViewById<TextInputLayout>(R.id.confirmPasswordLayout).error = "Confirm Password is required"
                editTextConfirmPassword.requestFocus()
                return@setOnClickListener
            } else if (password != confirmPassword) {
                findViewById<TextInputLayout>(R.id.confirmPasswordLayout).error = "Passwords do not match"
                editTextConfirmPassword.requestFocus()
                return@setOnClickListener
            } else {
                findViewById<TextInputLayout>(R.id.confirmPasswordLayout).error = null
            }

            // If all validations pass, create the user
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        val user = auth.currentUser
                        val profileUpdates = UserProfileChangeRequest.Builder()
                            .setDisplayName(name) // Now using the 'name' variable
                            .build()

                        user?.updateProfile(profileUpdates)
                            ?.addOnCompleteListener { profileTask ->
                                if (profileTask.isSuccessful) {
                                    Log.d(TAG, "User profile updated.")
                                    // Ab aap user ko dashboard par redirect kar sakte hain
                                    val intent = Intent(this, dashboard::class.java)
                                    startActivity(intent)
                                    finish()
                                } else {
                                    Log.e(TAG, "Error updating user profile.", profileTask.exception)
                                    Toast.makeText(baseContext, "Failed to update profile.",
                                        Toast.LENGTH_SHORT).show()
                                }
                            }
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "createUserWithEmail:failure", task.exception)
                        Toast.makeText(baseContext, "Authentication failed.",
                            Toast.LENGTH_SHORT).show()
                    }
                }
        }

        // Link back to Login Page
        loginNavButton.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}