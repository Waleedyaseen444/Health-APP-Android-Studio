package com.project.sfmd_project

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class Retype : AppCompatActivity() {
    private lateinit var databaseReference: DatabaseReference
    private lateinit var email: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_retype)

        databaseReference = FirebaseDatabase.getInstance().reference

        // Get the email passed from Forgetpassword activity
        email = intent.getStringExtra("email") ?: ""

        val passwordEditText = findViewById<EditText>(R.id.et_password)
        val retypeButton = findViewById<Button>(R.id.button3)
        retypeButton.setOnClickListener {
            val newPassword = passwordEditText.text.toString().trim()
            if (newPassword.isNotEmpty()) {
                // Update password in Realtime Database
                updatePasswordInFirebaseDatabase(newPassword)
            } else {
                Toast.makeText(this, "Please enter a new password", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updatePasswordInFirebaseDatabase(newPassword: String) {
        val userRef = databaseReference.child("Users").child(email.replace(".", "_"))

        userRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    userRef.child("password").setValue(newPassword)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Toast.makeText(
                                    this@Retype,
                                    "Password updated successfully in database",
                                    Toast.LENGTH_SHORT
                                ).show()
                                // Navigate back to Login activity or wherever appropriate
                                startActivity(Intent(this@Retype, Login::class.java))
                                finish()
                            } else {
                                Toast.makeText(
                                    this@Retype,
                                    "Failed to update password in database: " + task.exception?.message,
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                } else {
                    Toast.makeText(
                        this@Retype,
                        "User not found in database",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(
                    this@Retype,
                    "Database error: " + databaseError.message,
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

}
