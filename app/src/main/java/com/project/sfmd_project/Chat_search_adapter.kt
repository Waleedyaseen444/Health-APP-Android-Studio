package com.project.sfmd_project

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class Chat_search_adapter(private val doclist: ArrayList<Doctors_data>, private val listener: OnItemClickListener) :
    RecyclerView.Adapter<Chat_search_adapter.MyViewHolder>(), Filterable {

    private var filteredList: ArrayList<Doctors_data> = doclist

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.chat_search, parent, false)
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return filteredList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = filteredList[position]
        holder.nameTextView.text = currentItem.name
        holder.specializationTextView.text = currentItem.specialization

        // Load image using Picasso library
        if (currentItem.imageUrl.isNotEmpty()) {
            Picasso.get().load(currentItem.imageUrl).placeholder(R.drawable.doctor).into(holder.dpImageView)
        } else {
            holder.dpImageView.setImageResource(R.drawable.doctor)
        }
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val dpImageView: CircleImageView = itemView.findViewById(R.id.dp)
        val nameTextView: TextView = itemView.findViewById(R.id.name)
        val specializationTextView: TextView = itemView.findViewById(R.id.type)

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

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val filteredResults = ArrayList<Doctors_data>()

                if (constraint.isNullOrEmpty()) {
                    filteredResults.addAll(doclist)
                } else {
                    val filterPattern = constraint.toString().trim().toLowerCase()

                    for (item in doclist) {
                        if (item.name.toLowerCase().contains(filterPattern) ||
                            item.specialization.toLowerCase().contains(filterPattern)
                        ) {
                            filteredResults.add(item)
                        }
                    }
                }

                val results = FilterResults()
                results.values = filteredResults
                return results
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                filteredList.clear()
                filteredList.addAll(results?.values as ArrayList<Doctors_data>)
                notifyDataSetChanged()
            }
        }
    }
}
