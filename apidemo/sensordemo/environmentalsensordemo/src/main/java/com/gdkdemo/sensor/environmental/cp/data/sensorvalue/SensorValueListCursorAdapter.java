package com.gdkdemo.sensor.environmental.cp.data.sensorvalue;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.SimpleCursorAdapter;

import com.gdkdemo.sensor.environmental.cp.data.sensorvalue.SensorValueData;
import com.gdkdemo.sensor.environmental.cp.data.sensorvalue.SensorValueData.SensorValues;


public class SensorValueListCursorAdapter extends SimpleCursorAdapter implements ListAdapter
{

    public SensorValueListCursorAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags)
    {
        super(context, layout, c, from, to, flags);
    }


    @Override
    public void bindView(View view, Context context, Cursor cursor)
    {
        // TBD:
        super.bindView(view, context, cursor);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent)
    {
        // TBD:
        return super.newView(context, cursor, parent);
    }

    @Override
    public int getCount()
    {
        // TBD:
        return super.getCount();
    }

    @Override
    public Object getItem(int position)
    {
        // TBD:
        return super.getItem(position);
    }

    @Override
    public long getItemId(int position)
    {
        // TBD:
        return super.getItemId(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        // TBD:
        return super.getView(position, convertView, parent);
    }

    @Override
    public boolean hasStableIds()
    {
        // TBD:
        return super.hasStableIds();
    }

    @Override
    public Cursor runQueryOnBackgroundThread(CharSequence constraint)
    {
        // TBD:
        return super.runQueryOnBackgroundThread(constraint);
    }

    @Override
    public int getItemViewType(int position)
    {
        // TBD:
        return super.getItemViewType(position);
    }

    @Override
    public int getViewTypeCount()
    {
        // TBD:
        return super.getViewTypeCount();
    }

    @Override
    public boolean isEnabled(int position)
    {
        // TBD:
        return super.isEnabled(position);
    }


}
