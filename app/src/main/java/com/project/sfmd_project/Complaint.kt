package com.project.sfmd_project

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import org.json.JSONException

class Complaint : AppCompatActivity(), Complaint_Adapter.OnItemClickListener {

    private lateinit var newRecyclerView: RecyclerView
    private lateinit var newArrayList: ArrayList<Complaint_Data>
    private lateinit var complaintAdapter: Complaint_Adapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_complaint)

        // Initialize RecyclerView and ArrayList
        newRecyclerView = findViewById(R.id.recycler)
        newArrayList = ArrayList()
        complaintAdapter = Complaint_Adapter(newArrayList, this)

        // Set up RecyclerView
        newRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@Complaint)
            adapter = complaintAdapter
        }

        // Fetch complaints from server
        getComplaints()

        // Handle button clicks
        findViewById<Button>(R.id.back).setOnClickListener {
            startActivity(Intent(this, PatientProfile::class.java))
        }

        findViewById<Button>(R.id.plus).setOnClickListener {
            startActivity(Intent(this, Add::class.java))
        }
    }

    private fun getComplaints() {
        val url = "http://192.168.100.44/api/get_complaints.php"

        val stringRequest = StringRequest(
            Request.Method.GET, url,
            Response.Listener<String> { response ->
                try {
                    val jsonArray = JSONArray(response)
                    for (i in 0 until jsonArray.length()) {
                        val jsonObject = jsonArray.getJSONObject(i)
                        val complaintText = jsonObject.getString("complaint_text")
                        val complaintData = Complaint_Data(complaintText)
                        newArrayList.add(complaintData)
                        Log.d("Complaints", "Response: $response")

                    }
                    // Notify adapter that data set has changed
                    complaintAdapter.notifyDataSetChanged()
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            },
            Response.ErrorListener { error ->
                error.printStackTrace()
            })

        // Add request to Volley request queue
        Volley.newRequestQueue(this).add(stringRequest)
    }

    override fun onItemClick(position: Int) {
        // Handle item click if needed
    }
}
