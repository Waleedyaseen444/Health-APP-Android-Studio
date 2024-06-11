package com.project.sfmd_project

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import de.hdodenhof.circleimageview.CircleImageView

class Search : AppCompatActivity(), SearchRecent_Adapter.OnItemClickListener , SearchHigh_Adapter.OnItemClickListener {

    private lateinit var newRecyclerView1: RecyclerView
    private lateinit var newArrayList1: ArrayList<Profiledata2>
    private lateinit var dp1: Array<Int>
    private lateinit var rating1: Array<Float>
    private lateinit var name1: Array<String>
    private lateinit var type1: Array<String>

    private lateinit var newRecyclerView2: RecyclerView
    private lateinit var newArrayList2: ArrayList<Profiledata2>
    private lateinit var dp2: Array<Int>
    private lateinit var rating2: Array<Float>
    private lateinit var name2: Array<String>
    private lateinit var type2: Array<String>

    lateinit var toggle: ActionBarDrawerToggle

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var searchEditText: EditText


    private var auth = FirebaseAuth.getInstance()
    private var database: DatabaseReference = FirebaseDatabase.getInstance().reference

    private var currentUser = FirebaseAuth.getInstance().currentUser



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        Log.d("SearchActivity", "onCreate: Activity created") // Add logging statement







        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference


        currentUser = FirebaseAuth.getInstance().currentUser


        // Check if user is logged in
        val currentUser = auth.currentUser


        var email = currentUser!!.email








        if (email != null) {
            val userRef = database.child("Users").orderByChild("email").equalTo(email)
            userRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for (data in dataSnapshot.children) {
                        val user = data.getValue(Signupuser::class.java)
                        if (user != null) {


                          val namet: TextView = findViewById(R.id.textView5)

                            namet.text=user.name


                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.d("FirebaseError", "Error fetching user data: ${error.message}")
                }
            })
        } else {
            Log.d("EmailError", "Email is null")
        }





        val home  = findViewById<Button>(R.id.home)
        home.setOnClickListener {
            val intent = Intent(this@Search, Home::class.java)
            startActivity(intent)
        }

        val profile = findViewById<Button>(R.id.person)
        profile.setOnClickListener {
            val intent = Intent(this@Search, PatientProfile::class.java)
            startActivity(intent)
        }


        val appointment = findViewById<Button>(R.id.appointment)
        appointment.setOnClickListener {
            val intent = Intent(this@Search, Appointment::class.java)
            startActivity(intent)
        }

        val record = findViewById<Button>(R.id.record)
        record.setOnClickListener {
            val intent = Intent(this@Search, Record::class.java)
            startActivity(intent)
        }


        val navView : NavigationView = findViewById(R.id.nav)
        val logoutButton = navView.getHeaderView(0).findViewById<Button>(R.id.logout)
        logoutButton.setOnClickListener {
            val intent = Intent(this@Search, Login::class.java)
            startActivity(intent)
            finish()
        }
        val chat = navView.getHeaderView(0).findViewById<Button>(R.id.chatbtn)
        chat.setOnClickListener {
            val intent = Intent(this@Search, ChatSearch::class.java)
            startActivity(intent)
            finish()
        }


        val emergency = navView.getHeaderView(0).findViewById<Button>(R.id.emergency)
        emergency.setOnClickListener {
            val intent = Intent(this@Search, Emergency::class.java)
            startActivity(intent)
            finish()
        }

        val customer = navView.getHeaderView(0).findViewById<Button>(R.id.customer)
        customer.setOnClickListener {
            val intent = Intent(this@Search, Customer::class.java)
            startActivity(intent)
            finish()
        }





        drawerLayout = findViewById(R.id.sidebar)
        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)




        newRecyclerView1= findViewById(R.id.recycler1)
        newRecyclerView1.layoutManager = LinearLayoutManager(this,
            LinearLayoutManager.HORIZONTAL,false)

        newRecyclerView1.setHasFixedSize(true)



        searchEditText = findViewById(R.id.editTextText)
        val searchButton = findViewById<ImageButton>(R.id.imageButton)

        searchButton.setOnClickListener {
            val query = searchEditText.text.toString().trim()
            val intent = Intent(this@Search, Searchresults::class.java)
            intent.putExtra("searchtext", query)
            startActivity(intent)
        }

        dp1 = arrayOf(
            R.drawable.person_2,
            R.drawable.person_3,
            R.drawable.person_4,
            R.drawable.person_5
        )

        rating1 = arrayOf(
            4.0f,
            4.0f,
            4.0f,
            4.0f
        )

        name1 = arrayOf(

            "Savannah Johnson",
            "Caleb Monroe",
            "Harper Hayes",
            "Miles Donovan"


        )

        type1 = arrayOf(
            "Neurosurgoen",
            "Orthopedic Surgeon",
            "Cardiologist",
            "Pediatrician"
        )

        newArrayList1 = arrayListOf<Profiledata2>()
        getUserData1()

        newRecyclerView2= findViewById(R.id.recycler2)
        newRecyclerView2.layoutManager = LinearLayoutManager(this,
            LinearLayoutManager.HORIZONTAL,false)

        newRecyclerView2.setHasFixedSize(true)


        dp2 = arrayOf(
            R.drawable.person_2,
            R.drawable.person_3,
            R.drawable.person_4,
            R.drawable.person_5
        )

        rating2 = arrayOf(
            4.0f,
            4.0f,
            4.0f,
            4.0f
        )

        name2 = arrayOf(

            "Savannah Johnson",
            "Caleb Monroe",
            "Harper Hayes",
            "Miles Donovan"


        )

        type2 = arrayOf(
            "Neurosurgoen",
            "Orthopedic Surgeon",
            "Cardiologist",
            "Pediatrician"
        )

        newArrayList2 = arrayListOf<Profiledata2>()
        getUserData2()






    }



    private fun getUserData1(){
        for(i in name1.indices){
            val docs = Profiledata2(dp1[i],rating1[i],name1[i],type1[i])
            newArrayList1.add(docs)
        }
        newRecyclerView1.adapter =SearchHigh_Adapter(newArrayList1, this)
    }

    private fun getUserData2(){
        for(i in name2.indices){
            val docs = Profiledata2(dp2[i],rating2[i],name2[i],type2[i])
            newArrayList2.add(docs)
        }
        newRecyclerView2.adapter =SearchRecent_Adapter(newArrayList2, this)
    }
    override fun onItemClick(position: Int) {

        val intent = Intent(this, DoctorProfile::class.java)
        startActivity(intent)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if(toggle.onOptionsItemSelected(item)){
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    fun toggleDrawer(view: View) {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            drawerLayout.openDrawer(GravityCompat.START)
        }
    }

}
