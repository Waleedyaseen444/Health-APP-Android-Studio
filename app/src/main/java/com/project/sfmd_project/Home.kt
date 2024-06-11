package com.project.sfmd_project

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import de.hdodenhof.circleimageview.CircleImageView

class Home : AppCompatActivity(), Profile_Adapter.OnItemClickListener {

    private lateinit var newRecyclerView: RecyclerView
    private lateinit var newArrayList: ArrayList<Profile_Data>
    private lateinit var databaseReference: DatabaseReference

    lateinit var toggle: ActionBarDrawerToggle
    private lateinit var drawerLayout: DrawerLayout


    private var auth =FirebaseAuth.getInstance()
    private var database: DatabaseReference = FirebaseDatabase.getInstance().reference

    private var currentUser = FirebaseAuth.getInstance().currentUser
    private lateinit var mySQLiteHelper: MySQLiteHelper // Initialize SQLiteHelper

    private var isGuestUser = true


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val layoutParams = WindowManager.LayoutParams().apply {
            flags = WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        }
        window.attributes = layoutParams

        setContentView(R.layout.activity_home)




        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference


        currentUser = FirebaseAuth.getInstance().currentUser
        isGuestUser = currentUser == null


        mySQLiteHelper = MySQLiteHelper(this)

        // Check if user is logged in
        val currentUser = auth.currentUser

        var name = ""
        var imgurl = ""

        var email: String? =null

        if(currentUser != null){
            email=  currentUser.email;

        }



        // Check if user is logged in




