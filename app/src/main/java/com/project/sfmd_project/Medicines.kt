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

class Medicines : AppCompatActivity(), Medicine_Adapter.OnItemClickListener {
    private lateinit var newRecyclerView: RecyclerView
    private lateinit var newArrayList: ArrayList<Medicine_Data>
    private lateinit var medicineAdapter: Medicine_Adapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val layoutParams = WindowManager.LayoutParams().apply {
            flags = WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        }
        window.attributes = layoutParams

        setContentView(R.layout.activity_medicines)

        val back = findViewById<Button>(R.id.back)
        back.setOnClickListener {
            val intent = Intent(this@Medicines, PatientProfile::class.java)
            startActivity(intent)
        }

        val plus = findViewById<Button>(R.id.plus)
        plus.setOnClickListener {
            val intent = Intent(this@Medicines, Add5::class.java)
            startActivity(intent)
        }

        newRecyclerView = findViewById(R.id.recycler)
        newRecyclerView.layoutManager = LinearLayoutManager(this)
        newRecyclerView.setHasFixedSize(true)

        newArrayList = ArrayList()
        medicineAdapter = Medicine_Adapter(newArrayList, this)

        // Set adapter to RecyclerView
        newRecyclerView.adapter = medicineAdapter

        // Fetch medicines from server
        getMedicines()
    }

    private fun getMedicines() {
        val url = "http://192.168.100.44/api/get_medicines.php"

        val stringRequest = StringRequest(
            Request.Method.GET, url,
            Response.Listener<String> { response ->
                try {
                    val jsonArray = JSONArray(response)
                    for (i in 0 until jsonArray.length()) {
                        val jsonObject = jsonArray.getJSONObject(i)
                        val medicineName = jsonObject.getString("medicine_name")
                        val medicineData = Medicine_Data(medicineName)
                        newArrayList.add(medicineData)
                    }
                    // Notify adapter that data set has changed
                    medicineAdapter.notifyDataSetChanged()
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
