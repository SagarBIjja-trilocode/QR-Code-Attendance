package com.example.attendanceapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


import static android.content.Context.MODE_PRIVATE;

public class DataBaseOp extends SQLiteOpenHelper
{
    SQLiteDatabase db;
    static String dbname = "attendancedb";
//    static String tablename;
    String query;

    public DataBaseOp(Context ctx)
    {
        super(ctx, dbname, null, 1);
        getWritableDatabase();
    }

    public void insertData(String tablename, String subject, String mode, String absentRolls, String cr_date, int startroll, int endroll) {
        try
        {
            SQLiteDatabase dbs = this.getWritableDatabase();
            ContentValues values = new ContentValues();

            values.put("Subject", subject);
            values.put("Mode", mode);
            values.put("AbsentRolls", absentRolls);
            values.put("Date", cr_date);
            values.put("StartRoll", startroll);
            values.put("EndRoll", endroll);

            dbs.insert(tablename, null, values);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void onCreate(SQLiteDatabase dbs)
    // CreTe Creafe Query.
    {
        try {
                query ="CREATE TABLE IF NOT EXISTS first_year(Subject text, Mode text, AbsentRolls text, Date text, StartRoll int, EndRoll int);";
                dbs.execSQL(query);
                query = "CREATE TABLE IF NOT EXISTS second_year(Subject text, Mode text, AbsentRolls text, Date text, StartRoll int, EndRoll int);";
                dbs.execSQL(query);
                query = "CREATE TABLE IF NOT EXISTS third_year(Subject text, Mode text, AbsentRolls text, Date text, StartRoll int, EndRoll int);";
                dbs.execSQL(query);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    String[][] fetchMonthData(String table_name, String subject,String mode,String month_no)
    {
        // For Date Format  2 -> 02
        if(month_no.length()==1)
            month_no = "0"+month_no;
        // Array List to store result
        String result[][];

        // Execute Query
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "Select AbsentRolls, Date, StartRoll, EndRoll from "+table_name+" where " +
                "Subject='"+subject+"' AND "+
                "Mode='"+mode+"' AND "+
                "Date like '__/"+month_no+"/____' ORDER BY Date ;";

        Cursor cursor = db.rawQuery(query,null);
        try {
            if (cursor.getCount() > 0) {
                result = new String[cursor.getCount()][4];
                int index=0;
                cursor.moveToFirst();
                do {
                    result[index][0] = cursor.getString(0);
                    result[index][1] = cursor.getString(1).substring(0,1);
                    result[index][2] = cursor.getString(2);
                    result[index][3] = cursor.getString(3);
                    index++;
                }
                while (cursor.moveToNext());

                return result;
            } else {
                int a;
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        cursor.close();
        return  null;
    }

    String[] getDateData(String table, String subj, String mode, String date)
    {

        String result[];

        // Execute Query
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "Select AbsentRolls, StartRoll, EndRoll from "+table+" where " +
                "Subject='"+subj+"' AND "+
                "Mode='"+mode+"' AND "+
                "Date='"+date+"'";

        Cursor cursor = db.rawQuery(query,null);
        try {
            if (cursor.getCount() > 0) {
                result = new String[3];
                int index=0;
                cursor.moveToFirst();
                do {
                    result[0] = cursor.getString(0);
                    result[1] = cursor.getString(1);
                    result[2] = cursor.getString(2);
                    index++;
                }
                while (cursor.moveToNext());

                return result;
            } else {

            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        cursor.close();
        return  null;
    }

//    public void chooseTable(String year)
//    {
//        if(year.equals("1st Year"))
//            this.tablename = "first_year";
//        if(year.equals("2nd Year"))
//            this.tablename = "second_year";
//        if(year.equals("3rd Year"))
//            this.tablename = "third_year";
//    }
}
