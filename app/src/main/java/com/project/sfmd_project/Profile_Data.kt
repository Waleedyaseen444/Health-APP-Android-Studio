package com.project.sfmd_project

import android.widget.RatingBar

data class Profile_Data(
    val imageUrl: String, // Assuming imageUrl is the URL of the doctor's image
    val ratingBar: Float, // Assuming you have a float rating
    val name: String,
    val type: String ,
    val phoneNumber: String
)
