package com.project.sfmd_project

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import org.json.JSONException

class Emergency : AppCompatActivity(), Emergency_Adapter.OnItemClickListener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var arrayList: ArrayList<Emergency_Data>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_emergency)

        val back = findViewById<Button>(R.id.back)
        back.setOnClickListener {
            val intent = Intent(this@Emergency, Home::class.java)
            startActivity(intent)
        }

        recyclerView = findViewById(R.id.recycler)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        arrayList = arrayListOf()

        val searchView = findViewById<SearchView>(R.id.searchView)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let { searchEmergencyServices(it) }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
    }

    private fun searchEmergencyServices(city: String) {
        val hospitalsAndCenters = mapOf(
            "Islamabad" to mapOf(
                "Shifa International Hospital" to Pair("33.6949", "73.0491"),
                "Pakistan Institute of Medical Sciences (PIMS)" to Pair("33.6844", "73.0479"),
                "Capital Diagnostic Centre" to Pair("33.7023", "73.0614"),
                "Capital Diagnostic Centre" to Pair("33.7023", "73.0614"),
                "Maroof International Hospital" to Pair("33.6669", "73.0498"),
                "Quaid-e-Azam International Hospital" to Pair("33.6721", "73.0228"),
                "Holy Family Hospital" to Pair("33.5951", "73.0571"),
                "Federal Government Services Hospital" to Pair("33.6829", "73.0479"),
                "Ali Medical Center" to Pair("33.6841", "73.0512"),
                "Islamabad Diagnostic Centre" to Pair("33.6632", "73.0256"),
                "Federal General Hospital" to Pair("33.6926", "73.0473"),
                "Al-Shifa Trust Eye Hospital" to Pair("33.6843", "73.0461"),
                "Poly Clinic Hospital" to Pair("33.6980", "73.0451"),
                "Emergency & Allied Hospital" to Pair("33.6512", "73.0217"),
                "Ghurki Trust Teaching Hospital" to Pair("33.6826", "73.0651"),
                "Fatima Memorial Hospital" to Pair("33.6340", "73.0515"),
                "Rawal Institute of Health Sciences" to Pair("33.6690", "73.0281"),
                "Shaukat Khanum Laboratory Collection Centre" to Pair("33.7006", "73.0719")
            ),
            "Lahore" to mapOf(
                "Shaukat Khanum Memorial Cancer Hospital & Research Centre" to Pair("31.5313", "74.3523"),
                "Jinnah Hospital" to Pair("31.5607", "74.3125"),
                "Services Hospital" to Pair("31.5536", "74.3174"),
                "Services Hospital" to Pair("31.5536", "74.3174"),
                "Lahore General Hospital" to Pair("31.5831", "74.3183"),
                "Ittefaq Hospital" to Pair("31.5517", "74.3643"),
            ),
            "Karachi" to mapOf(
                "Aga Khan University Hospital" to Pair("24.8826", "67.0682"),
                "Jinnah Postgraduate Medical Centre" to Pair("24.8611", "67.0327"),
                "Liaquat National Hospital" to Pair("24.8977", "67.0689"),
                "Liaquat National Hospital" to Pair("24.8977", "67.0689"),
                "Indus Hospital" to Pair("24.8703", "67.0385"),
                "Ziauddin Hospital" to Pair("24.8869", "67.0645"),
            )
        )

        if (city.equals("Islamabad", ignoreCase = true)) {
            displayHospitalsAndCenters(hospitalsAndCenters[city])
        } else if (city.equals("Lahore", ignoreCase = true)) {
            displayHospitalsAndCenters(hospitalsAndCenters[city])
        } else if (city.equals("Karachi", ignoreCase = true)) {
            displayHospitalsAndCenters(hospitalsAndCenters[city])
        } else {
            // If city is not Islamabad, Lahore, or Karachi, perform API call to fetch location
            val url = "https://nominatim.openstreetmap.org/search?q=$city&format=json"

            val jsonArrayRequest = JsonArrayRequest(
                Request.Method.GET, url, null,
                { response ->
                    try {
                        Log.d("JSON_RESPONSE", response.toString()) // Print JSON response

                        if (response.length() > 0) {
                            val place = response.getJSONObject(0)
                            val lat = place.getString("lat")
                            val lon = place.getString("lon")

                            fetchEmergencyServices(lat, lon)
                        } else {
                            Toast.makeText(
                                this,
                                "Location not found",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } catch (e: JSONException) {
                        e.printStackTrace()
                        Toast.makeText(
                            this,
                            "Error: ${e.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                },
                { error ->
                    error.printStackTrace()
                    Toast.makeText(
                        this,
                        "Failed to fetch location: ${error.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            )

            Volley.newRequestQueue(this).add(jsonArrayRequest)
        }
    }

    private fun displayHospitalsAndCenters(hospitalsAndCenters: Map<String, Pair<String, String>>?) {
        if (hospitalsAndCenters != null) {
            arrayList.clear()
            hospitalsAndCenters.forEach { (place, location) ->
                arrayList.add(Emergency_Data(place, "")) // Empty number for now
            }
            // Set the adapter after adding the data
            recyclerView.adapter = Emergency_Adapter(arrayList, this)
        } else {
            Toast.makeText(
                this,
                "No hospital data available for this city",
                Toast.LENGTH_SHORT
            ).show()
        }
    }


    private fun fetchEmergencyServices(latitude: String, longitude: String) {
        val url = "https://nominatim.openstreetmap.org/reverse?format=json&lat=$latitude&lon=$longitude"

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                try {
                    // Check if the "hospital" key exists in the response
                    if (response.has("hospital")) {
                        // Accessing the response as a JSONObject
                        val hospitalsString = response.getString("hospital")
                        val numbersString = response.getString("number")

                        // Split the hospital and number strings by a delimiter, assuming it's a comma
                        val hospitals = hospitalsString.split(",")
                        val numbers = numbersString.split(",")

                        // Clear the list
                        arrayList.clear()

                        // Iterate over the split strings and add them to the list
                        for (i in hospitals.indices) {
                            val hospital = hospitals[i].trim() // Trim any leading or trailing whitespace
                            val number = numbers.getOrNull(i)?.trim() ?: "" // Ensure that number exists and trim whitespace
                            val data = Emergency_Data(hospital, number)
                            arrayList.add(data)
                        }

                        // Notify the adapter of the data change
                        recyclerView.adapter?.notifyDataSetChanged()
                    } else {
                        Toast.makeText(
                            this,
                            "No hospital data available",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    Toast.makeText(
                        this,
                        "Error: ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            },
            { error ->
                error.printStackTrace()
                Toast.makeText(
                    this,
                    "Failed to fetch emergency services: ${error.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        )

        Volley.newRequestQueue(this).add(jsonObjectRequest)
    }




    override fun onItemClick(position: Int) {
        val selectedItem = arrayList[position]
        val hospitalName = selectedItem.hospital

        val encodedHospitalName = Uri.encode(hospitalName)
        val gmmIntentUri = Uri.parse("geo:0,0?q=$encodedHospitalName")
        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
        mapIntent.setPackage("com.google.android.apps.maps")
        if (mapIntent.resolveActivity(packageManager) != null) {
            startActivity(mapIntent)
        } else {
            Toast.makeText(this, "Google Maps app not installed", Toast.LENGTH_SHORT).show()
        }
    }



}
