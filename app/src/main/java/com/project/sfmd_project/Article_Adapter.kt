package com.project.sfmd_project

// ArticleAdapter.kt
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.project.sfmd_project.R

class Article_Adapter(private val articleList: ArrayList<Article_Data>, private val listener: OnItemClickListener) :
    RecyclerView.Adapter<Article_Adapter.ArticleViewHolder>() {


    fun updateList(newList: List<Article_Data>) {
        articleList.clear()
        articleList.addAll(newList)
        notifyDataSetChanged()
    }

    fun getArticle(position: Int): Article_Data {
        return articleList[position]
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.health_articles_layout, parent, false)
        return ArticleViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val currentItem = articleList[position]
        holder.articleTitle.text = currentItem.title
        holder.articleDescription.text = currentItem.description
        Glide.with(holder.itemView.context)
            .load(currentItem.imageUrl)
            .placeholder(R.drawable.doctor) // Placeholder image while loading
            .error(R.drawable.doctor) // Error image if loading fails
            .into(holder.articleImage)
    }


    override fun getItemCount(): Int = articleList.size

    inner class ArticleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val articleTitle: TextView = itemView.findViewById(R.id.articleTitle)
        val articleDescription: TextView = itemView.findViewById(R.id.articleDescription)
        val articleImage: ImageView = itemView.findViewById(R.id.articleImage)

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
