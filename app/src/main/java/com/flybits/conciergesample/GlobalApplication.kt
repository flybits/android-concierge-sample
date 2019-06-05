package com.flybits.conciergesample

import android.annotation.SuppressLint
import android.app.Application
import com.flybits.concierge.FlybitsConcierge
import com.flybits.concierge.smartrewards.viewproviders.BenefitsViewProvider
import com.flybits.concierge.smartrewards.viewproviders.ConfirmationViewProvider
import com.flybits.concierge.smartrewards.viewproviders.OffersViewProvider
import com.flybits.concierge.smartrewards.viewproviders.OptInViewProvider

class GlobalApplication: Application() {

    @SuppressLint("ResourceType")
    override fun onCreate() {
        super.onCreate()
        val concierge = FlybitsConcierge.with(this)
        //This needs to happen prior to using API, doesn't need to be in Application.onCreate()
        concierge.initialize(R.xml.concierge)

        //Add view providers for content templates you want displayed
        concierge.registerFlybitsViewProvider(OptInViewProvider(this))
        concierge.registerFlybitsViewProvider(OffersViewProvider(this))
        concierge.registerFlybitsViewProvider(BenefitsViewProvider(this))
        concierge.registerFlybitsViewProvider(ConfirmationViewProvider(this))
        concierge.enableDebugMode()

    }
}