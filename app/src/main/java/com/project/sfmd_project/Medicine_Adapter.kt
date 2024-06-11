package com.project.sfmd_project

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class Medicine_Adapter (private val medicine_list : ArrayList<Medicine_Data>, private val listener: Medicine_Adapter.OnItemClickListener):
    RecyclerView.Adapter<Medicine_Adapter.MyViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): Medicine_Adapter.MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.disease_layout, parent, false)
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return medicine_list.size
    }

    override fun onBindViewHolder(holder: Medicine_Adapter.MyViewHolder, position: Int) {
        val currentItem = medicine_list[position]
        holder.disease.setText(currentItem.med)

    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {

        val disease: TextView = itemView.findViewById(R.id.disease)

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
