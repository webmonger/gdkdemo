GDK Demo
=======

Collection of GDK sample apps and example code.

Refer to [the official GDK doc](https://developers.google.com/glass/develop/gdk/index)
for more information on the Glass Development Kit.

This repository contains (will contain) example programs and sample code,
and other apps and libraries, which are built with GDK,
or which are otherwise useful for Glassware development.


You can build the demo Glassware using Gradle:

    gradle clean build

You can install it on your Glass (if you have one) using `adb`.

    adb install -r build/apk/[app name]-release.apk



## LiveCard Demo

This Glassware demonstrates a simple implementation of the 
[LiveCard](https://developers.google.com/glass/develop/gdk/ui/live-cards) API.

It creates a LiveCard when the app's main activity is first activated (through voice input).
The lifecycle of the LiveCard is associated with that of a background `Service` (.service.LiveDemoLocalService).

It also shows how to add a menu to a LiveCard, 
which is bound to the main activity.


## Gesture Demo

This simple Glass app is based on the sample code
found in the GDK doc: [Touch Gesture](https://developers.google.com/glass/develop/gdk/input/touch).

It adds some minor changes
so that the gesture event is displayed on the screen
(either on a separrate activity or as a Toast,
based on the value of the Config variable, `USE_GESTURE_INFO_ACTIVITY`).

You can start the app through the following voice input.

_OK, Glass._ _Start Gesture Demo_





