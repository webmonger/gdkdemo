/*
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
*/

using System;
using System.Linq;
using Android.App;
using Android.Widget;
using Android.OS;
using Android.Glass.Touchpad;
using Android.Content;
using VoiceDemo.Common;
using Android.Speech;
using System.Collections.Generic;
using Android.Views;

namespace VoiceDemo
{


	public class VoiceDictationActivity : Activity
	{
	    // For tap events
		private Android.Glass.Touchpad.GestureDetector _gestureDetector;
	    // The main content TextView.
	    private TextView contentView = null;

		protected override void OnDestroy()
	    {
			base.OnDestroy();
	    }


		protected override void OnCreate(Bundle savedInstanceState)
	    {
			base.OnCreate(savedInstanceState);
			//Log.d("onCreate() called.");

			SetContentView(Resource.Layout.activity_voice_dictation);
			contentView = (TextView) FindViewById(Resource.Id.voicedictation_main_content);

	        // For gesture handling.
			_gestureDetector = createGestureDetector(this);

	    }

		protected override void OnActivityResult (int requestCode, Result resultCode, Intent data)
		{
			if (requestCode == VoiceDemoConstants.SPEECH_REQUEST && resultCode == Result.Ok)
			{
				IList<string> results = data.GetStringArrayListExtra(RecognizerIntent.ExtraResults);
				string spokenText = results.ElementAt(0);
				if(spokenText == null) {
					spokenText = "";
				}
				contentView.SetText(spokenText, TextView.BufferType.Normal);
			}
			base.OnActivityResult (requestCode, resultCode, data);
		}

		///////////////////////////////////////////////////////
	    /// Gesture handling
	    //

		public override bool OnGenericMotionEvent(MotionEvent e)
	    {
	        if (_gestureDetector != null) 
			{
				return _gestureDetector.OnMotionEvent(e);
	        }
			return base.OnGenericMotionEvent(e);
	    }

	    private Android.Glass.Touchpad.GestureDetector createGestureDetector(Context context)
	    {
	        Android.Glass.Touchpad.GestureDetector gestureDetector = new Android.Glass.Touchpad.GestureDetector(context);
	        //Create a base listener for generic gestures

			gestureDetector.SetBaseListener (new MyGestureDetector (this));
	        
	        return gestureDetector;
	    }

		public void HandleGestureTap()
	    {
			//Log.d("handleGestureTap() called.");
			Intent intent = new Intent(RecognizerIntent.ActionRecognizeSpeech);
			StartActivityForResult(intent, VoiceDemoConstants.SPEECH_REQUEST);
	    }




		private class MyGestureDetector : Android.Glass.Touchpad.GestureDetector.IBaseListener
		{
			VoiceDictationActivity _context;

			public MyGestureDetector (VoiceDictationActivity context)
			{
				_context = context;
			}

			#region IBaseListener implementation
			public bool OnGesture (Gesture gesture)
			{
				//if(Log.I) Log.i("gesture = " + gesture);
				if (gesture == Gesture.Tap) 
				{
					_context.HandleGestureTap();
					return true;
				}
				else if (gesture == Gesture.SwipeDown) 
				{   // IS this necessary???
					_context.Finish();
				} // etc...
					return false;
			}


			#endregion
			#region IDisposable implementation
			public void Dispose ()
			{
			}
			#endregion
			#region IJavaObject implementation
			public IntPtr Handle {
				get {
					return new IntPtr ();
				}
			}
			#endregion
		}



	}
}