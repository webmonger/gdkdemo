package com.gdkdemo.sensor.environmental.cp.core;

import java.util.HashMap;


/**
 * Global config holder.
 */
public class ConfigManager
{

    private ConfigManager()
    {
    }

    // Initialization-on-demand holder.
    private static final class ConfigManagerHolder
    {
        private static final ConfigManager INSTANCE = new ConfigManager();
    }

    // Singleton method
    public static ConfigManager getInstance()
    {
        return ConfigManagerHolder.INSTANCE;
    }


    // This should match the setting in Android manifest (content provider definitions).
    public String getDefaultDBType()
    {
        // ???
        return DBType.TYPE_GLOBALDB;
    }

    public String getDBType()
    {
        // ???
        return getDefaultDBType();
    }
    public void setDBType(String dbType)
    {
        // TBD:
        // ...
    }

    public String getDefaultSyncMode()
    {
        // ???
        return SyncMode.MODE_NOSYNC;
    }
    public void setDefaultSyncMode(String syncMode)
    {
        // TBD:
        // ...
    }

    public String getDefaultSyncType()
    {
        // ???
        return SyncType.TYPE_DUPLEX;
    }
    public void setDefaultSyncType(String syncType)
    {
        // TBD:
        // ...
    }

    public boolean isAutoFetchEnabledByDefault()
    {
        // ???
        return false;
    }


}