package com.project.sfmd_project

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class Record_Adapter (private val record_list : ArrayList<Record_data>, private val listener: Record_Adapter.OnItemClickListener):
    RecyclerView.Adapter<Record_Adapter.MyViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): Record_Adapter.MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.records_layout, parent, false)
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return record_list.size
    }

    override fun onBindViewHolder(holder: Record_Adapter.MyViewHolder, position: Int) {
        val currentItem = record_list[position]
        holder.records.setText(currentItem.rec)

    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {

        val records: TextView = itemView.findViewById(R.id.record)

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
