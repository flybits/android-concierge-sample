package com.flybits.conciergesample.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import com.flybits.commons.library.logging.VerbosityLevel
import com.flybits.concierge.Concierge
import com.flybits.concierge.ConciergeFragment
import com.flybits.concierge.FlybitsConciergeConfiguration
import com.flybits.concierge.enums.ConciergeOptions
import com.flybits.concierge.enums.Container
import com.flybits.conciergesample.R

/**
 * The [AppCompatActivity] that uses Theme.AppCompat theme to simulate client host activity with
 * [androidx.appcompat.app.ActionBar] for [ConciergeFragment] that is constructed with
 * [ConciergeOptions.DisplayNavigation], [ConciergeOptions.Settings], and [ConciergeOptions.Notifications] to
 * demonstrate [ConciergeFragment] ability to respect hosting [androidx.appcompat.app.ActionBar] presence and not
 * setting its own [androidx.appcompat.widget.Toolbar] as [androidx.appcompat.app.ActionBar].
 */
class DemoAppCompatActivityActionBar : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_demo_appcompat_activity)

        val transaction = supportFragmentManager.beginTransaction()
        Concierge.fragment(applicationContext,
                Container.None,
                null,
                arrayListOf(ConciergeOptions.DisplayNavigation,
                        ConciergeOptions.Settings,
                        ConciergeOptions.Notifications)
        ).let {
            transaction.replace(R.id.fragment_container, it)
        }
        transaction.commit()
    }
}