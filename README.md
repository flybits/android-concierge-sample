# Concierge Sample Application

## Overview

The purpose of this application is to demonstrate how the Flybits Concierge SDK can be integrated into a client application. 

## Setup Steps

* Open the concierge.xml file in the /res/xml package and add your project id as the value for the `projectId` field
* Alternatively to doing the above you can configure the concierge programmatically, a code example can be found in the class `GlobalApplication`
* Add a google-services.json file to the project's /app directory associated with the com.flybits.conciergesample application package name
* Add the Firebase Cloud Messaging (FCM) service account key to your Flybits project's Push Settings on the Flybits developer portal to use FCM Push service
* Add a agconnect-services.json file to the project's /app directory associated with the com.flybits.conciergesample application package name
* Add the EMUI credential and secret to your Flybits project's Push Settings on the Flybits developer portal to use Huawei Push service

## Features Present

* Authentication
* Logout
* `FlybitsViewProvider` registration
* Opt in and out
* `FlybitsContextPlugin` registration
* Embedded `ConciergeFragment` display
* Full screen `ConciergeActivity` display
* Push notifications integration
* Navigation component integration

## Run Configuration setup in Android Studio
1. In Android Studio open Menu -> Run -> Edit Configurations...
2. In the opened window locate Launch Options section
3. In the Launch dropdown select Specified Activity
4. In the Activity menu select the Activity desired for launch and click OK
5. Locate Run button at the bottom anc click it

## Configure using JSON instead of FlybitsConciergeConfiguration.Builder
1. Flybits Concierge provides alternative way of configuring using config() API with JSON format.
2. For that, just create a JSON named `ConciergeConfiguration.json` under **_application's assets folder_**, as below structure
3. ```json
    {
      "conciergeConfiguration": {
        "projectConfiguration": {
          "projectId": "project_id",
          "gatewayUrl": "gateway_url",
          "pushProvider": "fcm or huawei" 
        },
        "settingsConfiguration": {
          "tncUrl": "tnc_url",
          "privacyUrl": "privacy_url",
          "showOptOut": true
        },
        "webService": "webservice_url",
        "configuredContainerDefaultHeight": 300,
        "uploadPushTokenOnConnect": true, 
        "webViewPoolingSize": 0
      }
    }
    ```
4. All values in above json have corresponding values to `FlybitsConciergeConfiguration.Builder`, make sure the above json is ready with expected values.
5. On compiling the application and running it, the `configure()` API will now **ONLY** utilize the values from the `ConciergeConfiguration.json` file and ignore the `FlybitsConciergeConfiguration.Builder`.
6. In order to use the `FlybitsConciergeConfiguration.Builder` values again for configuring, either rename the `ConciergeConfiguration.json` or remove it completely.

## For Opening push notifications from system trey.
1. In order to open the correct launcher Activity on click of push notification from system trey, developer needs to comment out all
intent filters defined for launcher in AndroidManifest.xml.
2. Only keep the intent filter launcher for the activity targeting to run the application.
3. This will ensure, when the push notification is clicked only the targeted activity gets launched and push's pending intent is handled correctly.
4. Once push notification click is tested, undo the changes made in step 1.
