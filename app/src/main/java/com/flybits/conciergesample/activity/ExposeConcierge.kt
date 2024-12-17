package com.flybits.conciergesample.activity

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.flybits.android.push.models.newPush.Push
import com.flybits.android.push.services.EXTRA_PUSH_NOTIFICATION
import com.flybits.conciergesample.R

class ExposeConcierge : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        handlePushIntent(intent)
        val appBarConfig = AppBarConfiguration
            .Builder(R.id.expose_fragment)
            .build()
        val navController = Navigation.findNavController(this, R.id.nav_host_fragment)
        val navGraph = navController.navInflater.inflate(R.navigation.nav_graph)
        navGraph.startDestination = R.id.expose_fragment
        navController.graph = navGraph
        setupActionBarWithNavController(navController, appBarConfig)
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

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}