package com.project.sfmd_project

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import de.hdodenhof.circleimageview.CircleImageView

class SearchDisplayAdapter(private val profileList: ArrayList<Profiledata2>, private val listener: OnItemClickListener) :
    RecyclerView.Adapter<SearchDisplayAdapter.MyViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_display_search, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = profileList[position]

        Glide.with(holder.itemView.context)
            .load(currentItem.dp)
            .error(R.drawable.patient) // Set the error drawable
            .placeholder(R.drawable.personicon)
            .into(holder.Dp)
        holder.Name.setText(currentItem.name)
        holder.Type.setText(currentItem.type)
        holder.Rating.rating = currentItem.ratingBar
        holder.Rating.setOnRatingBarChangeListener { ratingBar, rating, fromUser ->
            currentItem.ratingBar = rating
        }


    }

    override fun getItemCount(): Int {
        return profileList.size
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val Dp: CircleImageView = itemView.findViewById(R.id.dp)
        val Name: TextView = itemView.findViewById(R.id.name)
        val Type: TextView = itemView.findViewById(R.id.type)
        val Rating: RatingBar = itemView.findViewById(R.id.rating)




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

    fun updateData(newList: ArrayList<Profiledata2>) {
        profileList.clear()
        profileList.addAll(newList)
        notifyDataSetChanged()
    }
}