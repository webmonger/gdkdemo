package com.gdkdemo.timeline.card;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.StrictMode;
import android.speech.RecognizerIntent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;

import com.gdkdemo.timeline.card.R;
import com.google.android.glass.app.Card;
import com.google.android.glass.timeline.TimelineManager;
import com.google.android.glass.touchpad.Gesture;
import com.google.android.glass.touchpad.GestureDetector;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


// The "main" activity...
public class StaticCardDemoActivity extends Activity
{
    // This is the id of the last card inserted.
    private long cardId = -1L;  // ???

    // For tap event
    private GestureDetector mGestureDetector;


    // temporary
    StrictMode.ThreadPolicy defaultThreadPolicy = null;
    // temporary


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Log.d("onCreate() called.");

        // temporary
        defaultThreadPolicy = StrictMode.getThreadPolicy();
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder(defaultThreadPolicy).permitAll().build());
        // temporary


        setContentView(R.layout.activity_staticcarddemo);

        // For gesture handling.
        mGestureDetector = createGestureDetector(this);


//        // temporary
//        // Create a card.
//        createAndInsertCard();
//
    }

    @Override
    protected void onDestroy()
    {
//        // temporary heck
//        removeAllCards(false);

        // temporary
        if(defaultThreadPolicy != null) {
            StrictMode.setThreadPolicy(defaultThreadPolicy);
        }
        // temporary

        super.onDestroy();
    }


    // Card-related methods

    private void createAndInsertCard()
    {
        // Create a card.
        Card staticCard = new Card(this);
        // TBD: layout, etc...
        staticCard.setText(R.string.staticcarddemo_staticcard_content);

        TimelineManager tm = TimelineManager.from(this);
        cardId = tm.insert(staticCard);
        if(Log.I) Log.i("Static Card inserted: cardId = " + cardId);

        // Update the card content with the card Id.
        updateCardContent(cardId);
    }

    private void removeCard()
    {
        removeCard(cardId);
        cardId = -1L;   // ???
    }
    private void removeCard(long id)
    {
        try {
            TimelineManager tm = TimelineManager.from(this);
            boolean suc = tm.delete(id);
            if(Log.I) Log.i("Static Card removed: cardId = " + id + "; suc = " + suc);
        } catch(Exception e) {
            // ignore
            Log.e("Failed to delete card with id = " + id, e);
        }
    }
    private void removeAllCards(boolean includeCurrent)
    {
        // hack....
        if(cardId > -1L) {   //  ????
            for(long i=1L;i<cardId;i++) {
                removeCard(i);
            }
            if(includeCurrent) {
                removeCard();
            }
        }
    }

    // The sole purpose of this function is
    // to add the cardId to the card's content.
    private void updateCardContent(final long id)
    {
        final Handler handler = new Handler();
        TimerTask staticCardUpdateTask = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        try {
                            if(id > -1L) { // ???
                                TimelineManager tm = TimelineManager.from(StaticCardDemoActivity.this);
                                Card staticCard = tm.query(id);
                                if(staticCard != null) {
                                    String content = staticCard.getText();
                                    if(content == null) {  // ????
                                        content = "";
                                    }
                                    content += "\nID: " + id;
                                    if(Log.D) Log.d("New Static Card content: cardId = " + id + "; content = " + content);
                                    staticCard.setText(content);
                                    boolean suc = tm.update(id, staticCard);
                                    if(Log.I) Log.i("Static Card updated: cardId = " + id + "; suc = " + suc);
                                } else {
                                    // ???
                                    Log.w("Card not found for cardId = " + id);
                                }
                            } else {
                                // ???
                                Log.w("Card Id is invalid: cardId = " + id);
                            }
                        } catch (Exception e) {
                            Log.e("Failed to run the task.", e);
                        }
                    }
                });
            }
        };
        (new Timer()).schedule(staticCardUpdateTask, 2000L); // 2 second delay.
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
        // Create a base listener for generic gestures
        gestureDetector.setBaseListener( new GestureDetector.BaseListener() {
            @Override
            public boolean onGesture(Gesture gesture) {
                if(Log.D) Log.d("gesture = " + gesture);
                if (gesture == Gesture.TAP) {
                    Log.i("Gesture.TAP");
                    handleGestureTap();
                    return true;
                } else if (gesture == Gesture.SWIPE_RIGHT) {
                    Log.i("Gesture.SWIPE_RIGHT");
                    handleGestureSwipeRight();
                    return true;
                }
                return false;
            }
        });
        return gestureDetector;
    }


    // Note that the activity is dismissed in any other way (e.g., swipe down)
    // then the static card will remain even after this activity is destroyed...
    private void handleGestureTap()
    {
        Log.d("handleGestureTap() called.");

        // Remove all cards first
        removeAllCards(true);

        // Then, exit this activity...
        finish();
    }

    private void handleGestureSwipeRight()
    {
        Log.d("handleGestureSwipeRight() called.");

        // Create a card.
//        if(cardId == -1L) {
              createAndInsertCard();
//        } else {
//            Log.w("Card already exists: cardId = " + cardId);
//        }
    }


}
