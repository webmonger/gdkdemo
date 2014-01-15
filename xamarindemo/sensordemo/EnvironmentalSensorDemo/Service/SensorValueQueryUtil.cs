using System;

namespace EnvironmentalSensorDemo
{
	public class SensorValueQueryUtil
	{
		public static string[] PROJECTION = new string[] {
			Android.Provider.BaseColumns.Id,             // 0
			SensorValueData.SensorValues.GUID,       // 1
			SensorValueData.SensorValues.TYPE,       // 2
			SensorValueData.SensorValues.ACCURACY,       // 3
			SensorValueData.SensorValues.VAL0,       // 4
			SensorValueData.SensorValues.VAL1,       // 5
			SensorValueData.SensorValues.VAL2,       // 6
			SensorValueData.SensorValues.VAL3,       // 7
			SensorValueData.SensorValues.TIMESTAMP,       // 8
			SensorValueData.SensorValues.CREATEDTIME,       // 9
			SensorValueData.SensorValues.MODIFIEDTIME,       // 10
		};

		public static int COLUMN_INDEX_GUID = 1;
		public static int COLUMN_INDEX_TYPE = 2;
		public static int COLUMN_INDEX_ACCURACY = 3;
		public static int COLUMN_INDEX_VAL0 = 4;
		public static int COLUMN_INDEX_VAL1 = 5;
		public static int COLUMN_INDEX_VAL2 = 6;
		public static int COLUMN_INDEX_VAL3 = 7;
		public static int COLUMN_INDEX_TIMESTAMP = 8;
		public static int COLUMN_INDEX_CREATEDTIME = 9;
		public static int COLUMN_INDEX_MODIFIEDTIME = 10;


	}
}

