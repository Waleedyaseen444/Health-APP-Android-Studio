package com.project.sfmd_project

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*

class Appointment : AppCompatActivity(), Appointment_adapter.OnItemClickListener {

    private lateinit var newRecyclerView: RecyclerView
    private lateinit var newArrayList: ArrayList<Appointment_data>
    private lateinit var appointmentAdapter: Appointment_adapter
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val layoutParams = WindowManager.LayoutParams().apply {
            flags = WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        }
        window.attributes = layoutParams
        setContentView(R.layout.activity_appointment)

        val back = findViewById<Button>(R.id.back)
        back.setOnClickListener {
            val intent = Intent(this@Appointment, Home::class.java)
            startActivity(intent)
        }

        newRecyclerView = findViewById(R.id.recycler)
        newRecyclerView.layoutManager = LinearLayoutManager(this)
        newRecyclerView.setHasFixedSize(true)

        newArrayList = arrayListOf()
        appointmentAdapter = Appointment_adapter(newArrayList, this)
        newRecyclerView.adapter = appointmentAdapter

        // Initialize Firebase
        val database = FirebaseDatabase.getInstance()
        databaseReference = database.getReference("appointments")

        // Add value event listener to fetch appointments
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                newArrayList.clear()
                for (postSnapshot in snapshot.children) {
                    val appointment = postSnapshot.getValue(Appointment_data::class.java)
                    appointment?.let {
                        newArrayList.add(it)
                    }
                }
                appointmentAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle database error
            }
        })
    }

    override fun onItemClick(position: Int) {
        // Handle item click event if needed
        val intent = Intent(this, Chat::class.java)
        startActivity(intent)
    }
}
