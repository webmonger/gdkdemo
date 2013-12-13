package com.gdkdemo.sensor.environmental.cp.data.sensorvalue;

import android.net.Uri;
import android.provider.BaseColumns;

import com.gdkdemo.sensor.environmental.cp.data.DataHelper;


public final class SensorValueData
{
    // This class cannot be instantiated
    private SensorValueData() {}

    /**
     * SensorValue table
     */
    public static final class SensorValues implements BaseColumns
    {
        // This class cannot be instantiated
        private SensorValues() {}

        public static final Uri CONTENT_URI = Uri.parse("content://" + DataHelper.getInstance().getContentProviderAuthority("SensorValue") + "/sensorvalues");
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.gdkdemosensordemo.sensorvalue";
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.gdkdemosensordemo.sensorvalue";
        public static final String DEFAULT_SORT_ORDER = "createdTime desc";

        ///////////////////////////////////
        // Column definitions
        ///////////////////////////////////

        // String
        public static final String GUID = "guid";

        // Integer
        public static final String TYPE = "type";

        // Integer
        public static final String ACCURACY = "accuracy";

        // Float
        public static final String VAL0 = "val0";

        // Float
        public static final String VAL1 = "val1";

        // Float
        public static final String VAL2 = "val2";

        // Float
        public static final String VAL3 = "val3";

        // Long
        public static final String TIMESTAMP = "timestamp";

        // Long
        public static final String CREATEDTIME = "createdTime";

        // Long
        public static final String MODIFIEDTIME = "modifiedTime";


        // The following fields are added 
        // for bookkeeping purposes during "sync'ing"

        // Integer. Bit mask, for POST, PUT, DELETE. (Currently, no more than one bit can be set. ???)
        public static final String _REST_STATE = "_rest_state";

        // String. Result (HTTP status code) of the last HTTP method.
        public static final String _REST_RESULT = "_rest_result";

        // String. Last HTTP action/method which is currently ongoing. Default null/"".
        // Note that this can be null even when _REST_STATE != 0.
        // When the action is successful, both _REST_STATE and _REST_CURRENT_ACTION should be cleared.
        public static final String _REST_CURRENT_ACTION = "_rest_current_action";

        // String. To be used by the calling/requesting activity to check the status of the request... ???
        public static final String _REST_REQUEST_ID = "_rest_request_id";

        // Long. Last time when the record has been refreshed from the server.
        public static final String _REST_REFRESHED_TIME = "_rest_refreshed_time";

        // Long. 0 means temporary. -1 means no expiration.
        public static final String _REST_PURGE_TIME = "_rest_purge_time";


	}
    
}
