package com.project.sfmd_project

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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

class FamilyDiseases : AppCompatActivity(), FamilyDisease_Adapter.OnItemClickListener {
    private lateinit var newRecyclerView: RecyclerView
    private lateinit var newArrayList: ArrayList<Family_Disease_Data>
    private lateinit var mySQLiteHelper: MySQLiteHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val layoutParams = WindowManager.LayoutParams().apply {
            flags = WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        }
        window.attributes = layoutParams
        setContentView(R.layout.activity_family_diseases)

        val back = findViewById<Button>(R.id.back)
        back.setOnClickListener {
            val intent = Intent(this@FamilyDiseases, PatientProfile::class.java)
            startActivity(intent)
        }
        val plus = findViewById<Button>(R.id.plus)
        plus.setOnClickListener {
            val intent = Intent(this@FamilyDiseases, Add6::class.java)
            startActivity(intent)
        }

        newRecyclerView = findViewById(R.id.recycler)
        newRecyclerView.layoutManager = LinearLayoutManager(this)
        newRecyclerView.setHasFixedSize(true)

        newArrayList = ArrayList()

        // Initialize SQLite database helper
        mySQLiteHelper = MySQLiteHelper(this)

        // Fetch family diseases from server or SQLite database
        if (NetworkUtils.isOnline(this)) {
            getFamilyDiseasesFromServer()
        } else {
            getFamilyDiseasesFromSQLite()
        }
    }

    private fun getFamilyDiseasesFromServer() {
        val url = "http://192.168.100.44/api/get_familydiseases.php"

        val stringRequest = StringRequest(
            Request.Method.GET, url,
            { response ->
                try {
                    val jsonArray = JSONArray(response)
                    for (i in 0 until jsonArray.length()) {
                        val jsonObject = jsonArray.getJSONObject(i)
                        val diseaseText = jsonObject.getString("disease_text")
                        val diseaseData = Family_Disease_Data(diseaseText)
                        newArrayList.add(diseaseData)
                        // Save the fetched data locally
                        mySQLiteHelper.insertData(diseaseText)
                    }
                    // Set adapter to RecyclerView
                    newRecyclerView.adapter = FamilyDisease_Adapter(newArrayList, this)
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

    private fun getFamilyDiseasesFromSQLite() {
        // Retrieve family diseases from SQLite database
        val localData = mySQLiteHelper.getAllData()
        for (data in localData) {
            val diseaseText = data // Assuming data is the string you want to display
            val diseaseData = Family_Disease_Data(diseaseText)
            newArrayList.add(diseaseData)
        }
        // Set adapter to RecyclerView
        newRecyclerView.adapter = FamilyDisease_Adapter(newArrayList, this)
    }

    override fun onItemClick(position: Int) {
        // Handle item click if needed
    }
}
