package com.flybits.conciergesample.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.flybits.android.push.models.newPush.Push
import com.flybits.android.push.services.EXTRA_PUSH_NOTIFICATION
import com.flybits.concierge.ConciergeConstants
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
                val extra = it.getParcelableExtra<Push>(EXTRA_PUSH_NOTIFICATION)
                val intentActivity = Intent(this, DemoAppCompatActivityActionBar::class.java)
                intentActivity.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                intentActivity.putExtra(EXTRA_PUSH_NOTIFICATION, extra)
                startActivity(intentActivity)
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}