package com.project.sfmd_project

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import org.json.JSONException

class Diseases : AppCompatActivity(), Disease_Adapter.OnItemClickListener {

    private lateinit var newRecyclerView: RecyclerView
    private lateinit var newArrayList: ArrayList<Disease_Data>
    private lateinit var diseaseAdapter: Disease_Adapter
    private lateinit var mySQLiteHelper: MySQLiteHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val layoutParams = WindowManager.LayoutParams().apply {
            flags = WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        }
        window.attributes = layoutParams

        setContentView(R.layout.activity_diseases)

        val back = findViewById<Button>(R.id.back)
        back.setOnClickListener {
            val intent = Intent(this@Diseases, PatientProfile::class.java)
            startActivity(intent)
        }

        val plus = findViewById<Button>(R.id.plus)
        plus.setOnClickListener {
            val intent = Intent(this@Diseases, Add4::class.java)
            startActivity(intent)
        }

        newRecyclerView = findViewById(R.id.recycler)
        newRecyclerView.layoutManager = LinearLayoutManager(this)
        newRecyclerView.setHasFixedSize(true)

        newArrayList = ArrayList()
        diseaseAdapter = Disease_Adapter(newArrayList, this)

        // Set adapter to RecyclerView
        newRecyclerView.adapter = diseaseAdapter

        // Initialize SQLite database helper
        mySQLiteHelper = MySQLiteHelper(this)

        // Fetch diseases from server or SQLite database
        if (NetworkUtils.isOnline(this)) {
            getDiseasesFromServer()
        } else {
            getDiseasesFromSQLite()
        }
    }

    private fun getDiseasesFromServer() {
        val url = "http://192.168.100.44/api/get_diseases.php"

        val stringRequest = StringRequest(
            Request.Method.GET, url,
            { response ->
                try {
                    val jsonArray = JSONArray(response)
                    for (i in 0 until jsonArray.length()) {
                        val diseaseText = jsonArray.getString(i)
                        val diseaseData = Disease_Data(diseaseText)
                        newArrayList.add(diseaseData)
                        // Save the fetched data locally
                        mySQLiteHelper.insertData(diseaseText)
                    }
                    // Notify adapter that data set has changed
                    diseaseAdapter.notifyDataSetChanged()
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            },
            { error ->
                error.printStackTrace()
            })

        // Add request to Volley request queue
        Volley.newRequestQueue(this).add(stringRequest)
    }

    private fun getDiseasesFromSQLite() {
        // Retrieve diseases from SQLite database
        val localData = mySQLiteHelper.getAllData()
        for (data in localData) {
            val diseaseText = data // Assuming data is the string you want to display
            val diseaseData = Disease_Data(diseaseText)
            newArrayList.add(diseaseData)
        }
        // Notify adapter that data set has changed
        diseaseAdapter.notifyDataSetChanged()
    }

    override fun onItemClick(position: Int) {
        // Handle item click if needed
    }
}
