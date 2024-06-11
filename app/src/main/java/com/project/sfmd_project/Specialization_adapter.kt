package com.project.sfmd_project

import android.graphics.Typeface
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView

class Specialization_adapter(
    private val spec_list: ArrayList<Specializations_data>,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<Specialization_adapter.MyViewHolder>() {

    private var selectedItem = -1

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        val specTextView: TextView = itemView.findViewById(R.id.specialisations)
        val descTextView: TextView = itemView.findViewById(R.id.description)
        val symptomsTextView: TextView = itemView.findViewById(R.id.symptoms)
        val itemLayout: ConstraintLayout = itemView.findViewById(R.id.item_layout)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                listener.onItemClick(position)
                // Toggle item expansion
                selectedItem = if (selectedItem == position) -1 else position
                notifyDataSetChanged()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.specialisations_layout, parent, false)
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return spec_list.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = spec_list[position]
        holder.specTextView.text = currentItem.spec

        // Change text appearance based on item state
        if (selectedItem == position) {
            holder.specTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24f) // Larger text size
            holder.specTextView.setTypeface(null, Typeface.BOLD) // Bold text
            holder.itemLayout.setBackgroundResource(R.drawable.selected_item_background) // Change background color
        } else {
            holder.specTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20f) // Normal text size
            holder.specTextView.setTypeface(null, Typeface.NORMAL) // Normal text
            holder.itemLayout.setBackgroundResource(R.drawable.item_background) // Default background color
        }

        // Show/hide description and symptoms based on item state
        if (selectedItem == position) {
            holder.descTextView.visibility = View.VISIBLE
            holder.symptomsTextView.visibility = View.VISIBLE
            // Set description and symptoms here
            holder.descTextView.text = currentItem.description
            holder.symptomsTextView.text = currentItem.symptoms
        } else {
            holder.descTextView.visibility = View.GONE
            holder.symptomsTextView.visibility = View.GONE
        }
    }
}
