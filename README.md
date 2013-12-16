Google Glass GDK Playground
=======

`GDK Demo` is a collection of Glass Development Kit sample apps and example code.
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



## Frequently Asked Questions

* Should I fork the GDK Demo repository or just clone it?
* How do I "sync" my forked repo with GDK Demo?

Here's my thought on these questions:
[Git for GDK Glassware Developers](http://blog.glassdiary.com/post/70034111310/git-for-gdk-glassware-developers).




# API Demo

## GDK Live Card Sample Code

The [apidemo](https://github.com/harrywye/gdkdemo/tree/master/apidemo) directory contains three Glass apps
which demonstrate simple implementation of the 
[LiveCard](https://developers.google.com/glass/develop/gdk/ui/live-cards) API.
The apps, Live Card Demos 2 and 3, include sample code
for updating the LiveCard content via "low frequency rendering"
and "high freqency rendering", respectively.


## GDK Touch Gesture Sample Code

This Glassware shows to how to implement
the [Touch Gesture](https://developers.google.com/glass/develop/gdk/input/touch) input modes.
The app can be started using the following voice input:  _OK, Glass._ _Start Gesture Demo_


## GDK Voice Input Sample Code

This demo Glassware uses the GDK Voice Input API.
You can start the app via Voice Trigger, _Start Voice Demo..._, followed by the phrase _dictate_.
This voice command opens a new activity, 
which includes the "dictation" function.
Tap to start dictation.


## GDK Camera Sample Code

This GDK demo app illustrates how to use Glass camera to take pictures
and display the photos on LiveCard.


## GDK Location Sample Code

This example app uses the Android `LocationManager` API
to display the user's dynamic location on the LiveCard.



## GDK Sensor Sample Code

The [Sensor Demo directory](https://github.com/harrywye/gdkdemo/tree/master/apidemo/sensordemo)
includes a number of example apps for using Android Sensor APIs.
For more information, refer to
[the GDK doc on Sensors](https://developers.google.com/glass/develop/gdk/location-sensors/index).




# GDK Tutorials

_Coming soon..._



# Google Glass Sample Apps

_Coming soon..._





