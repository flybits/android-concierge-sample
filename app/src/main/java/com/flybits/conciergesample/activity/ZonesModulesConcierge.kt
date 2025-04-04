package com.flybits.conciergesample.activity

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.flybits.android.push.models.newPush.Push
import com.flybits.android.push.services.EXTRA_PUSH_NOTIFICATION
import com.flybits.commons.library.api.results.callbacks.BasicResultCallback
import com.flybits.commons.library.exceptions.FlybitsException
import com.flybits.commons.library.logging.VerbosityLevel
import com.flybits.concierge.Concierge
import com.flybits.concierge.ConciergeConstants
import com.flybits.concierge.ConciergeConstants.BROADCAST_CONCIERGE_EVENT
import com.flybits.concierge.enums.ConciergeOptions
import com.flybits.concierge.enums.ConciergeParams
import com.flybits.concierge.enums.Container
import com.flybits.conciergesample.R
import com.flybits.flybitscoreconcierge.idps.AnonymousConciergeIDP
import com.flybits.internal.db.CommonsDatabase
import kotlinx.android.synthetic.main.activity_zones_modules.call_handleActionableLink
import kotlinx.android.synthetic.main.activity_zones_modules.connect_disconnect
import kotlinx.android.synthetic.main.activity_zones_modules.optin_optout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ZonesModulesConcierge : AppCompatActivity() {
    private var broadcastReceiver: BroadcastReceiver? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_zones_modules)
        handlePushIntent(intent)

        Concierge.setLoggingVerbosity(VerbosityLevel.ALL)

        connect_disconnect.setOnClickListener {
            if (Concierge.isConnected(applicationContext)) {
                Concierge.disconnect(applicationContext, object : BasicResultCallback {
                    override fun onSuccess() {

                    }

                    override fun onException(exception: FlybitsException) {

                    }
                })
            } else {
                Concierge.connect(
                    context = applicationContext,
                    idp = AnonymousConciergeIDP(),
                    // Use the OpenID Connect (OIDC) IDP if required.
//                    idp = OpenIDConnectConciergeIDP("Your ID Token here"),
                    basicResultCallback = object : BasicResultCallback {
                        override fun onSuccess() {
                        }

                        override fun onException(exception: FlybitsException) {
                        }
                    }, fetchConfiguration = true
                )
            }
        }

        optin_optout.setOnClickListener {
            if (Concierge.isConnected(applicationContext)) {
                val commonsDatabase = CommonsDatabase.getDatabase(applicationContext)
                CoroutineScope(Dispatchers.IO).launch {
                    val user = commonsDatabase.userDao().activeUser
                    Handler(Looper.getMainLooper()).post {
                        if (user.isOptedIn) {
                            Concierge.optOut(applicationContext, object : BasicResultCallback {
                                override fun onSuccess() {
                                }

                                override fun onException(exception: FlybitsException) {
                                }
                            })
                        } else {
                            Concierge.optIn(applicationContext, object : BasicResultCallback {
                                override fun onSuccess() {
                                }

                                override fun onException(exception: FlybitsException) {
                                }
                            })
                        }
                    }
                }
            } else {
                // Concierge not connected.
            }
        }

        call_handleActionableLink.setOnClickListener {
            val transaction = supportFragmentManager.beginTransaction()
            Concierge.handleActionableLink(this, Uri.parse("app://?name=id1"))
                ?.let { fragment ->
                    transaction.replace(R.id.fragment_container, fragment)
                    transaction.commit()
                } ?: run {
                // Returned null
            }
        }

        // Configure zones to be displayed.
        val zonesConfig = Concierge.zonesConfiguration(this, listOf("default_zone"))

        // Register for the broadcast to receive Concierge events
        broadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                val actionableLink = intent?.extras?.getString(ConciergeConstants.ACTIONABLE_URL)

                val transaction = supportFragmentManager.beginTransaction()

                // Example of handling actionable link
                actionableLink?.let {
                    Concierge.handleActionableLink(
                        this@ZonesModulesConcierge,
                        Uri.parse(actionableLink),
                        conciergeOptions = arrayListOf(),
                        requestEvents = ConciergeParams.RequestEvents(),
                    )?.let { fragment ->
                        transaction.add(R.id.fragment_container, fragment)
                        transaction.addToBackStack(null)
                        transaction.commit()
                    } ?: run {
                        // Returned null
                    }
                }
            }
        }
        val intentFilter = IntentFilter()
        intentFilter.addAction(BROADCAST_CONCIERGE_EVENT)
        @SuppressLint("WrongConstant")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            ContextCompat.registerReceiver(
                this,
                broadcastReceiver,
                intentFilter,
                ContextCompat.RECEIVER_NOT_EXPORTED
            )
        } else {
            LocalBroadcastManager.getInstance(this)
                .registerReceiver(broadcastReceiver as BroadcastReceiver, intentFilter)
        }

        // Display Concierge
        val optionsList = arrayListOf(
            ConciergeOptions.DisplayNavigation("Concierge"),
            ConciergeOptions.Settings,
            ConciergeOptions.Notifications
        )
        if (savedInstanceState == null) {
            val transaction = supportFragmentManager.beginTransaction()
            Concierge.fragment(
                applicationContext,
                Container.Configured,
                arrayListOf(ConciergeParams.ZonesFilter(zonesConfig)),
                optionsList
            ).let {
                transaction.replace(R.id.fragment_container, it)
            }
            transaction.commit()
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        handlePushIntent(intent)
    }

    private fun handlePushIntent(intent: Intent?) {
        intent?.let {
            if (it.hasExtra(EXTRA_PUSH_NOTIFICATION)) {
                // Example of passing Intent that has Flybits Push for handling with Concierge.handlePush() API in the DemoAppCompatActivityActionBar
                @Suppress("DEPRECATION") val push: Push? =
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        it.getParcelableExtra(EXTRA_PUSH_NOTIFICATION, Push::class.java)
                    } else {
                        it.getParcelableExtra(EXTRA_PUSH_NOTIFICATION)
                    }
                val intentActivity = Intent(this, PushHandleActivity::class.java)
                intentActivity.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                intentActivity.putExtra(EXTRA_PUSH_NOTIFICATION, push)
                startActivity(intentActivity)
            } else {
                // Since Intent does not have Flybits Push the RemoteMessage should be extracted before passing it to the Concierge.handle() API.
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        broadcastReceiver?.let {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                this.unregisterReceiver(it)
            } else {
                LocalBroadcastManager.getInstance(this).unregisterReceiver(it)
            }
        }
    }
}