package com.revosleap.text.adapters

import android.support.v7.widget.RecyclerView
import android.telephony.SmsManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.revosleap.text.R
import com.revosleap.text.interfaces.ContactList
import com.revosleap.text.interfaces.OnContactClicked
import com.revosleap.text.models.ContactModel


class SendingAdapter(onContactClicked: OnContactClicked, sendList: ContactList)
    : RecyclerView.Adapter<SendingAdapter.PendingVh>() {
    private val contacts = mutableListOf<ContactModel>()
    private val contactClicked = onContactClicked
    private val sendingList = sendList
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): PendingVh {
        return PendingVh(LayoutInflater.from(p0.context).inflate(R.layout.pending_contacts, p0, false))
    }

    override fun getItemCount(): Int {
        return contacts.size
    }

    override fun onBindViewHolder(p0: PendingVh, p1: Int) {
        sendingList.contactList(contacts)
        p0.bind(contacts[p1])
        try {
            p0.removeBtn.setOnClickListener { contactClicked.removeContact(contacts[p1], p1) }

        } catch (e: IndexOutOfBoundsException) {
            e.printStackTrace()
        }

    }

    fun addNewItems(contactList: MutableList<ContactModel>){
        contacts.clear()
        contacts.addAll(contactList)
        notifyDataSetChanged()
    }

    fun removeItem(position: Int) {
        contacts.removeAt(position)
        notifyItemRemoved(position)
        notifyDataSetChanged()
    }

    fun clearAll() {
        contacts.clear()
        notifyDataSetChanged()
    }

    fun sendSMS(text: String) {
        val smsManager = SmsManager.getDefault()
        contacts.forEach {
            smsManager.sendTextMessage(it.phoneNo, null, text, null, null)
        }
    }

    class PendingVh(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val removeBtn = itemView.findViewById<Button>(R.id.buttonCancel)
        fun bind(contactModel: ContactModel) {
            val textViewNum = itemView.findViewById<TextView>(R.id.textViewPending)
            val numInfo = contactModel.name + "\n" + contactModel.phoneNo
            textViewNum.text = numInfo

        }
    }
}