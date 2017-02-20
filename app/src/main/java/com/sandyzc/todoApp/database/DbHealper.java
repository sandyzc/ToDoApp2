package com.sandyzc.todoApp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import com.sandyzc.todoApp.beans.beans;

/**
 * Created by manjula on 18-02-2017.
 */

public class DbHealper extends SQLiteOpenHelper{


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
        super(context,DB_NAME,null,DB_VER);
        ctx= context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String table = " CREATE TABLE IF NOT EXISTS "+TABLE_NAME + "(" +
                ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                TITLE + "TEXT," +
                DESCRIPTION + "TEXT," +
                DATE + "TEXT," +
                STATUS + "INTEGER)";

        db.execSQL(table);
        Log.i("create table","Creating table...........");
        Toast.makeText(ctx,"Database created",Toast.LENGTH_LONG).show();

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        String upgrade = "Drop Table if exists " + TABLE_NAME;
        db.execSQL(upgrade);

    }

    public void open(){

        Log.i("opendb","Checking weather database is null..........");
        if (this.database==null){
            Log.i("Database null","getting Writable database......");
            this.database = this.getWritableDatabase();
            Toast.makeText(ctx,"DATABASE is open now",Toast.LENGTH_SHORT).show();
        }

    }
    public void close(){

        if (this.database.isOpen()){
            this.database.close();
            Log.i("Close db","Database closed..........");
        }

    }

    public long insertData(beans beans){

        ContentValues values = new ContentValues();
        values.put(TITLE,beans.getTitle());
        values.put(DESCRIPTION,beans.getDescp());
        values.put(DATE,beans.getDate());

        return this.database.insert(TABLE_NAME,null,values);

    }

    public long taskcompleated(){
        ContentValues values = new ContentValues();
        values.put(STATUS,1);
        return this.database.insert(TABLE_NAME,null,values);
    }

    public long taskincompleated(){
        ContentValues values = new ContentValues();
        values.put(STATUS,0);
        return this.database.insert(TABLE_NAME,null,values);
    }


}
