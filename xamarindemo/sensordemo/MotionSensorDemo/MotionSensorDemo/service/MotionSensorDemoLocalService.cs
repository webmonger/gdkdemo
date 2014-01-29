using System;
using Android.App;
using Android.Content;
using Android.Glass.App;
using Android.Glass.Touchpad;
using Android.Hardware;
using Android.OS;
using Android.Runtime;
using Android.Views;
using Android.Widget;
using Android.Glass.Timeline;
using System.Threading;
using Android.Util;
using Android.Database;
using System.Collections.Generic;
using System.Linq;

namespace MotionSensorDemo
{
	public class MotionSensorDemoLocalService : Service, ISensorEventListener
	{
		public MotionSensorDemoLocalService () :base()
		{
		}

		private string _tag = "EnvironmentalSensorDemoLocalService";

		// For live card
		private LiveCard liveCard = null;

		// Sensor manager
		private SensorManager mSensorManager = null;

		// Motion sensors
		private Sensor mSensorAccelerometer = null;
		private Sensor mSensorGravity = null;
		private Sensor mSensorLinearAcceleration = null;
		private Sensor mSensorGyroscope = null;
		private Sensor mSensorRotationVector = null;


		// Last known motionsensor values.
		private SensorValueStruct lastSensorValuesAccelerometer = null;
		private SensorValueStruct lastSensorValuesGravity = null;
		private SensorValueStruct lastSensorValuesLinearAcceleration = null;
		private SensorValueStruct lastSensorValuesGyroscope = null;
		private SensorValueStruct lastSensorValuesRotationVector = null;

		// TBD:
		// Need a timer, etc. to refresh the UI (LiveCard) ever x seconds, etc..
		// For now, we just use the "last timestamp".
		private long lastRefreshedTime = 0L;


		// No need for IPC...
		public class LocalBinder : Binder {
			MotionSensorDemoLocalService _service;

			public LocalBinder (MotionSensorDemoLocalService service)
			{
				_service = service;
			}

			public MotionSensorDemoLocalService GetService() {
				return _service;
			}
		}
		private IBinder mBinder = new LocalBinder(new MotionSensorDemoLocalService());


		public override void OnCreate()
		{
			base.OnCreate();
			InitializeSensorManager();
		}

		public StartCommandResult OnStartCommand(Intent intent, StartCommandFlags flags, int startId)
		{
			Log.Info(_tag, "Received start id " + startId + ": " + intent);
			OnServiceStart();
			return StartCommandResult.Sticky;
		}

		public override IBinder OnBind(Intent intent)
		{
			// ????
			OnServiceStart();
			return mBinder;
		}

		public override void OnDestroy()
		{
			OnServiceStop();
			base.OnDestroy();
		}


		public void OnSensorChanged(SensorEvent e)
		{
			Log.Debug(_tag, "onSensorChanged() called.");

			ProcessMotionSensorData(e);

		}

		public void OnAccuracyChanged(Sensor sensor, SensorStatus accuracy)
		{
			Log.Debug(_tag, "onAccuracyChanged() called.");
		}

		// Service state handlers.
		// ....

		private bool OnServiceStart()
		{
			Log.Debug(_tag, "onServiceStart() called.");

			mSensorManager.RegisterListener(this, mSensorAccelerometer, SensorDelay.Normal);
			mSensorManager.RegisterListener(this, mSensorGravity, SensorDelay.Normal);
			mSensorManager.RegisterListener(this, mSensorLinearAcceleration, SensorDelay.Normal);
			mSensorManager.RegisterListener(this, mSensorGyroscope, SensorDelay.Normal);
			mSensorManager.RegisterListener(this, mSensorRotationVector, SensorDelay.Normal);

			// Publish live card...
			PublishCard(this);

			return true;
		}

		private bool OnServicePause()
		{
			Log.Debug(_tag, "onServicePause() called.");
			return true;
		}
		private bool OnServiceResume()
		{
			Log.Debug(_tag, "onServiceResume() called.");
			return true;
		}

		private bool OnServiceStop()
		{
			Log.Debug(_tag, "onServiceStop() called.");

			mSensorManager.UnregisterListener(this);

			// TBD:
			// Unpublish livecard here
			// .....
			UnpublishCard(this);
			// ...

			return true;
		}




		// For live cards...

		private void PublishCard(Context context)
		{
			publishCard(context, false);
		}

