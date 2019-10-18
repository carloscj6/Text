package com.revosleap.text.dialogs

import android.os.Bundle
import android.support.design.widget.BottomSheetDialogFragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.revosleap.text.R
import com.revosleap.text.adapters.RecipientAdapter
import com.revosleap.text.models.SentMessages
import kotlinx.android.synthetic.main.sent_recipients_dialog.*
import java.text.SimpleDateFormat
import java.util.*


class SentRecipients : BottomSheetDialogFragment() {
    private var sentMessages: SentMessages? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.sent_recipients_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setList()
        textViewMessage?.text = sentMessages?.message
        imageButtonDismiss?.setOnClickListener {
            dismiss()
        }
        enterText()
    }

    private fun enterText() {
        val sdf = SimpleDateFormat("dd MMMM yyyy HH:mm", Locale.getDefault())
        textViewDateWritten.text = sdf.format(0)
        textViewDateSent.text = sdf.format(sentMessages?.time)
        textViewRecipients.text = sentMessages?.contacts?.size.toString()
    }


    private fun setList() {
        val adapter = RecipientAdapter()
        val manager = LinearLayoutManager(context)
        recyclerViewRecipients.adapter = adapter
        recyclerViewRecipients.layoutManager = manager
        recyclerViewRecipients.hasFixedSize()
        adapter.adItems(sentMessages?.contacts!!)
    }


    companion object {
        fun getInstance(message: SentMessages): SentRecipients {
            val sentRecipients = SentRecipients()
            sentRecipients.sentMessages = message
            return sentRecipients
        }
    }
}