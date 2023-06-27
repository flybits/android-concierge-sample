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
            .setProjectId("08A0D8CA-2173-4756-B70C-96A0984D8C68")
            .setGatewayUrl("https://api.demo.flybits.com")
            .setWebService("https://static-files-concierge.development.flybits.com/latest")
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