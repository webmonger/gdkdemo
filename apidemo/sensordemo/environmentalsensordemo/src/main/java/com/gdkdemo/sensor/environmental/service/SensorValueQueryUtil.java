package com.gdkdemo.sensor.environmental.service;

import com.gdkdemo.sensor.environmental.cp.data.sensorvalue.SensorValueData.SensorValues;


public final class SensorValueQueryUtil
{

    public static final String[] PROJECTION = new String[] {
            SensorValues._ID,             // 0
            SensorValues.GUID,       // 1
            SensorValues.TYPE,       // 2
            SensorValues.ACCURACY,       // 3
            SensorValues.VAL0,       // 4
            SensorValues.VAL1,       // 5
            SensorValues.VAL2,       // 6
            SensorValues.VAL3,       // 7
            SensorValues.TIMESTAMP,       // 8
            SensorValues.CREATEDTIME,       // 9
            SensorValues.MODIFIEDTIME,       // 10
    };

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




}
