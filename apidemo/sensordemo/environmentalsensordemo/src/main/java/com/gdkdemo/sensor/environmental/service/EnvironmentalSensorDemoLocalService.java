package com.gdkdemo.sensor.environmental.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.widget.RemoteViews;

import com.gdkdemo.sensor.environmental.EnvironmentalSensorDemoActivity;
import com.gdkdemo.sensor.environmental.R;
import com.gdkdemo.sensor.environmental.common.SensorValueStruct;
import com.gdkdemo.sensor.environmental.cp.core.GUID;
import com.gdkdemo.sensor.environmental.cp.data.sensorvalue.SensorValueData;
import com.google.android.glass.timeline.LiveCard;
import com.google.android.glass.timeline.TimelineManager;

import java.util.Timer;
import java.util.TimerTask;


// ...
public class EnvironmentalSensorDemoLocalService extends Service implements SensorEventListener
{
    // For live card
    private LiveCard liveCard = null;
    private static final String cardId = "environmentalsensordemo_card";

    // Sensor manager
    private SensorManager mSensorManager = null;

    // Environmental sensors
    private Sensor mSensorLight = null;


    // Last known environmentalsensor values.
    private SensorValueStruct lastSensorValuesLight = null;
    // private long lastRefreshedTime = 0L;  // In nano seconds ???


    // "Heart beat".
    private Timer heartBeat = null;


    // No need for IPC...
    public class LocalBinder extends Binder {
        public EnvironmentalSensorDemoLocalService getService() {
            return EnvironmentalSensorDemoLocalService.this;
        }
    }
    private final IBinder mBinder = new LocalBinder();


    @Override
    public void onCreate()
    {
        super.onCreate();
        initializeSensorManager();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        Log.i("Received start id " + startId + ": " + intent);
        onServiceStart();
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent)
    {
        // ????
        onServiceStart();
        return mBinder;
    }

    @Override
    public void onDestroy()
    {
        // ???
        if(heartBeat != null) {
            heartBeat.cancel();
            heartBeat = null;
        }
        onServiceStop();
        super.onDestroy();
    }


    @Override
    public void onSensorChanged(SensorEvent event)
    {
        Log.d("onSensorChanged() called.");

        processEnvironmentalSensorData(event);

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy)
    {
        Log.d("onAccuracyChanged() called.");

    }



    // Service state handlers.
    // ....

    private boolean onServiceStart()
    {
        Log.d("onServiceStart() called.");

        mSensorManager.registerListener(this, mSensorLight, SensorManager.SENSOR_DELAY_NORMAL);

        // Publish live card...
        publishCard(this);

        if(heartBeat == null) {
            heartBeat = new Timer();
        }
        startHeartBeat();

        return true;
    }

    private boolean onServicePause()
    {
        Log.d("onServicePause() called.");
        return true;
    }
    private boolean onServiceResume()
    {
        Log.d("onServiceResume() called.");
        return true;
    }

    private boolean onServiceStop()
    {
        Log.d("onServiceStop() called.");

        mSensorManager.unregisterListener(this);

        // TBD:
        // Unpublish livecard here
        // .....
        unpublishCard(this);
        // ...

        return true;
    }




    // For live cards...

