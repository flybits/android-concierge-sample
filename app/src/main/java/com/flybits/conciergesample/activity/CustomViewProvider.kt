package com.flybits.conciergesample.activity

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.Navigation
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.flybits.android.kernel.models.LocalizedValue
import com.flybits.android.push.models.newPush.Push
import com.flybits.android.push.services.EXTRA_PUSH_NOTIFICATION
import com.flybits.commons.library.api.results.callbacks.BasicResultCallback
import com.flybits.commons.library.exceptions.FlybitsException
import com.flybits.concierge.Concierge
import com.flybits.concierge.ConciergeConstants
import com.flybits.concierge.enums.ConciergeOptions
import com.flybits.concierge.enums.Container
import com.flybits.concierge.models.CardBackgroundColor
import com.flybits.concierge.theming.ConciergeAppearance
import com.flybits.concierge.theming.getThemeColor
import com.flybits.concierge.theming.isDarkThemeOn
import com.flybits.conciergesample.R
import com.flybits.flybitscoreconcierge.BaseTemplate
import com.flybits.flybitscoreconcierge.idps.AnonymousConciergeIDP
import com.flybits.flybitscoreconcierge.viewproviders.FlybitsNavigator
import com.flybits.flybitscoreconcierge.viewproviders.FlybitsViewHolder
import com.flybits.flybitscoreconcierge.viewproviders.FlybitsViewProvider
import kotlinx.android.parcel.Parcelize

class CustomViewProvider : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fullscreen_concierge)

        val transaction = supportFragmentManager.beginTransaction()

        Concierge.fragment(
            this,
            Container.None,
            emptyList(),
            arrayListOf(ConciergeOptions.CustomViewProviders(arrayListOf(
                CardOfferViewProvider()
            )))
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

@Parcelize
class CardOffer(var title: String? = null,
                var header: String? = null,) : BaseTemplate() {}

class CardOfferViewProvider: FlybitsViewProvider<CardOffer> {
    override fun getClassType(): Class<CardOffer> {
        return CardOffer::class.java
    }

    override fun getContentType(): String {
        return "card-offer"
    }

    override fun onCreateViewHolder(parent: ViewGroup): FlybitsViewHolder<CardOffer> {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_offer_custom_template, parent, false)

        return CardOfferViewHolder(view)
    }

    override fun onPushOpened(model: CardOffer, flybitsNavigator: FlybitsNavigator) {
    }
}

class CardOfferViewHolder(val view: View): FlybitsViewHolder<CardOffer>(view) {
    override fun bindData(data: CardOffer, flybitsNavigator: FlybitsNavigator) {
        val title: TextView? = view.findViewById(R.id.card_offer_title)
        val header: TextView? = view.findViewById(R.id.card_offer_header)

        title?.text = data.title
        header?.text = data.header
    }
}