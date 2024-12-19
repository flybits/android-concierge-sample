package com.flybits.conciergesample.activity.java;

import static com.flybits.android.push.services.PushServiceKt.EXTRA_PUSH_NOTIFICATION;
import static com.flybits.concierge.ConciergeConstants.BROADCAST_CONCIERGE_EVENT;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.flybits.android.push.models.newPush.Push;
import com.flybits.commons.library.api.results.callbacks.BasicResultCallback;
import com.flybits.commons.library.exceptions.FlybitsException;
import com.flybits.commons.library.logging.VerbosityLevel;
import com.flybits.commons.library.models.User;
import com.flybits.concierge.Concierge;

import com.flybits.concierge.ConciergeConstants;
import com.flybits.concierge.enums.ConciergeOptions;
import com.flybits.concierge.enums.ConciergeParams;
import com.flybits.concierge.enums.Container;
import com.flybits.concierge.models.ZonesConfig;
import com.flybits.conciergesample.R;
import com.flybits.conciergesample.activity.PushHandleActivity;
import com.flybits.flybitscoreconcierge.idps.AnonymousConciergeIDP;
import com.flybits.internal.db.CommonsDatabase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ZonesModulesConcierge extends AppCompatActivity {
    @Nullable
    BroadcastReceiver broadcastReceiver = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zones_modules);

        handlePushIntent(getIntent());

        Concierge.INSTANCE.setLoggingVerbosity(VerbosityLevel.ALL);

        final Button connect_disconnect = findViewById(R.id.connect_disconnect);
        connect_disconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Concierge.isConnected(getApplicationContext())) {
                    Concierge.INSTANCE.disconnect(getApplicationContext(), new BasicResultCallback() {
                        @Override
                        public void onSuccess() {
                        }

                        @Override
                        public void onException(@NonNull FlybitsException e) {
                        }
                    });
                } else {
                    Concierge.INSTANCE.connect(
                            getApplicationContext(),
                            new AnonymousConciergeIDP(),
                            null,
                            null,
                            // Use the OpenID Connect (OIDC) IDP if required.
//                    new OpenIDConnectConciergeIDP("Your ID Token here"),
                            true,
                            new BasicResultCallback() {
                                @Override
                                public void onException(@NonNull FlybitsException e) {
                                }

                                @Override
                                public void onSuccess() {
                                }
                            });
                }
            }
        });

        final Button optin_optout = findViewById(R.id.optin_optout);
        optin_optout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Concierge.isConnected(getApplicationContext())) {
                    final CommonsDatabase commonsDatabase = CommonsDatabase.getDatabase(getApplicationContext());
                    final Handler handler = new Handler(Looper.getMainLooper());
                    final ExecutorService executorService = Executors.newSingleThreadExecutor();
                    executorService.execute(() -> {
                        final User user = commonsDatabase.userDao().getActiveUser();

                        handler.post(() -> {
                            if (user.isOptedIn()) {
                                Concierge.INSTANCE.optOut(getApplicationContext(), new BasicResultCallback() {
                                    @Override
                                    public void onSuccess() {
                                    }
                                    @Override
                                    public void onException(@NonNull FlybitsException e) {
                                    }
                                });
                            } else {
                                Concierge.INSTANCE.optIn(getApplicationContext(), new BasicResultCallback() {
                                    @Override
                                    public void onSuccess() {
                                    }
                                    @Override
                                    public void onException(@NonNull FlybitsException e) {
                                    }
                                });
                            }
                        });
                    });
                } else {
                    // Concierge not connected.
                }
            }
        });

        final Button call_handleActionableLink = findViewById(R.id.call_handleActionableLink);
        call_handleActionableLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final FragmentTransaction transaction = ZonesModulesConcierge.this.getSupportFragmentManager().beginTransaction();
                final Fragment fragment = Concierge.INSTANCE.handleActionableLink(
                        ZonesModulesConcierge.this,
                        Uri.parse("details://?contentId=D68B465C-34B5-41E9-877C-5813CA040C62"),
                        null,
                        null,
                        null);
                if (fragment != null) {
                    transaction.replace(R.id.fragment_container, fragment);
                    transaction.commit();
                } else {
                    // Returned null
                }
            }
        });

        // Configure zones to be displayed.
        final ZonesConfig zonesConfig = Concierge.INSTANCE.zonesConfiguration(this, new ArrayList<>(Collections.singletonList("default_zone")));

        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent != null && intent.getExtras() != null) {
                    final String actionableLink = intent.getExtras().getString(ConciergeConstants.ACTIONABLE_URL);

                    if (actionableLink != null) {
                        final FragmentTransaction transaction = ZonesModulesConcierge.this.getSupportFragmentManager().beginTransaction();
                        final Fragment fragment = Concierge.INSTANCE.handleActionableLink(
                                ZonesModulesConcierge.this,
                                Uri.parse(actionableLink),
                                null,
                                new ConciergeParams.RequestEvents(),
                                null
                        );
                        if(fragment != null) {
                            transaction.add(R.id.fragment_container, fragment);
                            transaction.addToBackStack(null);
                            transaction.commit();
                        } else {
                            // Returned null
                        }
                    }
                }
            }
        };

        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BROADCAST_CONCIERGE_EVENT);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            ContextCompat.registerReceiver(this, broadcastReceiver, intentFilter, ContextCompat.RECEIVER_NOT_EXPORTED);
        } else {
            LocalBroadcastManager.getInstance(this)
                    .registerReceiver(broadcastReceiver, intentFilter);
        }

        // Display Concierge
        final ArrayList<ConciergeOptions> optionsList = new ArrayList<>();
        optionsList.add(new ConciergeOptions.DisplayNavigation("Concierge"));
        optionsList.add(ConciergeOptions.Settings.INSTANCE);
        optionsList.add(ConciergeOptions.Notifications.INSTANCE);

        if (savedInstanceState == null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, Concierge.INSTANCE.fragment(getApplicationContext(),
                    Container.Configured.INSTANCE,
                    new ArrayList<ConciergeParams>() {{
                        add(new ConciergeParams.ZonesFilter(zonesConfig));
                    }},
                    optionsList));
            transaction.commit();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handlePushIntent(intent);
    }

    private void handlePushIntent(@Nullable Intent intent) {
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
    protected void onDestroy() {
        super.onDestroy();
        if (broadcastReceiver != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                this.unregisterReceiver(broadcastReceiver);
            } else {
                LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);
            }
        }
    }
}
