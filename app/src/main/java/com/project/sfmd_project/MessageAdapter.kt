package com.project.sfmd_project

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.MediaController
import android.widget.TextView
import android.widget.VideoView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide


class MessageAdapter(
    private val messages: List<Message>,
    private val onMessageClickListener: OnMessageClickListener
) : RecyclerView.Adapter<MessageAdapter.ViewHolder>() {

    interface OnMessageClickListener {
        fun onMessageClick(position: Int)
        fun onMessageLongClick(position: Int)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val messageTextView: TextView = itemView.findViewById(R.id.messageTextView)
        private val audioIcon: ImageView = itemView.findViewById(R.id.audioIcon)
        private val imageView: ImageView = itemView.findViewById(R.id.imageView)
        private val videoView: VideoView = itemView.findViewById(R.id.videoView)

        fun bind(message: Message) {
            messageTextView.text = message.messageText

            if (message.imageUrl != null) {
                // Handle image messages
                audioIcon.visibility = View.GONE
                imageView.visibility = View.VISIBLE
                videoView.visibility = View.GONE
                Glide.with(itemView.context)
                    .load(message.imageUrl)
                    .into(imageView)
            } else if (message.fileUrl != null) {
                // Handle file messages
                audioIcon.visibility = View.GONE
                imageView.visibility = View.GONE
                videoView.visibility = View.GONE
                val fileName = getFileNameFromUrl(message.fileUrl)
                messageTextView.text = fileName
                messageTextView.setOnClickListener {
                    onMessageClickListener.onMessageClick(adapterPosition)
                }
            } else if (message.audioUrl != null) {
                // Handle audio messages
                audioIcon.visibility = View.VISIBLE
                imageView.visibility = View.GONE
                videoView.visibility = View.GONE
                audioIcon.setOnClickListener {
                    onMessageClickListener.onMessageClick(adapterPosition)
                }
            } else if (message.videoUrl != null) {
                // Handle video messages
                audioIcon.visibility = View.GONE
                imageView.visibility = View.GONE
                videoView.visibility = View.VISIBLE

                val mediaController = MediaController(itemView.context)
                mediaController.setAnchorView(videoView)
                videoView.setMediaController(mediaController)
                videoView.setVideoURI(Uri.parse(message.videoUrl))
                videoView.requestFocus()

                videoView.setOnPreparedListener {
                    // Start playback
                    it.start()
                }
                videoView.setOnCompletionListener {
                    // Video playback completed
                }
                videoView.setOnErrorListener { _, what, extra ->
                    // Handle video error
                    false
                }
            } else {
                // Hide all views if no content
                audioIcon.visibility = View.GONE
                imageView.visibility = View.GONE
                videoView.visibility = View.GONE
            }

            itemView.setOnClickListener {
                onMessageClickListener.onMessageClick(adapterPosition)
            }

            itemView.setOnLongClickListener {
                onMessageClickListener.onMessageLongClick(adapterPosition)
                return@setOnLongClickListener true
            }
        }

        private fun getFileNameFromUrl(fileUrl: String): String {
            val uri = Uri.parse(fileUrl)
            return uri.lastPathSegment ?: "File"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_message, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(messages[position])
    }

    override fun getItemCount() = messages.size
}
