package com.flybits.conciergesample.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.flybits.commons.library.api.results.callbacks.BasicResultCallback
import com.flybits.commons.library.exceptions.FlybitsException
import com.flybits.concierge.Concierge
import com.flybits.concierge.enums.ConciergeOptions
import com.flybits.concierge.enums.Container
import com.flybits.concierge.enums.ContentStyle
import com.flybits.conciergesample.R
import com.flybits.flybitscoreconcierge.idps.AnonymousConciergeIDP

class BannersConcierge : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_banners_concierge)
        val transaction = supportFragmentManager.beginTransaction()

        Concierge.fragment(
            this,
            Container.None,
            emptyList(),
            arrayListOf(
                ConciergeOptions.Style(ContentStyle.BANNER)
            )
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
                Concierge.connect(this, AnonymousConciergeIDP())
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