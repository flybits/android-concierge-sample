package com.flybits.conciergesample

import android.annotation.SuppressLint
import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import com.facebook.stetho.Stetho
import com.flybits.android.huaweiPush.provider.HuaweiDeliveryProvider
import com.flybits.android.push.models.newPush.HUAWEI_PUSH_PROVIDER
import com.flybits.android.push.provider.FcmV2DeliveryProvider
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
            .setGatewayUrl("https://api.demo.flybits.com")
            .setProjectId("2CE41988-B1D3-4116-98DD-42FFB8754384")
            .setPushProvider(FcmV2DeliveryProvider)
            // TO use Huawei Push Kit as a provider set push provider to HuaweiDeliveryProvider instead.
//            .setPushProvider(HuaweiDeliveryProvider)
            .build()


        Concierge.setLoggingVerbosity(VerbosityLevel.ALL)

        // Call configure on Concierge
        Concierge.configure(
            config,
            arrayListOf(
                ContextManager.PluginType.ReservedPlugin(ReservedContextPlugin.HUAWEI_LOCATION),
                ContextManager.PluginType.ReservedPlugin(ReservedContextPlugin.HUAWEI_GEOFENCE_LOCATION)
                // To add or use plugins designed for Google Location and Geofence use the below code:
//                ContextManager.PluginType.ReservedPlugin(ReservedContextPlugin.LOCATION),
//                ContextManager.PluginType.ReservedPlugin(ReservedContextPlugin.GEOFENCE_LOCATION)
            ),
            applicationContext
        )
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