package com.flybits.conciergesample.activity.java;

import static com.flybits.android.push.services.PushServiceKt.EXTRA_PUSH_NOTIFICATION;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.flybits.concierge.enums.ConciergePush;
import com.flybits.conciergesample.R;

import com.flybits.concierge.Concierge;

public class PushHandleActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_push_handle);

        final FragmentManager supportFragmentManager = getSupportFragmentManager();
        final FragmentTransaction transaction = supportFragmentManager.beginTransaction();
        final Intent intent = getIntent();
        if (intent != null) {
            if (intent.hasExtra(EXTRA_PUSH_NOTIFICATION)) {
                final ConciergePush push = Concierge.INSTANCE.handlePush(this, intent);
                if (push != null) {
                    final Fragment fragment = Concierge.INSTANCE.deepLink(push, this, null);
                    if (fragment != null) {
                        transaction.replace(R.id.fragment_container, fragment);
                    }
                }
            }
            transaction.commit();
        }
    }
}
