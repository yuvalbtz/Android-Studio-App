package com.example.myapp;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.DialogFragment;

import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.audiofx.Visualizer;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.ArrayList;
import java.util.Calendar;

import de.codecrafters.tableview.TableView;
import de.codecrafters.tableview.listeners.TableDataClickListener;
import de.codecrafters.tableview.listeners.TableHeaderClickListener;
import de.codecrafters.tableview.toolkit.SimpleTableDataAdapter;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;

import static com.example.myapp.DBHelper.TABLE_NAME;


public class TableVeiwActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{
    Integer id;

    Context c;
    float getmainbudget;
    float totalspend;
    float balance;
    SQLiteDatabase db;
    EditText date;

    EditText datefeild, amountfeild, detailsfeild, receiptfeild,mainbudgetfeild;
    Button addbtn,deletebtn,updatebtn,editbtn,confirmbtn,datepicker;
    TextView totalspendingtext,balancetext;

    TableHelper tableHelper;

    TableView<String[]> tableView;







    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table_veiw);



        final FloatingActionButton fab = findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displaydialog();

                deletebtn.setVisibility(View.INVISIBLE);
                updatebtn.setVisibility(View.INVISIBLE);


            }
        });

        balancetext = (TextView) findViewById(R.id.balanceview);
        totalspendingtext = (TextView) findViewById(R.id.totalSpendingview);
        mainbudgetfeild = (EditText) findViewById(R.id.mainbudgetfeild);
        editbtn = (Button) findViewById(R.id.edit_btn);
        confirmbtn = (Button) findViewById(R.id.confirm_btn);

        mainbudgetfeild.setFocusableInTouchMode(false);
        mainbudgetfeild.setEnabled(false);

        editbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mainbudgetfeild.setEnabled(true);
                mainbudgetfeild.setFocusableInTouchMode(true);
            }
        });


        confirmbtn.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {
         try {
             DBHelper myupdatemainbudget = new DBHelper(TableVeiwActivity.this);
             Integer id = 0;
             myupdatemainbudget.updatemainbudget(Float.valueOf(mainbudgetfeild.getText().toString()), id);
             mainbudgetfeild.setFocusableInTouchMode(false);
             mainbudgetfeild.setEnabled(false);

             totalspending();
             totalbudget();
             balance();
             Toast.makeText(TableVeiwActivity.this, "Budget updated!", Toast.LENGTH_SHORT).show();


         }catch (NumberFormatException e){

             Toast.makeText(TableVeiwActivity.this, "Budget field is empty! ", Toast.LENGTH_SHORT).show();

         }
            }
        });



         tableHelper=new TableHelper(TableVeiwActivity.this);

        tableView = findViewById(R.id.table_data_view);

        tableView.setColumnCount(4);


        totalspending();
        totalbudget();
        balance();

        tableView.setBackgroundColor(Color.parseColor("#FFFFe6"));

        tableView.setHeaderBackground(R.color.tableheader);

        tableView.setHeaderAdapter(new SimpleTableHeaderAdapter(this,tableHelper.getTableHeaders()));
        tableView.setDataAdapter(new SimpleTableDataAdapter(this, tableHelper.getTable()));



        fab.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(TableVeiwActivity.this);
                builder.setMessage("Are you sure you want to delete all items?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        DBHelper deletetable = new DBHelper(TableVeiwActivity.this);

                        deletetable.deletealltable(0);
                        tableView.setDataAdapter(new SimpleTableDataAdapter(TableVeiwActivity.this, tableHelper.getTable()));
                        Toast.makeText(TableVeiwActivity.this, "A New Session Has Started!", Toast.LENGTH_SHORT).show();
                        totalspending();
                        totalbudget();
                        balance();

                    }
                })
                .setNegativeButton("No",null);
                AlertDialog alertDialog = builder.create();
                alertDialog.show();


                return false;
            }
        });













        tableView.addDataClickListener(new TableDataClickListener<String[]>() {
             @Override
             public void onDataClicked(int rowIndex, final String[] clickedData) {

                 displaydialog();
                 datefeild.setText(clickedData[0]);
                 amountfeild.setText(clickedData[1]);
                 detailsfeild.setText(clickedData[2]);
                 receiptfeild.setText(clickedData[3]);

                 id = Integer.valueOf(clickedData[4]);

             }



         });




    }

    public void totalbudget(){
        DBAdapter mymainbudget = new DBAdapter(TableVeiwActivity.this);
        getmainbudget = mymainbudget.selectmainbudget();
        if(getmainbudget == 0) return;
        mainbudgetfeild.setText(getmainbudget +"");


    }


    public void totalspending(){
    //sumofspending
    DBHelper mysumofspending = new DBHelper(TableVeiwActivity.this);
    totalspend = mysumofspending.sumofspending();

    totalspendingtext.setText( totalspend + "");
    }

    public void balance(){
        balance = getmainbudget - totalspend;
        balancetext.setText(String.format("%.02f", balance) + "");

        if (totalspend > getmainbudget){
            balancetext.setBackgroundColor(Color.RED);


        }else {
            balancetext.setBackgroundColor(Color.blue(35));
        }

    }



    private void displaydialog() {

        final Dialog dialog = new Dialog(this);
        dialog.setTitle("SQLDATA");
        dialog.setContentView(R.layout.dialog);

        datefeild = (EditText) dialog.findViewById(R.id.date_text);
        amountfeild = (EditText) dialog.findViewById(R.id.amount_text);
        detailsfeild = (EditText) dialog.findViewById(R.id.details_text);
        receiptfeild = (EditText) dialog.findViewById(R.id.receipt_text);

        addbtn = (Button) dialog.findViewById(R.id.add_btn);
        deletebtn = (Button) dialog.findViewById(R.id.delete_btn);
        updatebtn = (Button) dialog.findViewById(R.id.update_btn);
        datepicker = (Button) dialog.findViewById(R.id.datepicker_btn);

        addbtn.setBackgroundColor(Color.BLUE);
        addbtn.setTextColor(Color.WHITE);
        deletebtn.setBackgroundColor(Color.RED);
        deletebtn.setTextColor(Color.WHITE);
        datepicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showdatepicker();
            }
        });






        updatebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
          try{


                DBHelper myupdateItem = new DBHelper(TableVeiwActivity.this);
                myupdateItem.updateItem(datefeild.getText().toString(), Float.valueOf(amountfeild.getText().toString()),detailsfeild.getText().toString(),receiptfeild.getText().toString(),id);
                tableView.setDataAdapter(new SimpleTableDataAdapter(TableVeiwActivity.this, tableHelper.getTable()));
                dialog.dismiss();
                Toast.makeText(TableVeiwActivity.this, "Item Updated!", Toast.LENGTH_SHORT).show();
                totalspending();
                totalbudget();
                balance();
          }catch (NumberFormatException e){
              Toast.makeText(TableVeiwActivity.this, "You cant update an item when Spending field is empty! ", Toast.LENGTH_SHORT).show();


          }
          }

        });



        deletebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


               DBHelper mydeletitem = new DBHelper(TableVeiwActivity.this);

                mydeletitem.deleteItem(id);
                tableView.setDataAdapter(new SimpleTableDataAdapter(TableVeiwActivity.this, tableHelper.getTable()));
                dialog.dismiss();
                Toast.makeText(TableVeiwActivity.this, "Item Deleted!", Toast.LENGTH_SHORT).show();
                totalspending();
                totalbudget();
                balance();


            }
        });


        addbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

