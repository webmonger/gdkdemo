package com.gdkdemo.gesture;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.Toast;

import com.gdkdemo.gesture.common.Config;
import com.gdkdemo.gesture.common.GestureDemoConstants;
import com.google.android.glass.touchpad.Gesture;
import com.google.android.glass.touchpad.GestureDetector;


// The "main" activity...
public class GestureDemoActivity extends Activity
{
    // For tap events
    private GestureDetector mGestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Log.d("onCreate() called.");

        setContentView(R.layout.activity_gesturedemo);

        // For gesture handling.
        mGestureDetector = createGestureDetector(this);

    }

    @Override
    protected void onResume()
    {
        super.onResume();
        Log.d("onResume() called.");
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
    }


    // New activity is opened on gesture if USE_GESTURE_INFO_ACTIVITY == true.
    private void openGestureInfoView(Gesture gesture)
    {
        Intent intent = new Intent(this, GestureInfoActivity.class);
        if(gesture != null) {
            intent.putExtra(GestureDemoConstants.EXTRA_KEY_GESTURE_TYPE, gesture.toString());
        }
        startActivity(intent);
    }
    // Just show a toast if USE_GESTURE_INFO_ACTIVITY == false.
    private void showGestureInfoToast(Gesture gesture)
    {
        if(gesture != null) {
            int duration = Toast.LENGTH_SHORT;
            String text = "Gesture:\n" + ((gesture == null) ? "" : gesture.toString());
            Toast toast = Toast.makeText(this, text, duration);
            toast.show();
        }
    }


    ///////////////////////////////////////////////////////
    /// Gesture handling
    //

    @Override
    public boolean onGenericMotionEvent(MotionEvent event)
    {
        if (mGestureDetector != null) {
            return mGestureDetector.onMotionEvent(event);
        }
        return super.onGenericMotionEvent(event);
    }

    private GestureDetector createGestureDetector(Context context)
    {
        GestureDetector gestureDetector = new GestureDetector(context);
        //Create a base listener for generic gestures
        gestureDetector.setBaseListener( new GestureDetector.BaseListener() {
            @Override
            public boolean onGesture(Gesture gesture) {
                if(Log.I) Log.i("gesture = " + gesture);
                handleGesture(gesture);

                // LONG_PRESS, SWIPE_DOWN, SWIPE_LEFT, SWIPE_RIGHT, SWIPE_UP,
                // TAP, THREE_LONG_PRESS, THREE_TAP, TWO_LONG_PRESS, TWO_SWIPE_DOWN,
                // TWO_SWIPE_LEFT, TWO_SWIPE_RIGHT, TWO_SWIPE_UP, TWO_TAP;

//                if (gesture == Gesture.TAP) {
//                    handleGestureTap();
//                    return true;
//                } else if (gesture == Gesture.TWO_TAP) {
//                    handleGestureTwoTap();
//                    return true;
//                } else if (gesture == Gesture.THREE_TAP) {
//                    handleGestureThreeTap();
//                    return true;
//                } else if (gesture == Gesture.SWIPE_RIGHT) {
//                    handleGestureSwipeRight();
//                    return true;
//                } else if (gesture == Gesture.SWIPE_LEFT) {
//                    handleGestureSwipeLeft();
//                    return true;
//                } // etc...
                return false;
            }
        });
        gestureDetector.setFingerListener(new GestureDetector.FingerListener() {
            @Override
            public void onFingerCountChanged(int previousCount, int currentCount) {
                // do something on finger count changes
                Log.i("onFingerCountChanged()");

            }
        });
        gestureDetector.setScrollListener(new GestureDetector.ScrollListener() {
            @Override
            public boolean onScroll(float displacement, float delta, float velocity) {
                // do something on scrolling
                Log.i("onScroll()");

                // ????
                return false;
            }
        });
        return gestureDetector;
    }



    private void handleGesture(Gesture gesture)
    {
        if(Log.D) Log.d("handleGesture() called with gesture = " + gesture);
        if(gesture != null) {
            if(Config.USE_GESTURE_INFO_ACTIVITY) {
                openGestureInfoView(gesture);
            } else {
                showGestureInfoToast(gesture);
            }
        }
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

    private void handleGestureThreeTap()
    {
        Log.d("handleGestureThreeTap() called.");
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
