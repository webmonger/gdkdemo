Glass Development Kit (GDK) Playground
=======

`GDK Demo` is a collection of GDK sample apps and example code.
You can quickly test new GDK API features using the sample Glassware.

Refer to [the official GDK doc](https://developers.google.com/glass/develop/gdk/index)
for more information on the Glass Development Kit.
This repository contains (will contain) a variety of example programs and sample code,
and other apps and libraries, which are built with GDK,
or which are otherwise relevant to Glassware development.


You can build the demo Glassware using Gradle:

    gradle clean build

You can install it on your Glass (if you have one) using `adb`.

    adb install -r build/apk/[app name]-release.apk


You can also import any of the Glassware projects
into an IDE such as Android Studio.


# API Demo

## GDK Live Card Demo 

The `apidemo` directory contains two Glass apps
which demonstrates a simple implementation of the 
[LiveCard](https://developers.google.com/glass/develop/gdk/ui/live-cards) API.


## GDK Touch Gesture Demo

This Glassware shows to how to implement
the [Touch Gesture](https://developers.google.com/glass/develop/gdk/input/touch) input modes.
The app can be started using the following voice input:

_OK, Glass._ _Start Gesture Demo_


## GDK Voice Input Demo

This demo Glassware uses the GDK Voice Input API.
You can start the app via Voice Trigger, _Start Voice Demo..._, followed by the phrase _dictate_.
This voice command opens a new activity, 
which includes the "dictation" function.
Tap to start dictation.



