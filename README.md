# Concierge Sample Application

## Overview

The purpose of this application is to demonstrate how the Flybits Concierge SDK can be integrated into a client application. 

## Setup Steps

1. Configure Concierge programmatically with `FlybitsConciergeConfiguration.Builder`, a code example can be found in `GlobalApplication.kt`, `Config.kt` and `Config.java`. Alternatively, Concierge can be configured with the `ConciergeConfiguration.json` file that has to be created and placed in the `app/src/main/assets` directory.
2. Add a google-services.json file to the project's /app directory associated with the com.flybits.conciergesample application package name 
3. Add the Firebase Cloud Messaging (FCM) service account key to your Flybits project's Push Settings on the Flybits developer portal to use FCM Push service 
4. Add a agconnect-services.json file to the project's /app directory associated with the com.flybits.conciergesample application package name 
5. Add the EMUI credential and secret to your Flybits project's Push Settings on the Flybits developer portal to use Huawei Push service

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
* Zones and Modules
* Kotlin and Java examples of API calls

##  Example of the `ConciergeConfiguration.json`:

```json
{
   "conciergeConfiguration": {
     "projectConfiguration": {
       "projectId": "<project ID>",
       "gatewayUrl": "<gateway URL>",
       "pushProvider": "<fcm | huawei>" 
     },
     "settingsConfiguration": {
       "tncUrl": "<terms and conditions URL>",
       "privacyUrl": "<privacy URL>",
       "showOptOut": <true | false>
     },
     "webService": "<webservice URL>",
     "configuredContainerDefaultHeight": <integer e.g.: 300>,
     "uploadPushTokenOnConnect": <true | false>, 
     "webViewPoolingSize": <integer>
   }
 }
```
1. All values in `ConciergeConfiguration.json` have corresponding values in the `FlybitsConciergeConfiguration.Builder`.
2. If the `ConciergeConfiguration.json` is provided in the `app/src/main/assets` the Concierge SDK will use values provided in it.

## Run Configuration setup in Android Studio
1. In Android Studio open Menu -> Run -> Edit Configurations...
2. In the opened window locate Launch Options section
3. In the Launch dropdown select Specified Activity
4. In the Activity menu select the Activity desired for launch and click OK
5. Locate Run button at the bottom anc click it

## Run Configuration setup in AndroidManifest
1. Open `app/src/main/AndroidManifest.xml`
2. Leave the `<intent-filter>` uncommented only for the `<activity>` that you wish to launch
3. In all other `<activity>`s comment out or delete the `<intent-filter>`

## Dealing with push notifications
1. Make sure that you edit the run configuration with the Run Configuration setup in AndroidManifest steps
