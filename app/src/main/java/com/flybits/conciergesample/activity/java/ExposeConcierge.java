package com.flybits.conciergesample.activity.java;

import static com.flybits.android.push.services.PushServiceKt.EXTRA_PUSH_NOTIFICATION;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.NavGraph;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.flybits.android.push.models.newPush.Push;
import com.flybits.conciergesample.R;
import com.flybits.conciergesample.activity.PushHandleActivity;

public class ExposeConcierge extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        handlePushIntent(getIntent());

        final AppBarConfiguration appBarConfig = new AppBarConfiguration.Builder(R.id.expose_fragment).build();
        final NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        final NavGraph navGraph = navController.getNavInflater().inflate(R.navigation.nav_graph);
        navGraph.setStartDestination(R.id.expose_fragment);
        navController.setGraph(navGraph);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfig);
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
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
