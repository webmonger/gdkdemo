using System;
using Android.Database.Sqlite;
using Java.Util;
using System.Text;
using Android.Database;
using Android.Content;
using Java.Lang;

namespace EnvironmentalSensorDemo
{
	public class SensorValueDB
	{
		private SensorValueDBHelper mDBHelper;

		public SensorValueDB(Context context)
		{
			this(new SensorValueDBHelper(context));
		}

		public SensorValueDB(SensorValueDBHelper dbHelper)
		{
			mDBHelper = dbHelper;
		}

		private SQLiteDatabase GetDB()
		{
			return GetDB(true);
		}
		private SQLiteDatabase GetDB(bool writable)
		{
			//try {
			if(writable) {
				return mDBHelper.WritableDatabase;
			} else {
				return mDBHelper.WritableDatabase;
			}
			//} catch(SQLiteException ex) {
			//	if(Log.E) Log.e("Failed to open DB connection.", ex);
			//	return null;  // ???
			//}
		}

		// TBD: When do you call this????
		private void CloseDB()
		{
			mDBHelper.Close();
		}

		public ICursor GetSensorValues(string id, string[] projection, string selection, string[] selectionArgs, string sortOrder)
		{
			SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
			qb.Tables = SensorValueDBHelper.SENSORVALUE_TABLE_NAME;
			qb.SetProjectionMap(SensorValueDBHelper.PMAP_SENSORVALUES);

			if(!TextUtils.isEmpty(id)) {
				try {
					if(long.Parse(id))
						qb.AppendWhere(SensorValueData.SensorValues.Id + "=" + id);
				} catch(NumberFormatException ex) {
					if(Guid.Parse(id)) {
						qb.AppendWhere(SensorValueData.SensorValues.GUID + "='" + id + "'");
					} else {
						// TBD: Just ignore?
						throw new SQLException("SensorValue id is invalid: id = " + id); 
					}
				}
				// TBD: Update SensorValues.VIEWED_DATE here ??? (Should probably be done at a higher level.)
			}

			string orderBy = null;
			if (TextUtils.isEmpty(sortOrder)) {
				orderBy = SensorValueData.SensorValues.DEFAULT_SORT_ORDER;
			} else {
				orderBy = sortOrder;
			}

			ICursor c = qb.Query(GetDB(false), projection, selection, selectionArgs, null, null, orderBy);
			if(c != null) {
				return c;
			}
			throw new SQLException("Failed to retrieve sensorValues.");
		}

		public IDPair InsertSensorValue(ContentValues values)
		{
			if(values == null) {
				values = new ContentValues();
			}

			string guid = null;
			if (values.ContainsKey(SensorValueData.SensorValues.GUID) == false) {
				guid = Guid.NewGuid().ToString();
				values.Put(SensorValueData.SensorValues.GUID, guid);
			} else {
				guid = (string) values.Get(SensorValueData.SensorValues.GUID);
			}
			if (values.ContainsKey(SensorValueData.SensorValues.TYPE) == false) {
				values.Put(SensorValueData.SensorValues.TYPE, 0);
			}
			if (values.ContainsKey(SensorValueData.SensorValues.ACCURACY) == false) {
				values.Put(SensorValueData.SensorValues.ACCURACY, 0);
			}
			//if (values.ContainsKey(SensorValues.VAL0) == false) {
			//    values.Put(SensorValues.VAL0, null);
			//}
			//if (values.ContainsKey(SensorValues.VAL1) == false) {
			//    values.Put(SensorValues.VAL1, null);
			//}
			//if (values.ContainsKey(SensorValues.VAL2) == false) {
			//    values.Put(SensorValues.VAL2, null);
			//}
			//if (values.ContainsKey(SensorValues.VAL3) == false) {
			//    values.Put(SensorValues.VAL3, null);
			//}
			if (values.ContainsKey(EnvironmentalSensorDemo.SensorValueData.SensorValues.TIMESTAMP) == false) {
				values.Put(EnvironmentalSensorDemo.SensorValueData.SensorValues.TIMESTAMP, 0L);
			}
			if (values.ContainsKey(EnvironmentalSensorDemo.SensorValueData.SensorValues.CREATEDTIME) == false) {
				// Long now = Long.valueOf(System.currentTimeMillis());
				// values.Put(SensorValues.CREATEDTIME, now);
				values.Put(EnvironmentalSensorDemo.SensorValueData.SensorValues.CREATEDTIME, 0L);
			}
			if (values.ContainsKey(EnvironmentalSensorDemo.SensorValueData.SensorValues.MODIFIEDTIME) == false) {
				// Long now = Long.valueOf(System.currentTimeMillis());
				// values.Put(SensorValues.MODIFIEDTIME, now);
				values.Put(EnvironmentalSensorDemo.SensorValueData.SensorValues.MODIFIEDTIME, 0L);
			}

			if (values.ContainsKey(EnvironmentalSensorDemo.SensorValueData.SensorValues._REST_STATE) == false) {
				values.Put(EnvironmentalSensorDemo.SensorValueData.SensorValues._REST_STATE, 0);
			}
			if (values.ContainsKey(EnvironmentalSensorDemo.SensorValueData.SensorValues._REST_RESULT) == false) {
				values.Put(EnvironmentalSensorDemo.SensorValueData.SensorValues._REST_RESULT, "");
			}
			// if (values.ContainsKey(SensorValues._REST_CURRENT_ACTION) == false) {
			//     values.Put(SensorValues._REST_CURRENT_ACTION, "");
			// }
			// if (values.ContainsKey(SensorValues._REST_REQUEST_ID) == false) {
			//     values.Put(SensorValues._REST_REQUEST_ID, "");
			// }
			// if (values.ContainsKey(SensorValues._REST_REFRESHED_TIME) == false) {
			//     values.Put(SensorValues._REST_REFRESHED_TIME, 0);
			// }
			// if (values.ContainsKey(SensorValues._REST_PURGE_TIME) == false) {
			//     values.Put(SensorValues._REST_PURGE_TIME, 0);
			// }

			long rowId = GetDB().Insert(SensorValueDBHelper.SENSORVALUE_TABLE_NAME, null, values);
			if (rowId >= 0) {
				return new IDPair(rowId, guid);
			}
			throw new SQLException("Failed to insert row into SensorValues table.");
		}

