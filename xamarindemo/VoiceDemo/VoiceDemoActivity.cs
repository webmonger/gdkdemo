
/*
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
*/
using System;
using System.Linq;
using Android.Glass.Touchpad;
using Android.App;
using Android.OS;
using Android.Speech;
using Android.Content;
using VoiceDemo.Common;
using Android.Views;
using System.Collections.Generic;
using Android.Gestures;



namespace VoiceDemo
{

	[Activity (Label = "VoiceDemo", MainLauncher = true)]
	[IntentFilter(new[] {"com.google.android.glass.action.VOICE_TRIGGER"})]
	public class VoiceDemoActivity : Activity
	{
	    // For tap events
		private Android.Glass.Touchpad.GestureDetector _gestureDetector;
	    // Voice action.
		private string voiceAction = null;

		protected override void OnCreate(Bundle savedInstanceState)
	    {
			base.OnCreate(savedInstanceState);
			//Log.d("onCreate() called.");

			SetContentView(Resource.Layout.activity_voicedemo);

	        // For gesture handling.
			_gestureDetector = CreateGestureDetector(this);

			voiceAction = GetVoiceAction(Intent);
			//if(Log.I) Log.Info("voiceAction = " + voiceAction);
			ProcessVoiceAction(voiceAction);
	    }

		protected override void OnResume()
	    {
			base.OnResume();
			//Log.d("onResume() called.");

	        // This makes it impossible to exit the app.
	//        voiceAction = getVoiceAction(getIntent());
	//        if(Log.I) Log.i("voiceAction = " + voiceAction);
	//        ProcessVoiceAction(voiceAction);
	    }

	    // Returns the "first" word from the phrase following the prompt.
		private string GetVoiceAction(Intent intent)
	    {
	        if(intent == null) 
			{
	            return null;
	        }
			string action = null;
			Bundle extras = intent.GetBundleExtra("Extras");// GetStringArrayListExtra(RecognizerIntent.ExtraResults);


			IList<string> voiceActions = null;
	        if(extras != null) 
			{
				voiceActions = extras.GetStringArrayList(RecognizerIntent.ExtraResults);

				if(voiceActions != null && !voiceActions.Any()) 
				{
					//if(Log.D) 
					//{
						foreach(string a in voiceActions) 
						{
							//Log.Debug("action = " + a);
						//Trace ("Action = ", a);
	                    }
					//}
					action = voiceActions.ElementAt(0);
	            }
	        }
	        return action;
	    }

	    // Opens the WordDictation activity,
	    // or, quits the program.
		private void ProcessVoiceAction(string voiceAction)
	    {
	        if(voiceAction != null)
			{
				if(voiceAction.Equals(VoiceDemoConstants.ACTION_START_DICTATION)) 
				{
					OpenVoiceDictationActivity();
	            }
				else if(voiceAction.Equals(VoiceDemoConstants.ACTION_STOP_VOICEDEMO)) 
				{
					//Log.i("VoiceDemo activity has been terminated upon start.");
					this.Finish();
	            }
				else 
				{
					//Log.w("Unknown voice action: " + voiceAction);
	            }
	        }
			else 
			{
				//Log.w("No voice action provided.");
	        }
	    }

		public void OpenVoiceDictationActivity()
	    {
			Intent intent = new Intent(this, typeof(VoiceDictationActivity));
			StartActivity(intent);
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

		private Android.Glass.Touchpad.GestureDetector CreateGestureDetector(Context context)
		{
			Android.Glass.Touchpad.GestureDetector gestureDetector = new Android.Glass.Touchpad.GestureDetector (context);
			//Create a base listener for generic gestures
			gestureDetector.SetBaseListener (new MyGestureDetector (this));

			return gestureDetector;
		}

		private class MyGestureDetector : Android.Glass.Touchpad.GestureDetector.IBaseListener
		{
			VoiceDemoActivity _context;

			public MyGestureDetector (VoiceDemoActivity context)
			{
				_context = context;
			}

			#region IBaseListener implementation
			public bool OnGesture (Android.Glass.Touchpad.Gesture gesture)
			{
				if (gesture == Android.Glass.Touchpad.Gesture.Tap) 
				{
					_context.OpenVoiceDictationActivity();
					return true;
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
