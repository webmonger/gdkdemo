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

namespace LiveCardDemo
{
	[Activity(Label = "@string/app_name", MainLauncher = true, Enabled = true)]
	[IntentFilter(new[] { "com.google.android.glass.action.VOICE_TRIGGER" })]
	[MetaData("com.google.android.glass.VoiceTrigger", Resource = "@xml/voiceinput_livecarddemo")]
	public class LiveCardDemoActivity : Activity//, Android.Views.GestureDetector.IOnGestureListener, Android.Glass.Touchpad.GestureDetector.IFingerListener
	{
		// Service to handle liveCard publishing, etc...
		private bool mIsBound = false;
		private static LiveCardDemoLocalService liveCardDemoLocalService;
		private static string _tag = "LiveCardDemo.LiveCardDemoActivity";

		protected override void OnCreate (Bundle bundle)
		{
			base.OnCreate (bundle);

			Log.Debug(_tag, "onCreate() called.");

			SetContentView(Resource.Layout.Activity_LiveCardDemo);

			// Service to control the life cycle of a LiveCard.
			// bind() does not work. We need to call start() explicitly...
			// doBindService();
			DoStartService();
			// TBD: We need to call doStopService() when the user "closes" the app....
			// ...
		}

		protected override void OnDestroy()
		{
			DoUnbindService();
			base.OnDestroy();
		}

		protected override void OnResume()
		{
			base.OnResume();
			Log.Debug(_tag, "onResume() called.");

			// For live card menu handling
			OpenOptionsMenu();
		}


		// Context menus
		// ...

		public override bool OnCreateOptionsMenu (IMenu menu)
		{
			Log.Debug(_tag, "onCreateOptionsMenu() called.");

			MenuInflater inflater = this.MenuInflater;
			inflater.Inflate(Resource.Menu.menu_livecarddemo_livecard, menu);
			return true;
		}

		public override bool OnOptionsItemSelected(IMenuItem item)
		{
			Log.Debug(_tag, "onOptionsItemSelected() called.");

			// Handle item selection.
			switch (item.ItemId) {
			case Resource.Id.menu_stop_livecarddemo:
				DoStopService();
				return true;
			default:
				return base.OnOptionsItemSelected(item);
			}
		}
		
		public override void OnOptionsMenuClosed(IMenu menu)
		{
			Log.Debug(_tag, "onOptionsItemSelected() called.");

			// Nothing else to do, closing the activity.
			Finish();
			}

//		private ServiceConnection serviceConnection = new ServiceConnection() {
//			public void OnServiceConnected(ComponentName className, IBinder service) {
//				Log.Debug(_tag, "onServiceConnected() called.");
//				liveCardDemoLocalService = ((LiveCardDemoLocalService.LocalBinder)service).getService();
//			}
//			public void OnServiceDisconnected(ComponentName className) {
//				Log.Debug(_tag, "onServiceDisconnected() called.");
//				liveCardDemoLocalService = null;
//			}
//		};

		public class MyServiceConnection : IServiceConnection {
			#region IServiceConnection implementation
			public void OnServiceConnected(ComponentName className, IBinder service) {
				Log.Debug(_tag, "onServiceConnected() called.");
				liveCardDemoLocalService = ((LiveCardDemoLocalService.LocalBinder)service).GetService();
			}
			public void OnServiceDisconnected(ComponentName className) {
				Log.Debug(_tag, "onServiceDisconnected() called.");
				liveCardDemoLocalService = null;
			}
			#endregion
			#region IDisposable implementation
			public void Dispose ()
			{
				//throw new NotImplementedException ();
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

		private IServiceConnection serviceConnection = new MyServiceConnection();

		private void DoBindService()
		{
			BindService(new Intent(this, typeof(LiveCardDemoLocalService)), serviceConnection, Bind.AutoCreate);
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
			StartService(new Intent(this, typeof(LiveCardDemoLocalService)));
		}
		private void DoStopService()
		{
			StopService(new Intent(this, typeof(LiveCardDemoLocalService)));
		}
	}
}