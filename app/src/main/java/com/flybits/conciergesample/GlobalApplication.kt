package com.flybits.conciergesample

import android.annotation.SuppressLint
import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.graphics.Region
import android.os.Build
import com.facebook.stetho.Stetho
import com.flybits.commons.library.logging.VerbosityLevel
import com.flybits.concierge.ConciergeConfiguration
import com.flybits.concierge.FlybitsConcierge
import com.flybits.context.ContextManager
import com.flybits.context.ReservedContextPlugin
import com.flybits.context.plugins.FlybitsContextPlugin

class GlobalApplication : Application() {

    @SuppressLint("ResourceType")
    override fun onCreate() {
        super.onCreate()

        Stetho.initializeWithDefaults(this)

        createNotificationChannel()

        val concierge = FlybitsConcierge.with(this)

        //This needs to happen prior to using API, doesn't need to be in Application.onCreate()

//      UNCOMMENT TO CONFIGURE CONCIERGE PROGRAMATICALLY
        val conciergeConfiguration =
            ConciergeConfiguration.Builder("027EFBE7-0123-4820-8947-B8637CABA835")
                .setTimeToUploadContext(5)
                .setGatewayUrl(com.flybits.commons.library.api.Region.Demo.url)
                .setTermsAndServicesRequired("https://flybits.com/legal/terms-of-use")
                .setPrivacyPolicyUrl("https://flybits.com/legal/privacy-policy")
                .build()
        concierge.initialize(conciergeConfiguration)
//        concierge.initialize(R.xml.concierge)
        concierge.setLoggingVerbosity(VerbosityLevel.ALL)

        //Add view providers for content templates you want displayed

        if (concierge.isAuthenticated) {
            startPlugins()
        }

    }

    private fun createNotificationChannel(
        channelId: String = CHANNEL_ID,
        channelName: String = "Flybits Android Demo",
        channelDesc: String = "Flybits Android Demo"
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

    internal fun startPlugins() {
        // Start location plugin
        ContextManager.start(
            applicationContext,
            FlybitsContextPlugin.Builder(ReservedContextPlugin.LOCATION).build()
        )
        // Start activity plugin
        ContextManager.start(
            applicationContext,
            FlybitsContextPlugin.Builder(ReservedContextPlugin.ACTIVITY).build()
        )

        // Start WiFi plugin
        ContextManager.start(
            applicationContext,
            FlybitsContextPlugin.Builder(ReservedContextPlugin.NETWORK_CONNECTIVITY).build()
        )
        // Start battery plugin
        ContextManager.start(
            applicationContext,
            FlybitsContextPlugin.Builder(ReservedContextPlugin.BATTERY).build()
        )

    }
}