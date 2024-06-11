package com.project.sfmd_project

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class Forgetpassword : AppCompatActivity() {
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgetpassword)

        databaseReference = FirebaseDatabase.getInstance().reference

        val emailEditText = findViewById<EditText>(R.id.et_email)
        val forgetButton = findViewById<Button>(R.id.button3)
        forgetButton.setOnClickListener {
            val email = emailEditText.text.toString().trim()
            if (email.isNotEmpty()) {
                checkEmailInDatabase(email)
            } else {
                Toast.makeText(this, "Please enter an email", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun checkEmailInDatabase(email: String) {
        // Check if the email exists in the database
        databaseReference.child("Users").orderByChild("email").equalTo(email)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        // Email exists in the database, navigate to the Retype activity
                        val intent = Intent(this@Forgetpassword, Retype::class.java)
                        intent.putExtra("email", email)
                        startActivity(intent)
                    } else {
                        // Email does not exist in the database
                        Toast.makeText(this@Forgetpassword, "Email not found", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Toast.makeText(
                        this@Forgetpassword,
                        "Database error: " + databaseError.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }
}

