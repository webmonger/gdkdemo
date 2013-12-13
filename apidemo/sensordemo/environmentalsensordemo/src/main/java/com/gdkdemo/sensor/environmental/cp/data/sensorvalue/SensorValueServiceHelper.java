package com.gdkdemo.sensor.environmental.cp.data.sensorvalue;

import java.util.HashMap;

import android.content.Context;


/**
 * REST Service Helper Class.
 */
public class SensorValueServiceHelper
{
    private SensorValueServiceHelper()
    {
    }

    // Initialization-on-demand holder.
    private static final class SensorValueServiceHelperHolder
    {
        private static final SensorValueServiceHelper INSTANCE = new SensorValueServiceHelper();
    }

    // Singleton method
    public static SensorValueServiceHelper getInstance()
    {
        return SensorValueServiceHelperHolder.INSTANCE;
    }


}