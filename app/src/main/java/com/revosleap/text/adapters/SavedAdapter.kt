package com.revosleap.text.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.revosleap.text.R
import com.revosleap.text.models.SentMessages

class SavedAdapter(messageList:MutableList<SentMessages>): RecyclerView.Adapter<SavedAdapter.SavedVH>() {
    private val messages= messageList

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): SavedVH {
        return SavedVH(LayoutInflater.from(p0.context).inflate(R.layout.saved_messages,p0,false))
    }

    override fun getItemCount(): Int {
     return messages.size
    }

    override fun onBindViewHolder(p0: SavedVH, p1: Int) {
      p0.bind(messages[p1])
    }

    class SavedVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
      fun bind(sentMessages: SentMessages){
       val message= itemView.findViewById<TextView>(R.id.textViewMsg)
          val msgCounter= itemView.findViewById<TextView>(R.id.textViewMsgCount)
          message.text= sentMessages.message
          msgCounter.text= sentMessages.contacts.size.toString()
      }
    }
}