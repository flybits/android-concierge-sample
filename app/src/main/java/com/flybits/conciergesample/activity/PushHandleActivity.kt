package com.flybits.conciergesample.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.flybits.android.push.services.EXTRA_PUSH_NOTIFICATION
import com.flybits.concierge.Concierge
import com.flybits.conciergesample.R

class PushHandleActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_push_handle)

        val transaction = supportFragmentManager.beginTransaction()
        intent?.let {
            if (it.hasExtra(EXTRA_PUSH_NOTIFICATION)) {
                val push = Concierge.handlePush(this, it)
                if (push != null) {
                    Concierge.deepLink(push, this)?.let { fragment ->
                        transaction.replace(R.id.fragment_container, fragment)
                    }
                }
            }
            transaction.commit()
        }
    }
}