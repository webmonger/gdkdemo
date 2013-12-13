package com.gdkdemo.sensor.environmental.cp.core;


public final class SyncMode
{
    public static final String MODE_NOSYNC = "nosync";
    public static final String MODE_ADAPTER = "adapter";
    public static final String MODE_DIRECT = "direct";

    // Static constants only.
    private SyncMode() {}


    public static boolean isValid(String syncMode)
    {
        if(MODE_NOSYNC.equals(syncMode) 
        || MODE_ADAPTER.equals(syncMode)
        || MODE_DIRECT.equals(syncMode)) {
            return true;
        } else {
            return false;
        }
    }


}
