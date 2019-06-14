package com.flybits.conciergesample

import com.flybits.android.push.models.Push
import com.flybits.concierge.services.ConciergeMessagingService
import com.google.firebase.messaging.RemoteMessage

const val CHANNEL_ID = "com.flybits.concierge.channel.id"

class AppMessagingService: ConciergeMessagingService() {
    override fun getNotificationIconRes(): Int {
        return R.drawable.ic_flybits_logo_opt_in
    }

    override fun onNonFlybitsPushReceived(remoteMessage: RemoteMessage) {
        //handle non flybits push here
    }

    override fun getNotificationChannelId(push: Push) = CHANNEL_ID
}