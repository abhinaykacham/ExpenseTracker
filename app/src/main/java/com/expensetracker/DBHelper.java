/* A SQLite Helper class for performing CRUD operations */

package com.expensetracker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;
public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "Expensetracker.db";
    public static final String USERS_TABLE_NAME = "users";
    public static final String USERS_COLUMN_USERNAME = "username";
    public static final String USERS_COLUMN_EMAIL = "email";
    public static final String USERS_COLUMN_PWD = "password";
    public static final String USERS_COLUMN_PHONE = "phone";

    private static final String CREATE_TABLE_USERS = "CREATE TABLE "
            + USERS_TABLE_NAME + "(" + USERS_COLUMN_USERNAME + " TEXT PRIMARY KEY," + USERS_COLUMN_EMAIL
            + " TEXT," + USERS_COLUMN_EMAIL + " TEXT," + USERS_COLUMN_PWD
            + " TEXT" + USERS_COLUMN_PHONE + "TEXT )";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME , null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(CREATE_TABLE_USERS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS " + USERS_TABLE_NAME);
        onCreate(db);
    }


    /*
     * Creating a user
     */
    public Boolean createUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(USERS_COLUMN_USERNAME, user.getUsername());
        values.put(USERS_COLUMN_EMAIL, user.getEmail());
        values.put(USERS_COLUMN_PWD, user.getPwd());
        values.put(USERS_COLUMN_PHONE, user.getPhone());

        // insert row
        long status = db.insert(USERS_TABLE_NAME, null, values);
        if(status!=-1)
            return true;
        else
            return false;

    }

    /*
     * Checking if user already exists
     */
    public Boolean getUser(String username) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + USERS_TABLE_NAME + " WHERE "
                + USERS_COLUMN_USERNAME + " = '" + username + "'";


        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null) {
            if (c.getCount() > 0) {
                return true;
            } else {
                return false;
            }
        }
        else
            return false;
    }
    /*
     * Validate User
     */
    public Boolean validateUser(String username,String password) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + USERS_TABLE_NAME + " WHERE "
                + USERS_COLUMN_USERNAME + " = '" + username + "'" + "AND " + USERS_COLUMN_PWD + " = '" + password + "'" ;


        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null) {
            if (c.getCount() > 0) {
                return true;
            } else {
                return false;
            }
        }
        else
            return false;
    }

    /*
     * Update Password
     */
    public boolean changePassword(String username,String previousPassword, String newPassword){
        boolean result=false;
        SQLiteDatabase db;
        db=this.getWritableDatabase();

        if(validateUser(username,previousPassword)){
            final String updatePassword="UPDATE " + USERS_TABLE_NAME + " SET "
                    + USERS_COLUMN_PWD + " = '" + newPassword + "' WHERE "
                    + USERS_COLUMN_USERNAME + " = '" + username + "'";
            Cursor c=db.rawQuery(updatePassword,null);
            if (c != null) {
                if (c.getCount() > 0) {
                    result = true;
                }
            }
        }
        return result;
    }


}