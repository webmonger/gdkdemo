using System;
using Android.App;
using Android.Content;
using Android.Runtime;
using Android.Views;
using Android.Widget;
using Android.OS;
using Android.Glass.App;
using Android.Util;
using Android.Glass.Touchpad;

namespace MotionSensorDemo
{
	[Activity (Label = "MotionSensorDemo", Icon = "@drawable/Icon", MainLauncher = true, Enabled = true)]
	[IntentFilter (new string[]{ "com.google.android.glass.action.VOICE_TRIGGER" })]
	[MetaData ("com.google.android.glass.VoiceTrigger", Resource = "@xml/voicetriggerstart")]
	public class MotionSensorDemoActivity : Activity
	{
		private string _tag = "MotionSensorDemoActivity";

		// For tap event
		private Android.Glass.Touchpad.GestureDetector mGestureDetector;

		// Service to handle liveCard publishing, etc...
		private bool mIsBound = false;
		private static MotionSensorDemoLocalService motionSensorDemoLocalService;

		private IServiceConnection serviceConnection = new MotionService ();

		private class MotionService : IServiceConnection{
			#region IServiceConnection implementation
			private string _tag = "MotionService";

			public void OnServiceConnected (ComponentName name, IBinder service)
			{
				Log.Debug(_tag, "OnServiceConnected() called.");
				motionSensorDemoLocalService = ((MotionSensorDemoLocalService.LocalBinder)service).GetService();
			}

			public void OnServiceDisconnected (ComponentName name)
			{
				Log.Debug(_tag, "OnServiceDisconnected() called.");
				motionSensorDemoLocalService = null;
			}

			#endregion

			#region IDisposable implementation

			public void Dispose ()
			{
				// Work this out later
			}

			#endregion

			#region IJavaObject implementation

			public IntPtr Handle {
				get {
					return new IntPtr ();
				}
			}

			#endregion
		};
		private void DoBindService()
		{
			BindService(new Intent(this, typeof(MotionSensorDemoLocalService)), serviceConnection, Bind.AutoCreate);
			mIsBound = true;
		}
		private void DoUnbindService() {
			if (mIsBound) {
				UnbindService(serviceConnection);
				mIsBound = false;
			}
		}
		private void DoStartService()
		{
			StartService(new Intent(this, typeof(MotionSensorDemoLocalService)));
		}
		private void DoStopService()
		{
			StopService(new Intent(this, typeof(MotionSensorDemoLocalService)));
		}

		protected override void OnDestroy()
		{
			DoUnbindService();
			// doStopService();   // TBD: When do we call Stop service???
			base.OnDestroy();
		}
		protected override void OnCreate (Bundle bundle)
		{
			base.OnCreate(bundle);
			Log.Debug(_tag, "OnCreate() called.");

			SetContentView(Resource.Layout.Activity_MotionSensorDemo);

			// For gesture handling.
			mGestureDetector = CreateGestureDetector(this);

			// bind does not work. We need to call start() explilicitly...
			// doBindService();
			DoStartService();
			// TBD: We need to call doStopService() when user "closes" the app....
			// ...

		}


		protected override void OnResume()
		{
			base.OnResume();
			Log.Debug(_tag, "OnResume() called.");

		}



		// TBD:
		// Just use context menu instead of gesture ???
		// ...

		public override bool OnGenericMotionEvent(MotionEvent e)
		{
			if (mGestureDetector != null) {
				return mGestureDetector.OnMotionEvent(e);
			}
			return false;
		}

		private Android.Glass.Touchpad.GestureDetector CreateGestureDetector(Context context)
		{
			Android.Glass.Touchpad.GestureDetector gestureDetector = new Android.Glass.Touchpad.GestureDetector (context);
			//Create a base listener for generic gestures
			gestureDetector.SetBaseListener (new MyGestureDetector (this));

			return gestureDetector;
		}

		private void HandleGestureTap()
		{
			Log.Debug (_tag, "HandleGestureTap() called.");
			DoStopService ();
			Finish ();
		}

		private void HandleGestureTwoTap()
		{
			Log.Debug(_tag, "HandleGestureTwoTap() called.");
		}

		private class MyGestureDetector : Android.Glass.Touchpad.GestureDetector.IBaseListener
		{
			string _tag = "MyGestureDetector";

			MotionSensorDemoActivity _context;

			public MyGestureDetector (MotionSensorDemoActivity context)
			{
				_context = context;
			}

			#region IBaseListener implementation
			public bool OnGesture (Gesture gesture)
			{
				Log.Debug (_tag, "gesture = " + gesture);
				if (gesture == Gesture.Tap) {
					_context.HandleGestureTap ();
					return true;
				} else if (gesture == Gesture.TwoTap) {
					_context.HandleGestureTwoTap ();
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


