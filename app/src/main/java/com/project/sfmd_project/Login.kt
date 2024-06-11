package com.project.sfmd_project

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class Login : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()

        val emailEditText = findViewById<EditText>(R.id.et_email)
        val passwordEditText = findViewById<EditText>(R.id.editText)
        val sinupbutton = findViewById<Button>(R.id.button4)
        val addbutton = findViewById<Button>(R.id.button6)

        sinupbutton.setOnClickListener {
            val intent = Intent(this, PatientSignUp::class.java)
            startActivity(intent)
        }

        addbutton.setOnClickListener {
            val intent = Intent(this, Doctoradd::class.java)
            startActivity(intent)
        }

        val loginButton = findViewById<Button>(R.id.button3)
        loginButton.setOnClickListener {
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter both email and password", Toast.LENGTH_SHORT).show()
            } else {

                userSignIn(email, password)
            }
        }

        val guestButton = findViewById<Button>(R.id.button)
        guestButton.setOnClickListener {
            // Navigate to PatientProfile activity for guest users
            val intent = Intent(this, Home::class.java)
            intent.putExtra("isGuestUser", true)
            startActivity(intent)
        }
    }

    fun onForgotPasswordClicked(view: View?) {
        // Handle the click event by starting another activity
        val intent = Intent(
            this,
            Forgetpassword::class.java
        )
        startActivity(intent)
    }


    private fun userSignIn(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    Toast.makeText(this, "Sign in successful", Toast.LENGTH_SHORT).show()
                    val intent = if (user != null) {
                        Intent(this, PatientProfile::class.java)
                    } else {
                        // If no user is logged in, navigate to Login activity
                        Intent(this, Login::class.java)
                    }
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this, "Authentication failed.", Toast.LENGTH_SHORT).show()
                }
            }
    }
}
