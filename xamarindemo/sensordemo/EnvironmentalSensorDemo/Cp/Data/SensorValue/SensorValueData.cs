using System;
using Android.Provider;
using Android.OS;

namespace EnvironmentalSensorDemo
{
	public class SensorValueData
	{
		public SensorValueData ()
		{
		}

		public class SensorValues : BaseColumns
		{
			// This class cannot be instantiated

			public static Android.Net.Uri CONTENT_URI = Uri.Parse("content://" + DataHelper.GetInstance().GetContentProviderAuthority("SensorValue") + "/sensorvalues");
			public static string CONTENT_TYPE = "vnd.android.cursor.dir/vnd.gdkdemosensordemo.sensorvalue";
			public static string CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.gdkdemosensordemo.sensorvalue";
			public static string DEFAULT_SORT_ORDER = "createdTime desc";

			///////////////////////////////////
			// Column definitions
			///////////////////////////////////

			// string
			public static string GUID = "guid";

			// Integer
			public static string TYPE = "type";

			// Integer
			public static string ACCURACY = "accuracy";

			// Float
			public static string VAL0 = "val0";

			// Float
			public static string VAL1 = "val1";

			// Float
			public static string VAL2 = "val2";

			// Float
			public static string VAL3 = "val3";

			// Long
			public static string TIMESTAMP = "timestamp";

			// Long
			public static string CREATEDTIME = "createdTime";

			// Long
			public static string MODIFIEDTIME = "modifiedTime";


			// The following fields are added 
			// for bookkeeping purposes during "sync'ing"

			// Integer. Bit mask, for POST, PUT, DELETE. (Currently, no more than one bit can be set. ???)
			public static string _REST_STATE = "_rest_state";

			// string. Result (HTTP status code) of the last HTTP method.
			public static string _REST_RESULT = "_rest_result";

			// string. Last HTTP action/method which is currently ongoing. Default null/"".
			// Note that this can be null even when _REST_STATE != 0.
			// When the action is successful, both _REST_STATE and _REST_CURRENT_ACTION should be cleared.
			public static string _REST_CURRENT_ACTION = "_rest_current_action";

			// string. To be used by the calling/requesting activity to check the status of the request... ???
			public static string _REST_REQUEST_ID = "_rest_request_id";

			// Long. Last time when the record has been refreshed from the server.
			public static string _REST_REFRESHED_TIME = "_rest_refreshed_time";

			// Long. 0 means temporary. -1 means no expiration.
			public static string _REST_PURGE_TIME = "_rest_purge_time";


		}
	}
}

