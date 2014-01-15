using System;
using System.Collections;
using Android.Content;
using Android.Database.Sqlite;
using Android.Util;

namespace EnvironmentalSensorDemo
{
	public class SensorValueDBHelper : SQLiteOpenHelper
	{
		string _tag = "SensorValueDBHelper";

		private static string DATABASE_NAME = "gdkdemosensordemo.sensorvalue";
		private static int DATABASE_VERSION = 1;
		public static string SENSORVALUE_TABLE_NAME = "sensorvalues";

		public static Hashtable PMAP_SENSORVALUES = new Hashtable();

		public SensorValueDBHelper(Context context) : base(context, DATABASE_NAME, null, DATABASE_VERSION) {
			PMAP_SENSORVALUES.Add(SensorValueData.SensorValues.Id, SensorValueData.SensorValues.Id);

			PMAP_SENSORVALUES.Add(SensorValueData.SensorValues.GUID, SensorValueData.SensorValues.GUID);
			PMAP_SENSORVALUES.Add(SensorValueData.SensorValues.TYPE, SensorValueData.SensorValues.TYPE);
			PMAP_SENSORVALUES.Add(SensorValueData.SensorValues.ACCURACY, SensorValueData.SensorValues.ACCURACY);
			PMAP_SENSORVALUES.Add(SensorValueData.SensorValues.VAL0, SensorValueData.SensorValues.VAL0);
			PMAP_SENSORVALUES.Add(SensorValueData.SensorValues.VAL1, SensorValueData.SensorValues.VAL1);
			PMAP_SENSORVALUES.Add(SensorValueData.SensorValues.VAL2, SensorValueData.SensorValues.VAL2);
			PMAP_SENSORVALUES.Add(SensorValueData.SensorValues.VAL3, SensorValueData.SensorValues.VAL3);
			PMAP_SENSORVALUES.Add(SensorValueData.SensorValues.TIMESTAMP, SensorValueData.SensorValues.TIMESTAMP);
			PMAP_SENSORVALUES.Add(SensorValueData.SensorValues.CREATEDTIME, SensorValueData.SensorValues.CREATEDTIME);
			PMAP_SENSORVALUES.Add(SensorValueData.SensorValues.MODIFIEDTIME, SensorValueData.SensorValues.MODIFIEDTIME);
			PMAP_SENSORVALUES.Add(SensorValueData.SensorValues._REST_STATE, SensorValueData.SensorValues._REST_STATE);
			PMAP_SENSORVALUES.Add(SensorValueData.SensorValues._REST_RESULT, SensorValueData.SensorValues._REST_RESULT);
			PMAP_SENSORVALUES.Add(SensorValueData.SensorValues._REST_CURRENT_ACTION, SensorValueData.SensorValues._REST_CURRENT_ACTION);
			PMAP_SENSORVALUES.Add(SensorValueData.SensorValues._REST_REQUEST_ID, SensorValueData.SensorValues._REST_REQUEST_ID);
			PMAP_SENSORVALUES.Add(SensorValueData.SensorValues._REST_REFRESHED_TIME, SensorValueData.SensorValues._REST_REFRESHED_TIME);
			PMAP_SENSORVALUES.Add(SensorValueData.SensorValues._REST_PURGE_TIME, SensorValueData.SensorValues._REST_PURGE_TIME);


		}

		public override void OnCreate(SQLiteDatabase db) {
			db.ExecSQL("CREATE TABLE IF NOT EXISTS " + SENSORVALUE_TABLE_NAME + " ("
				+ SensorValueData.SensorValues.Id + " INTEGER PRIMARY KEY,"
				+ SensorValueData.SensorValues.GUID + " TEXT,"
				+ SensorValueData.SensorValues.TYPE + " INTEGER,"
				+ SensorValueData.SensorValues.ACCURACY + " INTEGER,"
				+ SensorValueData.SensorValues.VAL0 + " REAL,"
				+ SensorValueData.SensorValues.VAL1 + " REAL,"
				+ SensorValueData.SensorValues.VAL2 + " REAL,"
				+ SensorValueData.SensorValues.VAL3 + " REAL,"
				+ SensorValueData.SensorValues.TIMESTAMP + " INTEGER,"
				+ SensorValueData.SensorValues.CREATEDTIME + " INTEGER,"
				+ SensorValueData.SensorValues.MODIFIEDTIME + " INTEGER,"
				+ SensorValueData.SensorValues._REST_STATE + " INTEGER,"
				+ SensorValueData.SensorValues._REST_RESULT + " TEXT,"
				+ SensorValueData.SensorValues._REST_CURRENT_ACTION + " TEXT,"
				+ SensorValueData.SensorValues._REST_REQUEST_ID + " TEXT,"
				+ SensorValueData.SensorValues._REST_REFRESHED_TIME + " INTEGER,"
				+ SensorValueData.SensorValues._REST_PURGE_TIME + " INTEGER,"
				+ ");");
			db.ExecSQL("CREATE UNIQUE INDEX IF NOT EXISTS idx_guid ON " + SENSORVALUE_TABLE_NAME + " ("
				+ SensorValueData.SensorValues.GUID
				+ ")");

			// ...
			uploadFixtureData(db);
		}

		private void uploadFixtureData(SQLiteDatabase db) {        	
			// TBD:
			// Insert fixture data....
			// ...
			// TBD: predefined sensorValues???
			//      default project, 
			//      google tasks,
			//      slide project,
			//      ....
		}

		public override void OnUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// TBD: Data migration...
			Log.Warn(_tag, "Upgrading database from version " + oldVersion + " to "
				+ newVersion + ", which will destroy all old data");
			db.ExecSQL("DROP TABLE IF EXISTS " + SENSORVALUE_TABLE_NAME);
			OnCreate(db);
		}
	}
}

