package com.flybits.sample.tdus

import android.os.Bundle
import android.util.Log
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.flybits.commons.library.api.results.callbacks.BasicResultCallback
import com.flybits.commons.library.exceptions.FlybitsException
import com.flybits.concierge.Concierge
import com.flybits.flybitscoreconcierge.idps.APIKeyConciergeIDP
import com.flybits.flybitscoreconcierge.idps.AnonymousConciergeIDP
import com.flybits.sample.tdus.databinding.ActivityMainBinding
import kotlin.properties.Delegates

class MainActivity : AppCompatActivity(), Interaction {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private var fragmentID by Delegates.notNull<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

        if (Concierge.isConnected(this)){
            showToast("Concierge is connected")
        }else{
            showToast("Concierge is not connected ... so let's connect with hardcoded value")

            //val idp = APIKeyConciergeIDP(apiKey = "EA41D596-12B0-41A6-8E93-8F9704C260A3", email ="petar1")
            val idp = AnonymousConciergeIDP()
            Concierge.connect(this, idp,  basicResultCallback = object : BasicResultCallback {
                override fun onSuccess() {

                    val navHostFragment: Fragment? =
                        supportFragmentManager.findFragmentById(R.id.nav_host_fragment_content_main)
                    val firstFragment = navHostFragment?.childFragmentManager?.fragments?.get(0) as FirstFragment
                    firstFragment.loadConcierge()
                    showToast("Successfully Connected To Concierge")
                }

                override fun onException(exception: FlybitsException) {
                    showToast("Something went wrong, couldn't connect to Flybits server.")
                }
            })
        }

        supportActionBar?.hide();
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }

    private fun showToast(message: String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()
    }


    override fun updateConcierge(layout: Int, fragment: Fragment) {
        supportFragmentManager.beginTransaction().let {
            it.replace(layout, fragment,"fragment")
            it.commit()
        }
    }
}