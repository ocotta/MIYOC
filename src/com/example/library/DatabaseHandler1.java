package com.example.library;

import java.util.HashMap;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;



public class DatabaseHandler1 extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "seetoh88_mp18";

    // Login table name
    private static final String TABLE_PARCEL = "parcel";

    // Login Table Columns names

    private static final String KEY_ORDERID = "orderid";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_DETAILS = "details";
    


    public DatabaseHandler1(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    // Creating Parcel Tables
    public void onCreate(SQLiteDatabase db) {
        String CREATE_PARCEL_TABLE = "CREATE TABLE " + TABLE_PARCEL + "("
                + KEY_ORDERID + " INTEGER PRIMARY KEY,"
                + KEY_EMAIL + " TEXT,"
                + KEY_DETAILS + " TEXT," + ")";
        db.execSQL(CREATE_PARCEL_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PARCEL);

        // Create tables again
        onCreate(db);
    }

  

    public HashMap<String, String> getUserDetails(){
        HashMap<String,String> user = new HashMap<String,String>();
        String selectQuery = "SELECT  * FROM " + TABLE_PARCEL;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        if(cursor.getCount() > 0){
            user.put("orderid", cursor.getString(1));
            user.put("email", cursor.getString(2));
            user.put("details", cursor.getString(3));
        }
        cursor.close();
        db.close();
        // return user
        return user;
    }


    /**
     * Getting user login status
     * return true if rows are there in table
     * */
    public int getRowCount() {
        String countQuery = "SELECT  * FROM " + TABLE_PARCEL;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int rowCount = cursor.getCount();
        db.close();
        cursor.close();

        // return row count
        return rowCount;
    }


    /**
     * Re crate database
     * Delete all tables and create them again
     * */
    public void resetTables(){
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        db.delete(TABLE_PARCEL, null, null);
        db.close();
    }

}