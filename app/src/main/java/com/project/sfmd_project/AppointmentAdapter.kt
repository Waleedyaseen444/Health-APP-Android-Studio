package com.project.sfmd_project

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class AppointmentAdapter : RecyclerView.Adapter<AppointmentAdapter.AppointmentViewHolder>() {

    private var appointments: List<Appointments> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppointmentViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.row_appointment, parent, false)
        return AppointmentViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: AppointmentViewHolder, position: Int) {
        val appointment = appointments[position]
        holder.bind(appointment)
    }

    override fun getItemCount(): Int {
        return appointments.size
    }

    fun submitList(appointments: List<Appointments>) {
        this.appointments = appointments
        notifyDataSetChanged()
    }

    inner class AppointmentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val doctorNameTextView: TextView = itemView.findViewById(R.id.doctorNameTextView)
        private val timeTextView: TextView = itemView.findViewById(R.id.timeTextView)
        private val dateTextView: TextView = itemView.findViewById(R.id.dateTextView)

        fun bind(appointment: Appointments) {
            doctorNameTextView.text = appointment.doctorName
            timeTextView.text = appointment.appointmentTime
            dateTextView.text = appointment.appointmentDate
        }
    }
}
