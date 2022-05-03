package com.flybits.conciergesample.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.flybits.android.push.models.newPush.DisplayablePush
import com.flybits.android.push.models.newPush.Push
import com.flybits.android.push.services.EXTRA_PUSH_NOTIFICATION
import com.flybits.concierge.Concierge
import com.flybits.concierge.ConciergeConstants
import com.flybits.conciergesample.R
import com.flybits.context.ContextManager
import com.flybits.context.ReservedContextPlugin

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val appBarConfig = AppBarConfiguration
            .Builder(R.id.tabHolderFragment, R.id.loginFragment)
            .build()
        setupActionBarWithNavController(findNavController(R.id.nav_host_fragment), appBarConfig)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        handlePushIntent(intent)
    }

    private fun handlePushIntent(intent: Intent?) {
        intent?.let {
            if (it.hasExtra(EXTRA_PUSH_NOTIFICATION)) {
                val extra = it.getParcelableExtra<Push>(EXTRA_PUSH_NOTIFICATION)
                val bundle = Bundle()
                bundle.putParcelable(ConciergeConstants.PUSH_EXTRA, extra)
                /*val intentActivity = Intent(applicationContext, DemoActivityNotificationHosting::class.java)
                intentActivity.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                intentActivity.putExtra(ConciergeConstants.PUSH_EXTRA, extra)
                startActivity(intentActivity)*/
                findNavController(R.id.nav_host_fragment).navigate(R.id.action_loginFragment_to_accountFragment, bundle)
            }
        }
    }
}