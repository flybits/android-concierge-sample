package com.flybits.conciergesample

import android.annotation.SuppressLint
import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import com.facebook.stetho.Stetho
import com.flybits.concierge.FlybitsConcierge
import com.flybits.concierge.smartrewards.viewproviders.BenefitsViewProvider
import com.flybits.concierge.smartrewards.viewproviders.ConfirmationViewProvider
import com.flybits.concierge.smartrewards.viewproviders.OffersViewProvider
import com.flybits.concierge.smartrewards.viewproviders.OptInViewProvider

class GlobalApplication : Application() {

    @SuppressLint("ResourceType")
    override fun onCreate() {
        super.onCreate()

        Stetho.initializeWithDefaults(this)

        createNotificationChannel()

        val concierge = FlybitsConcierge.with(this)

        //This needs to happen prior to using API, doesn't need to be in Application.onCreate()

/*      UNCOMMENT TO CONFIGURE CONCIERGE PROGRAMATICALLY
        val conciergeConfiguration = ConciergeConfiguration.Builder("PROJECT-ID-HERE")
            .setTimeToUploadContext(5)
            .setTermsAndServicesRequired("https://flybits.com/legal/terms-of-use")
            .setPrivacyPolicyUrl("https://flybits.com/legal/privacy-policy")
            .build()
        concierge.initialize(conciergeConfiguration)
*/
        concierge.initialize(R.xml.concierge)
        concierge.enableDebugMode()

        //Add view providers for content templates you want displayed
        concierge.registerFlybitsViewProvider(OptInViewProvider(this))
        concierge.registerFlybitsViewProvider(OffersViewProvider(this))
        concierge.registerFlybitsViewProvider(BenefitsViewProvider(this))
        concierge.registerFlybitsViewProvider(ConfirmationViewProvider(this))
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