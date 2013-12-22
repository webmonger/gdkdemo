package com.gdkdemo.timeline.card2;

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
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;

import com.gdkdemo.timeline.card2.R;
import com.google.android.glass.app.Card;
import com.google.android.glass.timeline.TimelineManager;
import com.google.android.glass.touchpad.Gesture;
import com.google.android.glass.touchpad.GestureDetector;

import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;


// The "main" activity...
public class StaticCardDemoActivity extends Activity
{
    // To pick an arbitrary images
    private final static Random sRandom = new Random(System.currentTimeMillis());
    private final static int IMAGE_COUNT = 4;
    private static int getRandomPuppyImageResourceId()
    {
        int i = sRandom.nextInt(IMAGE_COUNT);
        switch(i) {
        case 0:
        default:
            return R.drawable.puppy0;
        case 1:
            return R.drawable.puppy1;
        case 2:
            return R.drawable.puppy2;
        case 3:
            return R.drawable.puppy3;
        }
    }

    // Timeline manager instance.
    private TimelineManager timelineManager = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Log.d("onCreate() called.");

        setContentView(R.layout.activity_staticcarddemo2);

        // Is it safe to keep the reference???
        timelineManager = TimelineManager.from(this);
    }

    @Override
    protected void onDestroy()
    {
//        // temporary heck
//        removeAllCards(false);

        super.onDestroy();
    }


    @Override
    protected void onResume()
    {
        super.onResume();
        Log.d("onResume() called.");

//         // For menu handling
//         openOptionsMenu();

    }


    // Context menu
    // ...


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        Log.d("onCreateOptionsMenu() called.");

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_staticcarddemo2_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        Log.d("onOptionsItemSelected() called.");

        // Handle item selection.
        switch (item.getItemId()) {
            case R.id.menu_staticcarddemo2_insert_card_text:
                insertNewCardText();
                return true;
            case R.id.menu_staticcarddemo2_insert_card_image_full:
                insertNewCardImageFull();
                return true;
            case R.id.menu_staticcarddemo2_insert_card_image_left:
                insertNewCardImageLeft();
                return true;
            case R.id.menu_staticcarddemo2_stop_activity:
                finish();
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
        // finish();
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER) {
            openOptionsMenu();
            return true;
        }
        return false;
    }


    // Card methods

    private void insertNewCardText()
    {
        // Create a card.
        Card staticCard = new Card(this);
        staticCard.setText(R.string.staticcarddemo2_staticcard_content);
        staticCard.setFootnote(R.string.staticcarddemo2_staticcard_text);

        long cardId = timelineManager.insert(staticCard);
        if(Log.I) Log.i("Static Card (text) inserted: cardId = " + cardId);

        // Update the card content with the card Id.
        updateCardWithID(staticCard, cardId);
    }
    private void insertNewCardImageFull()
    {
        // Create a card.
        Card staticCard = new Card(this);
        staticCard.setImageLayout(Card.ImageLayout.FULL);
        staticCard.addImage(getRandomPuppyImageResourceId());
        staticCard.setFootnote(R.string.staticcarddemo2_staticcard_image_full);

        long cardId = timelineManager.insert(staticCard);
        if(Log.I) Log.i("Static Card (image - full) inserted: cardId = " + cardId);

        // Update the card content with the card Id.
        updateCardWithID(staticCard, cardId);
    }
    private void insertNewCardImageLeft()
    {
        // Create a card.
        Card staticCard = new Card(this);
        staticCard.setImageLayout(Card.ImageLayout.LEFT);
        staticCard.addImage(getRandomPuppyImageResourceId());
        staticCard.addImage(getRandomPuppyImageResourceId());
        staticCard.setFootnote(R.string.staticcarddemo2_staticcard_image_left);

        long cardId = timelineManager.insert(staticCard);
        if(Log.I) Log.i("Static Card (image - left) inserted: cardId = " + cardId);

        // Update the card content with the card Id.
        updateCardWithID(staticCard, cardId);
    }

    private void updateCardWithID(Card staticCard, long id)
    {
        if(staticCard != null) {
            String content = staticCard.getText();
            if(content == null) {  // ????
                content = "";
            }
            content += "\nID: " + id;
            if(Log.D) Log.d("New Static Card content: cardId = " + id + "; content = " + content);
            staticCard.setText(content);
            boolean suc = timelineManager.update(id, staticCard);
            if(Log.I) Log.i("Static Card updated: cardId = " + id + "; suc = " + suc);
        } else {
            // ???
            Log.w("Card not found for cardId = " + id);
        }
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


}
