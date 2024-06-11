package com.project.sfmd_project

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase


class PatientSignUp : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth // Declare FirebaseAuth variable
    private lateinit var mySQLiteHelper: MySQLiteHelper


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        val layoutParams = WindowManager.LayoutParams().apply {
            flags = WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        }
        window.attributes = layoutParams

        setContentView(R.layout.activity_patient_sign_up)

        auth = FirebaseAuth.getInstance()
        mySQLiteHelper = MySQLiteHelper(this)


        val email = findViewById<EditText>(R.id.email)
        val password = findViewById<EditText>(R.id.password)
        val nameEditText = findViewById<EditText>(R.id.name)
        val phoneEditText = findViewById<EditText>(R.id.phone)
        val countryEditText = findViewById<EditText>(R.id.editText6)
        val age = findViewById<EditText>(R.id.editText2)


        val SignUp = findViewById<Button>(R.id.signup)
        SignUp.setOnClickListener {
            val name = nameEditText.text.toString().trim()
            val userEmail = email.text.toString().trim()
            val userPass = password.text.toString().trim()
            val age = age.text.toString().trim()
            val country = countryEditText.text.toString().trim()
            val phone = phoneEditText.text.toString().trim()



            if (userEmail.isEmpty() || userPass.isEmpty() || name.isEmpty() || phone.isEmpty() || country.isEmpty() || age.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            } else {
                val signupUser = Signupuser(name, userEmail, userPass, age , country  ,  phone  )
                userSignUp(userEmail, userPass, signupUser)
            }

        }

        val login = findViewById<Button>(R.id.login)
        login.setOnClickListener {
            val intent = Intent(this, Login::class.java)
            startActivity(intent)

        }
    }
    private fun userSignUp(email: String, password: String, signupUser: Signupuser) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user: FirebaseUser? = auth.currentUser
                    // Call the submitData function here, after successful sign up
                    submitData(signupUser)
                    Toast.makeText(this, "Sign up successful", Toast.LENGTH_SHORT).show()
                } else {
                    // If sign up fails due to existing email, show error message
                    if (task.exception is FirebaseAuthUserCollisionException) {
                        Toast.makeText(this, "Email already in use", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "Sign up failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
    }

    private fun submitData(signupUser: Signupuser) {
        Log.d("Firebase", "Submitting data to database: $signupUser")
        val database = FirebaseDatabase.getInstance().reference
        database.child("Users").child(signupUser.email.replace(".", ",")).setValue(signupUser)
            .addOnSuccessListener {
                // Store user registration data locally in SQLite
                val id = mySQLiteHelper.insertData(signupUser.toString())
                if (id != -1L) {
                    Toast.makeText(this@PatientSignUp, "User registered successfully", Toast.LENGTH_SHORT).show()
                    Log.d("Firebase", "Data submitted successfully: $signupUser")
                    val myFirebaseMessagingService = MyFirebaseMessagingService()
                    myFirebaseMessagingService.generateNotification(this,"Doctoroid", " Account created " )

                    val intent = Intent(this@PatientSignUp, Login::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this@PatientSignUp, "Failed to register user locally", Toast.LENGTH_SHORT).show()
                    Log.e("SQLite", "Failed to insert data locally")
                }
            }
            .addOnFailureListener {
                Toast.makeText(this@PatientSignUp, "Failed to register user: ${it.message}", Toast.LENGTH_SHORT).show()
                Log.e("Firebase", "Failed to submit data: ${it.message}")
            }
    }
}