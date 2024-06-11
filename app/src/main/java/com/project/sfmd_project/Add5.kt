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


class Add5 : AppCompatActivity() {

    private lateinit var editText: EditText
    private lateinit var mySQLiteHelper: MySQLiteHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_add5)

        editText = findViewById(R.id.editText)

        val back = findViewById<Button>(R.id.back)
        back.setOnClickListener {
            val intent = Intent(this@Add5, PatientProfile::class.java)
            startActivity(intent)
        }

        val add = findViewById<Button>(R.id.done)
        add.setOnClickListener {
            val medicineText = editText.text.toString().trim()

            // Check if the device is online
            if (NetworkUtils.isOnline(this)) {
                insertMedicineOnline(medicineText)
            } else {
                // Device is offline, save data locally
                saveMedicineLocally(medicineText)
                Toast.makeText(this@Add5, "Medicine saved locally", Toast.LENGTH_SHORT).show()
            }
        }

        // Initialize SQLite database helper
        mySQLiteHelper = MySQLiteHelper(this)
    }

    private fun insertMedicineOnline(medicineText: String) {
        val url = "http://192.168.100.44/api/insert_medicines.php"
        val request = object : StringRequest(
            Request.Method.POST, url,
            Response.Listener { response ->
                // Handle response if needed
                Toast.makeText(this, "Medicine added successfully", Toast.LENGTH_SHORT).show()
                // Sync local data with server on successful insertion
                val myFirebaseMessagingService = MyFirebaseMessagingService()
                myFirebaseMessagingService.generateNotification(this,"Doctoroid", " Medicine added successfully " )

                syncLocalDataWithServer()


            },
            Response.ErrorListener { error ->
                // Handle error
                Toast.makeText(this, "Error occurred: ${error.message}", Toast.LENGTH_SHORT).show()
            }) {
            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()
                params["medicineName"] = medicineText
                return params
            }
        }

        Volley.newRequestQueue(this).add(request)
    }

    private fun saveMedicineLocally(medicineText: String) {
        // Save data locally using SQLite
        val id = mySQLiteHelper.insertData(medicineText)
        if (id != -1L) {
            // Data saved successfully
        } else {
            // Failed to save data locally
            Toast.makeText(this@Add5, "Failed to save data locally", Toast.LENGTH_SHORT).show()
        }
    }

    private fun syncLocalDataWithServer() {
        // Retrieve locally saved data
        val localData = mySQLiteHelper.getAllData()

        // Iterate through each locally saved item
        for (data in localData) {
            val medicineText = data // Assuming data is the string you want to sync

            // Send data to the server
            insertMedicineOnline(medicineText)
        }

        // After syncing, you might want to clear the local database
        mySQLiteHelper.deleteAllData()
    }
}
