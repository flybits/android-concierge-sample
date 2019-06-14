package com.flybits.conciergesample

import android.annotation.SuppressLint
import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import com.flybits.concierge.FlybitsConcierge
import com.flybits.concierge.smartrewards.viewproviders.BenefitsViewProvider
import com.flybits.concierge.smartrewards.viewproviders.ConfirmationViewProvider
import com.flybits.concierge.smartrewards.viewproviders.OffersViewProvider
import com.flybits.concierge.smartrewards.viewproviders.OptInViewProvider

class GlobalApplication: Application() {

    @SuppressLint("ResourceType")
    override fun onCreate() {
        super.onCreate()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create the NotificationChannel
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val mChannel = NotificationChannel(CHANNEL_ID, "Smart Rewards", importance)
            mChannel.description = "Notifications for smart rewards"
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(mChannel)
        }

        val concierge = FlybitsConcierge.with(this)

        //This needs to happen prior to using API, doesn't need to be in Application.onCreate()
        concierge.initialize(R.xml.concierge)
        concierge.enableDebugMode()

        //Add view providers for content templates you want displayed
        concierge.registerFlybitsViewProvider(OptInViewProvider(this))
        concierge.registerFlybitsViewProvider(OffersViewProvider(this))
        concierge.registerFlybitsViewProvider(BenefitsViewProvider(this))
        concierge.registerFlybitsViewProvider(ConfirmationViewProvider(this))
    }
}