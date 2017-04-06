package com.sandyzc.todoApp.activitys;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.sandyzc.todoApp.DateComparator;
import com.sandyzc.todoApp.R;
import com.sandyzc.todoApp.adapters.AdaPter;
import com.sandyzc.todoApp.beans.beans;
import com.sandyzc.todoApp.database.DbHealper;

import java.util.ArrayList;
import java.util.Collections;



public class TaskCompleated extends Activity {

    // for displaying the list
    ListView listView;
    DbHealper db;
    SQLiteDatabase database;
    AdaPter adaPter;
    //for data
    ArrayList<beans> maArrayList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        listView = (ListView)findViewById(R.id.list);
        db = new DbHealper(this);

        database = db.getReadableDatabase();

        maArrayList=db.getAllCompletedTasks();
        Collections.sort(maArrayList, new DateComparator());
        adaPter =new AdaPter(maArrayList,this);

        listView.setAdapter(adaPter);


        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                beans compleatedtask = (beans)parent.getAdapter().getItem(position);

                boolean isdeleted = db.deleteTask(compleatedtask.getId());

                if (isdeleted){
                    refreshlist();
                    Toast.makeText(TaskCompleated.this,"Compleated task Deleted",Toast.LENGTH_LONG).show();
                }
                return true;
            }
        });


    }
    private void refreshlist() {

        maArrayList = db.getAllCompletedTasks();
        Collections.sort(maArrayList, new DateComparator());
        adaPter.setmTaskArrayList(maArrayList);
        adaPter.notifyDataSetChanged();
    }



}


