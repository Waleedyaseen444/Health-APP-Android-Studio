package com.project.sfmd_project


import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*

class Bookedresults : AppCompatActivity() {

    private lateinit var appointmentsAdapter: AppointmentsAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var appointmentsRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bookedresults)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Assuming appointmentsAdapter is already initialized

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
                appointmentsAdapter.updateAppointments(appointments)

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
