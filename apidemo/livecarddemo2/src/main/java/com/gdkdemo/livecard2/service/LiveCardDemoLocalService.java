package com.gdkdemo.livecard2.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.widget.RemoteViews;

import com.gdkdemo.livecard2.LiveCardDemoActivity;
import com.gdkdemo.livecard2.R;
import com.google.android.glass.timeline.LiveCard;
import com.google.android.glass.timeline.TimelineManager;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


// ...
public class LiveCardDemoLocalService extends Service
{
    // "Life cycle" constants
    // Currently not being used...

    // [1] Starts from this..
    private static final int STATE_NORMAL = 1;

    // [2] When panic action has been triggered by the user.
    private static final int STATE_PANIC_TRIGGERED = 2;

    // [3] Note that cancel, or successful send, etc. change the state back to normal
    // These are intermediate states...
    private static final int STATE_CANCEL_REQUESTED = 4;
    private static final int STATE_CANCEL_PROCESSED = 8;
    private static final int STATE_PANIC_PROCESSED = 16;
    // ....

    // Global "state" of the service.
    private int currentState;


    // For live card
    private LiveCard liveCard = null;

    private static final String cardId = "livecarddemo2_card";

    // "Heart beat".
    private Timer heartBeat = null;


    // No need for IPC...
    public class LocalBinder extends Binder {
        public LiveCardDemoLocalService getService() {
            return LiveCardDemoLocalService.this;
        }
    }
    private final IBinder mBinder = new LocalBinder();


    @Override
    public void onCreate()
    {
        super.onCreate();
        currentState = STATE_NORMAL;

        IntentFilter filter = new IntentFilter();
        filter.addAction("android.provider.xxx");   // TBD:..

//        if(heartBeat == null) {
//            heartBeat = new Timer();
//        }
//        startHeartBeat();
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



    // Service state handlers.
    // ....

    private boolean onServiceStart()
    {
        Log.d("onServiceStart() called.");

        // Publish live card...
        publishCard(this);
        if(heartBeat == null) {
            heartBeat = new Timer();
        }
        startHeartBeat();

        currentState = STATE_NORMAL;
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

        // Stop the heart beat.
        // ???
        // onServiceStop() is called when the service is destroyed.... ??? Need to check
        if(heartBeat != null) {
            heartBeat.cancel();
        }
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
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.livecard_livecarddemo2);
            liveCard.setViews(remoteViews);
            Intent intent = new Intent(context, LiveCardDemoActivity.class);
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
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.livecard_livecarddemo2);
            String content = "";

            // testing
            long now = System.currentTimeMillis();
            content = "Updated: " + now;
            // ...

            remoteViews.setCharSequence(R.id.livecard_content, "setText", content);
            liveCard.setViews(remoteViews);

            // Do we need to re-publish ???
            // Unfortunately, the view does not refresh without this....
            Intent intent = new Intent(context, LiveCardDemoActivity.class);
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


    private void startHeartBeat()
    {
        final Handler handler = new Handler();
        TimerTask liveCardUpdateTask = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        try {
                            updateCard(LiveCardDemoLocalService.this);
                        } catch (Exception e) {
                            Log.e("Failed to run the task.", e);
                        }
                    }
                });
            }
        };
        heartBeat.schedule(liveCardUpdateTask, 0L, 10000L); // Every 10 seconds...
    }



}
