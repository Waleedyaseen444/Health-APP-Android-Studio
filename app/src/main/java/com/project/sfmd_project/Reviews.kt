package com.project.sfmd_project

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class Reviews : AppCompatActivity() {
    private lateinit var doctorId: String
    private lateinit var recyclerView: RecyclerView
    private lateinit var reviewAdapter: ReviewAdapter



    private var auth = FirebaseAuth.getInstance()
    private var database: DatabaseReference = FirebaseDatabase.getInstance().reference

    private var currentUser = FirebaseAuth.getInstance().currentUser
    private lateinit var mySQLiteHelper: MySQLiteHelper // Initialize SQLiteHelper

    private var isGuestUser = true



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reviews)

        // Get doctor ID from intent
        doctorId = intent.getStringExtra("doctorId") ?: ""
        database = FirebaseDatabase.getInstance().reference.child("reviews").child(doctorId)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        reviewAdapter = ReviewAdapter()
        recyclerView.adapter = reviewAdapter

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference


        currentUser = FirebaseAuth.getInstance().currentUser
        isGuestUser = currentUser == null



        val addReviewButton = findViewById<Button>(R.id.plus)
        addReviewButton.setOnClickListener {


            if(isGuestUser){
                showSignUpPrompt()
            }
            else {
            val intent = Intent(this@Reviews, Add2::class.java)
            intent.putExtra("doctorId", doctorId)
            startActivity(intent)
            }
        }

        // Listen for changes in reviews
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val reviews = mutableListOf<Review>()
                for (reviewSnapshot in snapshot.children) {
                    val review = reviewSnapshot.getValue(Review::class.java)
                    review?.let {
                        reviews.add(it)
                    }
                }
                reviewAdapter.submitList(reviews)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Reviews", "Failed to read value.", error.toException())
            }
        })
    }

    private fun showSignUpPrompt() {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_signup_prompt)

        val signUpButton = dialog.findViewById<Button>(R.id.signUpButton)
        signUpButton.setOnClickListener {
            // Navigate to sign-up activity
            val intent = Intent(this, PatientSignUp::class.java)
            startActivity(intent)
            dialog.dismiss()
        }

        dialog.show()
    }

}
