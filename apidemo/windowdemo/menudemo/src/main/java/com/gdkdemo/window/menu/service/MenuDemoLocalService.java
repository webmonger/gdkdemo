package com.gdkdemo.window.menu.service;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.widget.RemoteViews;

import com.gdkdemo.window.menu.LiveCardMenuActivity;
import com.gdkdemo.window.menu.R;
import com.google.android.glass.timeline.LiveCard;
import com.google.android.glass.timeline.TimelineManager;


// ...
public class MenuDemoLocalService extends Service
{
//    private static final int NOTIFICATION = R.string.menudemo_localservice_started;
//    private NotificationManager mNM;

    // For live card
    private LiveCard liveCard;
    private static final String cardId = "menudemo_card";


    // No need for IPC...
    public class LocalBinder extends Binder {
        public MenuDemoLocalService getService() {
            return MenuDemoLocalService.this;
        }
    }
    private final IBinder mBinder = new LocalBinder();


    public MenuDemoLocalService()
    {
        super();
    }

    @Override
    public void onCreate()
    {
        super.onCreate();
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
        onServiceStop();

        super.onDestroy();
    }



    // Service state handlers.
    // ....

    private boolean onServiceStart()
    {
        Log.d("onServiceStart() called.");

        // TBD:
        // Publish live card...
        // ....
        publishCard(this);
        // ....

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
//             liveCard.setNonSilent(true);      // for testing, it's more convenient. Bring the card to front.
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.livecard_menudemo);
            liveCard.setViews(remoteViews);
            Intent intent = new Intent(context, LiveCardMenuActivity.class);
            liveCard.setAction(PendingIntent.getActivity(context, 0, intent, 0));
//            liveCard.publish(LiveCard.PublishMode.REVEAL);
            liveCard.publish(LiveCard.PublishMode.SILENT);
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

}
