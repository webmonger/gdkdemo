package com.gdkdemo.sensor.environmental.cp.data.sensorvalue;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

import com.gdkdemo.sensor.environmental.cp.data.sensorvalue.SensorValueData.SensorValues;


// SensorValue content provider.
// Note that we can use ONLY ONE of SensorValueContentProvider or GdkDemoSensorDemoContentProvider.
// This is configured through Android Manifest file.
public class SensorValueContentProvider extends ContentProvider
{
    public static final String AUTHORITY = "com.gdkdemo.sensor.environmental.cp.data.sensorvalue";

    private static final int SENSORVALUES = 21;
    private static final int SENSORVALUE_ID = 22;

    private static final UriMatcher sUriMatcher;
    static {
        sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        sUriMatcher.addURI(AUTHORITY, "sensorvalues", SENSORVALUES);
        sUriMatcher.addURI(AUTHORITY, "sensorvalues/*", SENSORVALUE_ID);
    }

    private SensorValueDB mDB;
    private boolean mSyncToWeb;

    @Override
    public boolean onCreate()
    {
        mDB = new SensorValueDB(getContext());
        mSyncToWeb = false;
        return true;
    }

    @Override
    public String getType(Uri uri)
    {
        switch (sUriMatcher.match(uri)) {
        case SENSORVALUE_ID:
            return SensorValues.CONTENT_ITEM_TYPE;
        case SENSORVALUES:
            return SensorValues.CONTENT_TYPE;
        default:
            throw new IllegalArgumentException("Unknown URI " + uri);
        }
    }
    
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
            String sortOrder)
    {
        String id = null;
        Cursor c = null;
        switch (sUriMatcher.match(uri)) {
        case SENSORVALUE_ID:
            id = uri.getPathSegments().get(1);
            // fall through
        case SENSORVALUES:
            c = mDB.getSensorValues(id, projection, selection, selectionArgs, sortOrder);
            break;
        default:
            throw new IllegalArgumentException("Unknown URI " + uri);
        }

        // Tell the cursor what uri to watch, so it knows when its source data changes
        if(c != null) {
            c.setNotificationUri(getContext().getContentResolver(), uri);
        }
        return c;
    }

    @Override
    public Uri insert(Uri uri, ContentValues initialValues)
    {    	
        Long rowId = null;
        Uri projectUri = null;
        switch (sUriMatcher.match(uri)) {
        case SENSORVALUES:
            rowId = mDB.insertSensorValue(initialValues).id();
            projectUri = ContentUris.withAppendedId(SensorValues.CONTENT_URI, rowId);
            break;
        default:
            throw new IllegalArgumentException("Unknown URI " + uri);
        }
        
        if(rowId >= 0) {
            getContext().getContentResolver().notifyChange(projectUri, null);
        }
        return projectUri;
    }

    @Override
    public int update(Uri uri, ContentValues values, String where, String[] whereArgs)
    {
        String id;
        switch (sUriMatcher.match(uri)) {
        case SENSORVALUE_ID:
            id = uri.getPathSegments().get(1);
            break;
        case SENSORVALUES:
            id = null;
            break;
        default:
            throw new IllegalArgumentException("Unknown URI " + uri);
        }
        
        Integer count = mDB.updateSensorValues(id, values, where, whereArgs);
        if(count > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return count;
    }

    
    @Override
    public int delete(Uri uri, String where, String[] whereArgs)
    {
        String id;
        switch (sUriMatcher.match(uri)) {
        case SENSORVALUE_ID:
            id = uri.getPathSegments().get(1);
            break;
        case SENSORVALUES:
            id = null;
            break;
        default:
            throw new IllegalArgumentException("Unknown URI " + uri);
        }

        Integer count = mDB.deleteSensorValues(id, where, whereArgs);
        if(count > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return count;
    }

}
