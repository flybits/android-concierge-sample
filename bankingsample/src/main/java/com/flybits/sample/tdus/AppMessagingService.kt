package com.flybits.sample.tdus

import android.util.Log
import com.flybits.android.push.models.newPush.DisplayablePush
import com.flybits.concierge.services.ConciergeMessagingService
import com.google.firebase.messaging.RemoteMessage

class AppMessagingService : ConciergeMessagingService(true) {
    override fun getNotificationChannelId(push: com.flybits.android.push.models.newPush.Push): String {
        return CHANNEL_ID
    }

    override fun getNotificationIconRes(push: DisplayablePush): Int {
        return R.drawable.ic_flybits_logo_opt_in
    }


    override fun onNonFlybitsPushReceived(remoteMessage: RemoteMessage) {
         Log.d("Testing", "RemoteMessage Recieved: ")
    }

}