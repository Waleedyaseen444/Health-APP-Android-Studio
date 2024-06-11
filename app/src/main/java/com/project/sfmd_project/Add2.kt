package com.project.sfmd_project

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.RatingBar
import com.google.firebase.database.DatabaseReference

import com.google.firebase.database.FirebaseDatabase

class Add2 : AppCompatActivity() {
    private lateinit var doctorId: String
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add2)


        doctorId = intent.getStringExtra("doctorId") ?: ""
        database = FirebaseDatabase.getInstance().reference.child("reviews").child(doctorId)

        val add = findViewById<Button>(R.id.done)
        add.setOnClickListener {
            val reviewText = findViewById<EditText>(R.id.editTextReview).text.toString()
            val rating = findViewById<RatingBar>(R.id.ratingBar).rating


            val review = Review(reviewText, rating)


            database.push().setValue(review)

            val myFirebaseMessagingService = MyFirebaseMessagingService()
            myFirebaseMessagingService.generateNotification(this,"Doctoroid", " Review added successfully " )



            val intent = Intent(this@Add2, Reviews::class.java)
            startActivity(intent)
        }
    }
}
