package com.example.ca1_assignment;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class PasswordDB extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 4;
    private static final String DATABASE_NAME = "PasswordDB";
    private static final String TABLE_CONTACTS = "Users";
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_LOCATION = "location";
    private static final String KEY_FAVOURITES = "favourites";
    private static final String KEY_imageURL = "imageURL";
    public static String strSeparator = "__,__";


    public PasswordDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        //3rd argument to be passed is CursorFactory instance
    }

    // Storing location in an array(converting array to string)
    public static String convertArrayToString(ArrayList<String> array){
        String str = "";
        for (int i = 0;i<array.size(); i++) {
            str = str+ array.get(i);
            // Do not append comma at the end of last element
            if(i<array.size()-1){
                str = str+strSeparator;
            }
        }
        return str;
    }

    // Converting string to array
    public static ArrayList<String> convertStringToArray(String str){
        ArrayList<String> arr = new ArrayList<String>(Arrays.asList(str.split(strSeparator)));
        return arr;
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_CONTACTS + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
                + KEY_PASSWORD + " TEXT," + KEY_LOCATION + " TEXT,"
                + KEY_FAVOURITES + " TEXT,"
                + KEY_imageURL + " TEXT"+ ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        // Drop older table if existed
//        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);
//
//        // Create tables again
//        onCreate(db);
        if (newVersion > oldVersion) {
            db.execSQL("ALTER TABLE Users ADD COLUMN favourites String DEFAULT NULL");
        }
    }

    // code to add the new contact
    void addContact(LoginInfo LoginInfo) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, LoginInfo.getName()); // Contact Name
        values.put(KEY_PASSWORD, LoginInfo.getPassword()); // Contact Password
        values.put(KEY_LOCATION, LoginInfo.getLocation()); // Contact Location

        // Inserting Row
        db.insert(TABLE_CONTACTS, null, values);
        //2nd argument is String containing nullColumnHack
        db.close(); // Closing database connection
    }

    // code to get the single contact
    public LoginInfo getLoginInfo(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_CONTACTS, new String[] { KEY_ID,
                        KEY_NAME, KEY_PASSWORD, KEY_LOCATION}, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        LoginInfo LoginInfo = new LoginInfo(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4));
        // return contact
        return LoginInfo;
    }

    // code to update profile picture
    public int updateURL(LoginInfo user, String URL) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_imageURL, user.getImageURL());

        // updating row
        return db.update(TABLE_CONTACTS, values, KEY_ID + " = ?",
                new String[] { String.valueOf(user.getID()) });
    }

    // code to add favourites
    public int addFavourite(LoginInfo user, String locationID) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        ArrayList<String> favouritesArr;
        Cursor cursor = db.query(TABLE_CONTACTS, new String[] {KEY_FAVOURITES}, KEY_ID + "=?",
                new String[] { String.valueOf(user.getID()) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        if (cursor.moveToFirst()) {
            favouritesArr = convertStringToArray(String.valueOf(cursor));
            favouritesArr.add(locationID);
            String updatedFav = convertArrayToString(favouritesArr);
            values.put(KEY_FAVOURITES, updatedFav);
        }

        // updating row
        return db.update(TABLE_CONTACTS, values, KEY_ID + " = ?",
                new String[] { String.valueOf(user.getID()) });
    }

    // code to remove favourites
    public int deleteFavourites(LoginInfo user, String locationID) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        ArrayList<String> updatedArr = new ArrayList<String>();
        Cursor cursor = db.query(TABLE_CONTACTS, new String[] {KEY_FAVOURITES}, KEY_ID + "=?",
                new String[] { String.valueOf(user.getID()) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        if (cursor.moveToFirst()) {
                ArrayList<String> favouritesArr = convertStringToArray(String.valueOf(cursor));
                for (int i = 0;i<favouritesArr.size(); i++) {
                    if (favouritesArr.get(i) != locationID) {
                        updatedArr.add(favouritesArr.get(i));
                    }
                }
            String updatedFav = convertArrayToString(updatedArr);
            values.put(KEY_FAVOURITES, updatedFav);
        }

        // updating row
        return db.update(TABLE_CONTACTS, values, KEY_ID + " = ?",
                new String[] { String.valueOf(user.getID()) });
    }

    // code to get all contacts in a list view
    public List<LoginInfo> getAllUsers() {
        List<LoginInfo> contactList = new ArrayList<LoginInfo>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_CONTACTS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                LoginInfo contact = new LoginInfo();
                contact.setID(Integer.parseInt(cursor.getString(0)));
                contact.setName(cursor.getString(1));
                contact.setPassword(cursor.getString(2));
                contact.setLocation(cursor.getString(3));
                // Adding contact to list
                contactList.add(contact);
            } while (cursor.moveToNext());
        }

        // return contact list
        return contactList;
    }

    // code to update the single contact
    public int updateContact(LoginInfo user, String userName, String userLoc) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID, user.getID());
        values.put(KEY_NAME, userName);
        values.put(KEY_LOCATION, userLoc);

        // updating row
        return db.update(TABLE_CONTACTS, values, KEY_ID + " = ?",
                new String[] { String.valueOf(user.getID()) });
    }

    // Deleting single contact
    public void deleteContact(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CONTACTS, KEY_ID + " = ?",
                new String[] { String.valueOf(id) });
        db.close();
    }

    // Getting contacts Count
    public int getContactsCount() {
        String countQuery = "SELECT  * FROM " + TABLE_CONTACTS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }

}
