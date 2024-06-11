package com.project.sfmd_project

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*

class Searchresults : AppCompatActivity() {

    private lateinit var profileAdapter: SearchDisplayAdapter
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_searchresults)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        profileAdapter = SearchDisplayAdapter(ArrayList(), object : SearchDisplayAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                // Handle item click eventf
            }
        })
        recyclerView.adapter = profileAdapter


        val type = intent.getStringExtra("type") // Assuming you pass the type from previous activity


        intent.getStringExtra("searchtext")?.let { searchDoctors(it, type) }

    }

    private fun searchDoctors(searchtxt: String, type: String? = null) {
        val database = FirebaseDatabase.getInstance()
        val reference = database.getReference("doctors")

        // Query by doctor's name and specialization
        val queryRef = reference.orderByChild("name")
            .startAt(searchtxt)
            .endAt(searchtxt + "\uf8ff")

        queryRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val doctors = ArrayList<Profiledata2>()
                for (dataSnapshot in snapshot.children) {
                    val doctor = dataSnapshot.getValue(Doctor::class.java)
                    doctor?.let {
                        // Check if the fetched doctor matches the provided specialization
                            val profileData = Profiledata2(
                                dp = 4, // can be treated later
                                ratingBar = 4.0f,// can be treated later
                                name = it.name,
                                type = it.specialization
                            )
                            doctors.add(profileData)

                    }
                }
                profileAdapter.updateData(doctors)
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle onCancelled...
            }
        })
    }
}