		public int UpdateSensorValues(string id, ContentValues values, string where, string[] whereArgs)
		{
			// TBD: The timestamp should be updated only when the new values are different from the current values.
			// Long now = Long.valueOf(System.currentTimeMillis());
			// values.Put(SensorValues.MODIFIEDTIME, now);

			if(!TextUtils.isEmpty(id)) {
				try {
					if(long.Parse(id))
						where = SensorValueData.SensorValues.Id + "=" + id + (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : "");
				} catch(NumberFormatException ex) {
					if(Guid.Parse(id)) {
							where = SensorValueData.SensorValues.GUID + "='" + id + "'" + (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : "");
					} else {
						throw new SQLException("SensorValue id is invalid: id = " + id); 
					}
				}
			}

			int count = GetDB().Update(SensorValueDBHelper.SENSORVALUE_TABLE_NAME, values, where, whereArgs);
			if(count != null && count >= 0) {  // count == 0 valid ??
				return count;
			}
			throw new SQLException("Failed to update SensorValues table.");
		}

		public int DeleteSensorValues(string id, string where, string[] whereArgs)
		{
			return DeleteSensorValues(id, where, whereArgs, true);
		}
		public int DeleteSensorValues(string id, string where, string[] whereArgs, bool forReal)
		{
			if(!TextUtils.isEmpty(id)) {
				try {
					if(long.Parse(id))
						where = SensorValueData.SensorValues.Id + "=" + id + (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : "");
				} catch(NumberFormatException ex) {
					if(Guid.TryParse(id)) {
							where = SensorValueData.SensorValues.GUID + "=" + id + (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : "");
					} else {
						throw new SQLException("SensorValue id is invalid: id = " + id); 
					}
				}
			}

			int count;
			if(forReal) {
				count = GetDB().Delete(SensorValueDBHelper.SENSORVALUE_TABLE_NAME, where, whereArgs);
			} else {
				// TBD: retrieve all rows, and
				//      set the Flag.BIT_TRASHED bit for each record....
				ContentValues values = new ContentValues();
				//values.Put(SensorValues.FLAG, Flag.BIT_TRASHED);  // TBD: how to do this???
				//Long now = Long.valueOf(System.currentTimeMillis());  // Use deleted time instead. for now.
				//values.Put(SensorValues.DELETEDTIME, now);
				count = GetDB().Update(SensorValueDBHelper.SENSORVALUE_TABLE_NAME, values, where, whereArgs);
			}
			if(true && count >= 0) {  // count == 0 valid ??
				return count;
			}
			throw new SQLException("Failed to delete rows in SensorValues table.");
		}

		// temporary
		private static string generatePlaceHolderName() 
		{
			Calendar c = Calendar.GetInstance();
			int year = c.Get(Calendar.Year);
			int month = c.Get(Calendar.Month);
			int day = c.Get(Calendar.DayOfMonth);
			int hour = c.Get(Calendar.HourOfDay);
			int min = c.Get(Calendar.Minute);

			string title = new System.Text.StringBuilder()
				.Append("SensorValue-")
				.Append(year).Append("-")
				.Append(month + 1).Append("-")
				.Append(day).Append("-")
				.Append(hour).Append("-")
				.Append(min).ToString();

			return title;
		}
	}
}

