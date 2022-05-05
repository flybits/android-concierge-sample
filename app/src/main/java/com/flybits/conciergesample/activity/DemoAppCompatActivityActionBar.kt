package com.flybits.conciergesample.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.flybits.concierge.Concierge
import com.flybits.concierge.ConciergeConstants
import com.flybits.conciergesample.R

class DemoAppCompatActivityActionBar : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_demo_appcompat_activity)

        val transaction = supportFragmentManager.beginTransaction()
        intent?.let {
            if (it.hasExtra(ConciergeConstants.PUSH_EXTRA)) {
                val push = Concierge.handlePush(it)
                if (push != null) {
                    Concierge.deepLink(push)?.let { fragment ->
                        transaction.replace(R.id.fragment_container, fragment)
                    }
                }
            }
            transaction.commit()
        }
    }
}