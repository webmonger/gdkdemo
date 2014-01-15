using System;
using Android.Content;
using Java.Lang;
using Android.Database;

namespace EnvironmentalSensorDemo
{
	public class SensorValueContentProvider : ContentProvider
	{
		public SensorValueContentProvider ()
		{
			sUriMatcher = new UriMatcher(UriMatcher.NoMatch);
			sUriMatcher.AddURI(AUTHORITY, "sensorvalues", SENSORVALUES);
			sUriMatcher.AddURI(AUTHORITY, "sensorvalues/*", SENSORVALUE_ID);
		}

		public static string AUTHORITY = "com.gdkdemo.sensor.environmental.cp.data.sensorvalue";

		private static int SENSORVALUES = 21;
		private static int SENSORVALUE_ID = 22;

		private static UriMatcher sUriMatcher;

		private SensorValueDB mDB;
		private bool mSyncToWeb;

		public override bool OnCreate()
		{
			mDB = new SensorValueDB(Context);
			mSyncToWeb = false;
			return true;
		}

		public string GetType(Uri uri)
		{
			switch (sUriMatcher.Match(uri)) {
			case SENSORVALUE_ID:
				return SensorValueData.SensorValues.CONTENT_ITEM_TYPE;
			case SENSORVALUES:
				return SensorValueData.SensorValues.CONTENT_TYPE;
			default:
				throw new IllegalArgumentException("Unknown URI " + uri);
			}
		}

		public override ICursor Query(Android.Net.Uri uri, string[] projection, string selection, string[] selectionArgs,
			string sortOrder)
		{
			string id = null;
			ICursor c = null;
			switch (sUriMatcher.Match(uri)) {
			case SENSORVALUE_ID:
				id = uri.PathSegments [1];
				// fall through
			case SENSORVALUES:
				c = mDB.GetSensorValues(id, projection, selection, selectionArgs, sortOrder);
				break;
			default:
				throw new IllegalArgumentException("Unknown URI " + uri);
			}

			// Tell the cursor what uri to watch, so it knows when its source data changes
			if(c != null) {
			c.SetNotificationUri(Context.ContentResolver, uri);
			}
			return c;
		}

		public override Android.Net.Uri Insert(Android.Net.Uri uri, ContentValues initialValues)
		{    	
			long rowId = null;
			Android.Net.Uri projectUri = null;
			switch (sUriMatcher.Match(uri)) {
			case SENSORVALUES:
				rowId = mDB.InsertSensorValue(initialValues).id();
				projectUri = ContentUris.WithAppendedId(SensorValueData.SensorValues.CONTENT_URI, rowId);
				break;
			default:
				throw new IllegalArgumentException("Unknown URI " + uri);
			}

			if(rowId >= 0) {
			Context.ContentResolver.NotifyChange(projectUri, null);
			}
			return projectUri;
		}

		public override int Update(Android.Net.Uri uri, ContentValues values, string where, string[] whereArgs)
		{
			string id;
			switch (sUriMatcher.Match(uri)) {
			case SENSORVALUE_ID:
				id = uri.PathSegments[1];
				break;
			case SENSORVALUES:
				id = null;
				break;
			default:
				throw new IllegalArgumentException("Unknown URI " + uri);
			}

			int count = mDB.UpdateSensorValues(id, values, where, whereArgs);
			if(count > 0) {
			Context.ContentResolver.NotifyChange(uri, null);
			}
			return count;
		}

		public override int Delete(Android.Net.Uri uri, string where, string[] whereArgs)
		{
			string id;
			switch (sUriMatcher.Match(uri)) {
			case SENSORVALUE_ID:
				id = uri.PathSegments [1];
				break;
			case SENSORVALUES:
				id = null;
				break;
			default:
				throw new IllegalArgumentException("Unknown URI " + uri);
			}

			int count = mDB.DeleteSensorValues(id, where, whereArgs);
			if(count > 0) {
			Context.ContentResolver.NotifyChange(uri, null);
			}
			return count;
		}
	}
}

