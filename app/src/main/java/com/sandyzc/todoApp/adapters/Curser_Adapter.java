package com.sandyzc.todoApp.adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.sandyzc.todoApp.R;

/**
 * Created by manjula on 18-02-2017.
 */

public class Curser_Adapter extends CursorAdapter {

    public Curser_Adapter(Context context, Cursor c) {
        super(context, c);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.customlist, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        TextView taskk = (TextView) view.findViewById(R.id.task);
        TextView taskdesc = (TextView) view.findViewById(R.id.taskdescp);
        TextView targetdate = (TextView) view.findViewById(R.id.targetdate);


        String task = cursor.getString(1);
        String descp = cursor.getString(3);
        String date = cursor.getString(2);

        taskk.setText(task);
        taskdesc.setText(descp);
        targetdate.setText(date);



    }
}
