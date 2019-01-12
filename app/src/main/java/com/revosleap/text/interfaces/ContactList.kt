package com.revosleap.text.interfaces

import com.revosleap.text.models.PendingModel

interface ContactList {
    fun contactList(contactList: MutableList<PendingModel>)
}