package com.flybits.conciergesample

import com.flybits.android.push.models.newPush.DisplayablePush
import com.flybits.concierge.services.ConciergeMessagingService
import com.google.firebase.messaging.RemoteMessage

const val CHANNEL_ID = "com.flybits.conciergeDemo.channel.id"
/***
 * Please place the below server key to your Project
 * AAAA16zj5w8:APA91bF78GspcPNiqrJle_kPYhjfX0LzN5Md7nHGn_LHFL9-Hz7qGAm3ABypS9B46UuPFOCd1XejnV-jtigfPMRaAh8GjGrqpOoSqqALC0e81e3vR7gyuXAy2IxBh6JUlQZ00v5pVncL
 */
class AppMessagingService : ConciergeMessagingService(true) {
    override fun getNotificationChannelId(push: com.flybits.android.push.models.newPush.Push): String {
        return CHANNEL_ID
    }

    override fun getNotificationIconRes(push: DisplayablePush): Int {
        return R.drawable.ic_flybits_logo_opt_in
    }
    override fun onNonFlybitsPushReceived(remoteMessage: RemoteMessage) {
        //handle non flybits push here
    }

}