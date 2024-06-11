package com.project.sfmd_project

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class EditProfile : AppCompatActivity() {

   // private lateinit var database: DatabaseReference
   // private lateinit var auth: FirebaseAuth
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var userEmail: String
    private lateinit var nameEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var cityEditText: EditText
    private lateinit var countryEditText: EditText
    private lateinit var passwordEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)



        // Initialize Firebase
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()



        nameEditText = findViewById(R.id.nameEditText)
        emailEditText = findViewById(R.id.phoneNumberEditText)
        cityEditText = findViewById(R.id.editTextText3)
        passwordEditText = findViewById(R.id.passwordEditText)

        // Fetch and display
        // fetchAndDisplayUserDetails()

        userEmail = auth.currentUser?.email ?: ""


        loadUserData()

        // Save button click listener
        val saveButton = findViewById<Button>(R.id.saveButton)
        saveButton.setOnClickListener {
            updateProfile()
       }


    }

    private fun loadUserData() {
        val userRef: DatabaseReference = database.reference.child("Users").child(userEmail.replace(".", ","))
        userRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val user = snapshot.getValue(Signupuser::class.java)
                    if (user != null) {
                        nameEditText.setText(user.name)
                        emailEditText.setText(user.email)
                        cityEditText.setText(user.age)
                        passwordEditText.setText(user.password)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
                Toast.makeText(this@EditProfile, "Failed to load user data.", Toast.LENGTH_SHORT).show()
            }
        })
    }



    private fun updateProfile() {
        val name = nameEditText.text.toString().trim()
        val email = emailEditText.text.toString().trim()
        val age = cityEditText.text.toString().trim()
        val password = passwordEditText.text.toString().trim()

        val userRef: DatabaseReference = database.reference.child("Users").child(userEmail.replace(".", ","))
        val updatedUser = Signupuser(name, email, "",  age , password)

        userRef.setValue(updatedUser)
            .addOnSuccessListener {
                Toast.makeText(this, "Profile updated successfully.", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to update profile.", Toast.LENGTH_SHORT).show()
            }
    }

}
