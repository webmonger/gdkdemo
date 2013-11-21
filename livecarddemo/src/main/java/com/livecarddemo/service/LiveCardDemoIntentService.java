package com.livecarddemo.service;

import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.livecarddemo.LiveCardDemoActivity;
import com.livecarddemo.R;
import com.livecarddemo.common.LiveCardDemoConstants;
import com.google.android.glass.timeline.LiveCard;
import com.google.android.glass.timeline.TimelineManager;


// ...
public class LiveCardDemoIntentService extends IntentService
{
    private static final String SERVICE_NAME = "LiveCardDemoIntentService";


    // "Life cycle" constants

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
    private LiveCard liveCard;


    public LiveCardDemoIntentService()
    {
        this(SERVICE_NAME);
    }
    public LiveCardDemoIntentService(String name)
    {
        super(name);
        currentState = STATE_NORMAL;
    }

    @Override
    protected void onHandleIntent(Intent intent)
    {
        String action = intent.getStringExtra(LiveCardDemoConstants.EXTRA_KEY_ACTION);
        if(action == null) {
            Log.i("Extra params for key action is not found in the intent.");
            // return;
            // --> When we start a service, no extra bundle is needed...
        }


        // TBD:
        // Check the current State before processing action events... ???
        // ...

        // 1. We only use GUARD_SERVICE_START, at this point, for service lifecycle related eventa.
        // 2. Note that action_panic is not used for this service. Only action cancel is used...
        if(action == null || action.equals(LiveCardDemoConstants.GUARD_SERVICE_START)) {
            if(Log.D) Log.d("Service: start");
            onServiceStart();
        } else if(action.equals(LiveCardDemoConstants.GUARD_SERVICE_PAUSE)) {
            if(Log.D) Log.d("Service: pause");
            onServicePause();
        } else if(action.equals(LiveCardDemoConstants.GUARD_SERVICE_RESUME)) {
            if(Log.D) Log.d("Service: resume");
            onServiceResume();
        } else if(action.equals(LiveCardDemoConstants.GUARD_SERVICE_STOP)) {
            if(Log.D) Log.d("Service: stop");
            onServiceStop();
        } else if(action.equals(LiveCardDemoConstants.GUARD_ACTION_PANIC)) {
            if(Log.D) Log.d("Action: panic");
            handleActionPanic();
            postProcessing();
        } else if(action.equals(LiveCardDemoConstants.GUARD_ACTION_CANCEL)) {
            if(Log.D) Log.d("Action: cancel");
            handleActionCancel();
            postProcessing();
        } else {
            Log.w("Extra param is invalid: " + action);
            return;
        }

        // ???
//        postProcessing();
    }



    // IntetntService lifetime methods
    // ....

//    @Override
//    public void onStart(Intent intent, int startId)
//    {
//        super.onStart(intent, startId);
//
//        onServiceStart();
//    }
//
//    @Override
//    public int onStartCommand(Intent intent, int flags, int startId)
//    {
//        int res =  super.onStartCommand(intent, flags, startId);
//
//        // ???
//        onServiceStart();
//
//        return res;
//    }

    @Override
    public void onDestroy()
    {
        // ???
        // onServiceStop();

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

        return true;
    }



    // User action handlers
    // ...

    private boolean handleActionPanic()
    {
        Log.d("handleActionPanic() called.");
        currentState = STATE_PANIC_TRIGGERED;



        currentState = STATE_PANIC_PROCESSED;
        return true;
    }

    private boolean handleActionCancel()
    {
        Log.d("handleActionCancel() called.");
        currentState = STATE_CANCEL_REQUESTED;



        currentState = STATE_CANCEL_PROCESSED;
        return true;
    }


    // ????
    private void postProcessing()
    {
//        currentState = STATE_NORMAL;
    }





    // For live cards...

    private void publishCard(Context context)
    {
        Log.d("publishCard() called.");
        if (liveCard == null) {
            String cardId = "my_card";
            TimelineManager tm = TimelineManager.from(context);
            liveCard = tm.getLiveCard(cardId);

            liveCard.setViews(new RemoteViews(context.getPackageName(),
                    R.layout.livecard_livecarddemo));
            Intent intent = new Intent(context, LiveCardDemoActivity.class);
            liveCard.setAction(PendingIntent.getActivity(context, 0, intent, 0));
            liveCard.publish();
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
