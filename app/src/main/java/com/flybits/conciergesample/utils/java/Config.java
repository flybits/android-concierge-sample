package com.flybits.conciergesample.utils.java;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.flybits.android.push.provider.FcmV2DeliveryProvider;
import com.flybits.android.push.provider.PushProvider;
import com.flybits.commons.library.logging.VerbosityLevel;
import com.flybits.concierge.Concierge;
import com.flybits.concierge.FlybitsConciergeConfiguration;
import com.flybits.context.ContextManager;
import com.flybits.context.ReservedContextPlugin;
import com.flybits.flybitscoreconcierge.actiontypes.DeepLinkHandler;

import java.util.ArrayList;
import java.util.Arrays;

public class Config {
    public static void configureConcierge(@NonNull final Context context){
        final FlybitsConciergeConfiguration config = new FlybitsConciergeConfiguration.Builder(context)
                .setGatewayUrl("https://api.demo.flybits.com")
                .setProjectId("2CE41988-B1D3-4116-98DD-42FFB8754384")
                .setWebService("https://static-files-concierge.demo.flybits.com/latest")
                .setPushProvider(FcmV2DeliveryProvider.INSTANCE)
                .setUploadPushtokenOnConnect(true)
                .build();

        Concierge.INSTANCE.setLoggingVerbosity(VerbosityLevel.ALL);

        // Adding the instance of DeepLinkHandler to array list in order to pass it
        // to configure() API.
        ArrayList<DeepLinkHandler> deepLinkHandlers = new ArrayList<>();
        deepLinkHandlers.add(new SettingsActivityHandler());

        // Call configure on Concierge
        Concierge.INSTANCE.configure(
                config,
                Arrays.asList(
                        new ContextManager.PluginType.ReservedPlugin(ReservedContextPlugin.HUAWEI_LOCATION),
                        new ContextManager.PluginType.ReservedPlugin(ReservedContextPlugin.HUAWEI_GEOFENCE_LOCATION)
                        // To add or use plugins designed for Google Location and Geofence use the below code:
//                        new ContextManager.PluginType.ReservedPlugin(ReservedContextPlugin.LOCATION),
//                        new ContextManager.PluginType.ReservedPlugin(ReservedContextPlugin.GEOFENCE_LOCATION)
                ),
                context,
                deepLinkHandlers,
                null
        );
    }

    public static void createNotificationChannel(
            @NonNull Context context,
            @NonNull String channelId,
            @Nullable String channelName,
            @Nullable String channelDesc) {

        if (channelName == null) {
            channelName = "CHANNEL_NAME";
        }
        if (channelDesc == null) {
            channelDesc = "CHANNEL_DESC";
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            final NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription(channelDesc);
            final NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

    }
}
