package com.example.myapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class DBAdapter {
    Context c;
    SQLiteDatabase db;
    DBHelper helper;

    String getmainbudget;

    /*
    1. INITIALIZE DB HELPER AND PASS IT A CONTEXT
    */


    public DBAdapter(Context c) {
        this.c = c;
        helper = new DBHelper(c);

    }



    /*
    SAVE DATA TO DB
    */
    public boolean insertdata(ModelTable modelTable) {
        try {
            db = helper.getWritableDatabase();
            ContentValues cv = new ContentValues();
            cv.put("date", modelTable.getDate());
            cv.put("amount", modelTable.getAmount());
            cv.put("details", modelTable.getDetails());
            cv.put("receipt", modelTable.getReceiptnum());
            cv.put("emp_id", modelTable.getEmp_id());
            cv.put("mainbudget", modelTable.getMainbudget());

            long result = db.insert("managment", "emp_id",cv);
            if (result > 0) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            helper.close();
        }
        return false;
    }
    /*
    1. RETRIEVE SPACECRAFTS FROM DB AND POPULATE ARRAYLIST
    2. RETURN THE LIST
    */
    public ArrayList<ModelTable> showModelTableData()
    {
        ArrayList<ModelTable> modelTables=new ArrayList<>();
        String[] columns={"emp_id","date","amount","details","receipt","mainbudget"};
        try
        {
            db = helper.getWritableDatabase();
            Cursor c=db.query("managment",columns,null,null,null,null,null);
            ModelTable s;
            if(c != null)
            {
                while (c.moveToNext())
                {
                    String s_ID=c.getString(0);
                    String s_date=c.getString(1);
                    String s_amount=c.getString(2);
                    String s_details=c.getString(3);
                    String s_receipt=c.getString(4);
                    String s_mainbudget=c.getString(5);


                    s=new ModelTable();
                    s.setEmp_id(Integer.valueOf(s_ID));
                    s.setDate(s_date);
                    s.setAmount(Float.valueOf(s_amount));
                    s.setDetails(s_details);
                    s.setReceiptnum(s_receipt);
                    s.setMainbudget(Float.valueOf(s_mainbudget));

                    modelTables.add(s);
                }
            }
        }catch (SQLException e)
        {
            e.printStackTrace();
        }
        return modelTables;
    }

    public Float selectmainbudget() {


        db = helper.getWritableDatabase();
        String[] columns = {"emp_id", "date", "amount", "details", "receipt", "mainbudget"};

        Cursor c = db.query("managment", columns, null, null, null, null, null);
        float result = 0;
        int irow = c.getColumnIndex("emp_id");
        int irow1 = c.getColumnIndex("date");
        int irow2 = c.getColumnIndex("amount");
        int irow3 = c.getColumnIndex("details");
        int irow4 = c.getColumnIndex("receipt");
        int irow5 = c.getColumnIndex("mainbudget");
        for (c.moveToLast(); !c.isAfterLast(); c.moveToNext()) {
            result = result + c.getFloat(irow5); }

        return result;

    }











}
