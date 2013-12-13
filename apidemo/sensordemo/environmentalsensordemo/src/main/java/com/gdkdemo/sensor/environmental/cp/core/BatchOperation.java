package com.gdkdemo.sensor.environmental.cp.core;

import java.util.ArrayList;
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.Context;
import android.content.OperationApplicationException;
import android.os.RemoteException;
import android.provider.ContactsContract;


/**
 * This class handles execution of batch operations on content provider.
 */
public class BatchOperation 
{
    private final Context mContext;
    private final ContentResolver mResolver;
    private final String mCpAuthority;

    // List for storing the batch mOperations
    private final ArrayList<ContentProviderOperation> mOperations;

    public BatchOperation(Context context, ContentResolver resolver, String cpAuthority)
    {
        mContext = context;
        mResolver = resolver;
        mCpAuthority = cpAuthority;
        mOperations = new ArrayList<ContentProviderOperation>();
    }

    public int size()
    {
        return mOperations.size();
    }

    public void add(ContentProviderOperation cpo)
    {
        mOperations.add(cpo);
    }

    public void execute() 
    {
        if (mOperations.size() == 0) {
            return;
        }
        // Apply the mOperations to the content provider
        try {
            mResolver.applyBatch(mCpAuthority, mOperations);
        } catch (final OperationApplicationException e1) {
            Log.e("storing contact data failed", e1);
        } catch (final RemoteException e2) {
            Log.e("storing contact data failed", e2);
        }
        mOperations.clear();
    }
}
