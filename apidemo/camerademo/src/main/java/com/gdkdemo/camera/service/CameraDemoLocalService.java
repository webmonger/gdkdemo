package com.gdkdemo.camera.service;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.StrictMode;
import android.widget.RemoteViews;

import com.gdkdemo.camera.CameraDemoActivity;
import com.gdkdemo.camera.R;
import com.google.android.glass.timeline.LiveCard;
import com.google.android.glass.timeline.TimelineManager;

import java.util.Timer;
import java.util.TimerTask;


// ...
public class CameraDemoLocalService extends Service
{
    // For live card
    private LiveCard liveCard = null;

    private static final String cardId = "camerademo_card";

    // "Heart beat".
    private Timer heartBeat = null;

    // Stored/caches the file path of the "last" photo taken...
    private String currentPhotoFilePath = null;
    // For "previous" photo
    private String previousPhotoFilePath = null;
    // Last updated time
    private long currentPhotoTime = 0L;


    // No need for IPC...
    public class LocalBinder extends Binder {
        public CameraDemoLocalService getService() {
            return CameraDemoLocalService.this;
        }
    }
    private final IBinder mBinder = new LocalBinder();


    @Override
    public void onCreate()
    {
        super.onCreate();

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


    // To be called by iBinder clients...
    public void setPhotoFilePath(String currentPhotoFilePath)
    {
        currentPhotoTime = System.currentTimeMillis();
        if(Log.D) Log.d("currentPhotoFilePath set to " + currentPhotoFilePath + " at " + currentPhotoTime);
        this.previousPhotoFilePath = this.currentPhotoFilePath;
        this.currentPhotoFilePath = currentPhotoFilePath;
    }


    // For live cards...

    private void publishCard(Context context)
    {
        Log.d("publishCard() called.");
        // if (liveCard == null || !liveCard.isPublished()) {
        if (liveCard == null) {
            TimelineManager tm = TimelineManager.from(context);
            liveCard = tm.getLiveCard(cardId);
            liveCard.setNonSilent(false);       // the livecard runs in the "background" only.
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.livecard_camerademo);
            liveCard.setViews(remoteViews);
            Intent intent = new Intent(context, CameraDemoActivity.class);
            liveCard.setAction(PendingIntent.getActivity(context, 0, intent, 0));
            liveCard.publish();
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
//            liveCard = tm.getLiveCard(cardId);
//            liveCard.setNonSilent(true);
            // TBD: The reference to remoteViews can be kept in this service as well....
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.livecard_camerademo);
            String content = "";

            // testing
            long now = System.currentTimeMillis();
            content = "Updated: " + now;
            content += "\nPhoto taken at " + this.currentPhotoTime;
            if(this.currentPhotoFilePath != null) {
                content += "\nPath = " + this.currentPhotoFilePath;
            }
            // ...

            remoteViews.setCharSequence(R.id.livecard_content, "setText", content);

            // TBD:
            // Set the image with the "last" photo ...
            // TBD:
            // The photo in the given file path may not have been saved.
            // We will need to use FileObserver or something similar/asynchronous...
            // ...

            if(this.currentPhotoFilePath != null) {
                try {
                    // temporary
                    StrictMode.ThreadPolicy old = StrictMode.getThreadPolicy();
                    StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder(old).permitAll().build());
                    // temporary

                    // ????
                    // What the ????
                    String filePath = this.currentPhotoFilePath;
                    if(this.currentPhotoFilePath.startsWith("/mnt")) {
                        filePath = this.currentPhotoFilePath.substring("/mnt".length());
                    }
                    Uri photoUri = Uri.parse(filePath);   // ???
                    remoteViews.setImageViewUri(R.id.livecard_image, photoUri);

                    // temporary
                    // StrictMode.setThreadPolicy(old);
                    // temporary
                } catch(Exception e) {
                    Log.e("Failed to set the remote imageView.", e);
                }
            } else {
                Log.i("currentPhotoFilePath is not set.");
            }


            liveCard.setViews(remoteViews);

            // Do we need to re-publish ???
            // Unfortunately, the view does not refresh without this....
            Intent intent = new Intent(context, CameraDemoActivity.class);
            liveCard.setAction(PendingIntent.getActivity(context, 0, intent, 0));
            // Is this if() necessary???? or Is it allowed/ok not to call publish() when updating????
            if(! liveCard.isPublished()) {
                liveCard.publish();
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
                            updateCard(CameraDemoLocalService.this);
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
