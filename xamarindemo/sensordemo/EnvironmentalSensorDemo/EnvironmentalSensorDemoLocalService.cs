using System;
using System.Collections.Generic;
using System.Linq;
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

namespace EnvironmentalSensorDemo
{
	public class EnvironmentalSensorDemoLocalService : Service, ISensorEventListener
	{
		public EnvironmentalSensorDemoLocalService () :base()
		{
		    mBinder = new LocalBinder(this);
		}

	    private string _tag = "EnvironmentalSensorDemoLocalService";

		// For live card
		private LiveCard liveCard = null;
		private string cardId = "environmentalsensordemo_card";

		// Sensor manager
		private SensorManager mSensorManager = null;

		// Environmental sensors
		private Sensor mSensorLight = null;


		// Last known environmentalsensor values.
		private SensorValueStruct lastSensorValuesLight = null;
		// private long lastRefreshedTime = 0L;  // In nano seconds ???


		// "Heart beat".
		private Timer heartBeat = null;


		// No need for IPC...
		public class LocalBinder : Binder {
			EnvironmentalSensorDemoLocalService _service;

			public LocalBinder (EnvironmentalSensorDemoLocalService service)
			{
				_service = service;
			}

			public EnvironmentalSensorDemoLocalService GetService() {
				return _service;
			}
		}
		private IBinder mBinder;


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
			// ???
			if(heartBeat != null) {
				heartBeat.Dispose();
				heartBeat = null;
			}
			OnServiceStop();
			base.OnDestroy();
		}