try{


                ModelTable s = new ModelTable();
                s.setDate(datefeild.getText().toString());
                s.setAmount(Float.valueOf(amountfeild.getText().toString()));
                s.setDetails(detailsfeild.getText().toString());
                s.setReceiptnum(receiptfeild.getText().toString());
                s.setMainbudget(Float.valueOf(mainbudgetfeild.getText().toString()));
                if (new DBAdapter(TableVeiwActivity.this).insertdata(s)) {
                   datefeild.setText("");
                    amountfeild.setText("");
                    detailsfeild.setText("");
                    receiptfeild.setText("");
                    tableView.setDataAdapter(new SimpleTableDataAdapter(TableVeiwActivity.this, tableHelper.getTable()));
                    dialog.dismiss();
                    Toast.makeText(TableVeiwActivity.this, "Item Added!", Toast.LENGTH_SHORT).show();
                    totalspending();
                    totalbudget();
                    balance();
                } else {
                    Toast.makeText(TableVeiwActivity.this, "Not Saved", Toast.LENGTH_SHORT).show();
                }
}catch (NumberFormatException e){
    Toast.makeText(TableVeiwActivity.this, "You cant add an item when Spending field is empty! ", Toast.LENGTH_SHORT).show();

}






            }
        });

        dialog.show();
    }
    private void showdatepicker(){
        DatePickerDialog datepicker = new DatePickerDialog(
                 this,
                this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)

        );
datepicker.show();

    }


    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int dayofmonth) {
        String date = dayofmonth + "/" + month + "/" + year;
       datefeild.setText(date);
    }

}




