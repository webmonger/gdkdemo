package com.gdkdemo.sensor.environmental.cp.data.sensorvalue;

import java.util.Map;
import java.util.HashMap;

import android.content.Context;

import com.gdkdemo.sensor.environmental.cp.data.sensorvalue.SensorValueData.SensorValues;


/**
 * Query Helper Class.
 */
public class SensorValueQueryHelper
{
    // Default projection.
    public static final String[] DEFAULT_PROJECTION = new String[] {
        SensorValues._ID,                         // 0
        SensorValues.GUID,                        // 1
        SensorValues.TYPE,                        // 2
        SensorValues.ACCURACY,                    // 3
        SensorValues.VAL0,                        // 4
        SensorValues.VAL1,                        // 5
        SensorValues.VAL2,                        // 6
        SensorValues.VAL3,                        // 7
        SensorValues.TIMESTAMP,                   // 8
        SensorValues.CREATEDTIME,                 // 9
        SensorValues.MODIFIEDTIME,                // 10
        SensorValues._REST_STATE,                 // 11
        SensorValues._REST_RESULT,                // 12
        SensorValues._REST_CURRENT_ACTION,        // 13
        SensorValues._REST_REQUEST_ID,            // 14
        SensorValues._REST_REFRESHED_TIME,        // 15
        SensorValues._REST_PURGE_TIME,            // 16
    };

    // Not currently being used...
    public static final int COLUMN_INDEX_ID = 0;
    public static final int COLUMN_INDEX_GUID = 1;
    public static final int COLUMN_INDEX_TYPE = 2;
    public static final int COLUMN_INDEX_ACCURACY = 3;
    public static final int COLUMN_INDEX_VAL0 = 4;
    public static final int COLUMN_INDEX_VAL1 = 5;
    public static final int COLUMN_INDEX_VAL2 = 6;
    public static final int COLUMN_INDEX_VAL3 = 7;
    public static final int COLUMN_INDEX_TIMESTAMP = 8;
    public static final int COLUMN_INDEX_CREATEDTIME = 9;
    public static final int COLUMN_INDEX_MODIFIEDTIME = 10;
    public static final int COLUMN_INDEX_REST_STATE = 11;
    public static final int COLUMN_INDEX_REST_RESULT = 12;
    public static final int COLUMN_INDEX_REST_CURRENT_ACTION = 13;
    public static final int COLUMN_INDEX_REST_REQUEST_ID = 14;
    public static final int COLUMN_INDEX_REST_REFRESHED_TIME = 15;
    public static final int COLUMN_INDEX_REST_PURGE_TIME = 16;


    // "Cache"
    // Map of mask -> projection
    // TBD: "Canonicalize" the mask (e.g., remove all trailing zeros, etc.)...
    private final Map<int[], String[]> mProjectionMap;


    private SensorValueQueryHelper()
    {
        mProjectionMap = new HashMap<int[], String[]>();
    }

    // Initialization-on-demand holder.
    private static final class SensorValueQueryHelperHolder
    {
        private static final SensorValueQueryHelper INSTANCE = new SensorValueQueryHelper();
    }

    // Singleton method
    public static SensorValueQueryHelper getInstance()
    {
        return SensorValueQueryHelperHolder.INSTANCE;
    }


    public String[] getProjection(int[] mask)
    {
        if(mask == null) {
           return null;
        }
        if(mProjectionMap.containsKey(mask)) {
            return mProjectionMap.get(mask);
        }
        String[] projection = new String[mask.length];
        int idx = 0;
        int limit = (mask.length <= DEFAULT_PROJECTION.length) ? mask.length : DEFAULT_PROJECTION.length;
        for(int i=0; i<limit; i++) {
            if(mask[i] > 0) {
               projection[idx++] = DEFAULT_PROJECTION[i];
            }
        }
        mProjectionMap.put(mask, projection);  
        return projection;
    }


}
