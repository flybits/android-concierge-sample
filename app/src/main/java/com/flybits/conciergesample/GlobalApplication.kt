package com.flybits.conciergesample

import android.annotation.SuppressLint
import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import com.facebook.stetho.Stetho
import com.flybits.commons.library.logging.VerbosityLevel
import com.flybits.concierge.Concierge
import com.flybits.concierge.FlybitsConciergeConfiguration
import com.flybits.context.ContextManager
import com.flybits.context.ReservedContextPlugin

class GlobalApplication : Application() {

    @SuppressLint("ResourceType")
    override fun onCreate() {
        super.onCreate()
        Stetho.initializeWithDefaults(this)
        createNotificationChannel()

        val config = FlybitsConciergeConfiguration.Builder(applicationContext)
            .setGateWayUrl("https://api.demo.flybits.com")
            .setProjectId("2CE41988-B1D3-4116-98DD-42FFB8754384")
            .setWebService("localhost:3000")
            .build()


        Concierge.setLoggingVerbosity(VerbosityLevel.ALL)

        //Call configure on Concierge
        Concierge.configure(config, emptyList(),applicationContext)
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