package com.project.sfmd_project

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import de.hdodenhof.circleimageview.CircleImageView

class SearchHigh_Adapter(private val profile_list : ArrayList<Profiledata2>, private val listener:OnItemClickListener):
    RecyclerView.Adapter<SearchHigh_Adapter.MyViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.doctor_high_layout, parent, false)

        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return profile_list.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = profile_list[position]
       // holder.Dp.setImageResource(currentItem.dp)
        holder.Name.setText(currentItem.name)
        holder.Type.setText(currentItem.type)
        holder.Rating.rating = currentItem.ratingBar
        holder.Rating.setOnRatingBarChangeListener { ratingBar, rating, fromUser ->
            currentItem.ratingBar = rating
        }
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {

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
        profile_list.clear()
        profile_list.addAll(newList)
        notifyDataSetChanged()
    }


}

