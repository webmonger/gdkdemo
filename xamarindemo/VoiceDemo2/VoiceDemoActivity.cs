using System;
using System.Linq;
using Android.App;
using Android.Content;
using Android.Glass.Timeline;
using System.Collections.Generic;
using Android.Glass.App;
using Android.Glass.Widget;
using Android.Glass.Touchpad;
using Android.OS;
using Android.Runtime;
using Android.Util;
using Android.Views;
using Android.Widget;

namespace VoiceDemo2
{
	[Activity (Label = "VoiceDemo2", MainLauncher = true, Theme = "@style/VoiceDemoTheme", Enabled = true)]
	[IntentFilter(new[] {"com.google.android.glass.action.VOICE_TRIGGER"})]
	[MetaData("com.google.android.glass.VoiceTrigger", Resource = "@xml/voiceinput_voicedemo2")]
	public class VoiceDemoActivity : Activity, Android.Views.GestureDetector.IOnGestureListener
	{    
		// For tap events
		private Android.Glass.Touchpad.GestureDetector mGestureDetector;
		string _tag = "VoiceDemoActivity";

		protected override void OnCreate (Bundle bundle)
		{
			base.OnCreate (bundle);

			// Set our view from the "main" layout resource
			SetContentView (Resource.Layout.Activity_VoiceDemo2);

			// For gesture handling.
			mGestureDetector = CreateGestureDetector(this);
		}

		protected override void OnResume()
		{
			base.OnResume();
			Log.Debug(_tag,"onResume() called.");
		}

		public void OpenVoiceDemoSecondActivity()
		{
			Intent intent = new Intent(this, typeof(VoiceDemoSecondActivity));
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
	}

	public class MyGestureDetector : Android.Glass.Touchpad.GestureDetector.IBaseListener
	{
		VoiceDemoActivity _context;
		string _tag = "MyGestureDetector";

		public MyGestureDetector (VoiceDemoActivity context)
		{
			_context = context;
		}

		#region IBaseListener implementation
		public bool OnGesture (Gesture gesture)
		{
			Log.Info(_tag, "gesture = " + gesture);
			if (gesture == Gesture.Tap) {
				_context.OpenVoiceDemoSecondActivity();
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


