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
