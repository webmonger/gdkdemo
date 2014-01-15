using System;

namespace EnvironmentalSensorDemo
{
	public class DataHelper
	{
		public DataHelper ()
		{
		}

		// Initialization-on-demand holder.
		private static class DataHelperHolder
		{
			private static DataHelper INSTANCE = new DataHelper();
		}

		// Singleton method
		public static DataHelper GetInstance()
		{
			return DataHelperHolder.INSTANCE;
		}


		public string GetContentProviderAuthority(string typeName)
		{
			if(DBType.TYPE_GLOBALDB.equals(ConfigManager.getInstance().getDBType())) {
				return GdkDemoSensorDemoContentProvider.AUTHORITY;
			} else {
				if(typeName == null || typeName.isEmpty()) {
					return GdkDemoSensorDemoContentProvider.AUTHORITY;
				}
				if(typeName.equals("SensorValue")) {
					return SensorValueContentProvider.AUTHORITY;
				}
				return GdkDemoSensorDemoContentProvider.AUTHORITY;
			}
		}
	}
}

