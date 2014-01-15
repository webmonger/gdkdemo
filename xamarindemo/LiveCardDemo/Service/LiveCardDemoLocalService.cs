using System;
using Android.App;
using Android.Glass.Timeline;
using Android.OS;
using Android.Util;
using Android.Content;
using Android.Widget;

namespace LiveCardDemo
{
	[Service(Label="Name", Enabled = true)]
	public class LiveCardDemoLocalService : Service
	{
		public LiveCardDemoLocalService () : base()
		{
		}

		private string _tag = "LiveCardDemo.Service.LiveCardDemoLocalService";

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

			return true;
		}


		// For live cards...

		private void PublishCard(Context context)
		{
			Log.Debug(_tag, "publishCard() called.");
			if (liveCard == null) {
				string cardId = "livecarddemo_card";
				TimelineManager tm = TimelineManager.From(context);
				liveCard = tm.CreateLiveCard(cardId);

				liveCard.SetViews(new RemoteViews(context.PackageName, Resource.Layout.Livecard_LiveCardDemo));
				Intent intent = new Intent(context, typeof(LiveCardDemoActivity));
				liveCard.SetAction(PendingIntent.GetActivity(context, 0, intent, 0));
				liveCard.Publish(LiveCard.PublishMode.Silent);
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
	}
}

