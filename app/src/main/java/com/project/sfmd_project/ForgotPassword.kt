package com.project.sfmd_project

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView

class ForgotPassword : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val layoutParams = WindowManager.LayoutParams().apply {
            flags = WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        }
        window.attributes = layoutParams

        setContentView(R.layout.activity_forgot_password)

        val back = findViewById<TextView>(R.id.back)
        back.setOnClickListener {
            val backIntent = Intent(this, Login::class.java)
            startActivity(backIntent)
        }

        val done = findViewById<Button>(R.id.button2)
        done.setOnClickListener {
            val doneIntent = Intent(this, Home::class.java)
            startActivity(doneIntent)
        }
    }
}