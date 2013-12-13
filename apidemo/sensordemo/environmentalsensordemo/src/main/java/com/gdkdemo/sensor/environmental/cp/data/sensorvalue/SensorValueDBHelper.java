package com.gdkdemo.sensor.environmental.cp.data.sensorvalue;

import java.util.HashMap;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.gdkdemo.sensor.environmental.cp.data.sensorvalue.SensorValueData.SensorValues;


/**
 * This class helps open, create, and upgrade the database file.
 */
public class SensorValueDBHelper extends SQLiteOpenHelper
{
    private static final String DATABASE_NAME = "gdkdemosensordemo.sensorvalue";
    private static final int DATABASE_VERSION = 1;
    public static final String SENSORVALUE_TABLE_NAME = "sensorvalues";
    
    public static HashMap<String, String> PMAP_SENSORVALUES;
    static {
        PMAP_SENSORVALUES = new HashMap<String, String>();
        PMAP_SENSORVALUES.put(SensorValues._ID, SensorValues._ID);

        PMAP_SENSORVALUES.put(SensorValues.GUID, SensorValues.GUID);
        PMAP_SENSORVALUES.put(SensorValues.TYPE, SensorValues.TYPE);
        PMAP_SENSORVALUES.put(SensorValues.ACCURACY, SensorValues.ACCURACY);
        PMAP_SENSORVALUES.put(SensorValues.VAL0, SensorValues.VAL0);
        PMAP_SENSORVALUES.put(SensorValues.VAL1, SensorValues.VAL1);
        PMAP_SENSORVALUES.put(SensorValues.VAL2, SensorValues.VAL2);
        PMAP_SENSORVALUES.put(SensorValues.VAL3, SensorValues.VAL3);
        PMAP_SENSORVALUES.put(SensorValues.TIMESTAMP, SensorValues.TIMESTAMP);
        PMAP_SENSORVALUES.put(SensorValues.CREATEDTIME, SensorValues.CREATEDTIME);
        PMAP_SENSORVALUES.put(SensorValues.MODIFIEDTIME, SensorValues.MODIFIEDTIME);

        PMAP_SENSORVALUES.put(SensorValues._REST_STATE, SensorValues._REST_STATE);
        PMAP_SENSORVALUES.put(SensorValues._REST_RESULT, SensorValues._REST_RESULT);
        PMAP_SENSORVALUES.put(SensorValues._REST_CURRENT_ACTION, SensorValues._REST_CURRENT_ACTION);
        PMAP_SENSORVALUES.put(SensorValues._REST_REQUEST_ID, SensorValues._REST_REQUEST_ID);
        PMAP_SENSORVALUES.put(SensorValues._REST_REFRESHED_TIME, SensorValues._REST_REFRESHED_TIME);
        PMAP_SENSORVALUES.put(SensorValues._REST_PURGE_TIME, SensorValues._REST_PURGE_TIME);
    }

    
    public SensorValueDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + SENSORVALUE_TABLE_NAME + " ("
                + SensorValues._ID + " INTEGER PRIMARY KEY,"
                + SensorValues.GUID + " TEXT,"
                + SensorValues.TYPE + " INTEGER,"
                + SensorValues.ACCURACY + " INTEGER,"
                + SensorValues.VAL0 + " REAL,"
                + SensorValues.VAL1 + " REAL,"
                + SensorValues.VAL2 + " REAL,"
                + SensorValues.VAL3 + " REAL,"
                + SensorValues.TIMESTAMP + " INTEGER,"
                + SensorValues.CREATEDTIME + " INTEGER,"
                + SensorValues.MODIFIEDTIME + " INTEGER,"
                + SensorValues._REST_STATE + " INTEGER,"
                + SensorValues._REST_RESULT + " TEXT,"
                + SensorValues._REST_CURRENT_ACTION + " TEXT,"
                + SensorValues._REST_REQUEST_ID + " TEXT,"
                + SensorValues._REST_REFRESHED_TIME + " INTEGER,"
                + SensorValues._REST_PURGE_TIME + " INTEGER,"
                + ");");
        db.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS idx_guid ON " + SENSORVALUE_TABLE_NAME + " ("
        		+ SensorValues.GUID
        		+ ")");

        // ...
        uploadFixtureData(db);
    }
    
    private void uploadFixtureData(SQLiteDatabase db) {        	
        // TBD:
        // Insert fixture data....
        // ...
        // TBD: predefined sensorValues???
        //      default project, 
        //      google tasks,
        //      slide project,
        //      ....
    }
    
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    	// TBD: Data migration...
        if(Log.W) Log.w("Upgrading database from version " + oldVersion + " to "
                + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + SENSORVALUE_TABLE_NAME);
        onCreate(db);
    }
}