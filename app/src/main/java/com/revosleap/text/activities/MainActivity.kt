package com.revosleap.text.activities

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.ContactsContract
import android.support.design.widget.BottomSheetBehavior
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.telephony.SmsManager
import android.view.View
import android.widget.Toast
import com.revosleap.text.Application
import com.revosleap.text.R
import com.revosleap.text.adapters.MessagesAdapter
import com.revosleap.text.adapters.SendingAdapter
import com.revosleap.text.interfaces.ContactList
import com.revosleap.text.interfaces.MessageClicked
import com.revosleap.text.interfaces.OnContactClicked
import com.revosleap.text.models.Contacts
import com.revosleap.text.models.PendingModel
import com.revosleap.text.models.SentMessages
import com.revosleap.text.utils.Utils
import com.wafflecopter.multicontactpicker.ContactResult
import com.wafflecopter.multicontactpicker.LimitColumn
import com.wafflecopter.multicontactpicker.MultiContactPicker
import io.objectbox.Box
import io.objectbox.kotlin.boxFor
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity(), OnContactClicked, ContactList, MessageClicked {
    private val pendingList: MutableList<PendingModel> = arrayListOf()
    private var savedList: MutableList<SentMessages> = arrayListOf()
    private var pendingAdapter: SendingAdapter? = null
    private var messagesAdapter: MessagesAdapter? = null
    private var textMessage = ""
    private val box: Box<SentMessages> = Application.boxStore!!.boxFor()
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<View>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        checkPermissions()
        setGreetings()
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
        setBotttomSheet()
        pendingAdapter = SendingAdapter(pendingList, this, this)
        messagesAdapter = MessagesAdapter(this)
        compose.setOnClickListener {
            textMessage = message.text.toString()
            if (textMessage.isEmpty()) {
                textInputLayout.error = "Type a message"
            } else {
                chooseContacts()
            }
        }
        loadSavedMessages()

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CONTACT_PICKER_RESULT) {
            if (resultCode == Activity.RESULT_OK) {
                val contacts = MultiContactPicker.obtainResult(data)
                showSelectedContacts(contacts)
            } else Toast.makeText(this@MainActivity, "Cancelled", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSIONS_REQUEST -> {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this@MainActivity, "Permissions Must be Allowed", Toast.LENGTH_LONG).show()
                    finish()
                }
            }
        }

    }

    private fun checkPermissions() {
        val permissionCheck = ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.READ_CONTACTS)
        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            val c = application.contentResolver.query(ContactsContract.Profile.CONTENT_URI, null, null, null, null)
            c!!.moveToFirst()
            toolbar.subtitle = c.getString(c.getColumnIndex("display_name"))
            c.close()
        } else {
            ActivityCompat.requestPermissions(this@MainActivity,
                    arrayOf(Manifest.permission.READ_CONTACTS, Manifest.permission.SEND_SMS),
                    PERMISSIONS_REQUEST)
        }
    }

    override fun removeContact(pendingModel: PendingModel, position: Int) {
        pendingAdapter!!.removeItem(position)
    }

    override fun contactList(contactList: MutableList<PendingModel>) {
        val smsManager = SmsManager.getDefault()
        val sentMessages = SentMessages()
        sentMessages.message = textMessage
        sentMessages.time = System.currentTimeMillis()
        buttonSend.setOnClickListener {
            contactList.forEach {
                val contacts = Contacts()
                contacts.contactName = it.name
                contacts.phoneNumber = it.phoneNo
                sentMessages.contacts.add(contacts)
                //  smsManager.sendTextMessage(it.phoneNo,null,textMessage,null,null)
            }
            box.put(sentMessages)
        }
    }

    override fun onMessageClicked(sentMessages: SentMessages, index: Int) {
        Utils.toast(this, sentMessages.message!!)
    }

    private fun chooseContacts() {
        MultiContactPicker.Builder(this@MainActivity)
                .setTitleText("Select Contacts")
                .limitToColumn(LimitColumn.PHONE)
                .setActivityAnimations(android.R.anim.fade_in, android.R.anim.fade_out,
                        android.R.anim.fade_in,
                        android.R.anim.fade_out)
                .showPickerForResult(CONTACT_PICKER_RESULT)

    }

    private fun setGreetings() {
        val hr = Calendar.getInstance()
        val currentHour = hr.get(Calendar.HOUR_OF_DAY)
        val greeting: String
        greeting = when {
            currentHour <= 11 -> "Good Morning"
            currentHour <= 15 -> "Good Afternoon"
            currentHour <= 19 -> "Good Evening"
            else -> "Good Evening"
        }
        toolbar.title = greeting
    }

    private fun showSelectedContacts(contacts: ArrayList<ContactResult>) {
        contacts.forEach {
            val model = PendingModel()
            model.name = it.displayName
            model.phoneNo = it.phoneNumbers[0].number
            pendingList.add(model)
        }
        recyclerView.apply {
            pendingAdapter!!.hasStableIds()
            adapter = pendingAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
            hasFixedSize()
        }
        textViewHeader.text = getString(R.string.recipients)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        buttonSend.visibility = View.VISIBLE
        setClearButton(1)

    }

    private fun setClearButton(action: Int) {
        buttonClear.setOnClickListener {
            when (action) {
                1 -> {
                    pendingAdapter?.clearAll()
                }
            }
        }

    }

    private fun setBotttomSheet() {
        bottomSheetBehavior.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(p0: View, p1: Float) {
            }

            override fun onStateChanged(p0: View, p1: Int) {
                if (p1 == BottomSheetBehavior.STATE_EXPANDED) {
                    textViewHeader.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_down, 0, 0)
                } else textViewHeader.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_up, 0, 0)
            }

        })
    }

    private fun loadSavedMessages() {
        savedList = box.all
        if (savedList.size > 0) {
            recyclerViewMessages.visibility = View.VISIBLE
            include.visibility = View.GONE
        }
        messagesAdapter?.setMessages(savedList)
        recyclerViewMessages.apply {
            hasFixedSize()
            adapter = messagesAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
        }

    }

    companion object {
        private const val PERMISSIONS_REQUEST = 100
        private const val CONTACT_PICKER_RESULT = 999
    }


}
