package com.flybits.conciergesample.activity

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
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