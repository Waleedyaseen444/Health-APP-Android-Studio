package com.project.sfmd_project

// AppointmentsAdapter.kt
// AppointmentsAdapter.kt
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.project.sfmd_project.Appointments
import com.project.sfmd_project.R

class AppointmentsAdapter(private var appointments: List<Appointments>) : RecyclerView.Adapter<AppointmentsAdapter.ViewHolder>() {

    fun updateAppointments(appointments: List<Appointments>) {
        this.appointments = appointments
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.appointment_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val appointment = appointments[position]
        holder.bind(appointment)
    }

    override fun getItemCount(): Int {
        return appointments.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val doctorNameTextView: TextView = itemView.findViewById(R.id.name)
        private val timeTextView: TextView = itemView.findViewById(R.id.time)
        private val dateTextView: TextView = itemView.findViewById(R.id.date)

        fun bind(appointment: Appointments) {
            doctorNameTextView.text = appointment.doctorName
            timeTextView.text = appointment.appointmentTime
            dateTextView.text = appointment.appointmentDate
        }
    }
}
