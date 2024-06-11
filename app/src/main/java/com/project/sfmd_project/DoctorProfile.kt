package com.project.sfmd_project

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class DoctorProfile : AppCompatActivity() {

    private var imageUrl: String? = null
    private var name: String? = null
    private var specialization: String? = null
    private var phoneNumber: String? = null


    private var auth =FirebaseAuth.getInstance()
    private var database: DatabaseReference = FirebaseDatabase.getInstance().reference

    private var currentUser = FirebaseAuth.getInstance().currentUser

    private var isGuestUser = true


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)




        val layoutParams = WindowManager.LayoutParams().apply {
            flags = WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        }
        window.attributes = layoutParams


        setContentView(R.layout.activity_doctor_profile)

        val profileImage = findViewById<ImageView>(R.id.circleImageView2)
        val nameTextView = findViewById<TextView>(R.id.id11)
        val specializationTextView = findViewById<TextView>(R.id.id12)
        val phoneNumberTextView = findViewById<TextView>(R.id.id13)

        // Get data from intent extras
        imageUrl = intent.getStringExtra("imageUrl")
        name = intent.getStringExtra("name")
        specialization = intent.getStringExtra("specialization")
        phoneNumber = intent.getStringExtra("phoneNumber")

        // Display doctor's information
        Glide.with(this).load(imageUrl).into(profileImage)
        nameTextView.text = name
        specializationTextView.text = specialization
        phoneNumberTextView.text = phoneNumber






        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference


        currentUser = FirebaseAuth.getInstance().currentUser
        isGuestUser = currentUser == null


        val appointment = findViewById<Button>(R.id.appointment)
        appointment.setOnClickListener {
            if(isGuestUser){

                val intent = Intent(this@DoctorProfile, Home::class.java)
                startActivity(intent)
                showSignUpPrompt()
            }
            else{
            val intent = Intent(this@DoctorProfile, BookAppointment::class.java)
            startActivity(intent)
            }
        }

        val record = findViewById<Button>(R.id.record)
        record.setOnClickListener {

            if(isGuestUser){

                showSignUpPrompt()
            }

            val intent = Intent(this@DoctorProfile, Record::class.java)
            startActivity(intent)
        }

        val profile = findViewById<Button>(R.id.person)
        profile.setOnClickListener {
            if(isGuestUser){

                showSignUpPrompt()
            }

            val intent = Intent(this@DoctorProfile, PatientProfile::class.java)
            startActivity(intent)
        }

        val home  = findViewById<Button>(R.id.home)
        home.setOnClickListener {


            val intent = Intent(this@DoctorProfile, Home::class.java)
            startActivity(intent)
        }

        val set  = findViewById<Button>(R.id.set)
        set.setOnClickListener {

            if(isGuestUser){

                showSignUpPrompt()
            }

            else {
                val intent = Intent(this@DoctorProfile, BookAppointment::class.java)
                intent.putExtra("name", nameTextView.text)
                startActivity(intent)
            }
        }

        val quali  = findViewById<Button>(R.id.quali)
        quali.setOnClickListener {
            val intent = Intent(this@DoctorProfile, Qualification::class.java)
            startActivity(intent)
        }

        val rev  = findViewById<Button>(R.id.rev)
        rev.setOnClickListener {
            val intent = Intent(this@DoctorProfile, Reviews::class.java)
            startActivity(intent)
        }

        val work  = findViewById<Button>(R.id.work)
        work.setOnClickListener {


            val intent = Intent(this@DoctorProfile, WorkExperience::class.java)
            startActivity(intent)
        }






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