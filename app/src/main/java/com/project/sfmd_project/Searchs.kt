package com.project.sfmd_project

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.navigation.NavigationView
import com.google.firebase.database.DatabaseReference

class Searchs : AppCompatActivity() {


    private lateinit var searchViews: SearchView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        Log.d("SearchActivity", "onCreate: Activity created") // Add logging statement

















        searchViews = findViewById(R.id.searchView)
        if (searchViews == null) {
            Log.e("SearchActivity", "SearchView not found in layout")
            // Handle the error, maybe show a Toast or log an error message
        } else {
            searchViews.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    query?.let {
                        val intent = Intent(this@Searchs, Searchresults::class.java)
                        intent.putExtra("query", it)
                        startActivity(intent)
                        return true
                    }
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    // You can implement real-time suggestions here if needed
                    return true
                }
            })
        }


    }




}
