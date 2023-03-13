package com.webmobi.gecmedia.LocalDatabase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.webmobi.gecmedia.Models.Events_Wishlist;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 1/19/2017.
 */

public class DatabaseHandler extends SQLiteOpenHelper {


    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "WishLIST";

    // Contacts table name
    private static final String TABLE_EVENTS = "Events";

    // Contacts Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_DETAILS = "details";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        //creating the table

        String CREATE_SCREENING = "CREATE TABLE " + TABLE_EVENTS + "("
                + KEY_ID + " TEXT PRIMARY KEY," + KEY_DETAILS + " TEXT" + ")";

        db.execSQL(CREATE_SCREENING);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EVENTS);

        // Create tables again
        onCreate(db);

    }

    public void addingwishlist(Events_Wishlist wishlist) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID, wishlist.getId()); // Contact Name
        values.put(KEY_DETAILS, wishlist.getDetails()); // Contact Phone Number

        // Inserting Row
        db.insert(TABLE_EVENTS, null, values);
        db.close(); // Closing database connection
    }


    public int getwishlist(String id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_EVENTS, new String[]{KEY_ID,
                        KEY_DETAILS}, KEY_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor == null)
            return 0;

        return cursor.getCount();
    }


    public List<Events_Wishlist> getAllwishlist() {
        List<Events_Wishlist> contactList = new ArrayList<Events_Wishlist>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_EVENTS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Events_Wishlist contact = new Events_Wishlist();
                contact.setId(cursor.getString(0));
                contact.setDetails(cursor.getString(1));

                // Adding contact to list
                contactList.add(contact);
            } while (cursor.moveToNext());
        }

        // return contact list
        return contactList;
    }





    public void deletewishlist(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_EVENTS, KEY_ID + " = ?",
                new String[]{String.valueOf(id)});
        db.close();
    }



}
