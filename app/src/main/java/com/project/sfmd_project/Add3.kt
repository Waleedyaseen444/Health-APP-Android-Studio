package com.project.sfmd_project

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.Button

class Add3 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val layoutParams = WindowManager.LayoutParams().apply {
            flags = WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        }
        window.attributes = layoutParams

        setContentView(R.layout.activity_add3)


        val back = findViewById<Button>(R.id.back)
        back.setOnClickListener {
            val intent = Intent(this@Add3, Home::class.java)
            startActivity(intent)
        }

        val add = findViewById<Button>(R.id.done)
        add.setOnClickListener {
            val intent = Intent(this@Add3, Home::class.java)
            startActivity(intent)
        }

    }
}