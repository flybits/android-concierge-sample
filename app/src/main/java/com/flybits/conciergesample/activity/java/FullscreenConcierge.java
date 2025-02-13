package com.flybits.conciergesample.activity.java;

import static com.flybits.android.push.services.PushServiceKt.EXTRA_PUSH_NOTIFICATION;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.flybits.android.push.models.newPush.Push;
import com.flybits.commons.library.api.results.callbacks.BasicResultCallback;
import com.flybits.commons.library.exceptions.FlybitsException;
import com.flybits.concierge.Concierge;
import com.flybits.concierge.enums.Container;
import com.flybits.conciergesample.activity.PushHandleActivity;
import com.flybits.flybitscoreconcierge.idps.AnonymousConciergeIDP;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.flybits.conciergesample.R;

import java.util.ArrayList;

public class FullscreenConcierge extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.fullscreen_concierge);
        handlePushIntent(getIntent());
        final FragmentTransaction transaction = this.getSupportFragmentManager().beginTransaction();
        final Fragment fragment = Concierge.INSTANCE.fragment(
                this,
                Container.None.INSTANCE,
                new ArrayList<>(), new ArrayList<>()
        );
        transaction.replace(R.id.concierge_framelayout, fragment);
        transaction.commit();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handlePushIntent(intent);
    }
    private void handlePushIntent(@androidx.annotation.Nullable Intent intent) {
        if (intent != null) {
            if (intent.hasExtra(EXTRA_PUSH_NOTIFICATION)) {
                // Example of passing Intent that has Flybits Push for handling with Concierge.handlePush() API in the DemoAppCompatActivityActionBar
                final Push push;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    push = intent.getParcelableExtra(EXTRA_PUSH_NOTIFICATION, Push.class);
                } else {
                    push = intent.getParcelableExtra(EXTRA_PUSH_NOTIFICATION);
                }
                Intent intentActivity = new Intent(this, PushHandleActivity.class);
                intentActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intentActivity.putExtra(EXTRA_PUSH_NOTIFICATION, push);
                startActivity(intentActivity);
            } else {
                // Since Intent does not have Flybits Push the RemoteMessage should be extracted before passing it to the Concierge.handle() API.
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(@Nullable Menu menu) {
        this.getMenuInflater().inflate(R.menu.menu_options, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NotNull MenuItem item) {
        if (item.getItemId() == R.id.login_flybits) {
            if (!Concierge.isConnected(this)) {
                Concierge.INSTANCE.connect(
                        this,
                        new AnonymousConciergeIDP(),
                        // Use the OpenID Connect (OIDC) IDP if required.
//                        new OpenIDConnectConciergeIDP("Your ID Token here"),
                        null, null,
                        false,
                        new BasicResultCallback() {
                            public void onException(@NonNull FlybitsException exception) {
                            }

                            public void onSuccess() {
                            }
                        });
            } else {
                Concierge.INSTANCE.disconnect(
                        this,
                        new BasicResultCallback() {
                            public void onException(@NonNull FlybitsException exception) {

                            }

                            public void onSuccess() {
                            }
                        });
            }

            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }
}
