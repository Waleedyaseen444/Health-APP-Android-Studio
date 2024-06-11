package com.project.sfmd_project

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ServerValue
import com.google.firebase.storage.FirebaseStorage
import java.util.*

class Doctoradd : AppCompatActivity() {
    private val database = FirebaseDatabase.getInstance().reference.child("doctors")
    private val storage = FirebaseStorage.getInstance().reference.child("doctor_images")

    private lateinit var nameEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var phoneEditText: EditText
    private lateinit var genderEditText: EditText
    private lateinit var ageEditText: EditText
    private var imageUri: Uri? = null
    private lateinit var mySQLiteHelper: MySQLiteHelper


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_doctoradd)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        mySQLiteHelper = MySQLiteHelper(this)


        val signupButton = findViewById<Button>(R.id.signup)
        val uploadImageButton = findViewById<Button>(R.id.signup1)
        nameEditText = findViewById(R.id.name)
        emailEditText = findViewById(R.id.email)
        passwordEditText = findViewById(R.id.password)
        phoneEditText = findViewById(R.id.phone)
        genderEditText = findViewById(R.id.editText6)
        ageEditText = findViewById(R.id.editText5)

        signupButton.setOnClickListener {
            val name = nameEditText.text.toString()
            val specialization = emailEditText.text.toString()
            val experience = passwordEditText.text.toString()
            val phoneNumber = phoneEditText.text.toString()
            val gender = genderEditText.text.toString()
            val age = ageEditText.text.toString()

            val doctor = Doctor(name, specialization, experience, phoneNumber, gender, age)
            val id = mySQLiteHelper.insertData(doctor.toString())

            if (id != -1L) {

                uploadImage(doctor)
            } else {
                Toast.makeText(this@Doctoradd, "Failed to add data to local storage", Toast.LENGTH_SHORT).show()

            }

            // Upload the image first
            uploadImage(doctor)
        }

        uploadImageButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            startActivityForResult(intent, REQUEST_IMAGE)
        }
    }


    private fun uploadImage(doctor: Doctor) {
        // Construct the image file name using a unique identifier
        val imageName = UUID.randomUUID().toString() + ".jpg"

        // Create a reference to the image file in Firebase Storage
        val imageRef = storage.child(imageName)

        // Upload the image to Firebase Storage
        imageUri?.let {
            imageRef.putFile(it).addOnSuccessListener { taskSnapshot ->
                // Image uploaded successfully
                imageRef.downloadUrl.addOnSuccessListener { uri ->
                    val imageUrl = uri.toString()
                    // Now imageUrl contains the download URL of the uploaded image

                    // Add the doctor details along with the image URL in the same database node
                    val doctorWithImage = doctor.copy(imageUrl = imageUrl)
                    val doctorKey = database.push().key ?: return@addOnSuccessListener

                    database.child(doctorKey).setValue(doctorWithImage).addOnSuccessListener {
                        val notificationRef = FirebaseDatabase.getInstance().reference.child("notifications").push()
                        val notificationMessage = "Doctor added:"
                        val notification = mapOf(
                            "message" to notificationMessage,
                            "timestamp" to ServerValue.TIMESTAMP
                        )
                        notificationRef.setValue(notification)
                        Toast.makeText(this, "Feedback submitted Successfully!", Toast.LENGTH_SHORT).show()
                        val myFirebaseMessagingService = MyFirebaseMessagingService()
                        myFirebaseMessagingService.generateNotification(this,"Doctoroid", " Doctor added successfully " )

                    }.addOnFailureListener { databaseError ->
                        // Handle database error
                    }
                }
            }.addOnFailureListener {
                // Handle image upload failure
                val notificationRef = FirebaseDatabase.getInstance().reference.child("notifications").push()
                val notificationMessage = "Doctor added:"
                val notification = mapOf(
                    "message" to notificationMessage,
                    "timestamp" to ServerValue.TIMESTAMP
                )
                notificationRef.setValue(notification)
                Toast.makeText(this, "Feedback submitted Successfully!", Toast.LENGTH_SHORT).show()
                val myFirebaseMessagingService = MyFirebaseMessagingService()
                myFirebaseMessagingService.generateNotification(this,"Doctoroid", " Doctor added successfully " )

            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE && resultCode == Activity.RESULT_OK) {
            imageUri = data?.data
        }
    }

    companion object {
        const val REQUEST_IMAGE = 101
    }

    override fun onDestroy() {
        mySQLiteHelper.close()
        super.onDestroy()
    }
}
