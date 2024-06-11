package com.project.sfmd_project

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class Labs : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_labs)
    }

    // Click listener for Chugtai Lab button
    fun openChugtaiWebsite(view: View) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.chugtai.com"))
        startActivity(intent)
    }

    // Click listener for Shaukat Khanam Lab button
    fun openShaukatKhanamWebsite(view: View) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://shaukatkhanum.org.pk"))
        startActivity(intent)
    }

    // Click listener for Lifecare button
    fun openLifecareWebsite(view: View) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.lifecare.com"))
        startActivity(intent)
    }

    // Click listener for Accredited Lab button
    fun openAccreditedLabWebsite(view: View) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.accreditedlab.com"))
        startActivity(intent)
    }
}
