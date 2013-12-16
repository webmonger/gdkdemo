package com.gdkdemo.window.menu;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.speech.RecognizerIntent;
import android.view.MotionEvent;

import com.gdkdemo.window.menu.service.MenuDemoLocalService;
import com.google.android.glass.touchpad.Gesture;
import com.google.android.glass.touchpad.GestureDetector;

import java.util.ArrayList;



// The "main" activity...
public class MenuDemoActivity extends Activity
{

    // For tap event
    private GestureDetector mGestureDetector;

    // Service to handle liveCard publishing, etc...
    private boolean mIsBound = false;
    private MenuDemoLocalService menuDemoLocalService;
    private ServiceConnection serviceConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            Log.d("onServiceConnected() called.");
            menuDemoLocalService = ((MenuDemoLocalService.LocalBinder)service).getService();
        }
        public void onServiceDisconnected(ComponentName className) {
            Log.d("onServiceDisconnected() called.");
            menuDemoLocalService = null;
        }
    };
    private void doBindService()
    {
        bindService(new Intent(this, MenuDemoLocalService.class), serviceConnection, Context.BIND_AUTO_CREATE);
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
        startService(new Intent(this, MenuDemoLocalService.class));
    }
    private void doStopService()
    {
        stopService(new Intent(this, MenuDemoLocalService.class));
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

        setContentView(R.layout.activity_menudemo);


        // For gesture handling.
        mGestureDetector = createGestureDetector(this);


        // TBD:
        // Create an asyncTask for publishing liveCard...
        // ????

        // Or,
        // Start MenuDemoIntentService and
        // delegate the liveCard publishing to MenuDemoIntentService.
        // ...

        // (Short-lived) IntentService does not work for us...
//        Intent ggIntent = new Intent(this, MenuDemoIntentService.class);
//        // trigger liveCard publishing ???   --> No, this is done in IntentService.onStart()...
//        ggIntent.putExtra(MenuDemoConstants.EXTRA_KEY_ACTION, MenuDemoConstants.DEMO_SERVICE_START);
//        this.startService(ggIntent);

        // We need a real service.
        // bind does not work. We need to call start() explilicitly...
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

    }


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



    private void handleGestureTap()
    {
        Log.d("handleGestureTap() called.");

        // Terminate the service...
        // and exit the activity as well...
        doStopService();
        finish();
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


}
