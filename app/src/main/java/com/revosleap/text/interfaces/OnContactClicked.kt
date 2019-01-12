package com.revosleap.text.interfaces

import com.revosleap.text.models.PendingModel

interface OnContactClicked {
    fun removeContact(pendingModel: PendingModel,position:Int)
}