package com.revosleap.text.adapters

import android.annotation.SuppressLint
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.revosleap.text.R
import com.revosleap.text.interfaces.MessageClicked
import com.revosleap.text.models.SentMessages
import java.text.SimpleDateFormat
import java.util.*

class MessagesAdapter(private val messageClicked: MessageClicked) : RecyclerView.Adapter<MessagesAdapter.SavedVH>() {
    private var messages = mutableListOf<SentMessages>()

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): SavedVH {
        return SavedVH(LayoutInflater.from(p0.context).inflate(R.layout.saved_messages, p0, false))
    }

    override fun getItemCount(): Int {
        return messages.size
    }

    override fun onBindViewHolder(p0: SavedVH, p1: Int) {
        p0.bind(messages[p1])
    }

    fun setMessages(messageList: MutableList<SentMessages>) {
        messages.clear()
        messages = messageList
        notifyDataSetChanged()
    }


    inner class SavedVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val message = itemView.findViewById<TextView>(R.id.textViewMsg)
        val msgCounter = itemView.findViewById<TextView>(R.id.textViewMsgCount)
        val dateCounter = itemView.findViewById<TextView>(R.id.textViewTime)
        val status= itemView.findViewById<TextView>(R.id.textViewMsgStatus)
        @SuppressLint("SetTextI18n")
        fun bind(sentMessages: SentMessages) {
            status.text = "Sent"
            message.text = sentMessages.message
            msgCounter.text = "${sentMessages.contacts.size}"
            val today = SimpleDateFormat("dd", Locale.getDefault())
            val dateDifference = (today.format(System.currentTimeMillis()).toInt()).minus(
                    today.format(sentMessages.time).toInt()
            )
            val difference = System.currentTimeMillis() - sentMessages.time
            when {
                dateDifference == 0 && difference <= 604800000 -> {
                    val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
                    dateCounter.text = "Today ${sdf.format(sentMessages.time)}"
                }
                dateDifference == 1 && difference <= 604800000 -> {
                    val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
                    dateCounter.text = "Yesterday ${sdf.format(sentMessages.time)}"
                }

                dateDifference in 2..7 && difference <= 604800000 -> {
                    val sdf = SimpleDateFormat("EEE HH:mm", Locale.getDefault())
                    dateCounter.text = sdf.format(sentMessages.time)
                }
                else -> {
                    val sdf = SimpleDateFormat("dd MMMM yyyy HH:mm", Locale.getDefault())
                    dateCounter.text = sdf.format(sentMessages.time)
                }
            }

            itemView.setOnClickListener {
                messageClicked.onMessageClicked(sentMessages, adapterPosition)
            }
        }
    }
}