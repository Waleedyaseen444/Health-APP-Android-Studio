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
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray

class Speacial : AppCompatActivity(), Specialization_adapter.OnItemClickListener {

    private lateinit var newRecyclerView: RecyclerView
    private lateinit var newArrayList: ArrayList<Specializations_data>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val layoutParams = WindowManager.LayoutParams().apply {
            flags = WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        }
        window.attributes = layoutParams

        setContentView(R.layout.activity_speacial)

        val back = findViewById<Button>(R.id.back)
        back.setOnClickListener {
            val intent = Intent(this@Speacial, Home::class.java)
            startActivity(intent)
        }

        newRecyclerView = findViewById(R.id.recycler)
        newRecyclerView.layoutManager = LinearLayoutManager(this)
        newRecyclerView.setHasFixedSize(true)

        fetchSpecializations()
    }

    private fun fetchSpecializations() {
        val queue = Volley.newRequestQueue(this)
        val url = "http://192.168.100.17/api/get_specializations.php"

        val request = JsonArrayRequest(Request.Method.GET, url, null,
            Response.Listener<JSONArray> { response ->
                // Parse the JSON response
                newArrayList = ArrayList()
                for (i in 0 until response.length()) {
                    val specialization = response.getJSONObject(i)
                    val name = specialization.getString("specialization_name")
                    val desc = specialization.getString("description")
                    val symptoms = specialization.getString("symptoms")
                    newArrayList.add(Specializations_data(name, desc, symptoms))
                }
                // Update the RecyclerView with the fetched data
                newRecyclerView.adapter = Specialization_adapter(newArrayList, this)
            },
            Response.ErrorListener { error ->
                // Handle errors
                error.printStackTrace()
            })

        // Add the request to the RequestQueue
        queue.add(request)
    }

    override fun onItemClick(position: Int) {
        // Handle item click if needed
    }
}
