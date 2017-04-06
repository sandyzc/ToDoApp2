package com.sandyzc.todoApp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.sandyzc.todoApp.R;
import com.sandyzc.todoApp.beans.beans;

import java.util.ArrayList;

/**
 * Created by manjula on 03-04-2017.
 */

public class AdaPter extends BaseAdapter {

    private ArrayList<beans> mTaskArrayList;
    private Context context;

    private LayoutInflater layoutInflater;

    public AdaPter(ArrayList<beans> mTaskArrayList, Context context) {
        this.mTaskArrayList = mTaskArrayList;
        this.context = context;
        layoutInflater = LayoutInflater.from(context);

    }

    @Override
    public int getCount() {
        return mTaskArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return mTaskArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void setmTaskArrayList(ArrayList<beans> mTaskArrayList) {
        this.mTaskArrayList = mTaskArrayList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = layoutInflater.inflate(R.layout.customlist, parent, false);

        TextView header = (TextView) convertView.findViewById(R.id.heading);
        TextView title = (TextView) convertView.findViewById(R.id.task);
        TextView descp = (TextView) convertView.findViewById(R.id.taskdescp);
        TextView targetdate = (TextView) convertView.findViewById(R.id.targetdate);

        ImageView imageView = (ImageView) convertView.findViewById(R.id.imageView);

        beans task = mTaskArrayList.get(position);
        header.setText(task.getDate());
        title.setText(task.getTitle());
        descp.setText(task.getDescp());
        targetdate.setText(task.getDate());

        if (task.getStatus() == 1) {
            imageView.setImageResource(R.drawable.complete);
        } else
            imageView.setImageResource(R.drawable.incomplete);


        return convertView;
    }
}
