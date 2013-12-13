package com.gdkdemo.sensor.environmental.cp.data;

import java.util.Calendar;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.text.TextUtils;

import com.gdkdemo.sensor.environmental.cp.core.GUID;
import com.gdkdemo.sensor.environmental.cp.core.IDPair;
import com.gdkdemo.sensor.environmental.cp.data.sensorvalue.SensorValueData.SensorValues;


public class GdkDemoSensorDemoDB
{
	private GdkDemoSensorDemoDBHelper mDBHelper;
	
    public GdkDemoSensorDemoDB(Context context)
    {
        this(new GdkDemoSensorDemoDBHelper(context));
    }
    public GdkDemoSensorDemoDB(GdkDemoSensorDemoDBHelper dbHelper)
    {
        mDBHelper = dbHelper;
    }

	private SQLiteDatabase getDB()
	{
		return getDB(true);
	}
	private SQLiteDatabase getDB(boolean writable)
	{
		//try {
			if(writable) {
				return mDBHelper.getWritableDatabase();
			} else {
				return mDBHelper.getReadableDatabase();
			}
		//} catch(SQLiteException ex) {
		//	if(Log.E) Log.e("Failed to open DB connection.", ex);
		//	return null;  // ???
		//}
	}

	// TBD: When do you call this????
	private void closeDB()
	{
		mDBHelper.close();
	}
	

    ////////////////////////////////////////////////////////////////////
    // CRUD methods for SensorValue.
    ////////////////////////////////////////////////////////////////////
	
	public Cursor getSensorValues(String id, String[] projection, String selection, String[] selectionArgs, String sortOrder)
	{
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(GdkDemoSensorDemoDBHelper.SENSORVALUE_TABLE_NAME);
        qb.setProjectionMap(GdkDemoSensorDemoDBHelper.PMAP_SENSORVALUES);
        
        if(!TextUtils.isEmpty(id)) {
            try {
                Long.parseLong(id);
                qb.appendWhere(SensorValues._ID + "=" + id);
            } catch(NumberFormatException ex) {
                if(GUID.isValid(id)) {
                    qb.appendWhere(SensorValues.GUID + "='" + id + "'");
                } else {
                    // TBD: Just ignore?
                    throw new SQLException("SensorValue id is invalid: id = " + id); 
                }
            }
            // TBD: Update SensorValues.VIEWED_DATE here ??? (Should probably be done at a higher level.)
        }

        String orderBy = null;
        if (TextUtils.isEmpty(sortOrder)) {
            orderBy = SensorValues.DEFAULT_SORT_ORDER;
        } else {
            orderBy = sortOrder;
        }

        Cursor c = qb.query(getDB(false), projection, selection, selectionArgs, null, null, orderBy);
        if(c != null) {
            return c;
        }
        throw new SQLException("Failed to retrieve sensorValues.");
	}
	
	public IDPair insertSensorValue(ContentValues values)
	{
	    if(values == null) {
	        values = new ContentValues();
	    }

        String guid = null;
        if (values.containsKey(SensorValues.GUID) == false) {
            guid = GUID.generate();
            values.put(SensorValues.GUID, guid);
        } else {
        	guid = (String) values.get(SensorValues.GUID);
        }
        if (values.containsKey(SensorValues.TYPE) == false) {
            values.put(SensorValues.TYPE, 0);
        }
        if (values.containsKey(SensorValues.ACCURACY) == false) {
            values.put(SensorValues.ACCURACY, 0);
        }
        //if (values.containsKey(SensorValues.VAL0) == false) {
        //    values.put(SensorValues.VAL0, null);
        //}
        //if (values.containsKey(SensorValues.VAL1) == false) {
        //    values.put(SensorValues.VAL1, null);
        //}
        //if (values.containsKey(SensorValues.VAL2) == false) {
        //    values.put(SensorValues.VAL2, null);
        //}
        //if (values.containsKey(SensorValues.VAL3) == false) {
        //    values.put(SensorValues.VAL3, null);
        //}
        if (values.containsKey(SensorValues.TIMESTAMP) == false) {
            values.put(SensorValues.TIMESTAMP, 0L);
        }
        if (values.containsKey(SensorValues.CREATEDTIME) == false) {
            // Long now = Long.valueOf(System.currentTimeMillis());
            // values.put(SensorValues.CREATEDTIME, now);
            values.put(SensorValues.CREATEDTIME, 0L);
        }
        if (values.containsKey(SensorValues.MODIFIEDTIME) == false) {
            // Long now = Long.valueOf(System.currentTimeMillis());
            // values.put(SensorValues.MODIFIEDTIME, now);
            values.put(SensorValues.MODIFIEDTIME, 0L);
        }

        if (values.containsKey(SensorValues._REST_STATE) == false) {
            values.put(SensorValues._REST_STATE, 0);
        }
        if (values.containsKey(SensorValues._REST_RESULT) == false) {
            values.put(SensorValues._REST_RESULT, "");
        }
        // if (values.containsKey(SensorValues._REST_CURRENT_ACTION) == false) {
        //     values.put(SensorValues._REST_CURRENT_ACTION, "");
        // }
        // if (values.containsKey(SensorValues._REST_REQUEST_ID) == false) {
        //     values.put(SensorValues._REST_REQUEST_ID, "");
        // }
        // if (values.containsKey(SensorValues._REST_REFRESHED_TIME) == false) {
        //     values.put(SensorValues._REST_REFRESHED_TIME, 0);
        // }
        // if (values.containsKey(SensorValues._REST_PURGE_TIME) == false) {
        //     values.put(SensorValues._REST_PURGE_TIME, 0);
        // }

        Long rowId = getDB().insert(GdkDemoSensorDemoDBHelper.SENSORVALUE_TABLE_NAME, null, values);
        if (rowId >= 0) {
            return new IDPair(rowId, guid);
        }
        throw new SQLException("Failed to insert row into SensorValues table.");
	}
    
