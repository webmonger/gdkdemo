using System;
using Android.App;
using Android.Content;
using Android.Runtime;
using Android.Views;
using Android.Widget;
using Android.OS;
using Android.Util;
using Android.Glass.App;
using Android.Glass.Widget;
using Android.Glass.Touchpad;

namespace LiveCardDemo
{
	[Activity (Label = "LiveCardDemo", MainLauncher = true)]
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
			inflater.Inflate(Resource.Layout.Livecard_LiveCardDemo, menu);
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
//				Log.d("onServiceConnected() called.");
//				liveCardDemoLocalService = ((LiveCardDemoLocalService.LocalBinder)service).getService();
//			}
//			public void OnServiceDisconnected(ComponentName className) {
//				Log.d("onServiceDisconnected() called.");
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