package com.project.sfmd_project

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.Button
import android.widget.CalendarView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.*

class BookAppointment : AppCompatActivity() {

    private lateinit var selectedDate: String
    private lateinit var selectedTime: String



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val layoutParams = WindowManager.LayoutParams().apply {
            flags = WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        }
        window.attributes = layoutParams

        setContentView(R.layout.activity_book_appointment)





        val calendarView = findViewById<CalendarView>(R.id.calendarView)
        val setButton = findViewById<Button>(R.id.set)

        // Define time buttons
        val timeButton1 = findViewById<Button>(R.id.timeButton1)
        val timeButton2 = findViewById<Button>(R.id.timeButton2)
        val timeButton3 = findViewById<Button>(R.id.timeButton3)
        // Add more buttons if needed

        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            // Format the selected date
            selectedDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(
                GregorianCalendar(year, month, dayOfMonth).time
            )
        }

        // Set click listeners for time buttons
        timeButton1.setOnClickListener { handleTimeButtonClick("9:00 am") }
        timeButton2.setOnClickListener { handleTimeButtonClick("10:00 am") }
        timeButton3.setOnClickListener { handleTimeButtonClick("11:00 am") }
        // Add more click listeners for additional buttons if needed

        setButton.setOnClickListener {



            // Get doctor's name from intent extras
            val doctorName = intent.getStringExtra("name") ?: ""

            // Create an Appointment_data object
            val appointmentData = Appointment_data(doctorName, selectedTime, selectedDate)

            // Add appointment to Firebase Realtime Database
            val database = FirebaseDatabase.getInstance()
            val reference = database.getReference("appointments")
            val appointmentId = reference.push().key
            appointmentId?.let {
                reference.child(it).setValue(appointmentData)
                val myFirebaseMessagingService = MyFirebaseMessagingService()
                myFirebaseMessagingService.generateNotification(this,"Doctoroid", " Booked successfully " )

            }

            val intent = Intent(this@BookAppointment, Appointment::class.java)
            startActivity(intent)

            // Navigate back to DoctorProfile activity
//            val intent = Intent(this@BookAppointment, DoctorProfile::class.java)
//            startActivity(intent)
        }

        val back = findViewById<Button>(R.id.back)
        back.setOnClickListener {
            val intent = Intent(this@BookAppointment, DoctorProfile::class.java)
            startActivity(intent)
        }
    }

    private fun handleTimeButtonClick(time: String) {
        selectedTime = time
    }



}
