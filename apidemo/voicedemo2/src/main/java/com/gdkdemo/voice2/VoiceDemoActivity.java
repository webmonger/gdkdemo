package com.gdkdemo.voice2;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.MotionEvent;

import com.gdkdemo.voice2.common.VoiceDemoConstants;
import com.google.android.glass.touchpad.Gesture;
import com.google.android.glass.touchpad.GestureDetector;

import java.util.ArrayList;


// The "main" activity...
public class VoiceDemoActivity extends Activity
{
    // For tap events
    private GestureDetector mGestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Log.d("onCreate() called.");

        setContentView(R.layout.activity_voicedemo2);

        // For gesture handling.
        mGestureDetector = createGestureDetector(this);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        Log.d("onResume() called.");
    }


    private void openVoiceDemoSecondActivity()
    {
        Intent intent = new Intent(this, VoiceDemoSecondActivity.class);
        // ???
        startActivity(intent.setFlags(    // Intent.FLAG_ACTIVITY_NEW_TASK |
                Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_SINGLE_TOP));
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
                if (gesture == Gesture.TAP) {
                    openVoiceDemoSecondActivity();
                    return true;
                } // etc...
                return false;
            }
        });
        return gestureDetector;
    }

}
