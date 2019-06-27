package com.flybits.conciergesample.activity

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.flybits.android.push.models.Push
import com.flybits.concierge.ConciergeConstants
import com.flybits.concierge.FlybitsConcierge
import com.flybits.concierge.enums.ShowMode
import com.flybits.conciergesample.CHANNEL_ID
import com.flybits.conciergesample.R

class MainActivity : AppCompatActivity() {

    private var flybitsConcierge: FlybitsConcierge? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val appBarConfig = AppBarConfiguration
            .Builder(R.id.tabHolderFragment, R.id.loginFragment)
            .build()
        setupActionBarWithNavController(findNavController(R.id.nav_host_fragment), appBarConfig)
        flybitsConcierge = FlybitsConcierge.with(applicationContext)
        intent?.let { handleIntent(it) }

        //Create Channel For notification : you can create it anywhere and return the Channel ID in AppMessaging Service
        createNotificationChannel(CHANNEL_ID, "CustomChannel_name", "hello")

    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        intent?.let { handleIntent(it) }
    }

    private fun handleIntent(intent: Intent) {
        if (intent.hasExtra(ConciergeConstants.PUSH_EXTRA)) {
            val extra = intent.getParcelableExtra<Push>(ConciergeConstants.PUSH_EXTRA)
            flybitsConcierge?.showPush(ShowMode.NEW_ACTIVITY, extra)
        }
    }

    private fun createNotificationChannel(channelId: String, channelName: String, channelDesc: String) {
        lateinit var channel: NotificationChannel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            channel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH)
            channel.description = channelDesc
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }

}