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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "Expensetracker.db";
    public static final String USERS_TABLE_NAME = "users";
    public static final String USERS_COLUMN_USERNAME = "username";
    public static final String USERS_COLUMN_EMAIL = "email";
    public static final String USERS_COLUMN_PWD = "password";
    public static final String USERS_COLUMN_PHONE = "phone";
    public static final String USERS_COLUMN_ANNUAL_INCOME="annual_income";
    public static final String USERS_COLUMN_DESIRED_SAVING="desired_saving";
    public static final String USERS_COLUMN_MAXIMUM_DAILY_EXPENSE="maximum_daily_expense";

    public static final String SAVED_EXPENSES_TABLE_NAME="saved_expenses";
    public static final String SAVED_EXPENSES_COLUMN_EXPENSE_ID="saved_expense_id";
    public static final String SAVED_EXPENSES_COLUMN_EXPENSE_NAME="expense_name";
    public static final String SAVED_EXPENSES_COLUMN_EXPENSE_AMOUNT="expense_amount";
    public static final String SAVED_EXPENSES_COLUMN_USER="username";
    public static final String SAVED_EXPENSES_COLUMN_CREATED_DATE ="created_date";

    public static final String DAILY_EXPENSES_TABLE_NAME ="daily_expenses";
    public static final String DAILY_EXPENSES_COLUMN_EXPENSE_ID="daily_expense_id";
    public static final String DAILY_EXPENSES_COLUMN_CREATED_DATE="created_date";
    public static final String DAILY_EXPENSES_COLUMN_SAVED_EXPENSE_ID="saved_expense_id";
    public static final String DAILY_EXPENSES_COLUMN_AMOUNT="expense_amount";


    private static final String CREATE_TABLE_SAVED_EXPENSES="CREATE TABLE "
            + SAVED_EXPENSES_TABLE_NAME + "(" + SAVED_EXPENSES_COLUMN_EXPENSE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
            + SAVED_EXPENSES_COLUMN_EXPENSE_NAME  + " TEXT ,"
            + SAVED_EXPENSES_COLUMN_EXPENSE_AMOUNT + " INTEGER, "
            + SAVED_EXPENSES_COLUMN_USER + " TEXT, "
            + SAVED_EXPENSES_COLUMN_CREATED_DATE + " TEXT, "
            + " FOREIGN KEY ("+SAVED_EXPENSES_COLUMN_USER+") REFERENCES "+USERS_TABLE_NAME+"("+USERS_COLUMN_USERNAME+"))";


    private static final String CREATE_TABLE_USERS = "CREATE TABLE "
            + USERS_TABLE_NAME + "(" + USERS_COLUMN_USERNAME + " TEXT PRIMARY KEY ,"
            + USERS_COLUMN_EMAIL + " TEXT,"
            + USERS_COLUMN_PWD + " TEXT,"
            + USERS_COLUMN_PHONE + " TEXT,"
            + USERS_COLUMN_ANNUAL_INCOME + " INTEGER, "
            + USERS_COLUMN_MAXIMUM_DAILY_EXPENSE + " INTEGER , "
            + USERS_COLUMN_DESIRED_SAVING + " INTEGER)";

    private static final String CREATE_TABLE_DAILY_EXPENSES = "CREATE TABLE "
            + DAILY_EXPENSES_TABLE_NAME + "(" + DAILY_EXPENSES_COLUMN_EXPENSE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
            + DAILY_EXPENSES_COLUMN_CREATED_DATE  + " TEXT ,"
            + DAILY_EXPENSES_COLUMN_SAVED_EXPENSE_ID + " INTEGER, "
            + DAILY_EXPENSES_COLUMN_AMOUNT + " INTEGER,"
            + " FOREIGN KEY ("+DAILY_EXPENSES_COLUMN_SAVED_EXPENSE_ID+") " +
              " REFERENCES "+SAVED_EXPENSES_TABLE_NAME+" ( "+SAVED_EXPENSES_COLUMN_EXPENSE_ID+"))";


    public DBHelper(Context context) {
        super(context, DATABASE_NAME , null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(CREATE_TABLE_USERS);
        db.execSQL(CREATE_TABLE_SAVED_EXPENSES);
        db.execSQL(CREATE_TABLE_DAILY_EXPENSES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS " + DAILY_EXPENSES_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + SAVED_EXPENSES_TABLE_NAME);
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

    /**
     * Change @param previousPassword to @param newPassword of
     * @param username
     * @return true if password has been changed and vice versa
     */
    public boolean changePassword(String username,String previousPassword, String newPassword){
        boolean result=false;
        SQLiteDatabase db;
        db=this.getWritableDatabase();
        if(validateUser(username,previousPassword)){
            final String updatePassword="UPDATE " + USERS_TABLE_NAME + " SET "
                    + USERS_COLUMN_PWD + " = '" + newPassword + "' WHERE"
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

    /**
     * Updating
     * @param annualIncome and @param desiredSaving
     * of @param username
     * @return true if successful and vice versa
     */
    public boolean updateFinances(String username,int annualIncome,int desiredSaving, int maximumDailyExpense){
        boolean result=false;
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        final String updateAmounts="UPDATE " + USERS_TABLE_NAME + " SET "
                +USERS_COLUMN_ANNUAL_INCOME + " = " + annualIncome + " , "
                +USERS_COLUMN_DESIRED_SAVING+ " = " + desiredSaving+ " , "
                +USERS_COLUMN_MAXIMUM_DAILY_EXPENSE + " = " + maximumDailyExpense + " "
                +" WHERE " + USERS_COLUMN_USERNAME + " = '" +username + "'";
        Cursor c = sqLiteDatabase.rawQuery(updateAmounts, null);

        if (c != null && (c.getCount() > 0)) {
            result = true;
        }
        return result;
    }

    /**
     * Adding saved
     * @param expense of the users
     * @return true if expense is added, false if expense could not be added/
     */
    public boolean addSavedExpense(Expense expense){
        boolean result;
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(SAVED_EXPENSES_COLUMN_EXPENSE_NAME, expense.getExpenseName());
        values.put(SAVED_EXPENSES_COLUMN_EXPENSE_AMOUNT, expense.getExpenseAmount());
        values.put(SAVED_EXPENSES_COLUMN_USER, expense.getUsername());
        values.put(SAVED_EXPENSES_COLUMN_CREATED_DATE, expense.getCreatedDate());

        // insert row
        long status = sqLiteDatabase.insert(SAVED_EXPENSES_TABLE_NAME, null, values);

        return status != -1;
    }

    /**
     * Adding daily
     * @param dailyExpense of the user
     * @return true if expense is added, false if expense could not be added/
     */
    public boolean addDailyExpense(Expense dailyExpense){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DAILY_EXPENSES_COLUMN_AMOUNT, dailyExpense.getExpenseAmount());
        values.put(DAILY_EXPENSES_COLUMN_CREATED_DATE,dailyExpense.getCreatedDate());
        values.put(DAILY_EXPENSES_COLUMN_SAVED_EXPENSE_ID,dailyExpense.getSavedExpenseID());
        // insert row
        long status = sqLiteDatabase.insert(DAILY_EXPENSES_TABLE_NAME, null, values);

        return status != -1;
    }

    /**
     * removes expense with
     * @param dailyExpenseId
     * @return true if expense got deleted, false if expense could not be deleted
     */
    public boolean deleteDailyExpense(int dailyExpenseId){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.setForeignKeyConstraintsEnabled(true);

        return sqLiteDatabase.delete(DAILY_EXPENSES_TABLE_NAME,DAILY_EXPENSES_COLUMN_EXPENSE_ID+" = "+dailyExpenseId,null)>0;
    }

    /**
     * removes expense with
     * @param savedExpenseId
     * @return true if expense got deleted, false if expense could not be deleted
     */
    public boolean deleteSavedExpense(int savedExpenseId){
        SQLiteDatabase sqLiteDatabase=this.getWritableDatabase();
        sqLiteDatabase.setForeignKeyConstraintsEnabled(true);
        return sqLiteDatabase.delete(SAVED_EXPENSES_TABLE_NAME,SAVED_EXPENSES_COLUMN_EXPENSE_ID+savedExpenseId,null)>0;
    }

    /**
     * updates DAILY_EXPENSES Table with
     * @param dailyExpenses
     * @return number of rows affected
     */
    public int updateExpense(Expense dailyExpenses){
        SQLiteDatabase sqLiteDatabase=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(DAILY_EXPENSES_COLUMN_AMOUNT,dailyExpenses.getExpenseAmount());
        return sqLiteDatabase.update(DAILY_EXPENSES_TABLE_NAME,contentValues,
                DAILY_EXPENSES_COLUMN_EXPENSE_ID+" = "+dailyExpenses.getDailyExpenseID(),
                null);
    }

    /**
     * updates SAVED_EXPENSE Table with
     * @param savedExpense
     * @return number of rows affected
     */
    public int updateSavedExpense(Expense savedExpense){
        SQLiteDatabase sqLiteDatabase=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(SAVED_EXPENSES_COLUMN_EXPENSE_AMOUNT,savedExpense.getExpenseAmount());
        contentValues.put(SAVED_EXPENSES_COLUMN_EXPENSE_NAME,savedExpense.getExpenseName());
        return sqLiteDatabase.update(SAVED_EXPENSES_TABLE_NAME,contentValues,
                SAVED_EXPENSES_COLUMN_EXPENSE_ID+" = "+savedExpense.getSavedExpenseID(),
                null);
    }

    /**
     * Takes @param username as input and
     * @return list of saved expenses of that user
     */
    public List<Expense> fetchSavedExpense(String username){
        List<Expense> savedExpenseList=new ArrayList<>();
        SQLiteDatabase sqLiteDatabase=this.getReadableDatabase();
        final String selectSavedExpense=" SELECT * FROM " + SAVED_EXPENSES_TABLE_NAME
                + " WHERE " + SAVED_EXPENSES_COLUMN_USER
                + " = '"+ username +"'";

        Cursor cursor=sqLiteDatabase.rawQuery(selectSavedExpense,null);

        if (cursor.moveToFirst()){
            do{
                Expense savedExpense= new Expense();
                savedExpense.setExpenseName(cursor.getString(cursor.getColumnIndex(SAVED_EXPENSES_COLUMN_EXPENSE_NAME)));
                savedExpense.setUsername(cursor.getString(cursor.getColumnIndex(SAVED_EXPENSES_COLUMN_USER)));
                savedExpense.setSavedExpenseID(cursor.getInt(cursor.getColumnIndex(SAVED_EXPENSES_COLUMN_EXPENSE_ID)));
                savedExpense.setCreatedDate(cursor.getString(cursor.getColumnIndex(SAVED_EXPENSES_COLUMN_CREATED_DATE)));
                savedExpense.setExpenseAmount(cursor.getInt(cursor.getColumnIndex(SAVED_EXPENSES_COLUMN_EXPENSE_AMOUNT)));
                savedExpenseList.add(savedExpense);
            }while(cursor.moveToNext());
        }
        cursor.close();
        return savedExpenseList;
    }

    /**
     * Fetched all expenses associated with
     * @param username created on @param date
     * @return
     */
    public List<Expense> fetchDailyExpense(String username,String date){
        /*select daily_expenses.daily_expense_id
        ,daily_expenses.expense_amount
        ,daily_expenses.created_date
        ,saved_expenses.expense_name
        from saved_expenses,daily_expenses
        where daily_expenses.saved_expense_id=saved_expenses.saved_expense_id
        and saved_expenses.username=username*/
        List<Expense> dailyExpensesList= new ArrayList<>();
        SQLiteDatabase sqLiteDatabase=this.getReadableDatabase();
        final String selectExpense="SELECT "
                + DAILY_EXPENSES_TABLE_NAME+"."+DAILY_EXPENSES_COLUMN_EXPENSE_ID+" , "
                + DAILY_EXPENSES_TABLE_NAME+"."+DAILY_EXPENSES_COLUMN_AMOUNT+" , "
                + DAILY_EXPENSES_TABLE_NAME+"."+DAILY_EXPENSES_COLUMN_CREATED_DATE+" , "
                + SAVED_EXPENSES_TABLE_NAME+"."+SAVED_EXPENSES_COLUMN_EXPENSE_NAME+" , "
                + DAILY_EXPENSES_TABLE_NAME+"."+DAILY_EXPENSES_COLUMN_SAVED_EXPENSE_ID
                + " FROM "+ DAILY_EXPENSES_TABLE_NAME
                + " , " + SAVED_EXPENSES_TABLE_NAME
                + " WHERE " + SAVED_EXPENSES_TABLE_NAME+ "." +SAVED_EXPENSES_COLUMN_USER+ " = '" + username +"'"
                + " AND " + SAVED_EXPENSES_TABLE_NAME +"."+SAVED_EXPENSES_COLUMN_EXPENSE_ID
                + " = " +DAILY_EXPENSES_TABLE_NAME+"."+DAILY_EXPENSES_COLUMN_SAVED_EXPENSE_ID;


        Cursor cursor=sqLiteDatabase.rawQuery(selectExpense,null);
        if (cursor.moveToFirst()){
            do{
                Expense dailyExpenses=new Expense();
                dailyExpenses.setDailyExpenseID(cursor.getInt(cursor.getColumnIndex(DAILY_EXPENSES_COLUMN_EXPENSE_ID)));
                dailyExpenses.setSavedExpenseID(cursor.getInt(cursor.getColumnIndex(DAILY_EXPENSES_COLUMN_SAVED_EXPENSE_ID)));
                dailyExpenses.setCreatedDate(cursor.getString(cursor.getColumnIndex(DAILY_EXPENSES_COLUMN_CREATED_DATE)));
                dailyExpenses.setExpenseAmount(cursor.getInt(cursor.getColumnIndex(DAILY_EXPENSES_COLUMN_AMOUNT)));
                dailyExpenses.setExpenseName(cursor.getString(cursor.getColumnIndex(SAVED_EXPENSES_COLUMN_EXPENSE_NAME)));
                dailyExpensesList.add(dailyExpenses);
            }while(cursor.moveToNext());
        }
        cursor.close();

        return dailyExpensesList;
    }

    /**
     * Fetched all expenses associated with
     * @param username that are added today
     * @return
     */
    public List<Expense> fetchDailyExpense(String username) {
        DateFormat dateFormat=new SimpleDateFormat("dd/MM/yyyy");
        String today= dateFormat.format(Calendar.getInstance().getTime());
        return fetchDailyExpense(username,today);
    }

    public User fetchUserDetails(String username){
        User user=new User();
        SQLiteDatabase sqLiteDatabase=this.getReadableDatabase();
        final String selectUser="SELECT "
                + USERS_COLUMN_DESIRED_SAVING +" , "
                + USERS_COLUMN_ANNUAL_INCOME  +" , "
                + USERS_COLUMN_EMAIL +" , "
                +USERS_COLUMN_MAXIMUM_DAILY_EXPENSE
                +" FROM "+USERS_TABLE_NAME
                +" WHERE "
                + USERS_COLUMN_USERNAME + " = '" + username + "'";

        Cursor cursor=sqLiteDatabase.rawQuery(selectUser,null);
        if (cursor.moveToFirst()){
                user.setAnnualIncome(cursor.getInt(cursor.getColumnIndex(USERS_COLUMN_ANNUAL_INCOME)));
                user.setDesiredSaving(cursor.getInt(cursor.getColumnIndex(USERS_COLUMN_DESIRED_SAVING)));
                user.setEmail(cursor.getString(cursor.getColumnIndex(USERS_COLUMN_EMAIL)));
                user.setMaximumDailyExpense(cursor.getColumnIndex(USERS_COLUMN_MAXIMUM_DAILY_EXPENSE));
        }
        cursor.close();
        return user;
    }
}