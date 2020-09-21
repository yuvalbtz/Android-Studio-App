package com.example.myapp;

import android.content.Context;

import java.util.ArrayList;

public class TableHelper {
    Context c;
    private String[] TableHeaders={"Date","Spending","Details","Receipt"};
    private String[][] Table;


    public TableHelper(Context c) {
        this.c = c;
    }

    public String[] getTableHeaders()
    {
        return TableHeaders;
    }

    public  String[][] getTable()
    {
        ArrayList<ModelTable> modelTables=new DBAdapter(c).showModelTableData();
        ModelTable s;
        Table= new String[modelTables.size()][6];
        for (int i=0;i<modelTables.size();i++) {
            s=modelTables.get(i);
            Table[i][0]=s.getDate();
            Table[i][1]= String.valueOf(s.getAmount());
            Table[i][2]=s.getDetails();
            Table[i][3]=s.getReceiptnum();
            Table[i][4]= String.valueOf(s.getEmp_id());
            Table[i][5]= String.valueOf(s.getMainbudget());

        }
        return Table;
    }
























}
