package com.flybits.conciergesample.utils.java;

import android.os.Bundle;

import androidx.annotation.NonNull;

import com.flybits.android.kernel.models.Content;
import com.flybits.conciergesample.activity.CustomSettingsActivity;
import com.flybits.flybitscoreconcierge.actiontypes.DeepLinkHandler;

/**
 * Java Implementation of DeepLinkHandler to specify the activity to display when app://? actionable
 * link is clicked.
 */
public class CustomScreenDeepLinkHandler implements DeepLinkHandler {

    /**
     * @return the identifier which uniquely identifies the activity to be opened
     * as specified by parameter "name" in app://? actionable link.
     */
    @NonNull
    @Override
    public String getIdentifier() {
        return "customScreenView";
    }

    /**
     * @return the Activity to be displayed on handling of actionable link app://?name=..
     */
    @NonNull
    @Override
    public Class<?> activity() {
        return CustomSettingsActivity.class;
    }

    /**
     * @return the data to be passed with the activity to display.
     */
    @NonNull
    @Override
    public Bundle bundle(Content content) {
        Bundle bundle = new Bundle();
        bundle.putParcelable("content", content);
        return bundle;
    }
}
