package com.revosleap.text.dialogs

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import com.revosleap.text.R
import com.revosleap.text.adapters.RecipientAdapter
import com.revosleap.text.models.SentMessages
import kotlinx.android.synthetic.main.sent_recipients_dialog.*



class SentRecipients(context: Context) : Dialog(context) {
    private var sentMessages: SentMessages? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        super.onCreate(savedInstanceState)
        setContentView(R.layout.sent_recipients_dialog)
        setList()
    }


//    override fun onResume() {
//        super.onResume()
//        val params = dialog.window!!.attributes
//        params.width = WindowManager.LayoutParams.WRAP_CONTENT
//        params.height = WindowManager.LayoutParams.WRAP_CONTENT
//        dialog.window!!.attributes = params as WindowManager.LayoutParams
//    }

    private fun setList(){
        val adapter = RecipientAdapter()
        val manager = LinearLayoutManager(context)
        recyclerViewRecipients.adapter = adapter
        recyclerViewRecipients.layoutManager = manager
        recyclerViewRecipients.hasFixedSize()
        adapter.adItems(sentMessages?.contacts!!)
    }



    companion object {
        fun getInstance(context: Context,message: SentMessages): SentRecipients {
            val sentRecipients = SentRecipients(context)
            sentRecipients.sentMessages = message
            return sentRecipients
        }
    }
}