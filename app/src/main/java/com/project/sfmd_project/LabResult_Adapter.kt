package com.project.sfmd_project

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class LabResult_Adapter(private val labList: ArrayList<LabResult_Data>, private val listener: OnItemClickListener) :
    RecyclerView.Adapter<LabResult_Adapter.MyViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val test: TextView = itemView.findViewById(R.id.test)
        val date: TextView = itemView.findViewById(R.id.date)
        val time: TextView = itemView.findViewById(R.id.time)
        val filePath: TextView = itemView.findViewById(R.id.file_path)

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
            .inflate(R.layout.lab_result_item, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = labList[position]
        holder.test.text = currentItem.test
        holder.date.text = currentItem.date
        holder.time.text = currentItem.time
        holder.filePath.text = currentItem.filePath
    }

    override fun getItemCount() = labList.size
}
