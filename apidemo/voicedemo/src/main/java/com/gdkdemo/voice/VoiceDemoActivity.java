package com.gdkdemo.voice;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.MotionEvent;

import com.gdkdemo.voice.common.VoiceDemoConstants;
import com.google.android.glass.touchpad.Gesture;
import com.google.android.glass.touchpad.GestureDetector;

import java.util.ArrayList;


// The "main" activity...
public class VoiceDemoActivity extends Activity
{
    // For tap events
    private GestureDetector mGestureDetector;
    // Voice action.
    private String voiceAction = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Log.d("onCreate() called.");

        setContentView(R.layout.activity_voicedemo);

        // For gesture handling.
        mGestureDetector = createGestureDetector(this);

        voiceAction = getVoiceAction(getIntent());
        if(Log.I) Log.i("voiceAction = " + voiceAction);
        processVoiceAction(voiceAction);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        Log.d("onResume() called.");

        // This makes it impossible to exit the app.
//        voiceAction = getVoiceAction(getIntent());
//        if(Log.I) Log.i("voiceAction = " + voiceAction);
//        processVoiceAction(voiceAction);
    }

    // Returns the "first" word from the phrase following the prompt.
    private String getVoiceAction(Intent intent)
    {
        if(intent == null) {
            return null;
        }
        String action = null;
        Bundle extras = intent.getExtras();
        ArrayList<String> voiceActions = null;
        if(extras != null) {
            voiceActions = extras.getStringArrayList(RecognizerIntent.EXTRA_RESULTS);
            if(voiceActions != null && !voiceActions.isEmpty()) {
                if(Log.D) {
                    for(String a : voiceActions) {
                        Log.d("action = " + a);
                    }
                }
                action = voiceActions.get(0);
            }
        }
        return action;
    }

    // Opens the WordDictation activity,
    // or, quits the program.
    private void processVoiceAction(String voiceAction)
    {
        if(voiceAction != null) {
            if(voiceAction.equals(VoiceDemoConstants.ACTION_START_DICTATION)) {
                openVoiceDictationActivity();
            } else if(voiceAction.equals(VoiceDemoConstants.ACTION_STOP_VOICEDEMO)) {
                Log.i("VoiceDemo activity has been terminated upon start.");
                this.finish();
            } else {
                Log.w("Unknown voice action: " + voiceAction);
            }
        } else {
            Log.w("No voice action provided.");
        }
    }

    private void openVoiceDictationActivity()
    {
        Intent intent = new Intent(this, VoiceDictationActivity.class);
        startActivity(intent);
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
                    openVoiceDictationActivity();
                    return true;
                } // etc...
                return false;
            }
        });
        return gestureDetector;
    }

}
