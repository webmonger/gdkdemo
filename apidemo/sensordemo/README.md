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

