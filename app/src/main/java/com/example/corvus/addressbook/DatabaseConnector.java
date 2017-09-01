package com.example.corvus.addressbook;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Corvus on 26-Aug-17.
 */

public class DatabaseConnector{

    private static final String DATABASE_NAME = "UserContacts";
    private SQLiteDatabase database;
    private DatabaseOpenHelper databaseOpenHelper;


    public DatabaseConnector(Context context){

        databaseOpenHelper = new DatabaseOpenHelper(context, DATABASE_NAME, null, 1);
    }//end connector constructor

    public void open() throws SQLException {
        database = databaseOpenHelper.getWritableDatabase();
    }

    public void close() {
        if (database != null) {
            database.close();
        }
    }


    public void addContacts(String name,  String number, String email, String street, String city){
        ContentValues newContact = new ContentValues();
        System.out.println("QRRRR" + name);
        newContact.put("name", name);
        newContact.put("phone", number);
        newContact.put("email", email);
        newContact.put("street", street);
        newContact.put("city", city);
        open();
        database.insert("contacts", null, newContact);
        close();
    }

    public void updateContact(long id, String name, String email, String number, String street, String city){
        ContentValues editContact = new ContentValues();
        editContact.put("name", name);
        editContact.put("phone", number);
        editContact.put("email", email);
        editContact.put("street", street);
        editContact.put("city", city);
        open();
        database.update("contacts", editContact, "_id=" + id, null);
        close();
    }

    public Cursor getAllContacts(){
        return database.query("contacts", new String[] {"_id", "name"}, null, null, null, null, "name");
    }

    public Cursor getOneContact(long id){
        System.out.println("-----" + String.valueOf(id));
        return database.query("contacts", null, "_id=" + id, null, null, null, null);
    }

    public void deleteContact(long id){
        open();
        database.delete("contacts", "_id=" + id, null);
        close();
    }

    private class DatabaseOpenHelper extends SQLiteOpenHelper {
        public DatabaseOpenHelper(Context context, String name, CursorFactory factory, int version){
            super(context, name, factory, version);
        }
        @Override
        public void onCreate(SQLiteDatabase db){
            String createQuery = "CREATE TABLE contacts" + "(_id integer primary key autoincrement," +
                    "name TEXT, email TEXT, phone TEXT," +
                    "street TEXT, city TEXT);";
            db.execSQL(createQuery);
        }
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){

        }

    }






}