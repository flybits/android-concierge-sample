package com.flybits.conciergesample.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import com.flybits.android.push.models.Push
import com.flybits.concierge.ConciergeConstants
import com.flybits.concierge.FlybitsConcierge
import com.flybits.concierge.enums.ShowMode
import com.flybits.conciergesample.R

class MainActivity: AppCompatActivity() {

    private var flybitsConcierge: FlybitsConcierge? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupActionBarWithNavController(findNavController(R.id.nav_host_fragment))
        flybitsConcierge = FlybitsConcierge.with(applicationContext)
        intent?.let { handleIntent(it) }

    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        intent?.let { handleIntent(it) }
    }

    private fun handleIntent(intent: Intent) {
        if (intent.hasExtra(ConciergeConstants.PUSH_EXTRA)){
            val extra = intent.getParcelableExtra<Push>(ConciergeConstants.PUSH_EXTRA)
            flybitsConcierge?.showPush(ShowMode.NEW_ACTIVITY, extra)
        }
    }
}