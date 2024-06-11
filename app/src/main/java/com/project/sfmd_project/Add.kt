package com.project.sfmd_project

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley

class Add : AppCompatActivity() {

    private lateinit var editText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val layoutParams = WindowManager.LayoutParams().apply {
            flags = WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        }
        window.attributes = layoutParams

        setContentView(R.layout.activity_add)

        editText = findViewById(R.id.editText)

        val back = findViewById<Button>(R.id.back)
        back.setOnClickListener {
            val intent = Intent(this@Add, PatientProfile::class.java)
            startActivity(intent)
        }

        val add = findViewById<Button>(R.id.done)
        add.setOnClickListener {
            val complaintText = editText.text.toString().trim()
            insertComplaint(complaintText)
        }

    }

    private fun insertComplaint(complaintText: String) {
        val url = "http://192.168.100.44/api/insert_complaint.php"
        val request = object : StringRequest(
            Request.Method.POST, url,
            Response.Listener { response ->
                // Handle response if needed
                Toast.makeText(this, "Complaint added successfully", Toast.LENGTH_SHORT).show()
            },
            Response.ErrorListener { error ->
                // Handle error
                Toast.makeText(this, "Error occurred: ${error.message}", Toast.LENGTH_SHORT).show()
            }) {
            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()
                params["complaintText"] = complaintText
                return params
            }
        }

        Volley.newRequestQueue(this).add(request)
    }
}