		public void OnSensorChanged(SensorEvent e)
		{
			Log.Debug(_tag, "onSensorChanged() called.");

			ProcessEnvironmentalSensorData(e);

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

			mSensorManager.RegisterListener(this, mSensorLight, SensorDelay.Normal);

			// Publish live card...
			PublishCard(this);

//			if(heartBeat == null) {
//				heartBeat = new Timer();
//			}
			StartHeartBeat();

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
			Log.Debug(_tag, "publishCard() called.");
			// if (liveCard == null || !liveCard.isPublished()) {
			if (liveCard == null) {
				TimelineManager tm = TimelineManager.From(context);
				liveCard = tm.CreateLiveCard(cardId);
//             // liveCard.setNonSilent(false);       // Initially keep it silent ???
//             liveCard.setNonSilent(true);      // for testing, it's more convenient. Bring the card to front.
				RemoteViews remoteViews = new RemoteViews(context.PackageName, Resource.Layout.LiveCard_EnvironmentalSensorDemo);
				liveCard.SetViews(remoteViews);
				Intent intent = new Intent(context, typeof(EnvironmentalSensorDemoActivity));
				liveCard.SetAction(PendingIntent.GetActivity(context, 0, intent, 0));
				liveCard.Publish(LiveCard.PublishMode.Reveal);
			} else {
				// Card is already published.
				return;
			}
		}
		// This will be called by the "HeartBeat".
		private void UpdateCard(Context context)
		{
			Log.Debug(_tag, "updateCard() called.");
			// if (liveCard == null || !liveCard.isPublished()) {
			if (liveCard == null) {
				// Use the default content.
				PublishCard(context);
			} else {
				// Card is already published.

//            // ????
//            // Without this (if use "republish" below),
//            // we will end up with multiple live cards....
//            liveCard.unpublish();
//            // ...


				// getLiveCard() seems to always publish a new card
				//       contrary to my expectation based on the method name (sort of a creator/factory method).
				// That means, we should not call getLiveCard() again once the card has been published.
//            TimelineManager tm = TimelineManager.from(context);
//            liveCard = tm.createLiveCard(cardId);
//            liveCard.setNonSilent(true);       // Bring it to front.
				// TBD: The reference to remoteViews can be kept in this service as well....
				RemoteViews remoteViews = new RemoteViews(context.PackageName, Resource.Layout.LiveCard_EnvironmentalSensorDemo);


				// Note:
				// This is not a "real" program.
				// ...
				// This demo app merely retrieves the 10th oldest entry in the sensor data DB
				// and compares it with the current value...
				// ...


				string content = "";
				long now = DateTime.UtcNow.Millisecond;
				if(lastSensorValuesLight != null) {
					long newTimestamp = lastSensorValuesLight.GetTimestamp();  // ~~ now * 1000000 ...
					IList<float> vals = lastSensorValuesLight.GetValues();
					float newSensorValue = 0.0f;
					if(vals != null && vals.Count() > 0) {
						newSensorValue = vals[0];
					}
					content += "Current value: " + newSensorValue;
					content += "lx at " + newTimestamp;
				}

				string selectionClause = null;   // e.g., "type = ?";
				string[] selectionArgs = null;   // e.g., new string[]{};
				ICursor cursor = ContentResolver.Query(SensorValueData.SensorValues.CONTENT_URI,
					SensorValueQueryUtil.PROJECTION,
					selectionClause,
					selectionArgs,
					// SensorValueData.SensorValues.DEFAULT_SORT_ORDER
					"timestamp desc limit 10"   // ???
				);
				if(cursor != null && cursor.MoveToFirst()) {
					cursor.MoveToLast();    // The db may have less than 10 entries. but that's ok...
					long oldTimestamp = cursor.GetLong(SensorValueQueryUtil.COLUMN_INDEX_TIMESTAMP);
					float oldSensorValue = cursor.GetFloat(SensorValueQueryUtil.COLUMN_INDEX_VAL0);

					content += "\nIt was " + oldSensorValue;
					content += "lx at " + oldTimestamp;
				}

				remoteViews.SetCharSequence(Resource.Id.livecard_content, "setText", content);
				liveCard.SetViews(remoteViews);

				// Do we need to re-publish ???
				// Unfortunately, the view does not refresh without this....
				Intent intent = new Intent(context, typeof(EnvironmentalSensorDemoActivity));
				liveCard.SetAction(PendingIntent.GetActivity(context, 0, intent, 0));
				// Is this if() necessary???? or Is it allowed/ok not to call publish() when updating????
				if(! liveCard.IsPublished) {
					liveCard.Publish(LiveCard.PublishMode.Reveal);
				} else {
					// ????
					// According to the doc,
					// it appears we should call publish() every time the content changes...
					// But, it seems to work without re-publishing...
					Log.Debug(_tag, "liveCard not published at " + now);
				}
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


		// EnvironmentalSensor methods
		private void InitializeSensorManager()
		{
			mSensorManager = (SensorManager) GetSystemService(Context.SensorService);
			mSensorLight = mSensorManager.GetDefaultSensor(SensorType.Light);
		}

		private void ProcessEnvironmentalSensorData(SensorEvent e)
		{
			long now = DateTime.UtcNow.Millisecond;
			Log.Debug(_tag, "processEnvironmentalSensorData() called at " + now);

			Sensor sensor = e.Sensor;
            SensorType type = sensor.Type;
			long timestamp = e.Timestamp;
			IList<float> values = e.Values;
			SensorStatus accuracy = e.Accuracy;
			SensorValueStruct data = new SensorValueStruct(type, timestamp, values, accuracy);

		    switch (type)
		    {
		        case SensorType.Light:
		            lastSensorValuesLight = data;
		            // lastRefreshedTime = timestamp;
		            break;
		        default:
		            Log.Warn(_tag, "Unknown type: " + type);
                    break;
		    }

		    // TBD:
			// Update the DB, etc..
			// ...

			// temporary
			SaveSensorValueEntry();
			// ...


//        // Update the UI.
//        // temporary
//        long delta = 5000L;   // every 5 seconds
//        if(lastRefreshedTime <= now - delta) {
//            // if(liveCard != null && liveCard.isPublished()) {
//                // Update...
//                publishCard(this, true);
//            // }
//            lastRefreshedTime = now;
//        }
		}


		// testing....
		private void SaveSensorValueEntry()
		{
			Log.Debug(_tag, "saveSensorValueEntry() called.");

			if(lastSensorValuesLight != null) {
				long now = DateTime.UtcNow.Millisecond;
				IList<float> val = lastSensorValuesLight.GetValues();
				if(val == null || !val.Any()) {
					// ????
					// Can this happen???
					// If this happens, there is no point of saving the entry.
					Log.Error(_tag, "lastSensorValuesLight has no value!");
					return;
				}

				// Save...
				ContentValues contentValues = new ContentValues();

				contentValues.Put(SensorValueData.SensorValues.GUID, Guid.NewGuid().ToString());
                contentValues.Put(SensorValueData.SensorValues.TYPE, SensorType.Light.ToString());
                contentValues.Put(SensorValueData.SensorValues.ACCURACY, lastSensorValuesLight.GetAccuracy().ToString());
				// if(val != null && val.length > 0) {
				Log.Debug(_tag, "Sensor value = " + val[0]);
				// TBD: What is this value 0.0???
				//      When does this happen????
				//      Do not save the entry if val[0] == 0.0 ????
				if(val[0] == 0.0f) {
					Log.Warn(_tag, "Invalid sensor value: 0.0. Skipping this record.");
					return;
				}
                contentValues.Put(SensorValueData.SensorValues.VAL0, val[0]);
				// Light sensor value is a scalar... --> only val[0] is used...
				// The following is not really needed.
				// (Also, val.length should always, I presume, be 4, and hence the if() checks are not needed.)
			    if (val.Count() > 1)
			    {
			        contentValues.Put(SensorValueData.SensorValues.VAL1, val[1]);
			        if (val.Count() > 2)
			        {
			            contentValues.Put(SensorValueData.SensorValues.VAL2, val[2]);
			            if (val.Count() > 3)
			            {
			                contentValues.Put(SensorValueData.SensorValues.VAL3, val[3]);
			            }
			        }
			    }
			    // }
                contentValues.Put(SensorValueData.SensorValues.TIMESTAMP, lastSensorValuesLight.GetTimestamp());
                contentValues.Put(SensorValueData.SensorValues.CREATEDTIME, now);
                contentValues.Put(SensorValueData.SensorValues.MODIFIEDTIME, 0L);

				Log.Debug(_tag, "Before calling getContentResolver().insert()");
				ContentResolver.Insert(SensorValueData.SensorValues.CONTENT_URI, contentValues);

			} else {
				// ???
				Log.Info(_tag, "lastSensorValuesLight is null!");
			}
		}

		private void StartHeartBeat()
		{
//			Handler handler = new Handler();
			heartBeat = new Timer (new TimerCallback (tmrThreadingTimer_TimerCallback), null, 0, 10000);
			//heartBeat.schedule(liveCardUpdateTask, 0L, 10000L); // Every 10 seconds...
		}

		public void tmrThreadingTimer_TimerCallback(object state) {
			try {
				UpdateCard (this);
			} catch (Exception e) {
				Log.Error (_tag, "Failed to run the task.", e);
			}
		}
	}
}

