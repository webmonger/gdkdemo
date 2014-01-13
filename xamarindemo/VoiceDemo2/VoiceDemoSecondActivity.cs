using System;
using System.Collections.Generic;
using System.Linq;
using Android.App;
using Android.Content;
using Android.Glass.App;
using Android.Glass.Timeline;
using Android.Glass.Touchpad;
using Android.Glass.Widget;
using Android.OS;
using Android.Runtime;
using Android.Speech;
using Android.Util;
using Android.Views;
using Android.Widget;

namespace VoiceDemo2
{
	[Activity (Label = "VoiceDemo2", MainLauncher = true, Theme = "@style/VoiceDemoTheme", Enabled = true)]
	[IntentFilter(new[] {"com.google.android.glass.action.VOICE_TRIGGER"})]
	[MetaData("com.google.android.glass.VoiceTrigger", Resource = "@xml/voiceinput_voicedemo2_second")]
	public class VoiceDemoSecondActivity : Activity, Android.Views.GestureDetector.IOnGestureListener
	{
		// For tap events
		private Android.Glass.Touchpad.GestureDetector mGestureDetector;
		// Voice action.
		private String voiceAction = null;
		string _tag = "VoiceDemoActivity";

		protected override void OnCreate (Bundle bundle)
		{
			base.OnCreate (bundle);

			// Set our view from the "main" layout resource
			SetContentView (Resource.Layout.Activity_VoiceDemo2_Second);

			// For gesture handling.
			mGestureDetector = CreateGestureDetector(this);

			voiceAction = GetVoiceAction(this.Intent);
			Log.Info(_tag, "voiceAction = " + voiceAction);
			ProcessVoiceAction(voiceAction);
		}

		protected override void OnResume()
		{
			base.OnResume();
			Log.Debug(_tag, "onResume() called.");

			// This makes it impossible to exit the app.
//        voiceAction = getVoiceAction(getIntent());
//        if(Log.I) Log.i("voiceAction = " + voiceAction);
//        processVoiceAction(voiceAction);
		}

		// Returns the "first" word from the phrase following the prompt.
		private string GetVoiceAction(Intent intent)
		{
			if(intent == null) {
				return null;
			}
			string action = null;
			Bundle extras = this.Intent.Extras;
			IList<string> voiceActions = null;
			if(extras != null) {
				voiceActions = extras.GetStringArrayList(RecognizerIntent.ExtraResults);
				if(voiceActions != null && !voiceActions.Any()) {
					foreach(string a in voiceActions) {
						Log.Debug(_tag, "action = " + a);
					}
                    action = string.Join(",", voiceActions.ToArray());
				}
			}
			return action;
		}

		// Opens the WordDictation activity,
		// or, quits the program.
		private void ProcessVoiceAction(string voiceAction)
		{
			if(voiceAction != null) {
				if(voiceAction.Equals(VoiceDemoConstants.ACTION_START_MAIN_ACTIVITY)
					|| voiceAction.Equals(VoiceDemoConstants.ACTION_START_FIRST_ACTIVITY)) {
					Log.Info(_tag, "Starting VoiceDemo2 main activity.");
                    OpenVoiceDemoMainActivity();
					this.Finish();   // ???
				} else if(voiceAction.Equals(VoiceDemoConstants.ACTION_START_SECOND_ACTIVITY)) {
                    Log.Info(_tag, "VoiceDemo2 second activity is being started.");
				} else if(voiceAction.Equals(VoiceDemoConstants.ACTION_STOP_VOICEDEMO)) {
                    Log.Info(_tag, "VoiceDemo2 second activity has been terminated upon start.");
					this.Finish();
				} else {
					Log.Warn(_tag, "Unknown voice action: " + voiceAction);
				}
			} else {
				Log.Warn(_tag, "No voice action provided.");
			}
		}

        private void OpenVoiceDemoMainActivity()
		{
			Intent intent = new Intent(this, typeof(VoiceDemoActivity));
			// ???
			StartActivity(intent.SetFlags(    // Intent.FLAG_ACTIVITY_NEW_TASK |
				ActivityFlags.ClearTop |
				ActivityFlags.SingleTop));
		}


		///////////////////////////////////////////////////////
		/// Gesture handling
		//
		public override bool OnGenericMotionEvent(MotionEvent e)
		{
			if (mGestureDetector != null) {
				return mGestureDetector.OnMotionEvent(e);
			}
			return base.OnGenericMotionEvent(e);
		}

		private Android.Glass.Touchpad.GestureDetector CreateGestureDetector(Context context)
		{
			Android.Glass.Touchpad.GestureDetector gestureDetector = new Android.Glass.Touchpad.GestureDetector(context);
			//Create a base listener for generic gestures
			gestureDetector.SetBaseListener(new MyGestureDetector(this));
			return gestureDetector;
		}

		public bool OnDown (MotionEvent e)
		{
			return false;
		}

		public bool OnFling (MotionEvent e1, MotionEvent e2, float velocityX, float velocityY)
		{
			return false;
		}

		public void OnLongPress (MotionEvent e)
		{

		}

		public bool OnScroll (MotionEvent e1, MotionEvent e2, float distanceX, float distanceY)
		{
			return false;
		}

		public void OnShowPress (MotionEvent e)
		{

		}

		public bool OnSingleTapUp (MotionEvent e)
		{
			return false;
		}

		private class MyGestureDetector : Android.Glass.Touchpad.GestureDetector.IBaseListener
		{
            VoiceDemoSecondActivity _context;
			string _tag = "MyGestureDetector";

            public MyGestureDetector(VoiceDemoSecondActivity context)
			{
				_context = context;
			}

			#region IBaseListener implementation
			public bool OnGesture (Gesture gesture)
			{
				Log.Info(_tag, "gesture = " + gesture);
				if (gesture == Gesture.Tap) {
					_context.OpenVoiceDemoMainActivity();
					return true;
				} 
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


