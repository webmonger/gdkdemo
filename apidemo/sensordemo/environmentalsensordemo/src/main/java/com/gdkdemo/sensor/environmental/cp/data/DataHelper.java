package com.gdkdemo.sensor.environmental.cp.data;

import com.gdkdemo.sensor.environmental.cp.core.DBType;
import com.gdkdemo.sensor.environmental.cp.core.ConfigManager;
import com.gdkdemo.sensor.environmental.cp.data.sensorvalue.SensorValueContentProvider;


/**
 * DB related util functions.
 */
public class DataHelper
{

    private DataHelper()
    {
    }

    // Initialization-on-demand holder.
    private static final class DataHelperHolder
    {
        private static final DataHelper INSTANCE = new DataHelper();
    }

    // Singleton method
    public static DataHelper getInstance()
    {
        return DataHelperHolder.INSTANCE;
    }


    public String getContentProviderAuthority(String typeName)
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
