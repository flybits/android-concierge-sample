package com.flybits.conciergesample

import com.flybits.concierge.services.ConciergeMessagingService
import com.google.firebase.messaging.RemoteMessage

class AppMessagingService: ConciergeMessagingService() {
    override fun getNotificationIconRes(): Int {
        return R.drawable.ic_flybits_logo_opt_in
    }

    override fun onNonFlybitsPushReceived(remoteMessage: RemoteMessage) {
        //handle non flybits push here
    }
}