        if (email != null) {
            val userRef = database.child("Users").orderByChild("email").equalTo(email)
            userRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for (data in dataSnapshot.children) {
                        val user = data.getValue(Signupuser::class.java)
                        if (user != null) {

                            imgurl=  user.imgurl

                            val navView1: NavigationView = findViewById(R.id.nav)
                            val nametTextView = navView1.getHeaderView(0).findViewById<TextView>(R.id.sidebarname)
                            val emailtTextView = navView1.getHeaderView(0).findViewById<TextView>(R.id.sidebaremail)
                            val sidebarImageView = navView1.getHeaderView(0).findViewById<CircleImageView>(R.id.sidebarpic)
                            val namet: TextView = findViewById(R.id.textView5)

                            namet.text=user.name

                            nametTextView.text = user.name
                            emailtTextView.text = user.email

                            Glide.with(this@Home)
                                .load(imgurl)
                                .error(R.drawable.patient) // Set the error drawable
                                .placeholder(R.drawable.personicon)
                                .into(navView1.getHeaderView(0).findViewById<CircleImageView>(R.id.sidebarpic))

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













        val specialisation = findViewById<Button>(R.id.speacializations)
        specialisation.setOnClickListener {
            val intent = Intent(this@Home, Speacial::class.java)
            startActivity(intent)
        }



        val articles = findViewById<Button>(R.id.articles)
        articles.setOnClickListener {
            val intent = Intent(this@Home, Articles::class.java)
            startActivity(intent)
        }

        val appointment = findViewById<Button>(R.id.appointment)
        appointment.setOnClickListener {

            if(isGuestUser){
                showSignUpPrompt()
            }
            else {

                val intent = Intent(this@Home, test1::class.java)
                startActivity(intent)
            }

        }

        val record = findViewById<Button>(R.id.record)
        record.setOnClickListener {

            if(isGuestUser){
                showSignUpPrompt()
            }
            else {

                val intent = Intent(this@Home, Record::class.java)
                startActivity(intent)
            }

        }

        val profile = findViewById<Button>(R.id.person)
        profile.setOnClickListener {


            if(isGuestUser){
                showSignUpPrompt()
            }
            else {
                val intent = Intent(this@Home, PatientProfile::class.java)
                startActivity(intent)
            }

        }

        val home = findViewById<Button>(R.id.home)
        home.setOnClickListener {
            val intent = Intent(this@Home, Home::class.java)
            startActivity(intent)
        }

        val navView: NavigationView = findViewById(R.id.nav)
        val logoutButton = navView.getHeaderView(0).findViewById<Button>(R.id.logout)
        logoutButton.setOnClickListener {
            val intent = Intent(this@Home, Login::class.java)
            startActivity(intent)
            finish()
        }
        val chat = navView.getHeaderView(0).findViewById<Button>(R.id.chatbtn)
        chat.setOnClickListener {

            if(isGuestUser){
                showSignUpPrompt()
            }
            else {
            val intent = Intent(this@Home, ChatSearch::class.java)
            startActivity(intent)
            finish()
            }
        }

        val search = navView.getHeaderView(0).findViewById<Button>(R.id.searchbtn)
        search.setOnClickListener {
            val intent = Intent(this@Home, Search::class.java)
            startActivity(intent)
            finish()
        }

        val emergency = navView.getHeaderView(0).findViewById<Button>(R.id.emergency)
        emergency.setOnClickListener {
            val intent = Intent(this@Home, Emergency::class.java)
            startActivity(intent)
            finish()
        }

        val customer = navView.getHeaderView(0).findViewById<Button>(R.id.customer)
        customer.setOnClickListener {
            val intent = Intent(this@Home, Customer::class.java)
            startActivity(intent)
            finish()
        }

        drawerLayout = findViewById(R.id.sidebar)

        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        newRecyclerView = findViewById(R.id.recycler)
        newRecyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        newRecyclerView.setHasFixedSize(true)

        // Initialize Firebase Database reference
        databaseReference = FirebaseDatabase.getInstance().getReference("doctors")

        // Initialize ArrayList to hold fetched data
        newArrayList = ArrayList()

        // Set up RecyclerView adapter
        newRecyclerView.adapter = Profile_Adapter(newArrayList, this)

        // Fetch data from Firebase Database
        fetchDataFromFirebase()


    }

    private fun fetchDataFromFirebase() {
        // First, try to fetch data from SQLite
        val mySQLiteHelper = MySQLiteHelper(this)
        val offlineData = mySQLiteHelper.getAllData()

        if (offlineData.isNotEmpty()) {
            // If offline data is available, populate the ArrayList and notify the adapter
            newArrayList.clear()
            for (data in offlineData) {
                // Convert the data string back to Doctor object and add to the ArrayList
                val doctor = Doctor.fromString(data)
                doctor?.let {
                    newArrayList.add(
                        Profile_Data(
                            it.imageUrl,
                            4.0f, // Default rating
                            it.name ?: "",
                            it.specialization ?: "",
                            "0300111222333"
                        )
                    )
                }
            }
            // Notify adapter that data has changed
            newRecyclerView.adapter?.notifyDataSetChanged()
        }

        // If no offline data available or device is online, fetch data from Firebase
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (offlineData.isEmpty()) { // Avoid fetching data again if already fetched from SQLite
                    newArrayList.clear()
                }
                for (snapshot in dataSnapshot.children) {
                    val doctor = snapshot.getValue(Doctor::class.java)
                    doctor?.let {
                        val imageUrl = it.imageUrl.toString() // Convert imageUrl to string URL
                        newArrayList.add(
                            Profile_Data(
                                imageUrl,
                                4.0f, // Default rating
                                it.name ?: "",
                                it.specialization ?: "",
                                "0300111222333"
                            )
                        )
                    }
                }
                // Notify adapter that data has changed
                newRecyclerView.adapter?.notifyDataSetChanged()

                // If offline data was fetched, update SQLite with the latest data from Firebase
                if (offlineData.isNotEmpty()) {
                    // Clear existing data in SQLite
                    mySQLiteHelper.deleteAllData()
                    // Insert new data from Firebase into SQLite
                    for (data in newArrayList) {
                        val id = mySQLiteHelper.insertData(data.toString())
                        // Check if insertion was successful and handle accordingly
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle database error
                Log.e("FirebaseError", "Database error: ${databaseError.message}")
            }
        })
    }

    override fun onItemClick(position: Int) {
        val doctor = newArrayList[position] // Get the clicked doctor
        val intent = Intent(this, DoctorProfile::class.java).apply {
            // Pass individual properties of the Profile_Data object
            putExtra("imageUrl", doctor.imageUrl)
            putExtra("name", doctor.name)
            putExtra("specialization", doctor.type)
            putExtra("phoneNumber", doctor.phoneNumber)
            // Add other properties as needed
        }
        startActivity(intent)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
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


    private fun showSignUpPrompt() {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_signup_prompt)

        val signUpButton = dialog.findViewById<Button>(R.id.signUpButton)
        signUpButton.setOnClickListener {
            // Navigate to sign-up activity
            val intent = Intent(this, PatientSignUp::class.java)
            startActivity(intent)
            dialog.dismiss()
        }

        dialog.show()
    }

}

