package com.project.sfmd_project

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class Disease_Adapter (private val disease_list : ArrayList<Disease_Data>, private val listener: Disease_Adapter.OnItemClickListener):
    RecyclerView.Adapter<Disease_Adapter.MyViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): Disease_Adapter.MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.disease_layout, parent, false)
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return disease_list.size
    }

    override fun onBindViewHolder(holder: Disease_Adapter.MyViewHolder, position: Int) {
        val currentItem = disease_list[position]
        holder.disease.setText(currentItem.dis)

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
