GDK Demo - GDK Glassware Playground
=======

_Collection of GDK sample apps and example code._

Refer to [the official GDK doc](https://developers.google.com/glass/develop/gdk/index)
for more information on the Glass Development Kit.
This repository contains (will contain) a variety of example programs and sample code,
and other apps and libraries, which are built with GDK,
or which are otherwise relevant to Glassware development.


You can build the demo Glassware using Gradle:

    gradle clean build

You can install it on your Glass (if you have one) using `adb`.

    adb install -r build/apk/[app name]-release.apk



## Live Card Demo

This Glassware demonstrates a simple implementation of the 
[LiveCard](https://developers.google.com/glass/develop/gdk/ui/live-cards) API.

It creates a LiveCard when the app's main activity is first activated (through voice input).
The lifecycle of the LiveCard is associated with that of a background Service (`LiveCardDemoLocalService`).

It also shows how to add a menu to a LiveCard, 
which is bound to the main activity.


## Live Card Demo 2

The purpose of this example is to test 
the ["low frequency rendering" of Live Cards](https://developers.google.com/glass/develop/gdk/ui/live-cards).

It uses an Android `TimerTask` to update the live card's content every 10 seconds.

It also has slightly different behavior from the preivious version "LiveCard Demo"
in that it sets the "NonSlient" flag to true.
If you want to stop the app, tap the LiveCard screen to go back to the main Activity screen,
from which tapping one more will exist the program (after removing the live card, etc.)


## Touch Gesture Demo

This simple Glass app is based on the sample code
found in the GDK doc: [Touch Gesture](https://developers.google.com/glass/develop/gdk/input/touch).

It adds some minor changes
so that the gesture event is displayed on the screen
(either on a separrate activity or as a Toast,
based on the value of the Config variable, `USE_GESTURE_INFO_ACTIVITY`).

You can start the app through the following voice input.

_OK, Glass._ _Start Gesture Demo_


## Voice Input Demo

This demo app includes two activities: 

* `VoiceDemoActivity` - This is the main activity, and it demonstrates the voice trigger and voice prompt.
* `VoiceDictationActivity` - This activity shows how to call the system-provided Speech Recognition API via the intent type `RecognizerIntent.ACTION_RECOGNIZE_SPEECH`.  

After starting the app via the voice command, _OK, Glass._ _Start Voice Demo_, 
speak _dictate_ at the voice prompt, "next action",
to start the `VoiceDictationActivity`. It can also be activated using the TAP gesture.

Tap to start dictation.



