package com.flybits.conciergesample

import android.annotation.SuppressLint
import android.app.Application
import com.flybits.concierge.FlybitsConcierge

class GlobalApplication: Application() {

    @SuppressLint("ResourceType")
    override fun onCreate() {
        super.onCreate()
        val concierge = FlybitsConcierge.with(this)
        //This needs to happen prior to using API, doesn't need to be in Application.onCreate()
        concierge.initialize(R.xml.concierge)
        concierge.enableDebugMode()

    }
}