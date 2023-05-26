package com.flybits.sample.tdus

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.flybits.android.push.services.EXTRA_PUSH_NOTIFICATION
import com.flybits.concierge.Concierge
import com.flybits.concierge.enums.ConciergeOptions

private const val TIMEOUT = 5000L

class SplashScreenActivity : AppCompatActivity() {

    private var nextActivityIntent: Intent? = null
    private var isTimeoutComplete = false
    private val uiDelayHandler = Handler()

    private var isClosed = false
    private val runnable = {

        //Check if FlybitsManager.isConnected completed. You know it completed if nextActivityIntent
        //is not null
        nextActivityIntent?.let {
            startNextActivity()
        }

        //It seems like FlybitsManager.isConnected completed is still running so when it completes
        //it should start the next activity
        isTimeoutComplete = true
    }

    @Override
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        nextActivityIntent = Intent(applicationContext, MainActivity::class.java)
        uiDelayHandler.postDelayed(runnable, TIMEOUT)
        processPush(intent)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        processPush(intent)
    }

    private fun processPush(intent : Intent?){
        intent?.let {
            if (it.hasExtra(EXTRA_PUSH_NOTIFICATION)) {
                val push = Concierge.handlePush(this, it)
                if (push != null) {
                    isClosed = true
                    Concierge.deepLink(push, this, null,
                        conciergeOptions = arrayListOf(
                            ConciergeOptions
                                .DisplayNavigation("It worked"),
                            ConciergeOptions.Settings,
                            ConciergeOptions.Notifications))?.let {} ?: run {}
                }
            }
        }
    }

    @Override
    override fun onDestroy() {
        super.onDestroy()
        uiDelayHandler.removeCallbacks(runnable)
    }

    private fun startNextActivity() {
        if (!isClosed) {
            startActivity(nextActivityIntent)
            finish()
        }
    }
}