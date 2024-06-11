package com.project.sfmd_project

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import com.bumptech.glide.Glide
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import de.hdodenhof.circleimageview.CircleImageView

class HealthMetrics : AppCompatActivity() {

    private var auth = FirebaseAuth.getInstance()
    private var database: DatabaseReference = FirebaseDatabase.getInstance().reference

    private var currentUser = FirebaseAuth.getInstance().currentUser


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_health_metrics)






        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference


        currentUser = FirebaseAuth.getInstance().currentUser


        // Check if user is logged in
        val currentUser = auth.currentUser

        var email = currentUser!!.email








        if (email != null) {
            val userRef = database.child("Users").orderByChild("email").equalTo(email)
            userRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for (data in dataSnapshot.children) {
                        val user = data.getValue(Signupuser::class.java)
                        if (user != null) {



                            val nametext: TextView = findViewById(R.id.name)


                            nametext.text=user.name



                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.d("FirebaseError", "Error fetching user data: ${error.message}")
                }
            })
        } else {
            Log.d("EmailError", "Email is null")
        }









        val layoutParams = WindowManager.LayoutParams().apply {
            flags = WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        }
        window.attributes = layoutParams


        setContentView(R.layout.activity_health_metrics)

        val back = findViewById<Button>(R.id.back)
        back.setOnClickListener {
            val intent = Intent(this@HealthMetrics, PatientProfile::class.java)
            startActivity(intent)
        }

    }
}