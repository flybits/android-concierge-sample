package com.flybits.conciergesample.activity

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.flybits.commons.library.api.results.callbacks.BasicResultCallback
import com.flybits.commons.library.exceptions.FlybitsException
import com.flybits.commons.library.logging.VerbosityLevel
import com.flybits.concierge.Concierge
import com.flybits.concierge.ConciergeConstants
import com.flybits.concierge.enums.ConciergeCustomerStatus
import com.flybits.concierge.enums.ConciergeOptions
import com.flybits.concierge.enums.ConciergeParams
import com.flybits.concierge.enums.Container
import com.flybits.conciergesample.R
import com.flybits.flybitscoreconcierge.idps.AnonymousConciergeIDP
import com.flybits.internal.db.CommonsDatabase
import kotlinx.android.synthetic.main.activity_zones_and_modules.call_deletePushToken
import kotlinx.android.synthetic.main.activity_zones_and_modules.call_handleActionable
import kotlinx.android.synthetic.main.activity_zones_and_modules.call_uploadPushToken
import kotlinx.android.synthetic.main.activity_zones_and_modules.connect_disconnect
import kotlinx.android.synthetic.main.activity_zones_and_modules.optin_optout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ZonesAndModules: AppCompatActivity() {
    private var broadcastReceiver: BroadcastReceiver? = null

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_zones_and_modules)

        Concierge.setLoggingVerbosity(VerbosityLevel.ALL)

        connect_disconnect.setOnClickListener {
            if (Concierge.isConnected(applicationContext)) {
                Concierge.disconnect(applicationContext, object : BasicResultCallback {
                    override fun onSuccess() {
                        Log.e("Connect", "Success")
                    }

                    override fun onException(exception: FlybitsException) {
                        Log.e("Connect", "Failure")
                    }
                })
            } else {
                Concierge.connect(
                    context = applicationContext,
                    idp = AnonymousConciergeIDP(),
                    basicResultCallback = object : BasicResultCallback {
                        override fun onSuccess() {
                            Log.d("Connect", "onSuccess called for the connect API.")
                        }

                        override fun onException(exception: FlybitsException) {
                            Log.e("Connect", "onException called for the connect API.")
                        }
                    }, fetchConfiguration = true)
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
                                    Log.e("Optout", "Success")
                                }

                                override fun onException(exception: FlybitsException) {
                                    Log.e("Optout", "Failure")
                                }
                            })
                        } else {
                            Concierge.optIn(applicationContext, object : BasicResultCallback {
                                override fun onSuccess() {
                                    Log.e("OptIn", "Success")
                                }

                                override fun onException(exception: FlybitsException) {
                                    Log.e("OptIn", "Failure")
                                }
                            })
                        }
                    }
                }
            } else {
                Log.e("Connect", "Not Connected")
            }
        }

        call_handleActionable.setOnClickListener {
            val transaction = supportFragmentManager.beginTransaction()
            Concierge.handleActionableLink(this, Uri.parse("details://?contentId=2C28452C-8A3D-4A9D-A07C-A3980C271DEF")/*, ConciergeTheme.FileName("custom_theming1.json")*/)
                ?.let { fragment ->
                    transaction.replace(R.id.fragment_container, fragment)
                    transaction.commit()
                } ?: run {
                Log.i("", "Returns null")
            }
        }

        // Example of client-code applying containerHeight retrieved from Concierge.zonesConfiguration API call.
        val zonesConfig = Concierge.zonesConfiguration(this, listOf("Zone2"))
        Log.d("ZonesConfig", zonesConfig.toString())

