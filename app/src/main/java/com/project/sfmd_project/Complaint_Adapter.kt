package com.project.sfmd_project

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class Complaint_Adapter(private val complaintList: ArrayList<Complaint_Data>, private val listener: OnItemClickListener) :
    RecyclerView.Adapter<Complaint_Adapter.ComplaintViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    inner class ComplaintViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val complaint: TextView = itemView.findViewById(R.id.complaint)

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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ComplaintViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.complaint_layout, parent, false)
        return ComplaintViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ComplaintViewHolder, position: Int) {
        val currentItem = complaintList[position]
        holder.complaint.text = currentItem.com
    }

    override fun getItemCount() = complaintList.size
}
