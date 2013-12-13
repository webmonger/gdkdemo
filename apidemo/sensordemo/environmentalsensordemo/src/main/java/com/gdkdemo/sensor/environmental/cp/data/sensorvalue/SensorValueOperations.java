package com.gdkdemo.sensor.environmental.cp.data.sensorvalue;

import android.content.ContentProviderOperation;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;

import com.gdkdemo.sensor.environmental.cp.core.CpConstants;


/**
 * Helper class for storing data in the platform content providers.
 */
public class SensorValueOperations 
{
    private final ContentValues mValues;
    private ContentProviderOperation.Builder mBuilder;
    private final SensorValueBatchOperation mBatchOperation;
    private final Context mContext;
    private boolean mYield;
    private long mRawSensorValueId;
    private int mBackReference;
    private boolean mIsNewSensorValue;

    /**
     * Returns an instance of SensorValueOperations instance for adding SensorValue.
     * 
     * @param context the Authenticator Activity context
     * @param userId the userId of the sample SyncAdapter user object
     * @param accountName the username of the current login
     * @return instance of SensorValueOperations
     */
    public static SensorValueOperations createNewSensorValue(Context context, int userId,
        String accountName, SensorValueBatchOperation batchOperation) 
    {
        return new SensorValueOperations(context, userId, accountName, batchOperation);
    }

    /**
     * Returns an instance of SensorValueOperations for updating existing contact in
     * the platform contacts provider.
     * 
     * @param context the Authenticator Activity context
     * @param rawSensorValueId the unique Id of the existing rawSensorValue
     * @return instance of SensorValueOperations
     */
    public static SensorValueOperations updateExistingSensorValue(Context context, long rawSensorValueId,
        SensorValueBatchOperation batchOperation)
    {
        return new SensorValueOperations(context, rawSensorValueId, batchOperation);
    }

    public SensorValueOperations(Context context, SensorValueBatchOperation batchOperation)
    {
        mValues = new ContentValues();
        mYield = true;
        mContext = context;
        mBatchOperation = batchOperation;
    }

    public SensorValueOperations(Context context, int userId, String accountName,
        SensorValueBatchOperation batchOperation) 
    {
        this(context, batchOperation);
        mBackReference = mBatchOperation.size();
        mIsNewSensorValue = true;
        // mValues.put(SensorValueData.XXX, xxx);
        // ...
        mBuilder = newInsertCpo(SensorValueData.SensorValues.CONTENT_URI, true).withValues(mValues);
        mBatchOperation.add(mBuilder.build());
    }

    public SensorValueOperations(Context context, long rawSensorValueId, SensorValueBatchOperation batchOperation) 
    {
        this(context, batchOperation);
        mIsNewSensorValue = false;
        mRawSensorValueId = rawSensorValueId;
    }


    public SensorValueOperations addGuid(String guid)
    {
        mValues.clear();
        if(guid != null) {
            // mValues.put();
            // ... 
            addInsertOp();
        }
        return this;
    }
    public SensorValueOperations updateGuid(String guid, Uri uri) 
    {
        if(guid != null) {
            mValues.clear();
            // mValues.put();
            // ... 
            addUpdateOp(uri);
        }
        return this;
    }

    public SensorValueOperations addType(Integer type)
    {
        mValues.clear();
        if(type != null) {
            // mValues.put();
            // ... 
            addInsertOp();
        }
        return this;
    }
    public SensorValueOperations updateType(Integer type, Uri uri) 
    {
        if(type != null) {
            mValues.clear();
            // mValues.put();
            // ... 
            addUpdateOp(uri);
        }
        return this;
    }

    public SensorValueOperations addAccuracy(Integer accuracy)
    {
        mValues.clear();
        if(accuracy != null) {
            // mValues.put();
            // ... 
            addInsertOp();
        }
        return this;
    }
    public SensorValueOperations updateAccuracy(Integer accuracy, Uri uri) 
    {
        if(accuracy != null) {
            mValues.clear();
            // mValues.put();
            // ... 
            addUpdateOp(uri);
        }
        return this;
    }

    public SensorValueOperations addVal0(Float val0)
    {
        mValues.clear();
        if(val0 != null) {
            // mValues.put();
            // ... 
            addInsertOp();
        }
        return this;
    }
    public SensorValueOperations updateVal0(Float val0, Uri uri) 
    {
        if(val0 != null) {
            mValues.clear();
            // mValues.put();
            // ... 
            addUpdateOp(uri);
        }
        return this;
    }

