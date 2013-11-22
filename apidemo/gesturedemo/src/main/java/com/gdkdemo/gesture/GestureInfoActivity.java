package com.gdkdemo.gesture;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.TextView;

import com.gdkdemo.gesture.common.GestureDemoConstants;
import com.google.android.glass.touchpad.Gesture;

import java.util.ArrayList;


public class GestureInfoActivity extends Activity
{
    // Which gesture opened this activity?
    private Gesture gestureType = null;
    // The main content TextView.
    private TextView contentView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Log.d("onCreate() called.");

        setContentView(R.layout.activity_gesture_info);
        contentView = (TextView) findViewById(R.id.gestureinfo_main_content);

        Intent intent = getIntent();
        gestureType = getGestureType(intent, true);

    }

    // Get gesture type from the Intent.
    private Gesture getGestureType(Intent intent, boolean updateContent)
    {
        Gesture gesture = null;
        if(intent != null) {
            String value = intent.getStringExtra(GestureDemoConstants.EXTRA_KEY_GESTURE_TYPE);
            if(value != null) {
                gesture = Gesture.valueOf(value);

                if(updateContent) {
                    // temporary
                    if(contentView != null) {
                        String contentText = generateMainContent(value);
                        contentView.setText(contentText);
                    }
                }
            }
        }
        return gesture;
    }

    private String generateMainContent(String gestureValue)
    {
        String contentText = "Gesture:\n" + ((gestureValue == null) ? "" : gestureValue);
        return contentText;
    }


    // TBD:
    // Do something with gestureType...


    @Override
    protected void onResume()
    {
        super.onResume();
        Log.d("onResume() called.");

        // TBD: Is this necessary???
        if(contentView != null) {
            if(gestureType != null) {
                String contentText = generateMainContent(gestureType.toString());
                contentView.setText(contentText);
            }
        }

    }


}
