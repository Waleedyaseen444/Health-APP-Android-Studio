package com.project.sfmd_project

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException

class LabResults : AppCompatActivity(), LabResult_Adapter.OnItemClickListener {

    private lateinit var newRecyclerView: RecyclerView
    private lateinit var labResultAdapter: LabResult_Adapter
    private lateinit var requestQueue: RequestQueue

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lab_results)

        newRecyclerView = findViewById(R.id.recycler)
        newRecyclerView.layoutManager = LinearLayoutManager(this)

        requestQueue = Volley.newRequestQueue(this)

        val plus = findViewById<Button>(R.id.plus)
        plus.setOnClickListener {
            val intent = Intent(this@LabResults, LabTestOptionsActivity::class.java)
            startActivity(intent)
        }

        fetchDataFromServer()
    }

    private fun fetchDataFromServer() {
        val url = "http://192.168.100.44/api/fetch_data.php" // Replace with your server IP

        val jsonArrayRequest = JsonArrayRequest(Request.Method.GET, url, null,
            Response.Listener { response ->
                try {
                    val labResults = ArrayList<LabResult_Data>()
                    for (i in 0 until response.length()) {
                        val result = response.getJSONObject(i)
                        val testName = result.getString("test_name")
                        val date = result.getString("date")
                        val time = result.getString("time")
                        val filePath = result.getString("file_path")
                        labResults.add(LabResult_Data(testName, date, time, filePath))
                    }
                    labResultAdapter = LabResult_Adapter(labResults, this)
                    newRecyclerView.adapter = labResultAdapter
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            },
            Response.ErrorListener { error ->
                Toast.makeText(this@LabResults, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
            })

        requestQueue.add(jsonArrayRequest)
    }

    override fun onItemClick(position: Int) {
        // Handle item click if needed
    }
}
