package com.flybits.conciergesample.activity

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.flybits.android.push.models.newPush.Push
import com.flybits.android.push.services.EXTRA_PUSH_NOTIFICATION
import com.flybits.commons.library.api.results.callbacks.BasicResultCallback
import com.flybits.commons.library.exceptions.FlybitsException
import com.flybits.concierge.Concierge
import com.flybits.concierge.enums.Container
import com.flybits.conciergesample.R
import com.flybits.flybitscoreconcierge.idps.AnonymousConciergeIDP
import com.flybits.flybitscoreconcierge.idps.OpenIDConnectConciergeIDP


class FullscreenConcierge : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fullscreen_concierge)
        handlePushIntent(intent)


        val transaction = supportFragmentManager.beginTransaction()

        Concierge.fragment(
            this,
            Container.None,
            emptyList(),
            emptyList()
        ).let {
            transaction.replace(R.id.concierge_framelayout, it)
        }

        transaction.commit()

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_options, menu)
        return true
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.login_flybits) {
            // Check if connected already.
            if (!Concierge.isConnected(this)) {
                // Call Connect and load the fragment.
                Concierge.connect(
                    this,
                    idp = AnonymousConciergeIDP(),
                    // Use the OpenID Connect (OIDC) IDP if required.
//                    idp = OpenIDConnectConciergeIDP("Your ID Token here"),
                    basicResultCallback = object : BasicResultCallback {
                        override fun onException(exception: FlybitsException) {

                        }

                        override fun onSuccess() {
                        }
                    })
            } else {
                Concierge.disconnect(this, object : BasicResultCallback {
                    override fun onException(exception: FlybitsException) {}

                    override fun onSuccess() {}
                })
            }
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}