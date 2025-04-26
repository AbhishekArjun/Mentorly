package com.example.mentorly

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var editTextLoginEmail: TextInputEditText
    private lateinit var editTextLoginPassword: TextInputEditText
    private lateinit var buttonLogin: Button
    private lateinit var signupNavButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        // Initialize UI elements by finding their IDs in the layout
        val emailTextInputLayout = findViewById<TextInputLayout>(R.id.emailLayout)
        val passwordTextInputLayout = findViewById<TextInputLayout>(R.id.passwordLayout)
        editTextLoginEmail = findViewById(R.id.emailInput)
        editTextLoginPassword = findViewById(R.id.passwordInput)
        buttonLogin = findViewById(R.id.loginBtn)
        signupNavButton = findViewById(R.id.signupBtn)

        // Set OnClickListener for the Login Button
        buttonLogin.setOnClickListener {
            val email = editTextLoginEmail.text.toString().trim()
            val password = editTextLoginPassword.text.toString().trim()

            // Basic input validation
            if (email.isEmpty()) {
                emailTextInputLayout.error = "Email is required"
                editTextLoginEmail.requestFocus()
                return@setOnClickListener
            } else {
                emailTextInputLayout.error = null // Clear error
            }

            if (password.isEmpty()) {
                passwordTextInputLayout.error = "Password is required"
                editTextLoginPassword.requestFocus()
                return@setOnClickListener
            } else {
                passwordTextInputLayout.error = null // Clear error
            }

            // Sign in with email and password using Firebase
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success
                        Toast.makeText(this, "Login successful.", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, dashboard::class.java) // Use your actual Dashboard Activity name
                        startActivity(intent)
                        finish() // Prevent going back to login after successful login
                    } else {
                        // If sign in fails, display an error message
                        Toast.makeText(
                            this, "Login failed: ${task.exception?.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }

        // Navigate to Signup Activity
        signupNavButton.setOnClickListener {
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
        }
    }
}