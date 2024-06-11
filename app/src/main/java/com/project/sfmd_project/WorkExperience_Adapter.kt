package com.project.sfmd_project

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class WorkExperience_Adapter (private val work_list : ArrayList<WorkExperience_Data>, private val listener: WorkExperience_Adapter.OnItemClickListener):
    RecyclerView.Adapter<WorkExperience_Adapter.MyViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): WorkExperience_Adapter.MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.specialisations_layout, parent, false)
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return work_list.size
    }

    override fun onBindViewHolder(holder: WorkExperience_Adapter.MyViewHolder, position: Int) {
        val currentItem = work_list[position]
        holder.Spec.setText(currentItem.work)

    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {

        val Spec: TextView = itemView.findViewById(R.id.specialisations)

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
