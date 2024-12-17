package com.flybits.conciergesample.activity.java;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.flybits.commons.library.api.results.callbacks.BasicResultCallback;
import com.flybits.commons.library.exceptions.FlybitsException;
import com.flybits.concierge.Concierge;
import com.flybits.concierge.enums.Container;
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
