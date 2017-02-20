package com.sandyzc.todoApp.activitys;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.sandyzc.todoApp.R;
import com.sandyzc.todoApp.adapters.Curser_Adapter;
import com.sandyzc.todoApp.beans.beans;
import com.sandyzc.todoApp.database.DbHealper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {


    ListView listView;
    DbHealper db;
    Curser_Adapter curseradapter;
    Cursor cursor;
    SQLiteDatabase database;

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
        AlertDialog.Builder additem = new AlertDialog.Builder(this);
        additem.setTitle("ADD NEW TASK");

        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        final EditText Title = new EditText(MainActivity.this);
        Title.setHint("Title");
        linearLayout.addView(Title);

        final EditText Task = new EditText(MainActivity.this);
        Task.setHint("Task");
        linearLayout.addView(Task);



        final DatePicker datePicker = new DatePicker(MainActivity.this);
        int   day  = datePicker.getDayOfMonth();
        int   month= datePicker.getMonth()+1;
        int   year = datePicker.getYear();

        DateFormat dateformat = new SimpleDateFormat("dd-mm-yy");
        final String formatteddate = dateformat.format(new Date(year,month,day));
        linearLayout.addView(datePicker);

        additem.setView(linearLayout);

        additem.setPositiveButton("SAVE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                beans task = new beans();
                task.setTitle(Title.getText().toString());
                task.setDescp(Task.getText().toString());
                task.setDate(formatteddate);
                db.open();
                db.insertData(task);

                updateUI();

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

    private void updateUI() {

        Cursor update = database.rawQuery("SELECT  * FROM data ", null);
        curseradapter.changeCursor(update);

    }
}
