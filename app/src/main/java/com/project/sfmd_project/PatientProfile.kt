package com.project.sfmd_project

import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.bumptech.glide.Glide
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import de.hdodenhof.circleimageview.CircleImageView
import java.util.UUID

class PatientProfile : AppCompatActivity() {

    companion object {
        private const val IMAGE_PICK_CODE = 1000
    }
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var drawerLayout: DrawerLayout

    private var auth =FirebaseAuth.getInstance()
    private var database: DatabaseReference = FirebaseDatabase.getInstance().reference

    private var currentUser = FirebaseAuth.getInstance().currentUser

    private lateinit var mySQLiteHelper: MySQLiteHelper




    private var isGuestUser = true

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IMAGE_PICK_CODE && resultCode == RESULT_OK && data != null) {
            val imageUri = data.data
            val email = FirebaseAuth.getInstance().currentUser?.email ?: return

            imageUri?.let {
                uploadImageToFirebase(it, email)
            }
        }
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_patient_profile)

        val layoutParams = WindowManager.LayoutParams().apply {
            flags = WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        }
        window.attributes = layoutParams

        setContentView(R.layout.activity_patient_profile)



        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference


        currentUser = FirebaseAuth.getInstance().currentUser
        mySQLiteHelper = MySQLiteHelper(this)



        // Check if user is logged in
        val currentUser = auth.currentUser
        isGuestUser = currentUser == null

        // Attach click listeners only if user is not a guest
        attachClickListeners()



        var name = ""

        var phone = ""
        var age = ""
        var country = ""
        var password = ""
        var imgurl = ""


        var email = currentUser!!.email

         var nameTextView: TextView
        // private lateinit var emailTextView: TextView
         var phoneTextView: TextView
         var ageTextView: TextView
         var countryTextView: TextView
         var circleImageView: CircleImageView

        nameTextView = findViewById(R.id.name)
        phoneTextView = findViewById(R.id.textView9)
        ageTextView = findViewById(R.id.view11)
        countryTextView = findViewById(R.id.textView8)
        circleImageView = findViewById(R.id.circleImageView2)


        circleImageView = findViewById(R.id.circleImageView2)


        if (email != null) {
            val userRef = database.child("Users").orderByChild("email").equalTo(email)
            userRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for (data in dataSnapshot.children) {
                        val user = data.getValue(Signupuser::class.java)
                        if (user != null) {
                            nameTextView.text = user.name
                            phoneTextView.text = user.phone
                            ageTextView.text = user.age
                            countryTextView.text = user.country
                            imgurl=  user.imgurl
                            Glide.with(this@PatientProfile)
                                .load(user.imgurl)
                                .error(R.drawable.patient)
                                .placeholder(R.drawable.personicon)
                                .into(circleImageView)

                            val navView1: NavigationView = findViewById(R.id.nav)

                            Glide.with(this@PatientProfile)
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


        circleImageView.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, IMAGE_PICK_CODE)
        }







        val navView1: NavigationView = findViewById(R.id.nav)
        val nametTextView = navView1.getHeaderView(0).findViewById<TextView>(R.id.sidebarname)
        val emailtTextView = navView1.getHeaderView(0).findViewById<TextView>(R.id.sidebaremail)
        val sidebarImageView = navView1.getHeaderView(0).findViewById<CircleImageView>(R.id.sidebarpic)


        nametTextView.text = name
        emailtTextView.text = email









        drawerLayout = findViewById(R.id.sidebar)

        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val isGuestUser = intent.getBooleanExtra("isGuestUser", false)


        if (!isGuestUser ) {

                   } else {
            // Disable buttons for guest usersnameTextView
            disableAllButtons()
        }

    }

    private fun emailToKey(email: String): String {
        return email.replace(".", ",")
    }

    private fun uploadImageToFirebase(fileUri: Uri, email: String) {
        val fileName = "images/${UUID.randomUUID()}.jpg"  // It's better to use a unique identifier for each image
        val storageReference = FirebaseStorage.getInstance().getReference(fileName)

        storageReference.putFile(fileUri)
            .addOnSuccessListener { taskSnapshot ->
                taskSnapshot.storage.downloadUrl.addOnSuccessListener { downloadUri ->
                    val imageUrl = downloadUri.toString()
                    updateProfileImageUrl(email, imageUrl)
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Upload failed: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }


    private fun updateProfileImageUrl(email: String, newImageUrl: String) {
        val databaseRef = FirebaseDatabase.getInstance().getReference("Users")
        val key = emailToKey(email) // Convert email to a safe key format if necessary

        // Update the imgurl field for this user
        databaseRef.child(key).child("imgurl").setValue(newImageUrl)
            .addOnSuccessListener {
                Log.d("UpdateImgUrl", "Image URL updated successfully")
                Toast.makeText(this, "Profile image updated successfully.", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Log.e("UpdateImgUrl", "Failed to update image URL", e)
                Toast.makeText(this, "Failed to update profile image.", Toast.LENGTH_SHORT).show()
            }
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

    private fun attachClickListeners() {
        val home = findViewById<Button>(R.id.home)
        home.setOnClickListener {
            if(isGuestUser){
                showSignUpPrompt()
            }
            else {
                val intent = Intent(this@PatientProfile, Home::class.java)
                startActivity(intent)
            }
        }

        val profile = findViewById<Button>(R.id.person)
        profile.setOnClickListener {
            if(isGuestUser){
                showSignUpPrompt()
            }
            else {
                val intent = Intent(this@PatientProfile, PatientProfile::class.java)
                startActivity(intent)
            }
        }

        val complaint = findViewById<Button>(R.id.complaint)
        complaint.setOnClickListener {
            if(isGuestUser){
                showSignUpPrompt()
            }
            else {
                val intent = Intent(this@PatientProfile, Complaint::class.java)
                startActivity(intent)
            }
        }

        val edit = findViewById<Button>(R.id.edit)
        edit.setOnClickListener {
            if(isGuestUser){
                showSignUpPrompt()
            }
            else {
                val intent = Intent(this@PatientProfile, EditProfile::class.java)
                startActivity(intent)
            }
        }

        val diseases = findViewById<Button>(R.id.diseases)
        diseases.setOnClickListener {
            if(isGuestUser){
                showSignUpPrompt()
            }
            else {
                val intent = Intent(this@PatientProfile, Diseases::class.java)
                startActivity(intent)
            }
        }

        val appointment = findViewById<Button>(R.id.appointment)
        appointment.setOnClickListener {
            if(isGuestUser){
                showSignUpPrompt()
            }
            else {
                val intent = Intent(this@PatientProfile, Bookedresults::class.java)
                startActivity(intent)
            }
        }

        val record = findViewById<Button>(R.id.record)
        record.setOnClickListener {
            if(isGuestUser){
                showSignUpPrompt()
            }
            else {
                val intent = Intent(this@PatientProfile, Articles::class.java)
                startActivity(intent)
            }
        }

        val medicine = findViewById<Button>(R.id.medicines2)
        medicine.setOnClickListener {
            if(isGuestUser){
                showSignUpPrompt()
            }
            else {
                val intent = Intent(this@PatientProfile, Medicines::class.java)
                startActivity(intent)
            }
        }

        val fam = findViewById<Button>(R.id.family)
        fam.setOnClickListener {
            if(isGuestUser){
                showSignUpPrompt()
            }
            else {
                val intent = Intent(this@PatientProfile, FamilyDiseases::class.java)
                startActivity(intent)
            }
        }

        val lab = findViewById<Button>(R.id.lab)
        lab.setOnClickListener {
            if(isGuestUser){
                showSignUpPrompt()
            }
            else {
                val intent = Intent(this@PatientProfile, Labs::class.java)
                startActivity(intent)
            }
        }

        val metric = findViewById<Button>(R.id.health)
        metric.setOnClickListener {
            if(isGuestUser){
                showSignUpPrompt()
            }
            else {
                val intent = Intent(this@PatientProfile, MedicationTracker::class.java)
                startActivity(intent)
            }
        }

        val pharm = findViewById<Button>(R.id.medicines)
        pharm.setOnClickListener {
            if(isGuestUser){
                showSignUpPrompt()
            }
            else {
                val intent = Intent(this@PatientProfile, PharmacySearchActivity::class.java)
                startActivity(intent)
            }
        }

        val navView: NavigationView = findViewById(R.id.nav)
        val logoutButton = navView.getHeaderView(0).findViewById<Button>(R.id.logout)
        logoutButton.setOnClickListener {

                val intent = Intent(this@PatientProfile, Login::class.java)
                startActivity(intent)
                finish()

        }
        val chat = navView.getHeaderView(0).findViewById<Button>(R.id.chatbtn)
        chat.setOnClickListener {

                val intent = Intent(this@PatientProfile, ChatSearch::class.java)
                startActivity(intent)
                finish()

        }
        val search = navView.getHeaderView(0).findViewById<Button>(R.id.searchbtn)
        search.setOnClickListener {

                val intent = Intent(this@PatientProfile, Search::class.java)
                startActivity(intent)
                finish()

        }

        val emergency = navView.getHeaderView(0).findViewById<Button>(R.id.emergency)
        emergency.setOnClickListener {
            if(isGuestUser){
                showSignUpPrompt()
            }
            else {
                val intent = Intent(this@PatientProfile, Emergency::class.java)
                startActivity(intent)
                finish()
            }
        }

        val customer = navView.getHeaderView(0).findViewById<Button>(R.id.customer)
        customer.setOnClickListener {
            val intent = Intent(this@PatientProfile, Customer::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun disableAllButtons() {
        val allButtons = listOf<Button>(
            findViewById(R.id.home),
            findViewById(R.id.person),
            findViewById(R.id.complaint),
            findViewById(R.id.edit),
            findViewById(R.id.diseases),
            findViewById(R.id.appointment),
            findViewById(R.id.record),
            findViewById(R.id.medicines),
            findViewById(R.id.medicines2),
            findViewById(R.id.family),
            findViewById(R.id.lab),
            findViewById(R.id.health)
            // Add more buttons if needed
        )
        for (button in allButtons) {
            //button.isEnabled = false
            button.setOnClickListener {
                showSignUpPrompt()
            }
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