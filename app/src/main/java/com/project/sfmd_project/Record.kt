package com.project.sfmd_project

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class Record : AppCompatActivity(), Record_Adapter.OnItemClickListener  {


    private lateinit var newRecyclerView: RecyclerView
    private lateinit var newArrayList: ArrayList<Record_data>
    private lateinit var rec: Array<String>

    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        val layoutParams = WindowManager.LayoutParams().apply {
            flags = WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        }
        window.attributes = layoutParams

        setContentView(R.layout.activity_record)


        val back = findViewById<Button>(R.id.back)
        back.setOnClickListener {
            val intent = Intent(this@Record, Home::class.java)
            startActivity(intent)
        }

        val add = findViewById<Button>(R.id.plus)
        add.setOnClickListener {
            val intent = Intent(this@Record, Add3::class.java)
            startActivity(intent)
        }

        newRecyclerView= findViewById(R.id.recycler)
        newRecyclerView.layoutManager = LinearLayoutManager(this)
        newRecyclerView.setHasFixedSize(true)

        rec = arrayOf(
            "Medical History:\n" +
                    "Chronic Conditions: None reported\n" +
                    "Allergies: Mild allergy to penicillin\n" +
                    "Medications: None currently prescribed\n" +
                    "Past Surgeries: Appendectomy in 2005",

        )

        newArrayList = arrayListOf<Record_data>()
        getUserData()

    }

    private fun getUserData(){
        for(i in rec.indices){
            val docs = Record_data(rec[i])
            newArrayList.add(docs)
        }
        newRecyclerView.adapter =Record_Adapter(newArrayList, this)
    }
    override fun onItemClick(position: Int) {

        val intent = Intent(this, Articles::class.java)
        startActivity(intent)
    }
}