    public SensorValueOperations addVal1(Float val1)
    {
        mValues.clear();
        if(val1 != null) {
            // mValues.put();
            // ... 
            addInsertOp();
        }
        return this;
    }
    public SensorValueOperations updateVal1(Float val1, Uri uri) 
    {
        if(val1 != null) {
            mValues.clear();
            // mValues.put();
            // ... 
            addUpdateOp(uri);
        }
        return this;
    }

    public SensorValueOperations addVal2(Float val2)
    {
        mValues.clear();
        if(val2 != null) {
            // mValues.put();
            // ... 
            addInsertOp();
        }
        return this;
    }
    public SensorValueOperations updateVal2(Float val2, Uri uri) 
    {
        if(val2 != null) {
            mValues.clear();
            // mValues.put();
            // ... 
            addUpdateOp(uri);
        }
        return this;
    }

    public SensorValueOperations addVal3(Float val3)
    {
        mValues.clear();
        if(val3 != null) {
            // mValues.put();
            // ... 
            addInsertOp();
        }
        return this;
    }
    public SensorValueOperations updateVal3(Float val3, Uri uri) 
    {
        if(val3 != null) {
            mValues.clear();
            // mValues.put();
            // ... 
            addUpdateOp(uri);
        }
        return this;
    }

    public SensorValueOperations addTimestamp(Long timestamp)
    {
        mValues.clear();
        if(timestamp != null) {
            // mValues.put();
            // ... 
            addInsertOp();
        }
        return this;
    }
    public SensorValueOperations updateTimestamp(Long timestamp, Uri uri) 
    {
        if(timestamp != null) {
            mValues.clear();
            // mValues.put();
            // ... 
            addUpdateOp(uri);
        }
        return this;
    }

    public SensorValueOperations addCreatedTime(Long createdTime)
    {
        mValues.clear();
        if(createdTime != null) {
            // mValues.put();
            // ... 
            addInsertOp();
        }
        return this;
    }
    public SensorValueOperations updateCreatedTime(Long createdTime, Uri uri) 
    {
        if(createdTime != null) {
            mValues.clear();
            // mValues.put();
            // ... 
            addUpdateOp(uri);
        }
        return this;
    }

    public SensorValueOperations addModifiedTime(Long modifiedTime)
    {
        mValues.clear();
        if(modifiedTime != null) {
            // mValues.put();
            // ... 
            addInsertOp();
        }
        return this;
    }
    public SensorValueOperations updateModifiedTime(Long modifiedTime, Uri uri) 
    {
        if(modifiedTime != null) {
            mValues.clear();
            // mValues.put();
            // ... 
            addUpdateOp(uri);
        }
        return this;
    }


    /**
     * Adds an insert operation into the batch
     */
    private void addInsertOp()
    {
        if (!mIsNewSensorValue) {
            mValues.put(SensorValueData.SensorValues._ID, mRawSensorValueId);   // ????
        }
        mBuilder = newInsertCpo(addCallerIsSyncAdapterParameter(SensorValueData.SensorValues.CONTENT_URI), mYield);
        mBuilder.withValues(mValues);
        if (mIsNewSensorValue) {
            mBuilder.withValueBackReference(SensorValueData.SensorValues._ID, mBackReference);
        }
        mYield = false;
        mBatchOperation.add(mBuilder.build());
    }

    /**
     * Adds an update operation into the batch
     */
    private void addUpdateOp(Uri uri)
    {
        mBuilder = newUpdateCpo(uri, mYield).withValues(mValues);
        mYield = false;
        mBatchOperation.add(mBuilder.build());
    }

    public static ContentProviderOperation.Builder newInsertCpo(Uri uri, boolean yield)
    {
        return ContentProviderOperation.newInsert(addCallerIsSyncAdapterParameter(uri))
            .withYieldAllowed(yield);
    }

    public static ContentProviderOperation.Builder newUpdateCpo(Uri uri, boolean yield)
    {
        return ContentProviderOperation.newUpdate(addCallerIsSyncAdapterParameter(uri))
            .withYieldAllowed(yield);
    }

    public static ContentProviderOperation.Builder newDeleteCpo(Uri uri, boolean yield)
    {
        return ContentProviderOperation.newDelete(addCallerIsSyncAdapterParameter(uri))
            .withYieldAllowed(yield);
    }

    private static Uri addCallerIsSyncAdapterParameter(Uri uri)
    {
        return uri.buildUpon().appendQueryParameter(CpConstants.CALLER_IS_SYNCADAPTER, "true").build();
    }

}
