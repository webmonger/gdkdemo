package com.gdkdemo.sensor.environmental.cp.core;

import android.os.SystemClock;


final class Log 
{
    static final String LOGTAG = "GdkDemoSensorDemo.cp.core";
    private static final int LOGLEVEL = Config.LOGLEVEL;

    static final boolean V = LOGLEVEL <= android.util.Log.VERBOSE;
    static final boolean D = LOGLEVEL <= android.util.Log.DEBUG;
    static final boolean I = LOGLEVEL <= android.util.Log.INFO;
    static final boolean W = LOGLEVEL <= android.util.Log.WARN;
    static final boolean E = LOGLEVEL <= android.util.Log.ERROR;

    static void v(String logMe) 
    {
        android.util.Log.v(LOGTAG, SystemClock.uptimeMillis() + " " + logMe);
    }
    static void v(String logMe, Throwable ex)
    {
        android.util.Log.v(LOGTAG, SystemClock.uptimeMillis() + " " + logMe, ex);
    }

    static void d(String logMe) 
    {
        android.util.Log.d(LOGTAG, SystemClock.uptimeMillis() + " " + logMe);
    }
    static void d(String logMe, Throwable ex)
    {
        android.util.Log.d(LOGTAG, SystemClock.uptimeMillis() + " " + logMe, ex);
    }

    static void i(String logMe)
    {
        android.util.Log.i(LOGTAG, logMe);
    }
    static void i(String logMe, Throwable ex)
    {
        android.util.Log.i(LOGTAG, logMe, ex);
    }

    static void w(String logMe)
    {
        android.util.Log.w(LOGTAG, logMe);
    }
    static void w(String logMe, Throwable ex)
    {
        android.util.Log.w(LOGTAG, logMe, ex);
    }

    static void e(String logMe)
    {
        android.util.Log.e(LOGTAG, logMe);
    }
    static void e(String logMe, Exception ex)
    {
        android.util.Log.e(LOGTAG, logMe, ex);
    }
}
