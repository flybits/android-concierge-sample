package com.flybits.conciergesample.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import com.flybits.android.push.provider.FcmV2DeliveryProvider
import com.flybits.commons.library.logging.VerbosityLevel
import com.flybits.concierge.Concierge
import com.flybits.concierge.FlybitsConciergeConfiguration
import com.flybits.conciergesample.CHANNEL_ID
import com.flybits.context.ContextManager
import com.flybits.context.ReservedContextPlugin

@JvmSynthetic
internal fun configureConcierge(context: Context){
    val config = FlybitsConciergeConfiguration.Builder(context)
        .setGatewayUrl("https://api.demo.flybits.com")
        .setProjectId("2CE41988-B1D3-4116-98DD-42FFB8754384")
        .setWebService("https://static-files-concierge.demo.flybits.com/latest")
        .setPushProvider(FcmV2DeliveryProvider)
        .setUploadPushtokenOnConnect(true)
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
        // Adding the instance of DeepLinkHandler to array list in order to pass it
        // to configure() API.
        deepLinkHandlers = arrayListOf(CustomScreenDeepLinkHandler()),
        context = context
    )
}

internal fun createNotificationChannel(
    context: Context,
    channelId: String = CHANNEL_ID,
    channelName: String = "CHANNEL_NAME",
    channelDesc: String = "CHANNEL_DESC"
) {
    lateinit var channel: NotificationChannel
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        channel =
            NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH)
        channel.description = channelDesc
        val notificationManager = context.getSystemService(NotificationManager::class.java)
        notificationManager.createNotificationChannel(channel)
    }
}