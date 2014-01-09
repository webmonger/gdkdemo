Glass Development Kit API Demo for Xamarin
=======

Work in progress converting the excelent Java Google Glass Demos built by [Harry Y](https://github.com/harrywye) to work in C# and Xamarin.

Harry's code is well documented but he also has a [series of blog posts](http://blog.glassdiary.com/tagged/googleglass) you might find helpful.

~Jon, 9/1/14

<!--## Live Card Demo

This Glassware demonstrates a simple implementation of the 
[LiveCard](https://developers.google.com/glass/develop/gdk/ui/live-cards) API.

It creates a LiveCard when the app's main activity is first activated (through voice input).
The lifecycle of the LiveCard is associated with that of a background Service (`LiveCardDemoLocalService`).
It also shows how to add a menu to a LiveCard, 
which is bound to the main activity.
(Cf. [Live Card Menu Demo](https://github.com/harrywye/gdkdemo/tree/master/apidemo/windowdemo).)

_Blog Post:_ [GDK LiveCard Sample Code](http://blog.glassdiary.com/post/67643976351/gdk-livecard-sample-code).



## Live Card Demo 2

The purpose of this example is to test 
the ["low frequency rendering" of Live Cards](https://developers.google.com/glass/develop/gdk/ui/live-cards).

It uses an Android `TimerTask` to update the live card's content every 15 seconds.

It also has slightly different behavior from the preivious version "LiveCard Demo"
in that it sets the "NonSlient" flag to true.
If you want to stop the app, tap the LiveCard screen to go back to the main Activity screen,
from which tapping one more will exit the program (after removing the live card, etc.)

_Blog Post:_ [Google GDK Playground: Live Card Example 2 - Low Frequency Rendering](http://blog.glassdiary.com/post/68019125742/google-gdk-playground-live-card-example-2-low).


## Live Card Demo 3

This demo app includes
sample code for the ["high frequency Live Cards"](https://developers.google.com/glass/develop/gdk/ui/live-cards).
It uses an Android local service which
implements the [LiveCardCallback](https://developers.google.com/glass/develop/gdk/reference/com/google/android/glass/timeline/LiveCardCallback) interface 
to draw on the card's canvas.

The sample app merely draws the solid bacground with a random/time-changing color.

_Blog Post:_ [Google Glass GDK Live Card Surface Rendering Example](http://blog.glassdiary.com/post/69539795521/google-glass-gdk-live-card-surface-rendering-example).



## Touch Gesture Demo

This simple Glass app is based on the sample code
found in the GDK doc: [Touch Gesture](https://developers.google.com/glass/develop/gdk/input/touch).

It adds some minor changes
so that the gesture event is displayed on the screen
(either on a separrate activity or as a Toast,
based on the value of the Config variable, `USE_GESTURE_INFO_ACTIVITY`).

You can start the app through the following voice input.

_OK, Glass._ _Start Gesture Demo_


_Blog Post:_ [Glass Development Kit Sample Code - Touch Gesture Detector](http://blog.glassdiary.com/post/67789851142/glass-development-kit-sample-code-touch-gesture).


## Voice Input Demo

This demo app includes two activities: 

* `VoiceDemoActivity` - This is the main activity, and it demonstrates the voice trigger and voice prompt.
* `VoiceDictationActivity` - This activity shows how to call the system-provided Speech Recognition API via the intent type `RecognizerIntent.ACTION_RECOGNIZE_SPEECH`.  

After starting the app via the voice command, _OK, Glass._ _Start Voice Demo_, 
speak _dictate_ at the voice prompt, "next action",
to start the `VoiceDictationActivity`. It can also be activated using the TAP gesture.

Tap to start dictation.

_Blog Post:_ [Google Glass GDK Sample Code - Voice Input Demo Application](http://blog.glassdiary.com/post/67878988264/google-glass-gdk-sample-code-voice-input-demo).



## Voice Input Demo 2

This is a simple extension of the first Voice Input Demo app.
It includes two voice trigger-enabled activities
to demonstrate the use of multiple voice trigger commands.

_Blog Post:_ [GDK Voice Trigger Sample App](http://blog.glassdiary.com/post/70365887264/gdk-voice-trigger-sample-app).



## Camera Demo

This first demo app using Google Glass Camera
relies on the stock Camera activity to take photos.

The path of the photo/image file (JPG) is passed to the calling activity, `CameraDemoActivity`, through the extra param, `Camera.EXTRA_PICTURE_FILE_PATH`. 
The image file at the given path is then polled to check if the file is ready.
If so, it is converted to a bitmatp and it is displyed in the `ImageView` of the livecard.

    Bitmap bitmap1 = BitmapFactory.decodeFile(filePath);
    remoteViews.setImageViewBitmap(R.id.livecard_image, bitmap1);

_Blog Posts:_ 

* [Google Glass GDK Camera API Sample](http://blog.glassdiary.com/post/69155251863/google-glass-gdk-camera-api-sample).
* [GDK Camera Demo: How to Display Dynamic Image Content on Live Card Using RemoteViews API](http://blog.glassdiary.com/post/69322026138/gdk-camera-demo-how-to-display-dynamic-image-content).



## Location API Demo

This demo Glassware uses the Android `LocationManager` API
to display the user's location on the LiveCard.
The location is dynamically updated based on the data supplied by a number of location providers
(including the "remote" location provider, if available).

A simple (toy) algorithm is used to get the "best" location information at any given time.

_Blog Post:_ [Glassware GDK Code Example - Location API Demo](http://blog.glassdiary.com/post/68508701710/glassware-gdk-code-example-location-api-demo).



## Sensor API Demo

A number of sample Glassware illustrating
the use of [Android Sensor API](http://developer.android.com/guide/topics/sensors/sensors_overview.html)
are included in the [Sensor Demo directory](https://github.com/harrywye/gdkdemo/tree/master/apidemo/sensordemo).


## Timeline Demo

The GDK examples in this [Timeline Demo direcotry](https://github.com/harrywye/gdkdemo/tree/master/apidemo/timelinedemo) are mostely related to `TimelineManager` or other classes such as `Card` which are relevant to timeline. 



## Window - UI Demo

Sample Glass apps illustrating
the use of general Window - UI related features
are included in the [Window Demo directory](https://github.com/harrywye/gdkdemo/tree/master/apidemo/windowdemo).

-->