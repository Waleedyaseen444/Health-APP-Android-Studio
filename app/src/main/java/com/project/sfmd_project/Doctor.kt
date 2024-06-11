package com.project.sfmd_project

import java.io.Serializable

data class Doctor(
    val name: String = "",
    val specialization: String = "",
    val experience: String = "",
    val phoneNumber: String = "",
    val gender: String = "",
    val age: String = "",
    val imageUrl: String = "" // Added imageUrl field
) : Serializable // Implement Serializable interface
{
    companion object {
        fun fromString(string: String): Doctor? {
            val parts = string.split(",") // Assuming the string format is comma-separated values
            if (parts.size == 6) {
                return Doctor(parts[0], parts[1], parts[2], parts[3], parts[4], parts[5])
            }
            return null
        }
    }

    override fun toString(): String {
        return "$name,$specialization,$experience,$phoneNumber,$gender,$age"
    }
}