    public Integer updateSensorValues(String id, ContentValues values, String where, String[] whereArgs)
    {
        // TBD: The timestamp should be updated only when the new values are different from the current values.
        // Long now = Long.valueOf(System.currentTimeMillis());
        // values.put(SensorValues.MODIFIEDTIME, now);
        
        if(!TextUtils.isEmpty(id)) {
            try {
                Long.parseLong(id);
                where = SensorValues._ID + "=" + id + (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : "");
            } catch(NumberFormatException ex) {
                if(GUID.isValid(id)) {
                    where = SensorValues.GUID + "='" + id + "'" + (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : "");
                } else {
                    throw new SQLException("SensorValue id is invalid: id = " + id); 
                }
            }
        }

        Integer count = getDB().update(GdkDemoSensorDemoDBHelper.SENSORVALUE_TABLE_NAME, values, where, whereArgs);
        if(count != null && count >= 0) {  // count == 0 valid ??
            return count;
        }
        throw new SQLException("Failed to update SensorValues table.");
    }
    
    public Integer deleteSensorValues(String id, String where, String[] whereArgs)
    {
        return deleteSensorValues(id, where, whereArgs, true);
    }
    public Integer deleteSensorValues(String id, String where, String[] whereArgs, boolean forReal)
    {
        if(!TextUtils.isEmpty(id)) {
            try {
                Long.parseLong(id);
                where = SensorValues._ID + "=" + id + (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : "");
            } catch(NumberFormatException ex) {
                if(GUID.isValid(id)) {
                    where = SensorValues.GUID + "=" + id + (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : "");
                } else {
                    throw new SQLException("SensorValue id is invalid: id = " + id); 
                }
            }
        }

        Integer count;
        if(forReal) {
            count = getDB().delete(GdkDemoSensorDemoDBHelper.SENSORVALUE_TABLE_NAME, where, whereArgs);
        } else {
            // TBD: retrieve all rows, and
            //      set the Flag.BIT_TRASHED bit for each record....
            ContentValues values = new ContentValues();
            //values.put(SensorValues.FLAG, Flag.BIT_TRASHED);  // TBD: how to do this???
            //Long now = Long.valueOf(System.currentTimeMillis());  // Use deleted time instead. for now.
            //values.put(SensorValues.DELETEDTIME, now);
            count = getDB().update(GdkDemoSensorDemoDBHelper.SENSORVALUE_TABLE_NAME, values, where, whereArgs);
        }
        if(count != null && count >= 0) {  // count == 0 valid ??
            return count;
        }
        throw new SQLException("Failed to delete rows in SensorValues table.");
    }



    // temporary
    private static String generateSensorValuePlaceHolderName() 
    {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int min = c.get(Calendar.MINUTE);

        String title = new StringBuilder()
        .append("SensorValue-")
        .append(year).append("-")
        .append(month + 1).append("-")
        .append(day).append("-")
        .append(hour).append("-")
        .append(min).toString();

        return title;
    }


}
