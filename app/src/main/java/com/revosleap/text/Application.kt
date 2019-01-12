package com.revosleap.text

import android.app.Application
import com.revosleap.text.models.MyObjectBox
import io.objectbox.BoxStore

class Application: Application() {
    override fun onCreate() {
        super.onCreate()
        boxStore= MyObjectBox.builder().androidContext(this).build()
    }
    companion object {
        var boxStore:BoxStore?=null
        private set
    }
}