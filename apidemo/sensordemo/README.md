Glass Development Kit - Sensor Demos
=======

Sensors can be classfied into three categories:
Motion sensors, position sensors, and environmetnal sensors.
The "demo apps" in this directory includes sample code 
showing the simple use of these sensors on Glass.

* Android doc: [Sensors Overview](http://developer.android.com/guide/topics/sensors/sensors_overview.html).
* GDK doc: [Location and Sensors](https://developers.google.com/glass/develop/gdk/location-sensors/index).




## Motion Sensor Demo

This simple Glassware uses a "demo app framework",
which displays dynamic sensor data on a live card via "low frequency rendering".
The app "listens" to five motion sensors:
* Accelerometer
* Gravity sensor
* Linear acceleration
* Gyroscope
* Rotation vector

You can start the app through the following voice trigger.

_OK, Glass._ _Start Motion Sensor Demo_


_Blog Post:_ [Google Glass Development Kit (GDK) Sample Code - Motion Sensor Demo](http://blog.glassdiary.com/post/68954384185/google-glass-development-kit-gdk-sample-code-motion).



## Position Sensor Demo

This second GDK sensor demo app.
The app "listens" to a position sensor, namely, magnetic field sensor
via the `SensorEventListener` interface.
Unlike the the Motion Sensor Demo Glassware, this example app
uses a "high frequency live card" to _graphically_ display the magnetic field information.

You can build this sample Glassware as follows:

    cd positionsensordemo
    gradle clean build
    adb install -r build/apk/positionsensordemo-release.apk

Then, the voice command to start app is, 
_OK, Glass._ _Start Position Sensor Demo._


_Blog Post:_ [Google GDK Sample Glassware - Position Sensor Demo](http://blog.glassdiary.com/post/69728978780/google-gdk-sample-glassware-position-sensor-demo).



## Environmental Sensor Demo

This third and final sensor demo Glassware illustrates the use of the ambient light sensor on google Glass.
This sample app is different from its two previous siblings in that it uses a persistent storage
to store the sensor values.
When the sensor data is received, it is first stored in the DB through the ContentProvider interface.
It is then queried on a regular interval, using a `TimerTask`,
to display the data on the live card.

Note that all classes under the `cp` directory have been auto-generated (using old/by-now-outdated templates),
and they are not really part of the "sample code".
They are mainly used for the DB - ContentProvider implementation,
which is not the integral part of this "demo app".

Use the phrase _Start Environmental Sensor Demo_ to start the app.


_Blog Post:_ [Environmental Sensor Demo App on Google Glass using GDK](http://blog.glassdiary.com/post/69919980966/environmental-sensor-demo-app-on-google-glass-using-gdk).