		private void publishCard(Context context, bool update)
		{
			Log.Debug(_tag, "publishCard() called: update = " + update);
			if (liveCard == null || update == true) {

//            // TBD:
//            // We get multiple liveCards if we just call setViews() and publish()...
//            // As a workaround, for now, we always unpublish the previous card first.
//            if (liveCard != null) {
//                liveCard.unpublish();
//            }
//            // ....

				string cardId = "motionsensordemo_card";
				// Note: getLiveCard() always publishes a new live card....
				if(liveCard == null) {
					// if(liveCard == null || ! liveCard.isPublished()) {
					TimelineManager tm = TimelineManager.From(context);
					liveCard = tm.CreateLiveCard(cardId);
//                 liveCard.setNonSilent(true);       // for testing.
				}
				RemoteViews remoteViews = new RemoteViews(context.PackageName, Resource.Layout.LiveCard_MotionSensorDemo);
				String content = "";
				if(lastSensorValuesAccelerometer != null) {
					content += "Accelerometer:\n" + lastSensorValuesAccelerometer.ToString() + "\n";
				}
				if(lastSensorValuesGravity != null) {
					content += "Gravity:\n" + lastSensorValuesGravity.ToString() + "\n";
				}
				if(lastSensorValuesLinearAcceleration != null) {
					content += "Linear Acceleration:\n" + lastSensorValuesLinearAcceleration.ToString() + "\n";
				}
				if(lastSensorValuesGyroscope != null) {
					content += "Gyroscope:\n" + lastSensorValuesGyroscope.ToString() + "\n";
				}
				if(lastSensorValuesRotationVector != null) {
					content += "Rotation Vector:\n" + lastSensorValuesRotationVector.ToString() + "\n";
				}

				remoteViews.SetCharSequence(Resource.Id.livecard_content, "setText", content);
				liveCard.SetViews(remoteViews);
				Intent intent = new Intent(context, typeof(MotionSensorDemoActivity));
				liveCard.SetAction(PendingIntent.GetActivity(context, 0, intent, 0));
				// ???
				// Without this if(),
				// I get an exception:
				// "java.lang.IllegalStateException: State CREATED expected, currently in PUBLISHED"
				// Why???
				if(! liveCard.IsPublished) {
					liveCard.Publish(LiveCard.PublishMode.Reveal);
				} else {
					// ????
					// According to the GDK doc,
					// it appears we should call publish() every time the content changes...
					// But, it seems to work without re-publishing...
					long now = DateTime.UtcNow.Ticks;
					Log.Debug(_tag, "liveCard not published at " + now);
				}
			} else {
				// Card is already published.
				return;
			}
		}

		private void UnpublishCard(Context context)
		{
			Log.Debug(_tag, "unpublishCard() called.");
			if (liveCard != null) {
				liveCard.Unpublish();
				liveCard = null;
			}
		}

		// MotionSensor methods
		private void InitializeSensorManager()
		{
			mSensorManager = (SensorManager) GetSystemService(Context.SensorService);
			mSensorAccelerometer = mSensorManager.GetDefaultSensor(SensorType.Accelerometer);
			mSensorGravity = mSensorManager.GetDefaultSensor(SensorType.Gravity);
			mSensorLinearAcceleration = mSensorManager.GetDefaultSensor(SensorType.LinearAcceleration);
			mSensorGyroscope = mSensorManager.GetDefaultSensor(SensorType.Gyroscope);
			mSensorRotationVector = mSensorManager.GetDefaultSensor(SensorType.RotationVector);
		}

		private void ProcessMotionSensorData(SensorEvent e)
		{
			long now = DateTime.UtcNow.Ticks;

			SensorType sensor = e.Sensor.Type;
			//Type type = sensor.GetType ();
			long timestamp = e.Timestamp;
			IList<float> values = e.Values;
			SensorStatus accuracy = e.Accuracy;
			SensorValueStruct data = new SensorValueStruct (sensor, timestamp, values, accuracy);

			switch (sensor) {
			case SensorType.Accelerometer:
				lastSensorValuesAccelerometer = data;
				break;
			case SensorType.Gravity:
				lastSensorValuesGravity = data;
				break;
			case SensorType.LinearAcceleration:
				lastSensorValuesLinearAcceleration = data;
				break;
			case SensorType.Gyroscope:
				lastSensorValuesGyroscope = data;
				break;
			case SensorType.RotationVector:
				lastSensorValuesRotationVector = data;
				break;
			default:
				Log.Warn (_tag, "Unknown type: " + sensor);
				break;
			}

			// TBD:
			// Update the DB, etc..
			// ...

			// Update the UI.
			// temporary
			long delta = 5000L;   // every 5 seconds
			if (lastRefreshedTime <= now - delta) {
				// if(liveCard != null && liveCard.isPublished()) {
				// Update...
				publishCard (this, true);
				// }
				lastRefreshedTime = now;
			}
		}
	}
}

