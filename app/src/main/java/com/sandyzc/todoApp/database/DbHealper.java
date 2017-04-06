package com.sandyzc.todoApp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import com.sandyzc.todoApp.beans.beans;

import java.util.ArrayList;



public class DbHealper extends SQLiteOpenHelper {


    private static final String DB_NAME = "ToDoData.db";
    private static final int DB_VER = 1;
    private static final String TABLE_NAME = "data";
    private static final String ID = "_id";
    private static final String TITLE = " title ";
    private static final String DESCRIPTION = " descp ";
    private static final String DATE = " date ";
    private static final String STATUS = " status ";
    private Context ctx;
    private SQLiteDatabase database;


    public DbHealper(Context context) {
        super(context, DB_NAME, null, DB_VER);
        ctx = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String table = " CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "(" +
                ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                TITLE + "TEXT," +
                DESCRIPTION + "TEXT," +
                DATE + "TEXT," +
                STATUS + "INTEGER)";

        db.execSQL(table);
        Log.i("create table", "Creating table...........");
        Toast.makeText(ctx, "Database created", Toast.LENGTH_LONG).show();

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        String upgrade = "Drop Table if exists " + TABLE_NAME;
        db.execSQL(upgrade);

    }

    public void open() {

        Log.i("opendb", "Checking weather database is null..........");
        if (this.database == null) {
            Log.i("Database null", "getting Writable database......");
            this.database = this.getWritableDatabase();
            Toast.makeText(ctx, "DATABASE is open now", Toast.LENGTH_SHORT).show();
        }

    }

    public void close() {

        if (this.database.isOpen()) {
            this.database.close();
            Log.i("Close db", "Database closed..........");
        }

    }

    public long insertData(String title, String desc, String date, int status) {

        database = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(TITLE, title);
        values.put(DESCRIPTION, desc);
        values.put(DATE, date);
        values.put(STATUS, status);

        return this.database.insert(TABLE_NAME, null, values);

    }

    public ArrayList<beans> getAllCompletedTasks() {
        ArrayList<beans> taskList = new ArrayList<>();


        database = this.getReadableDatabase();

        String selectQuery = "select * from " + TABLE_NAME + " where " + STATUS + " = 1";

        Cursor cursor = database.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {

                beans task = new beans();
                task.setId(cursor.getInt(0));
                task.setTitle(cursor.getString(1));
                task.setDescp(cursor.getString(2));
                task.setDate(cursor.getString(3));
                task.setStatus(cursor.getInt(4));
                taskList.add(task);

            } while (cursor.moveToNext());

        }
        database.close();
        return taskList;


    }

    public ArrayList<beans> getAllTasks () {
        ArrayList<beans> taskList = new ArrayList<>();



        database = this.getReadableDatabase();

        String selectQuery = "select * from " + TABLE_NAME;

        Cursor cursor = database.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {

                beans task = new beans();
                task.setId(cursor.getInt(0));
                task.setTitle(cursor.getString(1));
                task.setDescp(cursor.getString(2));
                task.setDate(cursor.getString(3));
                task.setStatus(cursor.getInt(4));

                taskList.add(task);

            } while (cursor.moveToNext());

        }

        database.close();
        return taskList;
    }

    public int updateTaskStatus (int whereID, int status) {
        database = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(STATUS, status);

        int count = database.update(TABLE_NAME, values, ID+"=?", new String[]{String.valueOf(whereID)});
        database.close();

        return count;
    }

    public int updateTask(beans theTask, String title, String description, String targetDate) {
        database = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(TITLE, title);
        values.put(DESCRIPTION, description);
        values.put(DATE, targetDate);

        int count = database.update(TABLE_NAME, values, ID+"=?", new String[]{String.valueOf(theTask.getId())});

        database.close();
        return count;
    }

    public boolean deleteTask(int id) {
        database = this.getWritableDatabase();

        database.delete(TABLE_NAME, ID+"=?", new String[]{String.valueOf(id)});

        database.close();

        return true;
    }
}


