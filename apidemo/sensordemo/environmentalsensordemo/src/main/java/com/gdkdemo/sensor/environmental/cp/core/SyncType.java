package com.gdkdemo.sensor.environmental.cp.core;


// TBD: Determines "Synchronization algorithm".
// Note that different SyncTypes can be used for different entities/tables.
public final class SyncType
{
    // One way. Downstream only. Sync the data on device from the cloud.
    public static final String TYPE_DOWNSTREAM = "downstream";

    // One way. Upstream only. Sync/back up the device data to the cloud
    public static final String TYPE_UPSTREAM = "upstream";

    // Both ways/Duplex. Sync based on timestamps???? (Note that server - device times may not have been synchronized.)
    // How to handle merge, version conflict, etc. ????
    public static final String TYPE_DUPLEX = "duplex";

    // Device primary. Only sync the data which is missing from device.
    public static final String TYPE_DEVICE = "device";

    // Cloud primary. Always sync the device data from the cloud. (Upstream sync is done only when the data/entity is missing on the cloud???? Or, Data on the device only is left alone, No sync.)
    public static final String TYPE_CLOUD = "cloud";


    // Static constants only.
    private SyncType() {}


    public static boolean isValid(String syncType)
    {
        if(TYPE_DOWNSTREAM.equals(syncType) 
        || TYPE_UPSTREAM.equals(syncType)
        || TYPE_DUPLEX.equals(syncType)
        || TYPE_DEVICE.equals(syncType)
        || TYPE_CLOUD.equals(syncType)
        ) {
            return true;
        } else {
            return false;
        }
    }


}
