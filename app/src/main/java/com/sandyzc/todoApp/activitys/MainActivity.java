package com.sandyzc.todoApp.activitys;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
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

import com.sandyzc.todoApp.R;
import com.sandyzc.todoApp.adapters.Curser_Adapter;
import com.sandyzc.todoApp.beans.beans;
import com.sandyzc.todoApp.database.DbHealper;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {


    ListView listView;
    DbHealper db;
    Curser_Adapter curseradapter;
    Cursor cursor;
    SQLiteDatabase database;
    String selected;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.list);
        db = new DbHealper(this);

        DbHealper handler = new DbHealper(this);

        database = handler.getWritableDatabase();

        cursor = database.rawQuery("SELECT  * FROM data ", null);

        curseradapter = new Curser_Adapter(this, cursor);

        listView.setAdapter(curseradapter);
        registerForContextMenu(listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                AlertDialog.Builder listdialog = new AlertDialog.Builder(MainActivity.this);
                LinearLayout linearLayout = new LinearLayout(MainActivity.this);
                linearLayout.setOrientation(LinearLayout.VERTICAL);

                final EditText Title = new EditText(MainActivity.this);
                Title.setText(cursor.getString(1));
                Title.setMaxLines(1);


                if (Title.length() < 2) {
                    Title.setError("Title Should be Minimum 3 Letters");
                }
                linearLayout.addView(Title);

                final EditText Task = new EditText(MainActivity.this);
                Task.setText(cursor.getString(2));

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
                        ContentValues vals = new ContentValues();
                      vals.put(DbHealper.TITLE,Title.getText().toString().trim());
                        vals.put(DbHealper.DESCRIPTION,Task.getText().toString().trim());
                        vals.put(DbHealper.DATE,selected);
                        String selct =String.valueOf( cursor.getInt(0));
                        database.update(DbHealper.TABLE_NAME,vals,selct,null);
                        updateUI();

                    }
                });





                listdialog.show();






            }
        });

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
        linearLayout.addView(datePicker);
        additem.setView(linearLayout);


        additem.setPositiveButton("SAVE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


                if (Title.length() > 2 && Task.length() > 2) {
                    beans task = new beans();
                    task.setTitle(Title.getText().toString());
                    task.setDescp(Task.getText().toString());
                    task.setDate(selected);
                    db.open();
                    db.insertData(task);
                    updateUI();
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
    private void updateUI() {

        Cursor update = database.rawQuery("SELECT  * FROM data ", null);
        curseradapter.changeCursor(update);

    }

    // Retreive date from datepicker
    public String getDateFromDatePicker(DatePicker datePicker) {

        final Calendar calendar = Calendar.getInstance();

        datePicker.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE), new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                selected = String.valueOf(dayOfMonth) + " / " + String.valueOf(monthOfYear + 1) + " / " + String.valueOf(year);

            }

        });
        return selected;
    }


}
