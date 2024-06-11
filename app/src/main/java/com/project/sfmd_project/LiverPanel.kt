package com.project.sfmd_project

// LiverPanelActivity.kt
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL
import kotlin.concurrent.thread

class LiverPanel : AppCompatActivity() {

    private var selectedFilePath: String? = null

    private val getContent =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                selectedFilePath = uri.toString()
                // Handle the file URI here (if needed)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_liver_panel)

        val attachFileButton = findViewById<Button>(R.id.attach_file_button)
        attachFileButton.setOnClickListener {
            // Launch file picker
            getContent.launch("*/*")
        }

        val saveButton = findViewById<Button>(R.id.save_button)
        saveButton.setOnClickListener {
            val dateEditText = findViewById<EditText>(R.id.date_edittext)
            val timeEditText = findViewById<EditText>(R.id.time_edittext)

            val date = dateEditText.text.toString()
            val time = timeEditText.text.toString()

            // Check if all fields are filled
            if (date.isNotEmpty() && time.isNotEmpty() && selectedFilePath != null) {
                // Save data to MySQL database
                saveToDatabase("Liver Panel", date, time, selectedFilePath!!)
            } else {
                // Show error message (e.g., toast)
            }
        }
    }

    private fun saveToDatabase(testName: String, date: String, time: String, filePath: String) {
        thread {
            val url = URL("http://192.168.100.44/api/save_data.php")
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "POST"
            connection.doOutput = true
            val postData = "test_name=$testName&date=$date&time=$time&file_path=$filePath"
            val writer = OutputStreamWriter(connection.outputStream)
            writer.write(postData)
            writer.flush()
            val response = StringBuilder()
            val reader = BufferedReader(InputStreamReader(connection.inputStream))
            var line: String?
            while (reader.readLine().also { line = it } != null) {
                response.append(line)
            }
            writer.close()
            reader.close()
            connection.disconnect()

            // Process response if needed
            val jsonResponse = JSONObject(response.toString())
            val success = jsonResponse.getBoolean("success")
            val message = jsonResponse.getString("message")
            runOnUiThread {
                // Show a toast or handle success/failure accordingly
            }
        }
    }
}
