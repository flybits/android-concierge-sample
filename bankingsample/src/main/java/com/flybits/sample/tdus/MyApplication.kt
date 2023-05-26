package com.flybits.sample.tdus

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import com.flybits.commons.library.logging.VerbosityLevel
import com.flybits.concierge.Concierge
import com.flybits.concierge.FlybitsConciergeConfiguration

const val CHANNEL_ID = "com.flybits.concierge.channel.id"

class MyApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()

        val config = FlybitsConciergeConfiguration.Builder(applicationContext)
            .setGatewayUrl("https://api.demo.flybits.com")
            .setProjectId("696DF9A2-8B20-4D46-9908-A6615C27B656")
            .setWebService("https://fb-mobile-apps.s3.amazonaws.com/public")
            .build()

        Concierge.setLoggingVerbosity(VerbosityLevel.ALL)

        //Call configure on Concierge
        Concierge.configure(config, emptyList(), applicationContext)
    }

    private fun createNotificationChannel(
        channelId: String = CHANNEL_ID,
        channelName: String = "CHANNEL_NAME",
        channelDesc: String = "CHANNEL_DESC"
    ) {
        lateinit var channel: NotificationChannel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            channel =
                NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH)
            channel.description = channelDesc
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }
}