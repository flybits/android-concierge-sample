# Concierge Sample Application

## Overview

The purpose of this application is to demonstrate how the Flybits Concierge SDK can be integrated into a client application. 

## Setup Steps

* Open the concierge.xml file in the /res/xml package and add your project id as the value for the `projectId` field
* Alternatively to doing the above you can configure the concierge programmatically, a code example can be found in the class `GlobalApplication`
* Add a google-services.json file to the project's /app directory associated with the com.flybits.conciergesample application package name
* Add the firebase cloud messaging server key to your Flybits project's settings on the Flybits developer portal. 

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
