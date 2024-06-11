package com.project.sfmd_project

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class Appointment_adapter(private val appointments: ArrayList<Appointment_data>, private val listener: OnItemClickListener) :
    RecyclerView.Adapter<Appointment_adapter.MyViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.appointment_layout, parent, false)
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return appointments.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = appointments[position]
        holder.Name.text = currentItem.name
        holder.Time.text = currentItem.time
        holder.Date.text = currentItem.date
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {

        val Name: TextView = itemView.findViewById(R.id.name)
        val Time: TextView = itemView.findViewById(R.id.time)
        val Date: TextView = itemView.findViewById(R.id.date)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                listener.onItemClick(position)
            }
        }
    }
}
