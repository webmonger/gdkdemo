package com.gdkdemo.sensor.position.service;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Binder;
import android.os.IBinder;
import android.os.SystemClock;
import android.view.SurfaceHolder;

import com.gdkdemo.sensor.position.PositionSensorDemoActivity;
import com.gdkdemo.sensor.position.common.SensorValueStruct;
import com.google.android.glass.timeline.LiveCard;
import com.google.android.glass.timeline.DirectRenderingCallback;
import com.google.android.glass.timeline.TimelineManager;

import java.util.Random;


public class PositionSensorDemoLocalService extends Service implements DirectRenderingCallback, SensorEventListener
{
    // For live card
    private static final String cardId = "positionsensor_card";
    private LiveCard liveCard = null;

    // Sensor manager
    private SensorManager mSensorManager = null;

    // Position sensors
    private Sensor mSensorMagneticField = null;
    // private Sensor mSensorRotationVector = null;


    // Last known positionsensor values.
    private SensorValueStruct lastSensorValuesMagneticField = null;
    // private SensorValueStruct lastSensorValuesRotationVector = null;


    // For Surface rendering...

    private static final long FRAME_TIME_MILLIS = 33;

    private SurfaceHolder mHolder = null;
    private boolean mPaused = false;
    private RenderThread mRenderThread = null;

    private Canvas canvas = null;
    private Paint textPaint = new Paint();


    // Canvas background color. Initial value.
    // For testing rendering...
    private int bgR = 0;
    private int bgG = 0;
    private int bgB = 0;


    // No need for IPC...
    public class LocalBinder extends Binder
    {
        public PositionSensorDemoLocalService getService()
        {
            return PositionSensorDemoLocalService.this;
        }
    }
    private final IBinder mBinder = new LocalBinder();


    @Override
    public void onCreate()
    {
        super.onCreate();

        initializeSensorManager();

        // For testing rendering...
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(32);
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
        onServiceStop();
        super.onDestroy();
    }

    @Override
    public void onSensorChanged(SensorEvent event)
    {
        // Log.d("onSensorChanged() called.");

        processPositionSensorData(event);
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

        mSensorManager.registerListener(this, mSensorMagneticField, SensorManager.SENSOR_DELAY_NORMAL);
        // mSensorManager.registerListener(this, mSensorRotationVector, SensorManager.SENSOR_DELAY_NORMAL);

        // Publish live card...
        publishCard(this);

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
//             liveCard.setNonSilent(true);      // for testing, it's more convenient. Bring the card to front, at least initially.

            // Enable direct rendering.
            liveCard.setDirectRenderingEnabled(true);
            liveCard.getSurfaceHolder().addCallback(this);

            Intent intent = new Intent(context, PositionSensorDemoActivity.class);
            liveCard.setAction(PendingIntent.getActivity(context, 0, intent, 0));
            liveCard.publish(LiveCard.PublishMode.REVEAL);
        } else {
            // Card is already published.
            return;
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


    // For Surface rendering...

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height)
    {
        // Update your views accordingly.
        if(Log.D) Log.d("surfaceChanged() called with format = " + format + "; width = " + width + "; height = " + height);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder)
    {
        mHolder = holder;
        updateRendering();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder)
    {
        mHolder = null;
        updateRendering();
    }

    @Override
    public void renderingPaused(SurfaceHolder holder, boolean paused)
    {
        mPaused = paused;
        updateRendering();
    }

    /**
     * Start or stop rendering according to the timeline state.
     */
    private synchronized void updateRendering()
    {
        boolean shouldRender = (mHolder != null) && !mPaused;
        boolean rendering = mRenderThread != null;

        if (shouldRender != rendering) {
            if (shouldRender) {
                mRenderThread = new RenderThread();
                mRenderThread.start();
            } else {
                mRenderThread.quit();
                mRenderThread = null;
            }
        }
    }

    /**
     * Draws the view in the SurfaceHolder's canvas.
     */
    private void draw()
    {
        try {
            canvas = mHolder.lockCanvas();
        } catch (Exception e) {
            Log.e("Could not draw().", e);
            return;
        }
        if (canvas != null) {
            // TBD: Draw on the canvas.

            
            // Testing...
            // Just change the background color for now....

            canvas.drawRGB(bgR, bgG, bgB);
            // ....

            long now = System.currentTimeMillis();
            StringBuilder sb = new StringBuilder();
            // sb.append("Now: ").append(now).append("\n");
            sb.append("Field: ");
            if(lastSensorValuesMagneticField != null) {
                sb.append(lastSensorValuesMagneticField.toString()).append("\n");
            }
            String text = sb.toString();
            canvas.drawText(text, 10.0f, 30.0f, textPaint);
            // ...


            mHolder.unlockCanvasAndPost(canvas);
        }
    }


    /**
     * Redraws in the background.
     */
    private class RenderThread extends Thread
    {
        private boolean mShouldRun;

        /**
         * Initializes the background rendering thread.
         */
        public RenderThread()
        {
            mShouldRun = true;
        }

        /**
         * Returns true if the rendering thread should continue to run.
         *
         * @return true if the rendering thread should continue to run
         */
        private synchronized boolean shouldRun()
        {
            return mShouldRun;
        }

        /**
         * Requests that the rendering thread exit at the next opportunity.
         */
        public synchronized void quit()
        {
            mShouldRun = false;
        }

        @Override
        public void run()
        {
            while (shouldRun()) {
                PositionSensorDemoLocalService.this.draw();
                SystemClock.sleep(FRAME_TIME_MILLIS);
            }
        }
    }


    // PositionSensor methods
    private void initializeSensorManager()
    {
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensorMagneticField = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        // mSensorRotationVector = mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
    }

    private void processPositionSensorData(SensorEvent event)
    {
        long now = System.currentTimeMillis();

        Sensor sensor = event.sensor;
        int type = sensor.getType();
        long timestamp = event.timestamp;
        float[] values = event.values;
        int accuracy = event.accuracy;
        SensorValueStruct data = new SensorValueStruct(type, timestamp, values, accuracy);

        switch(type) {
            case Sensor.TYPE_MAGNETIC_FIELD:
                lastSensorValuesMagneticField = data;
                processMagneticField();
                break;
            // case Sensor.TYPE_ROTATION_VECTOR:
            //     lastSensorValuesRotationVector = data;
            //     break;
            default:
                Log.w("Unknown type: " + type);
        }

        
        // TBD:
        // Update the DB, etc..
        // ...

    }


    // temporary
    private static final float scale = 3.0f;  // arbitrary
    private void processMagneticField()
    {
        if(lastSensorValuesMagneticField != null) {
            float[] values = lastSensorValuesMagneticField.getValues();

            float mX = 0.0f;
            float mY = 0.0f;
            float mZ = 0.0f;
            if(values != null) {
                if(values != null && values.length > 0) {
                    mX = values[0];
                    if(values.length > 1) {
                        mY = values[1];
                        if(values.length > 2) {
                            mZ = values[2];
                        }
                    }
                }
            }

            int R = Math.abs( ((int) (mX * scale)) );
            if(R < 256) {
                bgR = R;
            } else {
                bgR = 255;
            }
            int G = Math.abs( ((int) (mY * scale)) );
            if(G < 256) {
                bgG = G;
            } else {
                bgG = 255;
            }
            int B = Math.abs( ((int) (mZ * scale)) );
            if(B < 256) {
                bgB = B;
            } else {
                bgB = 255;
            }

        }
    }

}
