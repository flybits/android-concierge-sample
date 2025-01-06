package com.flybits.conciergesample

import com.flybits.android.push.models.newPush.DisplayablePush
import com.flybits.concierge.services.ConciergeMessagingService
import com.google.firebase.messaging.RemoteMessage

const val CHANNEL_ID = "com.flybits.concierge.channel.id"

class AppMessagingService : ConciergeMessagingService(true) {
    override fun getNotificationChannelId(push: com.flybits.android.push.models.newPush.Push): String {
        return CHANNEL_ID
    }

    override fun getNotificationIconRes(push: DisplayablePush): Int {
        return android.R.drawable.ic_btn_speak_now
    }


    override fun onNonFlybitsPushReceived(remoteMessage: RemoteMessage) {
        //handle non flybits push here
    }

    override fun onNewToken(recentToken: String) {
        super.onNewToken(recentToken)
    }
}