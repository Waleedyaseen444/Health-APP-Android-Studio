package com.project.sfmd_project

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity

class LabTestOptionsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lab_test_options)

        val spinner = findViewById<Spinner>(R.id.test_options_spinner)
        val options = resources.getStringArray(R.array.test_options)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, options)
        spinner.adapter = adapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedItem = options[position]
                // Navigate to the corresponding activity based on the selected item
                when (selectedItem) {
                    "CBC" -> startActivity(Intent(this@LabTestOptionsActivity, CBCActvity::class.java))
                    "Liver Panel" -> startActivity(Intent(this@LabTestOptionsActivity, LiverPanel::class.java))
                    "Urinalysis" -> startActivity(Intent(this@LabTestOptionsActivity, UralysisActvity::class.java))
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Handle case where nothing is selected (if needed)
            }
        }
    }
}
