package com.flybits.conciergesample.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.flybits.android.push.models.newPush.DisplayablePush
import com.flybits.concierge.ConciergeConstants
import com.flybits.concierge.ConciergeFragment
import com.flybits.concierge.DisplayConfiguration
import com.flybits.concierge.FlybitsConcierge
import com.flybits.concierge.enums.ShowMode
import com.flybits.conciergesample.R

class MainActivity: AppCompatActivity() {

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

    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        intent?.let { handleIntent(it) }
    }

    private fun handleIntent(intent: Intent) {
        if (intent.hasExtra(ConciergeConstants.PUSH_EXTRA)){
            val extra:DisplayablePush = intent.getParcelableExtra<DisplayablePush>(ConciergeConstants.PUSH_EXTRA)
            flybitsConcierge?.showPush(
                DisplayConfiguration(
                    ConciergeFragment.MenuType.MENU_TYPE_APP_BAR,
                    ShowMode.NEW_ACTIVITY,
                    true
                ),
                extra
            )
        }
    }
}