package com.project.sfmd_project

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type
import java.util.Calendar

class MedicationTracker : AppCompatActivity() {

    private lateinit var medicationList: ArrayList<Medication>
    private lateinit var medicationAdapter: MedicationAdapter
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_medication_tracker)

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("MedicationPreferences", Context.MODE_PRIVATE)

        // Load medication list from SharedPreferences
        loadMedicationList()

        // Initialize RecyclerView
        val medicationRecyclerView: RecyclerView = findViewById(R.id.medicationRecyclerView)
        medicationRecyclerView.layoutManager = LinearLayoutManager(this)
        medicationAdapter = MedicationAdapter(medicationList)
        medicationRecyclerView.adapter = medicationAdapter

        // Button to save medication
        val saveMedicationButton: Button = findViewById(R.id.saveMedicationButton)
        saveMedicationButton.setOnClickListener {
            // Add medication to the list
            addMedication()
            // Save medication list to SharedPreferences
            saveMedicationList()
            // Notify adapter of data change
            medicationAdapter.notifyDataSetChanged()

            val timeEditText = findViewById<EditText>(R.id.timeEditText)
            val time = timeEditText.text.toString()

            if (time.isNotEmpty()) {
                setUpNotification(time)
            } else {
                Log.d("Notification", "Time is empty. Please enter a valid time.")
                // Handle the case where the timeEditText is empty
                // For example, show a toast message or set a default time
            }
        }

        // Get the time entered by the user

    }

    private fun loadMedicationList() {
        val gson = Gson()
        val json = sharedPreferences.getString("medicationList", null)
        val type: Type = object : TypeToken<ArrayList<Medication>>() {}.type
        medicationList = gson.fromJson(json, type) ?: ArrayList()
    }

    private fun saveMedicationList() {
        val editor = sharedPreferences.edit()
        val gson = Gson()
        val json = gson.toJson(medicationList)
        editor.putString("medicationList", json)
        editor.apply()
    }

    private fun addMedication() {
        val medicationNameEditText = findViewById<EditText>(R.id.medicationNameEditText)
        val dosageEditText = findViewById<EditText>(R.id.dosageEditText)
        val frequencyEditText = findViewById<EditText>(R.id.frequencyEditText)
        val timeEditText = findViewById<EditText>(R.id.timeEditText)

        val name = medicationNameEditText.text.toString()
        val dosage = dosageEditText.text.toString()
        val frequency = frequencyEditText.text.toString()
        val time = timeEditText.text.toString()

        val newMedication = Medication(name, dosage, frequency, time)
        medicationList.add(newMedication)
    }

    private fun setUpNotification(time: String) {
        val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, MedicationNotificationReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        // Split the time string into hour, minute, and AM/PM parts
        val timeParts = time.split(":")
        val hour = timeParts[0].trim().toInt()
        val minute = timeParts[1].split(" ")[0].trim().toInt() // Extract minute part
        val amPm = timeParts[1].split(" ")[1].trim() // Extract AM/PM part

        // Adjust the hour for PM
        val adjustedHour = if (amPm.equals("PM", ignoreCase = true)) {
            if (hour != 12) hour + 12 else hour // If hour is 12 PM, keep it the same, otherwise add 12
        } else {
            if (hour == 12) 0 else hour // If hour is 12 AM, set it to 0, otherwise keep it the same
        }

        // Set the alarm time
        val calendar: Calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, adjustedHour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
        }

        Log.d("Notification", "Alarm scheduled for: ${calendar.time}")

        // Set the repeating alarm
        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            pendingIntent
        )
    }

}
