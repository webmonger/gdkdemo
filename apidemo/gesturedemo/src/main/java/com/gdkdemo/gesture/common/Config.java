package com.gdkdemo.gesture.common;


public final class Config
{
    private Config() {}

    // Log level.
    public static int LOGLEVEL = android.util.Log.VERBOSE;
    //static final int LOGLEVEL = android.util.Log.DEBUG;
    //static final int LOGLEVEL = android.util.Log.INFO;

    // Whether to use/open a new activity to show the gesture type.
    // If false, the gesture type is logged only.
    public static boolean USE_GESTURE_INFO_ACTIVITY = true;

}
