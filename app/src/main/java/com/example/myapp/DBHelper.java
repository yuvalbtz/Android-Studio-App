package com.example.myapp;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    final static String DB_NAME = "managmentdataxz";
    final static String DB_VER = "1";
    final static String TABLE_NAME = "managment";
    final static String COL_ID = "emp_id";
    final static String COL_DATE = "date";
    final static String COL_AMOUNT = "amount";
    final static String COL_DETAILS = "details";
    final static String COL_RECEIPT = "receipt";
    final static String COL_MAINBUDGET = "mainbudget";

    public DBHelper(Context context) {
        super(context, DB_NAME, null, 1);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL("create table " + TABLE_NAME + "(emp_id INTEGER PRIMARY KEY AUTOINCREMENT,date varchar(10),amount FLOAT,details varchar(100),receipt varchar(20),mainbudget FLOAT)");
            onCreate(db);
        }catch (SQLException e){

            e.printStackTrace();
        }






    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
      try {


       db.execSQL("DROP TABLE IF EXISTS "+ TABLE_NAME);
        db.execSQL(String.valueOf(db));
      }catch (SQLException e){
          e.printStackTrace();

      }



    }

    public void deleteItem(Integer idDB){


             SQLiteDatabase db = this.getWritableDatabase();
             String query = "DELETE FROM "+TABLE_NAME+" WHERE "+COL_ID+" = '"+idDB+"'";
             db.execSQL(query);

    }



    public void updateItem(String date,Float amount,String details  ,String receipt,Integer idDB){

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("date",date);
        values.put("amount",amount);
        values.put("details",details);
        values.put("receipt",receipt);
        values.put("emp_id",idDB);
        db.update("managment",values,"emp_id = ?",new String[] {String.valueOf(idDB)});


    }

public void updatemainbudget(Float mainbudget,Integer id ){
    SQLiteDatabase db = this.getWritableDatabase();
    ContentValues value = new ContentValues();
    value.put("mainbudget",mainbudget);
    db.update("managment",value,"emp_id>?",new String[] {String.valueOf(id)});

    }

    public float sumofspending(){
        float sum =0;
         SQLiteDatabase db = this.getWritableDatabase();


        Cursor cursor = db.rawQuery("SELECT SUM(amount) FROM managment",null);

        if (cursor.getCount() >0){
            cursor.moveToFirst();
            sum =cursor.getFloat(0);

        }
        cursor.close();
        sum = Float.parseFloat(String.format("%.02f",sum));

        return sum;
    }

public void deletealltable(Integer idDB){
        //int sumcount = count()-1;
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM "+TABLE_NAME+" WHERE "+COL_ID+" > '"+idDB+"'";
        db.execSQL(query);
    }

public  Integer count(){
    int sum =0;
    SQLiteDatabase db = this.getWritableDatabase();


    Cursor cursor = db.rawQuery("SELECT COUNT(amount) FROM managment",null);

    if (cursor.getCount() >0){
        cursor.moveToFirst();
        sum =cursor.getInt(0);

    }
    cursor.close();
    return sum;

}


}
