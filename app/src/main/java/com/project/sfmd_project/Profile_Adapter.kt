package com.project.sfmd_project

import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target

class Profile_Adapter(
    private var profileList: ArrayList<Profile_Data>,
    private val listener: OnItemClickListener
) :
    RecyclerView.Adapter<Profile_Adapter.MyViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.doctor_layout, parent, false)

        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return profileList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = profileList[position]
        holder.name.text = currentItem.name
        holder.type.text = currentItem.type
        holder.rating.rating = currentItem.ratingBar

        // Load image using Glide
        Glide.with(holder.itemView.context)
            .load(currentItem.imageUrl) // Loading image from URL
            .apply(RequestOptions.circleCropTransform()) // Optional: for circular image
            .placeholder(R.drawable.doctor) // Placeholder image
            .error(R.drawable.doctor) // Error image
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    Log.e("GlideError", "Error loading image: $e")
                    return false // Return false to allow Glide to handle the failure
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    // Resource is ready, do any additional processing if needed
                    return false // Return false to allow Glide to handle the resource
                }
            })
            .into(holder.dp)
    }

    // Function to update the data in the adapter
    fun updateData(newList: ArrayList<Profile_Data>) {
        profileList = newList
        notifyDataSetChanged()
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {

        val dp: ImageView = itemView.findViewById(R.id.dp)
        val name: TextView = itemView.findViewById(R.id.name)
        val type: TextView = itemView.findViewById(R.id.type)
        val rating: RatingBar = itemView.findViewById(R.id.rating)

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
