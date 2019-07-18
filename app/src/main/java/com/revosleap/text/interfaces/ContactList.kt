package com.revosleap.text.interfaces

import com.revosleap.text.models.ContactModel

interface ContactList {
    fun contactList(contactList: MutableList<ContactModel>)
}