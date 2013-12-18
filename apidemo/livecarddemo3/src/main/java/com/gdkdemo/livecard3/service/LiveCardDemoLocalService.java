package com.gdkdemo.livecard3.service;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Binder;
import android.os.IBinder;
import android.os.SystemClock;
import android.view.SurfaceHolder;

import com.gdkdemo.livecard3.LiveCardDemoActivity;
import com.google.android.glass.timeline.LiveCard;
import com.google.android.glass.timeline.DirectRenderingCallback;
import com.google.android.glass.timeline.TimelineManager;

import java.util.Random;


// Sample code based on the GDK doc:
//    https://developers.google.com/glass/develop/gdk/ui/live-cards
public class LiveCardDemoLocalService extends Service implements DirectRenderingCallback
{
    // For live card
    private static final String cardId = "livecarddemo3_card";
    private LiveCard liveCard = null;

    // For Surface rendering...

    private static final long FRAME_TIME_MILLIS = 33;

    private SurfaceHolder mHolder = null;
    private boolean mPaused = false;
    private RenderThread mRenderThread = null;

    private Canvas canvas = null;
    private Paint textPaint = new Paint();


    // Random number generator
    private static Random sRandom = new Random(System.currentTimeMillis());
    // ...

    // Canvas background color. Initial value.
    // For testing rendering...
    private int bgR = sRandom.nextInt(256);
    private int bgG = sRandom.nextInt(256);
    private int bgB = sRandom.nextInt(256);

    // temporary
    private static final int colorDelta = 5;   // range: [-delta, +delta]. delta > 0.
    private static int changeRGBComponent(int currentComponent)
    {
        int r = sRandom.nextInt(colorDelta * 2 + 1) - colorDelta;
        currentComponent += r;

        // (1) When the color component passes 255, there is "discontinuous" jump,
        //    which is a bit disconcerting...
        // currentComponent %= 256;
        // (2) So, instead of using modulo, just "truncate" the number.
//        if(currentComponent < 0) {
//            currentComponent = 0;
//        } else if (currentComponent > 255) {
//            currentComponent = 255;
//        }
        // (3) Or, we can actively "repulse" the number from the boundary...
        if(currentComponent < colorDelta) {
            currentComponent = Math.max(0, currentComponent) + sRandom.nextInt(colorDelta * 2);
        } else if (currentComponent > (255 - colorDelta)) {
            currentComponent = Math.min(255, currentComponent) - sRandom.nextInt(colorDelta * 2);
        }

        return currentComponent;
    }
    private void randomlyChangeRGB()
    {
        bgR = changeRGBComponent(bgR);
        bgG = changeRGBComponent(bgG);
        bgB = changeRGBComponent(bgB);
    }



    // No need for IPC...
    public class LocalBinder extends Binder
    {
        public LiveCardDemoLocalService getService()
        {
            return LiveCardDemoLocalService.this;
        }
    }
    private final IBinder mBinder = new LocalBinder();


    @Override
    public void onCreate()
    {
        super.onCreate();

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



    // Service state handlers.
    // ....

    private boolean onServiceStart()
    {
        Log.d("onServiceStart() called.");

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

            Intent intent = new Intent(context, LiveCardDemoActivity.class);
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

            randomlyChangeRGB();
            canvas.drawRGB(bgR, bgG, bgB);
            // ....

            long now = System.currentTimeMillis();
            StringBuilder sb = new StringBuilder();
            sb.append("Now: ").append(now).append(" ");
            sb.append("Color: ");
            sb.append(String.format("%03d", bgR)).append("-");
            sb.append(String.format("%03d", bgG)).append("-");
            sb.append(String.format("%03d", bgB));
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
                LiveCardDemoLocalService.this.draw();
                SystemClock.sleep(FRAME_TIME_MILLIS);
            }
        }
    }

}
