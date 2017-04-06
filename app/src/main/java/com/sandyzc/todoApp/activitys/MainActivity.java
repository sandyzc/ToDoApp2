package com.sandyzc.todoApp.activitys;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.sandyzc.todoApp.DateComparator;
import com.sandyzc.todoApp.R;
import com.sandyzc.todoApp.adapters.AdaPter;
import com.sandyzc.todoApp.beans.beans;
import com.sandyzc.todoApp.database.DbHealper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {


    ListView listView;
    //db handler
    DbHealper db;

    //array for data
    ArrayList<beans> taskArraylist;

    AdaPter taskAdapter;


    //database
    SQLiteDatabase database;

    //for context menu
    private static final int MarkAsCompleated = 100;
    private static final int DelteTask = 101;

    String selected;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.list);
        db = new DbHealper(this);


        DbHealper handler = new DbHealper(this);

        database = handler.getWritableDatabase();


        taskArraylist = db.getAllTasks();
        Collections.sort(taskArraylist, new DateComparator());
        taskAdapter = new AdaPter(taskArraylist, this);


        listView.setAdapter(taskAdapter);

        registerForContextMenu(listView);

        //update task from the list

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                final beans curenttask = (beans) parent.getAdapter().getItem(position);

                final AlertDialog.Builder listdialog = new AlertDialog.Builder(MainActivity.this);


                LinearLayout linearLayout = new LinearLayout(MainActivity.this);
                linearLayout.setOrientation(LinearLayout.VERTICAL);

                final EditText Title = new EditText(MainActivity.this);
                Title.setText(curenttask.getTitle());
                Title.setMaxLines(1);


                if (Title.length() < 2) {
                    Title.setError("Title Should be Minimum 3 Letters");
                }
                linearLayout.addView(Title);

                final EditText Task = new EditText(MainActivity.this);
                Task.setText(curenttask.getDescp());

                if (Task.length() < 2) {
                    Task.setError("Enter Valid Task");
                }
                linearLayout.addView(Task);


                final DatePicker datePicker2 = new DatePicker(MainActivity.this);
                getDateFromDatePicker(datePicker2);

                linearLayout.addView(datePicker2);
                listdialog.setView(linearLayout);

                listdialog.setPositiveButton("SAVE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                        db.updateTask(curenttask, Title.getText().toString(), Task.getText().toString(), selected);

                        refreshlist();


                    }
                });

                listdialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();

                    }
                });

                listdialog.setCancelable(false);
                listdialog.show();


            }
        });

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        menu.setHeaderTitle("Choose Action");
        menu.add(0, MarkAsCompleated, 1, "I've Compleated this Task");
        menu.add(0, DelteTask, 2, "Delete This Task");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int index = info.position;
//mark the task as compleated
        if (item.getItemId() == MarkAsCompleated && item.getGroupId() == 0) {
            beans theTask = (beans) listView.getAdapter().getItem(index);

            if (theTask.getStatus() == 1) {
                return true;
            }

            int updatestaus = db.updateTaskStatus(theTask.getId(), 1);

            refreshlist();

            Toast.makeText(MainActivity.this, "Task Compleated", Toast.LENGTH_LONG).show();
            return true;

        }

        //delete the task if not intrested in the task anymore :p

        if (item.getItemId() == DelteTask && item.getGroupId() == 0) {
            beans delete = (beans) listView.getAdapter().getItem(index);

            boolean isdeleded = db.deleteTask(delete.getId());

            if (isdeleded) {

                refreshlist();
                Toast.makeText(MainActivity.this, "Task Deleted", Toast.LENGTH_SHORT).show();
            }
        }
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();

        refreshlist();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.addtask) {
            alert();
            return true;
        }
        if (id == R.id.compleated_tasks) {
            Intent intent = new Intent(MainActivity.this, TaskCompleated.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    public void alert() {
        final AlertDialog.Builder additem = new AlertDialog.Builder(this);

        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        final EditText Title = new EditText(MainActivity.this);
        Title.setMaxLines(1);


        if (Title.length() < 2) {
            Title.setError("Title Should be Minimum 3 Letters");
        }
        Title.setHint("Title");
        linearLayout.addView(Title);

        final EditText Task = new EditText(MainActivity.this);

        if (Task.length() < 2) {
            Task.setError("Enter Valid Task");
        }
        linearLayout.addView(Task);


        final DatePicker datePicker = new DatePicker(this);
        getDateFromDatePicker(datePicker);
        // final String targetDate = String.format("%02d", datePicker.getDayOfMonth()) +"/" +
        //     String.format("%02d", Integer.valueOf(datePicker.getMonth()+1)) + "/" + datePicker.getYear();
        linearLayout.addView(datePicker);
        additem.setView(linearLayout);


        additem.setPositiveButton("SAVE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


                if (Title.length() > 2 && Task.length() > 2) {

                    db.open();
                    db.insertData(Title.getText().toString(), Task.getText().toString(), selected, 0);
                    refreshlist();

                } else {
                    Toast.makeText(MainActivity.this, "Feilds cannot be empty", Toast.LENGTH_LONG).show();

                }
            }
        });

        additem.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        additem.show();


    }

    // Updates List on adding the new task

    private void refreshlist() {

        taskArraylist = db.getAllTasks();
        Collections.sort(taskArraylist, new DateComparator());
        taskAdapter.setmTaskArrayList(taskArraylist);
        taskAdapter.notifyDataSetChanged();
    }

    // Retreive date from datepicker
    public String getDateFromDatePicker(DatePicker datePicker) {

        final Calendar calendar = Calendar.getInstance();

        datePicker.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE), new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                selected = String.format("%02d", view.getDayOfMonth()) + "/" +
                        String.format("%02d", Integer.valueOf(view.getMonth() + 1)) + "/" + view.getYear();

                //  selected = String.valueOf(dayOfMonth) + " / " + String.valueOf(monthOfYear + 1) + " / " + String.valueOf(year);

            }

        });
        return selected;
    }


}
