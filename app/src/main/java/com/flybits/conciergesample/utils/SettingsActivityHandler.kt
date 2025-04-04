package com.flybits.conciergesample.utils

import android.os.Bundle
import com.flybits.android.kernel.models.Content
import com.flybits.conciergesample.activity.CustomSettingsActivity
import com.flybits.flybitscoreconcierge.actiontypes.DeepLinkHandler

/**
 * Kotlin Implementation of DeepLinkHandler to specify the activity to display when app://? actionable
 * link is clicked.
 */
class SettingsActivityHandler : DeepLinkHandler {

    /**
     * @return the identifier which uniquely identifies the activity to be opened
     * as specified by parameter "name" in app://? actionable link.
     */
    override val identifier: String
        get() = "customScreenView"

    /**
     * @return the Activity to be displayed on handling of actionable link app://?name=..
     */
    override fun activity(): Class<*> {
        return CustomSettingsActivity::class.java
    }

    /**
     * @return the data to be passed with the activity to display.
     */
    override fun bundle(content: Content?): Bundle {
        val bundle = Bundle()
        bundle.putParcelable("content", content)
        return bundle
    }
}

