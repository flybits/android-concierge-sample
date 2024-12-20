package com.flybits.conciergesample

import com.flybits.android.huaweiPush.HuaweiPushService
import com.flybits.android.push.models.newPush.DisplayablePush
import com.flybits.android.push.models.newPush.Push
import com.huawei.hms.push.RemoteMessage

/***
 * A class that realizes the HuaweiPushService
 */
class HuaweiMessage() : HuaweiPushService() {
    override fun onNonFlybitsPushReceived(remoteMessage: RemoteMessage) {
    }

    override fun getNotificationIconRes(push: DisplayablePush): Int = android.R.drawable.ic_btn_speak_now

    override fun getNotificationChannelId(push: Push) = CHANNEL_ID
}