//        val layout = findViewById<FrameLayout>(R.id.fragment_container)
//        val params = layout.layoutParams
//        params.height = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, zonesConfig.containerHeight.toFloat(),
//                this.resources.displayMetrics).toInt()
//        params.width = layout.layoutParams.width
//        layout.layoutParams = params

        // Register for the broadcast and listen to it, do something be based on intent->extras
        broadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(p0: Context?, p1: Intent?) {
                // For example getting a dummy value
                val value = p1?.extras?.getString(ConciergeConstants.CONCIERGE_IDENTIFIER)
                val actionableLink = p1?.extras?.getString(ConciergeConstants.ACTIONABLE_URL)
                val customerStatus: ConciergeCustomerStatus? = p1?.extras?.getParcelable(
                    ConciergeConstants.CONCIERGE_AUTHENTICATION_STATUS)
                val analyticsType = p1?.extras?.getString(ConciergeConstants.ANALYTICS_TYPE)
                val contentData: HashMap<String, Any>? = p1?.extras?.getSerializable(
                    ConciergeConstants.CONTENT_DATA)
                        as HashMap<String, Any>?
                val contentInfo: HashMap<String, String>? = p1?.extras?.getSerializable(
                    ConciergeConstants
                    .CONTENT_INFO) as HashMap<String, String>?
                val hasContent = p1?.extras?.getString(ConciergeConstants.HAS_CONTENT)
//                val networkSecurity = p1?.extras?.getString(ConciergeConstants.NETWORK_SECURITY)

                actionableLink?.let {
                    Log.d("BroadcastActionLink", value.toString() + " / " + actionableLink.toString())
                }
                analyticsType?.let {
                    Log.d("BroadcastAnalytics", value.toString() + " / " +
                            it.toString() + " / " + contentData.toString() + " / " + contentInfo.toString())
                }
                customerStatus?.let {
                    Log.d("BroadcastStatus", value.toString() + " / " + it.toString())
                }
                hasContent?.let {
                    Log.d("BroadcastHasContent", value.toString() + " / " + it)
                }
//                networkSecurity?.let {
//                    Log.d("BroadcastSafeBrowsingOrSsl", value.toString() + " / " + it)
//                }

                val transaction = supportFragmentManager.beginTransaction()

//                // Example of how client can call handleActionable link
//                val conciergeOptions = arrayListOf(
//                    ConciergeOptions.CustomNoContentView(NoContentViewProviderClient()),
//                    ConciergeOptions.CustomLoadingView(LoadingViewProviderClient()),
//                    ConciergeOptions.Identifier("123")
//                )

                actionableLink?.let {
                    Concierge.handleActionableLink(this@ZonesAndModules,
                        Uri.parse(actionableLink),
                        conciergeOptions = listOf(),
                        requestEvents = ConciergeParams.RequestEvents(),
                        /*conciergeTheme = ConciergeTheme.FileName("custom_theming1.json")*/
                    )
                        ?.let { fragment ->
                            transaction.add(R.id.fragment_container, fragment)
                            transaction.addToBackStack(null)
                            transaction.commit()
                        } ?: run {
                        Log.i("", "Returns null")
                    }
                }
            }
        }
        val intentFilter = IntentFilter()
        intentFilter.addAction(ConciergeConstants.BROADCAST_CONCIERGE_EVENT)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            ContextCompat.registerReceiver(this, broadcastReceiver, intentFilter, ContextCompat.RECEIVER_NOT_EXPORTED)
        } else {
            LocalBroadcastManager.getInstance(this)
                .registerReceiver(broadcastReceiver as BroadcastReceiver, intentFilter)
        }
        // Uncomment the following to create prefetch object to fragment()
//        if (savedInstanceState == null) {
//            val prefetch = Concierge.prefetch(this, PrefetchContainer.Configured, arrayListOf(ConciergeParams
//                    .ZonesFilter(zonesConfig)), "identifier1")
//            prefetch.fetch { // Also try passing prefetch without calling fetch()
//                if (it.hasContent) { // this check is for content or can be it.status ,client can do some early detection
//                    // if they want, or else we take care of it in Concierge.
//                    val transaction = supportFragmentManager.beginTransaction()
//                    Concierge.fragment(applicationContext,
//                            Container.Configured,
//                            arrayListOf(ConciergeParams.Prefetch(prefetch), //Passing prefetch
//                                    ConciergeParams.RequestEvents()),
//                            arrayListOf(ConciergeOptions.DisplayNavigation("Eng Concierge"),
//                                    ConciergeOptions.Settings,
//                                    ConciergeOptions.Notifications,
//                                    ConciergeOptions.CustomNoContentView(NoContentViewProviderClient()),
//                                    ConciergeOptions.CustomLoadingView(LoadingViewProviderClient()))
//                    ).let {
//                        transaction.replace(R.id.fragment_container, it)
//                    }
//                    transaction.commit()
//                } else {
//                    Log.e("Content", "No content ${it.customerStatus}")
//                }
//            }
//        }
        // Comment the following if need to pass prefetch object above
        val optionsList = arrayListOf(
            ConciergeOptions.DisplayNavigation("Eng Concierge"),
            ConciergeOptions.Settings,
            ConciergeOptions.Notifications
        )
        if (savedInstanceState == null) {
            val transaction = supportFragmentManager.beginTransaction()
            Concierge.fragment(applicationContext,
                Container.Configured,
                arrayListOf(ConciergeParams.RequestEvents(), ConciergeParams.ZonesFilter(zonesConfig)),
                optionsList
            ).let {
                transaction.replace(R.id.fragment_container, it)
            }
            transaction.commit()
        }

        call_deletePushToken.setOnClickListener {
            Concierge.deletePushToken(this, object : BasicResultCallback {
                override fun onException(exception: FlybitsException) {
                    Log.d("ZOnesAndModules", "Failed to delete push token")
                }

                override fun onSuccess() {
                   Log.d("ZonesAndModules", "Successfully deleted push token")
                }
            })
        }

        call_uploadPushToken.setOnClickListener {
            Concierge.sendPush(this, "eTu4OLMDT6SWuq8DQOSRxi:APA91bHRycShxlK9kXYt1d_LysbcbOeuwgqyvuhQKJJg2Ez2SPP1KiOVOPUz1LNRtNSPGtf5FKu3lpHFLb8eDyw8PH2B3zStfhZ_qH58too4WR2j_qWepstEiA-rZKiZAAWA-dCwVi0A")
        }
    }
}