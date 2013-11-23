package com.gdkdemo.voice;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.MotionEvent;
import android.widget.TextView;

import com.gdkdemo.voice.common.VoiceDemoConstants;
import com.google.android.glass.touchpad.Gesture;
import com.google.android.glass.touchpad.GestureDetector;

import java.util.ArrayList;
import java.util.List;


public class VoiceDictationActivity extends Activity
{
    // For tap events
    private GestureDetector mGestureDetector;
    // The main content TextView.
    private TextView contentView = null;

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
    }



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Log.d("onCreate() called.");

        setContentView(R.layout.activity_voice_dictation);
        contentView = (TextView) findViewById(R.id.voicedictation_main_content);

        // For gesture handling.
        mGestureDetector = createGestureDetector(this);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == VoiceDemoConstants.SPEECH_REQUEST && resultCode == RESULT_OK) {
            List<String> results = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            String spokenText = results.get(0);
            if(spokenText == null) {
                spokenText = "";
            }
            contentView.setText(spokenText);
        }
        super.onActivityResult(requestCode, resultCode, data);
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
                    handleGestureTap();
                    return true;
                } else if (gesture == Gesture.SWIPE_DOWN) {   // IS this necessary???
                    VoiceDictationActivity.this.finish();
                } // etc...
                return false;
            }
        });
        return gestureDetector;
    }

    private void handleGestureTap()
    {
        Log.d("handleGestureTap() called.");
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        startActivityForResult(intent, VoiceDemoConstants.SPEECH_REQUEST);
    }

}
