package com.project.sfmd_project

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import android.util.Log




class test1 : AppCompatActivity() {
    private lateinit var appointmentsAdapter: AppointmentAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var appointmentsRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test1)

        Log.d("msg","This is test 1")


        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        appointmentsAdapter = AppointmentAdapter()
        recyclerView.adapter = appointmentsAdapter

        appointmentsRef = FirebaseDatabase.getInstance().getReference("appointments")
        appointmentsRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val appointments = mutableListOf<Appointments>()
                for (appointmentSnapshot in snapshot.children) {
                    val doctorName = appointmentSnapshot.child("name").getValue(String::class.java)
                    val appointmentTime = appointmentSnapshot.child("time").getValue(String::class.java)
                    val appointmentDate = appointmentSnapshot.child("date").getValue(String::class.java)
                    if (doctorName != null && appointmentTime != null && appointmentDate != null) {
                        val appointment = Appointments(doctorName, appointmentTime, appointmentDate)
                        appointments.add(appointment)
                    }
                }
                appointmentsAdapter.submitList(appointments)

                // Display appointment details in log
                for (appointment in appointments) {
                    Log.d("AppointmentDetails", "Doctor: ${appointment.doctorName}, Date: ${appointment.appointmentDate}, Time: ${appointment.appointmentTime}")
                }

            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })
    }
}
