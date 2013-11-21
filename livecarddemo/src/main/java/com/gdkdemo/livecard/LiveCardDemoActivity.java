package com.gdkdemo.livecard;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.speech.RecognizerIntent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;

import com.gdkdemo.livecard.service.LiveCardDemoLocalService;
import com.gdkdemo.livecard.R;
import com.google.android.glass.touchpad.Gesture;
import com.google.android.glass.touchpad.GestureDetector;

import java.util.ArrayList;


// TBD:
// Add context menu
//
// ...


// The "main" activity...
public class LiveCardDemoActivity extends Activity
{
    // For tap events
    private GestureDetector mGestureDetector;

    // Service to handle liveCard publishing, etc...
    private boolean mIsBound = false;
    private LiveCardDemoLocalService glassGuardLocalService;
    private ServiceConnection serviceConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            Log.d("onServiceConnected() called.");
            glassGuardLocalService = ((LiveCardDemoLocalService.LocalBinder)service).getService();
        }
        public void onServiceDisconnected(ComponentName className) {
            Log.d("onServiceDisconnected() called.");
            glassGuardLocalService = null;
        }
    };
    private void doBindService()
    {
        bindService(new Intent(this, LiveCardDemoLocalService.class), serviceConnection, Context.BIND_AUTO_CREATE);
        mIsBound = true;
    }
    private void doUnbindService() {
        if (mIsBound) {
            unbindService(serviceConnection);
            mIsBound = false;
        }
    }
    private void doStartService()
    {
        startService(new Intent(this, LiveCardDemoLocalService.class));
    }
    private void doStopService()
    {
        stopService(new Intent(this, LiveCardDemoLocalService.class));
    }


    @Override
    protected void onDestroy()
    {
        doUnbindService();
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Log.d("onCreate() called.");

        setContentView(R.layout.activity_livecarddemo);

        // For gesture handling.
        mGestureDetector = createGestureDetector(this);


        // TBD:
        // Create an asyncTask for publishing liveCard...
        // ????

        // Or,
        // Start LiveCardDemoIntentService and
        // delegate the liveCard publishing to LiveCardDemoIntentService.
        // ...

        // (Short-lived) IntentService does not work for us...
//        Intent ggIntent = new Intent(this, LiveCardDemoIntentService.class);
//        // trigger liveCard publishing ???   --> No, this is done in IntentService.onStart()...
//        ggIntent.putExtra(LiveCardDemoConstants.EXTRA_KEY_ACTION, LiveCardDemoConstants.GUARD_SERVICE_START);
//        this.startService(ggIntent);

        // We need a real service.
        // bind does not work. We need to call start() explicitly...
        // doBindService();
        doStartService();
        // TBD: We need to call doStopService() when user "closes" the app....
        // ...

    }


    @Override
    protected void onResume()
    {
        super.onResume();
        Log.d("onResume() called.");

        Bundle extras = getIntent().getExtras();
        ArrayList<String> voiceResults = null;
        if(extras != null) {
            voiceResults = extras.getStringArrayList(RecognizerIntent.EXTRA_RESULTS);
            // ...
        }

        // tbd...
        // ...

        // For live card menu handling
        openOptionsMenu();

    }


//    @Override
//    public int onStartCommand(Intent intent, int flags, int startId)
//    {
//        ArrayList<String> voiceResults = intent.getExtras()
//                .getStringArrayList(RecognizerIntent.EXTRA_RESULTS);
//        // ...
//        // TBD:
//        // ...
//
//
//        // ???
//        // return super.onStartCommand(intent, flags, startId);
//        // ...
//        return 0;
//    }


    // TBD:
    // Just use context menu instead of gesture ???
    // ...

    @Override
    public boolean onGenericMotionEvent(MotionEvent event)
    {
        if (mGestureDetector != null) {
            return mGestureDetector.onMotionEvent(event);
        }
        return false;
    }

    private GestureDetector createGestureDetector(Context context)
    {
        GestureDetector gestureDetector = new GestureDetector(context);
        //Create a base listener for generic gestures
        gestureDetector.setBaseListener( new GestureDetector.BaseListener() {
            @Override
            public boolean onGesture(Gesture gesture) {
                if(Log.D) Log.d("gesture = " + gesture);
                if (gesture == Gesture.TAP) {
                    // do something on tap
                    Log.i("Gesture.TAP");
                    handleGestureTap();
                    return true;
                } else if (gesture == Gesture.TWO_TAP) {
                    // do something on two finger tap
                    Log.i("Gesture.TWO_TAP");
                    handleGestureTwoTap();
                    return true;
                } else if (gesture == Gesture.SWIPE_RIGHT) {
                    // do something on right (forward) swipe
                    Log.i("Gesture.SWIPE_RIGHT");
                    handleGestureSwipeRight();
                    return true;
                } else if (gesture == Gesture.SWIPE_LEFT) {
                    // do something on left (backwards) swipe
                    Log.i("Gesture.SWIPE_LEFT");
                    handleGestureSwipeLeft();
                    return true;
                }
                return false;
            }
        });
//        gestureDetector.setFingerListener(new GestureDetector.FingerListener() {
//            @Override
//            public void onFingerCountChanged(int previousCount, int currentCount) {
//                // do something on finger count changes
//            }
//        });
//        gestureDetector.setScrollListener(new GestureDetector.ScrollListener() {
//            @Override
//            public boolean onScroll(float displacement, float delta, float velocity) {
//                // do something on scrolling
//
//                // ????
//                return false;
//            }
//        });
        return gestureDetector;
    }


    // TBD:

    private void handleGestureTap()
    {
        Log.d("handleGestureTap() called.");


    }

    private void handleGestureTwoTap()
    {
        Log.d("handleGestureTwoTap() called.");


    }

    private void handleGestureSwipeRight()
    {
        Log.d("handleGestureSwipeRight() called.");


    }

    private void handleGestureSwipeLeft()
    {
        Log.d("handleGestureSwipeLeft() called.");


    }



    // TBD:
    // Context menu
    // ...


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        Log.d("onCreateOptionsMenu() called.");

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_livecarddemo_livecard, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        Log.d("onOptionsItemSelected() called.");

        // Handle item selection.
        switch (item.getItemId()) {
            case R.id.menu_stop_livecarddemo:
                doStopService();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onOptionsMenuClosed(Menu menu)
    {
        Log.d("onOptionsItemSelected() called.");

        // Nothing else to do, closing the activity.
        finish();
    }


}
