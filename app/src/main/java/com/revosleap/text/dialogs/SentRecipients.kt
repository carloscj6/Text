package com.revosleap.text.dialogs

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.design.widget.BottomSheetDialogFragment
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



class SentRecipients : BottomSheetDialogFragment() {
    private var sentMessages: SentMessages? = null



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.sent_recipients_dialog,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
        fun getInstance(message: SentMessages): SentRecipients {
            val sentRecipients = SentRecipients()
            sentRecipients.sentMessages = message
            return sentRecipients
        }
    }
}