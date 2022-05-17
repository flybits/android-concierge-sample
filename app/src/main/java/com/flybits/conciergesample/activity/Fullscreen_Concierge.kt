package com.flybits.conciergesample.activity

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.Toolbar
import com.flybits.commons.library.api.results.callbacks.BasicResultCallback
import com.flybits.commons.library.exceptions.FlybitsException
import com.flybits.concierge.Concierge
import com.flybits.concierge.TAG_FLYBITS_CONCIERGE
import com.flybits.concierge.enums.Container
import com.flybits.conciergesample.R
import com.flybits.flybitscoreconcierge.idps.AnonymousConciergeIDP
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_account.*


class Fullscreen_Concierge : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fullscreen_concierge)


        val transaction = supportFragmentManager.beginTransaction()

        val concierge = com.flybits.concierge.Concierge.fragment(
            this,
            Container.None,
            emptyList(),
            emptyList()).let {
                transaction.replace(R.id.concierge_framelayout, it)
        }

        transaction.commit()

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_options, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        val context = this
        if (item.itemId == R.id.login_flybits) {
            // Check if connected already.
            if (!Concierge.isConnected(this)) {
                // Call Connect and load the fragment.
                Concierge.connect(this, AnonymousConciergeIDP())
            } else {
                Concierge.disconnect(this, object : BasicResultCallback {
                    override fun onException(exception: FlybitsException) {
//                        context?.let { _ ->
//                            progressBar.visibility = View.GONE
//                            view?.let {
//                                Snackbar.make(
//                                    it,
//                                    "Error logging out!",
//                                    Snackbar.LENGTH_LONG
//                                ).show()
//                            }
//                        }
                    }

                    override fun onSuccess() {
                        context?.let { _ ->
//                            progressBar.visibility = View.GONE
//                            view?.let {
//                                Snackbar.make(it, "Logged out!", Snackbar.LENGTH_SHORT).show()
//                            }
                        }
                    }
                })
            }
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}