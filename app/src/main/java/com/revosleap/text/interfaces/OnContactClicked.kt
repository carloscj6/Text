package com.revosleap.text.interfaces

import com.revosleap.text.models.ContactModel

interface OnContactClicked {
    fun removeContact(contactModel: ContactModel, position:Int)
}