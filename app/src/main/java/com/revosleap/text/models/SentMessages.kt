package com.revosleap.text.models

import io.objectbox.annotation.Backlink
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.relation.ToOne

@Entity
class SentMessages {
    @Id var id: Long=0
    var message:String?= null
    var state:String?=null
    var time: Long=0
    @Backlink(to="messages")
    lateinit var contacts:MutableList<Contacts>
}
@Entity
class Contacts{
   @Id var id: Long=0
    var phoneNumber:String?=null
    var contactName:String?=null
    lateinit var messages: ToOne<SentMessages>
}