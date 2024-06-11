package com.project.sfmd_project

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.WorkSource
import android.view.WindowManager
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class WorkExperience : AppCompatActivity() , WorkExperience_Adapter.OnItemClickListener  {
    private lateinit var newRecyclerView: RecyclerView
    private lateinit var newArrayList: ArrayList<WorkExperience_Data>
    private lateinit var work: Array<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val layoutParams = WindowManager.LayoutParams().apply {
            flags = WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        }
        window.attributes = layoutParams


        setContentView(R.layout.activity_work_experience)

        val back = findViewById<Button>(R.id.back)
        back.setOnClickListener {
            val intent = Intent(this@WorkExperience, DoctorProfile::class.java)
            startActivity(intent)
        }


        newRecyclerView= findViewById(R.id.recycler)
        newRecyclerView.layoutManager = LinearLayoutManager(this)
        newRecyclerView.setHasFixedSize(true)

        work = arrayOf(
            "Laparoscopic Cholecystectomy",
            "Total Knee Replacement (TKR)",
            "Coronary Artery Bypass Grafting (CABG)",
            "Spinal Fusion",
            "Appendectomy"
        )

        newArrayList = arrayListOf<WorkExperience_Data>()
        getUserData()

    }

    private fun getUserData(){
        for(i in work.indices){
            val docs = WorkExperience_Data(work[i])
            newArrayList.add(docs)
        }
        newRecyclerView.adapter =WorkExperience_Adapter(newArrayList, this)
    }
    override fun onItemClick(position: Int) {

        val intent = Intent(this, WorkExperience::class.java)
        startActivity(intent)
    }

}