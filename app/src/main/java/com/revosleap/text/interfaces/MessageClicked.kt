package com.revosleap.text.interfaces

import com.revosleap.text.models.SentMessages

interface MessageClicked {
    fun onMessageClicked(sentMessages: SentMessages, index: Int)
    fun onRecipientClicked(sentMessages: SentMessages)
}