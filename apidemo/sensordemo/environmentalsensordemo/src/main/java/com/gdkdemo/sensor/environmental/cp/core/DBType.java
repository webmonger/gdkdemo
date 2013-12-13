package com.gdkdemo.sensor.environmental.cp.core;


// Whether to use a single DB or separate DBs for each entity type.
public final class DBType
{
    // Use the single global app-level DB.
    public static final String TYPE_GLOBALDB = "globaldb";

    // Use separate DBs for each entity/table.
    public static final String TYPE_ENTITYDB = "entitydb";


    // Static constants only.
    private DBType() {}


    public static boolean isValid(String syncType)
    {
        if(TYPE_GLOBALDB.equals(syncType) 
        || TYPE_ENTITYDB.equals(syncType)
        ) {
            return true;
        } else {
            return false;
        }
    }


}
