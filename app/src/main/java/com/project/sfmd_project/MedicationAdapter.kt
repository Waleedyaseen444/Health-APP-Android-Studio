package com.project.sfmd_project

// MedicationAdapter.kt
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MedicationAdapter(private val medicationList: ArrayList<Medication>) :
    RecyclerView.Adapter<MedicationAdapter.MedicationViewHolder>() {

    inner class MedicationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val medicationNameTextView: TextView = itemView.findViewById(R.id.medicationNameTextView)
        val dosageTextView: TextView = itemView.findViewById(R.id.dosageTextView)
        val frequencyTextView: TextView = itemView.findViewById(R.id.frequencyTextView)
        val timeTextView: TextView = itemView.findViewById(R.id.timeTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MedicationViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_medication, parent, false)
        return MedicationViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MedicationViewHolder, position: Int) {
        val currentMedication = medicationList[position]
        holder.medicationNameTextView.text = currentMedication.name
        holder.dosageTextView.text = "Dosage: ${currentMedication.dosage}"
        holder.frequencyTextView.text = "Frequency: ${currentMedication.frequency}"
        holder.timeTextView.text = "Time of Intake: ${currentMedication.time}"
    }

    override fun getItemCount(): Int {
        return medicationList.size
    }
}

