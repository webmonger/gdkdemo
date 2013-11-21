GDK Demo
=======

Collection of GDK sample apps and example code.

Refer to [the official GDK doc](https://developers.google.com/glass/develop/gdk/index)
for more information on the Glass Development Kit.

This repository contains (will contain) example programs and sample code,
and other apps and libraries, which are built with GDK,
or which are otherwise useful for Glassware development.



## LiveCard Demo

This Glassware demonstrates a simple implementation of the 
[LiveCard](https://developers.google.com/glass/develop/gdk/ui/live-cards) API.

It creates a LiveCard when the app's main activity is first activated (through voice input).
The lifecycle of the LiveCard is associated with that of a background `Service` (.service.LiveDemoLocalService).

It also shows how to add a menu to a LiveCard, 
which is bound to the main activity.



