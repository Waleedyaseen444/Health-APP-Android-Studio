package com.project.sfmd_project

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class Emergency_Adapter(private val emergencyList: ArrayList<Emergency_Data>, private val listener: OnItemClickListener) :
    RecyclerView.Adapter<Emergency_Adapter.MyViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val hospital: TextView = itemView.findViewById(R.id.hospital)
        val number: TextView = itemView.findViewById(R.id.number)

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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.emergency_layout, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = emergencyList[position]
        holder.hospital.text = currentItem.hospital
        holder.number.text = currentItem.number
    }

    override fun getItemCount(): Int {
        return emergencyList.size
    }
}
