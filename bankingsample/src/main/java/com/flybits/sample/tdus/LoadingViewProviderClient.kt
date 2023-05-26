package com.flybits.sample.tdus

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.flybits.flybitscoreconcierge.loadingviewprovider.LoadingViewProvider

class LoadingViewProviderClient : LoadingViewProvider {
    override fun getView(parent: ViewGroup): View {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_progress, parent, false)
        return view
    }

    override fun start() {

    }

    override fun stop() {

    }
}