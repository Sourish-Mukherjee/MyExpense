package com.example.myexpense;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DataBaseHelper extends SQLiteOpenHelper {

    private static int day_exp_total = 0;
    private static int month_exp_total = 0;
    private static int month_exp_food_total = 0;
    private static int month_exp_travel_total = 0;
    private static int month_exp_entertainment_total = 0;

    public DataBaseHelper(Context context) {
        super(context, "Trans.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table TransDetails (date text, amount real, name text, category text )");
        db.execSQL("create table LoginDetails (username text, password text )");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists TransDetails");
        db.execSQL("drop table if exists LoginDetails");
        onCreate(db);
    }

    public static void writeData(SQLiteDatabase db, String date, String amount, String name, String category) {
        ContentValues values = new ContentValues();
        values.put("date", date);
        values.put("amount", amount);
        values.put("name", name);
        values.put("category", category);
        db.insert("TransDetails", null, values);
    }

    public static String getData(SQLiteDatabase db, int num) {
        String resultfromsql;
        String category;
        String projection[] = {"date", "amount", "name", "category"};
        Cursor c = db.query("TransDetails", projection, null, null, null, null, null);
        c.moveToPosition(num - 1);
        resultfromsql = " Name  : " + c.getString(2);
        resultfromsql += " Date : " + c.getString(0);
        resultfromsql += " Amount : ₹" + c.getString(1);
        resultfromsql += category = c.getString(3);
        if ("" + c.getString(1) == "" || Integer.parseInt(c.getString(1)) == 0
                || (c.getString(0).equals(DashBoard.getDateForDashboard()) == false))
            resultfromsql = null;
        else
            day_exp_total += Integer.parseInt("" + c.getString(1));
        return resultfromsql;
    }

    public static String getMonthData(SQLiteDatabase db, String month, int num) {
        String resultfromsql = null;
        String projection[] = {"date", "amount", "name", "category"};
        Cursor c = db.query("TransDetails", projection, null, null, null, null, null);
        c.moveToPosition(num - 1);
        String datefromsql = month;
        resultfromsql = " Name  : " + c.getString(2);
        resultfromsql += " Date : " + c.getString(0);
        resultfromsql += " Amount : ₹" + c.getString(1);
        String givenDate = c.getString(0);
        if (!(givenDate.indexOf('/') == -1))
            givenDate = givenDate.substring(givenDate.indexOf('/') + 1, givenDate.lastIndexOf('/'));
        resultfromsql += c.getString(3);
        if ("" + c.getString(1) == "" || Integer.parseInt(c.getString(1)) == 0 || givenDate.equals(datefromsql) == false)
            resultfromsql = null;
        else {
            checkPieTotal(c.getString(3), Integer.parseInt(c.getString(1)));
            month_exp_total += Integer.parseInt("" + c.getString(1));
        }
        return resultfromsql;
    }

    private static void checkPieTotal(String category, int amount) {
        if (category.equals("Food"))
            month_exp_food_total += amount;
        else if (category.equals("Travel"))
            month_exp_travel_total += amount;
        else
            month_exp_entertainment_total += amount;
    }

    public static long getProfilesCount(SQLiteDatabase db, String name) {
        long count;
        if (name.equals("TransDetails"))
            count = DatabaseUtils.queryNumEntries(db, "TransDetails");
        else
            count = DatabaseUtils.queryNumEntries(db, "LoginDetails");
        db.close();
        return count;
    }

    public static void setMonth_exp_total(int month_exp_total) {
        DataBaseHelper.month_exp_total = month_exp_total;
    }

    public static int getMonthlyExpenses() {
        return month_exp_total;
    }

    public static int getTotalExpenses(SQLiteDatabase db) {
        return day_exp_total;
    }

    public static void setDay_exp_total(int value) {
        day_exp_total = value;
    }

    public static void writeNewAccountData(SQLiteDatabase db, String username, String password) {
        ContentValues values = new ContentValues();
        values.put("username", username);
        values.put("password", password);
        db.insert("LoginDetails", null, values);
    }

    public static boolean getLoginData(SQLiteDatabase db, String username, String password, int num) {
        String projection[] = {"username", "password"};
        Cursor c = db.query("LoginDetails", projection, null, null, null, null, null);
        c.moveToPosition(num - 1);
        String user, pass;
        user = c.getString(0);
        pass = c.getString(1);
        if (user.equals(username) && pass.equals(password))
            return true;
        else
            return false;
    }

    public static int getMonth_exp_food_total() {
        return month_exp_food_total;
    }

    public static int getMonth_exp_travel_total() {
        return month_exp_travel_total;
    }

    public static int getMonth_exp_entertainment_total() {
        return month_exp_entertainment_total;
    }

    public static void setMonth_exp_food_total(int month_exp_food_total) {
        DataBaseHelper.month_exp_food_total = month_exp_food_total;
    }

    public static void setMonth_exp_travel_total(int month_exp_travel_total) {
        DataBaseHelper.month_exp_travel_total = month_exp_travel_total;
    }

    public static void setMonth_exp_entertainment_total(int month_exp_entertainment_total) {
        DataBaseHelper.month_exp_entertainment_total = month_exp_entertainment_total;
    }

}