    private void publishCard(Context context)
    {
        Log.d("publishCard() called.");
        // if (liveCard == null || !liveCard.isPublished()) {
        if (liveCard == null) {
            TimelineManager tm = TimelineManager.from(context);
            liveCard = tm.createLiveCard(cardId);
//             // liveCard.setNonSilent(false);       // Initially keep it silent ???
//             liveCard.setNonSilent(true);      // for testing, it's more convenient. Bring the card to front.
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.livecard_environmentalsensordemo);
            liveCard.setViews(remoteViews);
            Intent intent = new Intent(context, EnvironmentalSensorDemoActivity.class);
            liveCard.setAction(PendingIntent.getActivity(context, 0, intent, 0));
            liveCard.publish(LiveCard.PublishMode.REVEAL);
        } else {
            // Card is already published.
            return;
        }
    }
    // This will be called by the "HeartBeat".
    private void updateCard(Context context)
    {
        Log.d("updateCard() called.");
        // if (liveCard == null || !liveCard.isPublished()) {
        if (liveCard == null) {
            // Use the default content.
            publishCard(context);
        } else {
            // Card is already published.

//            // ????
//            // Without this (if use "republish" below),
//            // we will end up with multiple live cards....
//            liveCard.unpublish();
//            // ...


            // getLiveCard() seems to always publish a new card
            //       contrary to my expectation based on the method name (sort of a creator/factory method).
            // That means, we should not call getLiveCard() again once the card has been published.
//            TimelineManager tm = TimelineManager.from(context);
//            liveCard = tm.createLiveCard(cardId);
//            liveCard.setNonSilent(true);       // Bring it to front.
            // TBD: The reference to remoteViews can be kept in this service as well....
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.livecard_environmentalsensordemo);


            // Note:
            // This is not a "real" program.
            // ...
            // This demo app merely retrieves the 10th oldest entry in the sensor data DB
            // and compares it with the current value...
            // ...


            String content = "";
            long now = System.currentTimeMillis();
            if(lastSensorValuesLight != null) {
                long newTimestamp = lastSensorValuesLight.getTimestamp();  // ~~ now * 1000000 ...
                float[] vals = lastSensorValuesLight.getValues();
                float newSensorValue = 0.0f;
                if(vals != null && vals.length > 0) {
                    newSensorValue = vals[0];
                }
                content += "Current value: " + newSensorValue;
                content += "lx at " + newTimestamp;
            }

            String selectionClause = null;   // e.g., "type = ?";
            String[] selectionArgs = null;   // e.g., new String[]{};
            Cursor cursor = getContentResolver().query(SensorValueData.SensorValues.CONTENT_URI,
                    SensorValueQueryUtil.PROJECTION,
                    selectionClause,
                    selectionArgs,
                    // SensorValueData.SensorValues.DEFAULT_SORT_ORDER
                    "timestamp desc limit 10"   // ???
            );
            if(cursor != null && cursor.moveToFirst()) {
                cursor.moveToLast();    // The db may have less than 10 entries. but that's ok...
                long oldTimestamp = cursor.getLong(SensorValueQueryUtil.COLUMN_INDEX_TIMESTAMP);
                float oldSensorValue = cursor.getFloat(SensorValueQueryUtil.COLUMN_INDEX_VAL0);

                content += "\nIt was " + oldSensorValue;
                content += "lx at " + oldTimestamp;
            }

            remoteViews.setCharSequence(R.id.livecard_content, "setText", content);
            liveCard.setViews(remoteViews);

            // Do we need to re-publish ???
            // Unfortunately, the view does not refresh without this....
            Intent intent = new Intent(context, EnvironmentalSensorDemoActivity.class);
            liveCard.setAction(PendingIntent.getActivity(context, 0, intent, 0));
            // Is this if() necessary???? or Is it allowed/ok not to call publish() when updating????
            if(! liveCard.isPublished()) {
                liveCard.publish(LiveCard.PublishMode.REVEAL);
            } else {
                // ????
                // According to the doc,
                // it appears we should call publish() every time the content changes...
                // But, it seems to work without re-publishing...
                if(Log.D) Log.d("liveCard not published at " + now);
            }
        }
    }

    private void unpublishCard(Context context)
    {
        Log.d("unpublishCard() called.");
        if (liveCard != null) {
            liveCard.unpublish();
            liveCard = null;
        }
    }


    // EnvironmentalSensor methods
    private void initializeSensorManager()
    {
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensorLight = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
    }

    private void processEnvironmentalSensorData(SensorEvent event)
    {
        long now = System.currentTimeMillis();
        if(Log.D) Log.d("processEnvironmentalSensorData() called at " + now);

        Sensor sensor = event.sensor;
        int type = sensor.getType();
        long timestamp = event.timestamp;
        float[] values = event.values;
        int accuracy = event.accuracy;
        SensorValueStruct data = new SensorValueStruct(type, timestamp, values, accuracy);

        switch(type) {
            case Sensor.TYPE_LIGHT:
                lastSensorValuesLight = data;
                // lastRefreshedTime = timestamp;
                break;
            default:
                Log.w("Unknown type: " + type);
        }

        // TBD:
        // Update the DB, etc..
        // ...

        // temporary
        saveSensorValueEntry();
        // ...


//        // Update the UI.
//        // temporary
//        final long delta = 5000L;   // every 5 seconds
//        if(lastRefreshedTime <= now - delta) {
//            // if(liveCard != null && liveCard.isPublished()) {
//                // Update...
//                publishCard(this, true);
//            // }
//            lastRefreshedTime = now;
//        }
    }


    // testing....
    private void saveSensorValueEntry()
    {
        Log.d("saveSensorValueEntry() called.");

        if(lastSensorValuesLight != null) {
            long now = System.currentTimeMillis();
            float[] val = lastSensorValuesLight.getValues();
            if(val == null || val.length == 0) {
                // ????
                // Can this happen???
                // If this happens, there is no point of saving the entry.
                Log.e("lastSensorValuesLight has no value!");
                return;
            }

            // Save...
            ContentValues contentValues = new ContentValues();

            contentValues.put(SensorValueData.SensorValues.GUID, GUID.generate());
            contentValues.put(SensorValueData.SensorValues.TYPE, Sensor.TYPE_LIGHT);
            contentValues.put(SensorValueData.SensorValues.ACCURACY, lastSensorValuesLight.getAccuracy());
            // if(val != null && val.length > 0) {
                if(Log.D) Log.d("Sensor value = " + val[0]);
                // TBD: What is this value 0.0???
                //      When does this happen????
                //      Do not save the entry if val[0] == 0.0 ????
                if(val[0] == 0.0f) {
                    Log.w("Invalid sensor value: 0.0. Skipping this record.");
                    return;
                }
                contentValues.put(SensorValueData.SensorValues.VAL0, val[0]);
                // Light sensor value is a scalar... --> only val[0] is used...
                // The following is not really needed.
                // (Also, val.length should always, I presume, be 4, and hence the if() checks are not needed.)
                if(val.length > 1) {
                    contentValues.put(SensorValueData.SensorValues.VAL1, val[1]);
                    if(val.length > 2) {
                        contentValues.put(SensorValueData.SensorValues.VAL2, val[2]);
                        if(val.length > 3) {
                            contentValues.put(SensorValueData.SensorValues.VAL3, val[3]);
                        }
                    }
                }
            // }
            contentValues.put(SensorValueData.SensorValues.TIMESTAMP, lastSensorValuesLight.getTimestamp());
            contentValues.put(SensorValueData.SensorValues.CREATEDTIME, now);
            contentValues.put(SensorValueData.SensorValues.MODIFIEDTIME, 0L);

            Log.d("Before calling getContentResolver().insert()");
            getContentResolver().insert(SensorValueData.SensorValues.CONTENT_URI, contentValues);

        } else {
            // ???
            Log.i("lastSensorValuesLight is null!");
        }
    }


    private void startHeartBeat()
    {
        final Handler handler = new Handler();
        TimerTask liveCardUpdateTask = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        try {
                            updateCard(EnvironmentalSensorDemoLocalService.this);
                        } catch (Exception e) {
                            Log.e("Failed to run the task.", e);
                        }
                    }
                });
            }
        };
        heartBeat.schedule(liveCardUpdateTask, 0L, 5000L); // Every 5 seconds...
    }





}
