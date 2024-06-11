package com.project.sfmd_project

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.Button
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*

class ChatSearch : AppCompatActivity(), Chat_search_adapter.OnItemClickListener {

    private lateinit var newRecyclerView: RecyclerView
    private lateinit var newArrayList: ArrayList<Doctors_data>
    private lateinit var databaseReference: DatabaseReference
    private lateinit var adapter: Chat_search_adapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val layoutParams = WindowManager.LayoutParams().apply {
            flags = WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        }
        window.attributes = layoutParams

        setContentView(R.layout.activity_chat_search)

        val back = findViewById<Button>(R.id.back)
        back.setOnClickListener {
            val backIntent = Intent(this, Home::class.java)
            startActivity(backIntent)
        }

        newRecyclerView = findViewById(R.id.search)
        newRecyclerView.layoutManager = LinearLayoutManager(this)
        newRecyclerView.setHasFixedSize(true)

        newArrayList = arrayListOf()
        databaseReference = FirebaseDatabase.getInstance().reference.child("doctors")

        getUserData()

        // Initialize the adapter
        adapter = Chat_search_adapter(newArrayList, this)
        newRecyclerView.adapter = adapter

        // Initialize the search view
        val searchView = findViewById<SearchView>(R.id.searchView)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                adapter.filter.filter(newText)
                return true
            }
        })
    }

    private fun getUserData() {
        databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (docSnapshot in snapshot.children) {
                        val doctor = docSnapshot.getValue(Doctor::class.java)
                        doctor?.let {
                            val doctors_data = Doctors_data(
                                it.name,
                                it.specialization,
                                it.experience,
                                it.phoneNumber,
                                it.gender,
                                it.age,
                                it.imageUrl
                            )
                            newArrayList.add(doctors_data)
                        }
                    }
                    adapter.notifyDataSetChanged() // Notify adapter that data set has changed
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle database error
            }
        })
    }

    override fun onItemClick(position: Int) {
        val selectedDoctor = newArrayList[position]

        // Create an Intent to start the Chat activity
        val chatIntent = Intent(this, Chat::class.java).apply {
            // Pass the selected doctor's name and image URL as extras
            putExtra("doctorName", selectedDoctor.name)
            putExtra("doctorImageUrl", selectedDoctor.imageUrl)
        }

        // Start the Chat activity
        startActivity(chatIntent)
    }
}
