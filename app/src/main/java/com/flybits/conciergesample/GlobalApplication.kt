package com.flybits.conciergesample

import android.annotation.SuppressLint
import android.app.Application
import com.facebook.stetho.Stetho
import com.flybits.conciergesample.utils.configureConcierge
import com.flybits.conciergesample.utils.createNotificationChannel

class GlobalApplication : Application() {

    @SuppressLint("ResourceType")
    override fun onCreate() {
        super.onCreate()
        Stetho.initializeWithDefaults(this)

        createNotificationChannel(this)
        // Alternatively call Java createNotificationChannel()
//        Config.createNotificationChannel(this, CHANNEL_ID, null, null)

        configureConcierge(this)
        // Alternatively call Java configureConcierge()
//        Config.configureConcierge(this)
    }
}