package com.revosleap.text.utils

import android.content.Context
import android.widget.Toast

object Utils {
    fun toast(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    const val DRAFT_STATE = "Draft"
    const val SENT_STATE = "Sent"
    const val EDIT_STATE= "Editing"
}