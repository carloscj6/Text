package com.revosleap.text.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.revosleap.text.R
import com.revosleap.text.models.Contacts

class RecipientAdapter : RecyclerView.Adapter<RecipientAdapter.RecipientViewHolder>() {
    private var recipients = mutableListOf<Contacts>()

    override fun onCreateViewHolder(viewGroup: ViewGroup, p1: Int): RecipientViewHolder {
        return RecipientViewHolder(LayoutInflater.from(viewGroup.context).inflate(R.layout.recipients_view, viewGroup, false))
    }

    override fun getItemCount(): Int {
        return recipients.size
    }

    override fun onBindViewHolder(p0: RecipientViewHolder, p1: Int) {
        p0.bind(recipients[p1])
    }

    fun adItems(contacts:MutableList<Contacts>){
        recipients.clear()
        recipients.addAll(contacts)
        notifyDataSetChanged()
    }

    class RecipientViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val name = itemView.findViewById<TextView>(R.id.textViewRecipientName)
        private val number = itemView.findViewById<TextView>(R.id.textViewRecipientNumber)
        private val initial= itemView.findViewById<TextView>(R.id.textViewFL)

        fun bind(contacts: Contacts) {
            name.text = contacts.contactName
            number.text = contacts.phoneNumber
            initial.text= contacts.contactName?.get(0).toString()
        }
    }
}