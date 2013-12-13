package com.gdkdemo.sensor.environmental.cp.data.sensorvalue;

import java.util.ArrayList;
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.Context;
import android.content.OperationApplicationException;
import android.os.RemoteException;
import android.provider.ContactsContract;

import com.gdkdemo.sensor.environmental.cp.core.BatchOperation;
import com.gdkdemo.sensor.environmental.cp.data.DataHelper;


/**
 * This class handles execution of batch operations on content provider.
 */
final public class SensorValueBatchOperation extends BatchOperation
{

    public SensorValueBatchOperation(Context context, ContentResolver resolver)
    {
        super(context, resolver, DataHelper.getInstance().getContentProviderAuthority("SensorValue"));
    }

}
