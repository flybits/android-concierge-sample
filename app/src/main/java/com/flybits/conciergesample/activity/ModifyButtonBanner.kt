package com.flybits.conciergesample.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.Navigation
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.flybits.conciergesample.R

class ModifyButtonBanner : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val appBarConfig = AppBarConfiguration
            .Builder(R.id.modify_banners)
            .build()
        val navController = Navigation.findNavController(this, R.id.nav_host_fragment)
        val navGraph = navController.navInflater.inflate(R.navigation.nav_graph)
        navGraph.startDestination = R.id.modify_banners
        navController.graph = navGraph
        setupActionBarWithNavController(navController, appBarConfig)
    }
}