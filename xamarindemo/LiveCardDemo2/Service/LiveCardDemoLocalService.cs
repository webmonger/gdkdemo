using System;
using Android.App;
using Android.Glass.Timeline;
using Android.OS;
using Android.Util;
using Android.Content;
using Android.Widget;
using System.Threading;

namespace LiveCardDemo2
{
	[Service(Label="Name", Enabled = true)]
	public class LiveCardDemoLocalService : Service
	{
		public LiveCardDemoLocalService () : base()
		{
		}

		private string _tag = "LiveCardDemo.Service.LiveCardDemoLocalService";

		int countOfCalls = 0;

		// "Life cycle" constants

		// [1] Starts from this..
		private static int STATE_NORMAL = 1;

		// [2] When panic action has been triggered by the user.
		private static int STATE_PANIC_TRIGGERED = 2;

		// [3] Note that cancel, or successful send, etc. change the state back to normal
		// These are intermediate states...
		private static int STATE_CANCEL_REQUESTED = 4;
		private static int STATE_CANCEL_PROCESSED = 8;
		private static int STATE_PANIC_PROCESSED = 16;
		// ....

		// Global "state" of the service.
		// Currently not being used...
		private int currentState;


		// For live card
		private LiveCard liveCard;

		private static string cardId = "livecarddemo2_card";

		// "Heart beat".
		private Timer heartBeat = null;

		static LiveCardDemoLocalService _liveCardDemoLocalService = new LiveCardDemoLocalService();

		// No need for IPC...
		public class LocalBinder : Binder {
			public LiveCardDemoLocalService GetService() {
				return _liveCardDemoLocalService;
			}
		}
		private IBinder mBinder = new LocalBinder();

		public override void OnCreate()
		{
			base.OnCreate();

			currentState = STATE_NORMAL;

			IntentFilter filter = new IntentFilter();
			filter.AddAction("android.provider.xxx");   // TBD:..

//        if(heartBeat == null) {
//            heartBeat = new Timer();
//        }
//        startHeartBeat();
		}

		public override StartCommandResult OnStartCommand(Intent intent, StartCommandFlags flags, int startId)
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
			OnServiceStop();

			base.OnDestroy();
		}


		// Service state handlers.
		// ....

		private bool OnServiceStart()
		{
			Log.Debug(_tag, "onServiceStart() called.");

			// TBD:
			// Publish live card...
			// ....
			PublishCard(this);
//			if(heartBeat == null) {
//				heartBeat = new Timer(new TimerCallback (tmrThreadingTimer_TimerCallback), null, System.Threading.Timeout.Infinite, 10000);
//			}
			StartHeartBeat();
			// ....

			currentState = STATE_NORMAL;
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

			// TBD:
			// Unpublish livecard here
			// .....
			UnpublishCard(this);
			// ...

			// Stop the heart beat.
			// ???
			// onServiceStop() is called when the service is destroyed.... ??? Need to check
			if(heartBeat != null) {
				heartBeat.Dispose();
			}
			// ...


			return true;
		}


		// For live cards...

		private void PublishCard(Context context)
		{
			Log.Debug(_tag, "publishCard() called.");
			if (liveCard == null) {
				String cardId = "livecarddemo_card";
				TimelineManager tm = TimelineManager.From(context);
				liveCard = tm.CreateLiveCard(cardId);

				RemoteViews remoteViews = new RemoteViews(context.PackageName, Resource.Layout.LiveCard_LiveCardDemo2);
				liveCard.SetViews(remoteViews);
				Intent intent = new Intent(context, typeof(LiveCardDemoActivity));
				liveCard.SetAction(PendingIntent.GetActivity(context, 0, intent, 0));
				liveCard.Publish(LiveCard.PublishMode.Silent);
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
				RemoteViews remoteViews = new RemoteViews(context.PackageName, Resource.Layout.LiveCard_LiveCardDemo2);
				string content = "";

				remoteViews.SetTextViewText (Resource.Id.livecard_count, countOfCalls.ToString());

				// testing
				string now = DateTime.UtcNow.ToString();
				content = "Updated: " + now;
				// ...

				remoteViews.SetCharSequence(Resource.Id.livecard_content, "setText", content);
				liveCard.SetViews(remoteViews);

				// Do we need to re-publish ???
				// Unfortunately, the view does not refresh without this....
				Intent intent = new Intent(context, typeof(LiveCardDemoActivity));
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

		private void StartHeartBeat()
		{
//			Handler handler = new Handler();
			heartBeat = new Timer (new TimerCallback (tmrThreadingTimer_TimerCallback), null, 0, 10000);
			                           //heartBeat.schedule(liveCardUpdateTask, 0L, 10000L); // Every 10 seconds...
		}

		public void tmrThreadingTimer_TimerCallback(object state) {
			countOfCalls++;
//			handler.post(new Runnable() {
//				public void run() {
			try {
				UpdateCard (_liveCardDemoLocalService);
			} catch (Exception e) {
				Log.Error (_tag, "Failed to run the task.", e);
			}
//				}
//			});
		}
	}
}

