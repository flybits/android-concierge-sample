package com.flybits.conciergesample

import android.annotation.SuppressLint
import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import com.flybits.concierge.FlybitsConcierge

class GlobalApplication : Application() {

    @SuppressLint("ResourceType")
    override fun onCreate() {
        super.onCreate()


        createNotificationChannel()

        val concierge = FlybitsConcierge.with(this)

        //This needs to happen prior to using API, doesn't need to be in Application.onCreate()
        concierge.initialize(R.xml.concierge)
        concierge.enableDebugMode()

        //Add view providers for content templates you want displayed
        /*concierge.registerFlybitsViewProvider(OptInViewProvider(this))
        concierge.registerFlybitsViewProvider(OffersViewProvider(this))
        concierge.registerFlybitsViewProvider(BenefitsViewProvider(this))
        concierge.registerFlybitsViewProvider(ConfirmationViewProvider(this))*/
    }

    private fun createNotificationChannel(channelId: String = CHANNEL_ID, channelName: String="CHANNEL_NAME", channelDesc: String="CHANNEL_DESC") {
        lateinit var channel: NotificationChannel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            channel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH)
            channel.description = channelDesc
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }
}