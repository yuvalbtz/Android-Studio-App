package com.example.myapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.apache.poi.ss.usermodel.HorizontalAlignment.CENTER;





public class SendMailXlsx extends AppCompatActivity {

    float mainbudget;
    float totalspend;
    float balance;


    SQLiteDatabase db;

    Button sendbtn,opengmailbtn;
     EditText exeltitle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_mail_xlsx);


        System.setProperty("org.apache.poi.javax.xml.stream.XMLInputFactory", "com.fasterxml.aalto.stax.InputFactoryImpl");
        System.setProperty("org.apache.poi.javax.xml.stream.XMLOutputFactory", "com.fasterxml.aalto.stax.OutputFactoryImpl");
        System.setProperty("org.apache.poi.javax.xml.stream.XMLEventFactory", "com.fasterxml.aalto.stax.EventFactoryImpl");




        sendbtn  = (Button) findViewById(R.id.sendbtn);
         opengmailbtn = (Button) findViewById(R.id.opengmail);

        final AlertDialog.Builder builderfinish = new AlertDialog.Builder(SendMailXlsx.this);
        builderfinish.setTitle("Your Exel file is ready!");
        builderfinish.setMessage("You can access your file in Budget Reports Directory...");


        final AlertDialog.Builder builder = new AlertDialog.Builder(SendMailXlsx.this);
        builder.setTitle("Create Your Exel File");
        builder.setIcon(R.drawable.ic_excel);
        builder.setMessage("Type your Exel title:");
        exeltitle = new EditText(SendMailXlsx.this);
        builder.setView(exeltitle);
        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {


                    try {
                    sendmessageexel();
                    builderfinish.show();



                } catch (FileNotFoundException | IllegalArgumentException e ) {
                    Toast.makeText(getApplicationContext(),"You must to type something!",Toast.LENGTH_LONG).show();

                    e.printStackTrace();


                }

              builderfinish.create();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                dialog.dismiss();
            }
        });












        final AlertDialog ad =builder.create();

        sendbtn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {

               ad.show();


           }



       });

        opengmailbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.putExtra(Intent.EXTRA_EMAIL,"");
                    intent.putExtra(Intent.EXTRA_SUBJECT,"");
                    intent.putExtra(Intent.EXTRA_TEXT,"");
                    intent.putExtra(Intent.EXTRA_CC,"");
                    intent.setType("text/html");
                    intent.setPackage("com.google.android.gm");
                    startActivity(Intent.createChooser(intent,"Send Mail"));

                }catch (ActivityNotFoundException e){
                    Toast.makeText(SendMailXlsx.this,"You need to install Gmail app",Toast.LENGTH_LONG).show();
                }



            }
        });







    }



public void sendmessageexel() throws FileNotFoundException {


    DBAdapter adapter = new DBAdapter(SendMailXlsx.this);
    DBHelper mysumofspending = new DBHelper(SendMailXlsx.this);
    totalspend = mysumofspending.sumofspending();

    mainbudget = adapter.selectmainbudget();

    balance = mainbudget - totalspend;


    DBHelper helper = new DBHelper(this);

    db = helper.getWritableDatabase();
    String[] columns = {"emp_id", "date", "amount", "details", "receipt", "mainbudget"};

    Cursor c = db.query("managment", columns, null, null, null, null, null);
    String result = "";
    int irow = c.getColumnIndex("emp_id");
    int irow1 = c.getColumnIndex("date");
    int irow2 = c.getColumnIndex("amount");
    int irow3 = c.getColumnIndex("details");
    int irow4 = c.getColumnIndex("receipt");
    int irow5 = c.getColumnIndex("mainbudget");

    // for (c.moveToLast(); !c.isAfterLast(); c.moveToNext()){
    //   result = result +c.getString(irow5);

    // }


    XSSFWorkbook workbook = new XSSFWorkbook();
    XSSFSheet sheet = workbook.createSheet(exeltitle.getText().toString() + "");

    XSSFCellStyle styletitle = workbook.createCellStyle();
    Font fonttitle = workbook.createFont();
    fonttitle.setFontHeightInPoints((short) 14);
    fonttitle.setFontName(HSSFFont.FONT_ARIAL);
    fonttitle.setUnderline(Font.U_DOUBLE);
    styletitle.setAlignment(CENTER);
    styletitle.setFont(fonttitle);

    XSSFRow header5 = sheet.createRow(1);
    header5.createCell(1).setCellValue(exeltitle.getText().toString() + "");
    sheet.addMergedRegion(new CellRangeAddress(1, 1, 1, 2));

    XSSFRow rowtitle = sheet.getRow(1);
    rowtitle.getCell(1).setCellStyle(styletitle);


    ///////////////////////////////////////////////////////////////////////////////////////////////////


    XSSFCellStyle style2 = workbook.createCellStyle();
    Font font2 = workbook.createFont();
    font2.setBold(true);
    font2.setFontName(HSSFFont.FONT_ARIAL);
    style2.setFillForegroundColor(IndexedColors.LIGHT_YELLOW.getIndex());
    style2.setFillPattern(FillPatternType.SOLID_FOREGROUND);
    style2.setAlignment(CENTER);
    style2.setFont(font2);

    XSSFCellStyle style22 = workbook.createCellStyle();
    style22.setFillForegroundColor(IndexedColors.LIGHT_YELLOW.getIndex());
    style22.setFillPattern(FillPatternType.SOLID_FOREGROUND);
    style22.setAlignment(CENTER);


    XSSFRow header1 = sheet.createRow(2);
    header1.createCell(0).setCellValue("Budget:");
    //mainbudget
    header1.createCell(1).setCellValue( mainbudget + "");

    XSSFRow row2 = sheet.getRow(2);
    row2.getCell(0).setCellStyle(style2);
    row2.getCell(1).setCellStyle(style22);

//////////////////////////////////////////////////////////////////////////////////////////////
    XSSFCellStyle style3 = workbook.createCellStyle();
    Font font3 = workbook.createFont();
    font3.setBold(true);
    font3.setFontName(HSSFFont.FONT_ARIAL);
    style3.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
    style3.setFillPattern(FillPatternType.SOLID_FOREGROUND);
    style3.setAlignment(CENTER);
    style3.setFont(font3);

    XSSFCellStyle style33 = workbook.createCellStyle();
    style33.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
    style33.setFillPattern(FillPatternType.SOLID_FOREGROUND);
    style33.setAlignment(CENTER);


    XSSFRow header2 = sheet.createRow(3);
    header2.createCell(0).setCellValue("Total Spending:");
    //total spending
    header2.createCell(1).setCellValue( totalspend + "");


    XSSFRow row3 = sheet.getRow(3);
    row3.getCell(0).setCellStyle(style3);
    row3.getCell(1).setCellStyle(style33);
    /////////////////////////////////////////////////////////////////////////////////////
    XSSFCellStyle style4 = workbook.createCellStyle();
    Font font4 = workbook.createFont();
    font4.setBold(true);
    font4.setFontName(HSSFFont.FONT_ARIAL);
    style4.setFillForegroundColor(IndexedColors.ORANGE.getIndex());
    style4.setFillPattern(FillPatternType.SOLID_FOREGROUND);
    style4.setAlignment(CENTER);
    style4.setFont(font4);

    XSSFCellStyle style44 = workbook.createCellStyle();
    style44.setFillForegroundColor(IndexedColors.ORANGE.getIndex());
    style44.setFillPattern(FillPatternType.SOLID_FOREGROUND);
    style44.setAlignment(CENTER);


    XSSFRow header3 = sheet.createRow(4);
    header3.createCell(0).setCellValue("Balance:");
    //balance
    header3.createCell(1).setCellValue(balance + "");

    XSSFRow row4 = sheet.getRow(4);
    row4.getCell(0).setCellStyle(style4);
    row4.getCell(1).setCellStyle(style44);

    //////////////////////////////////////////////////////////////////////////////
    XSSFCellStyle style6 = workbook.createCellStyle();
    Font font6 = workbook.createFont();
    font6.setBold(true);
    font6.setFontName(HSSFFont.FONT_ARIAL);
    font6.setColor(IndexedColors.WHITE.getIndex());
    style6.setFillForegroundColor(IndexedColors.BLUE.getIndex());
    style6.setFillPattern(FillPatternType.SOLID_FOREGROUND);
    style6.setAlignment(CENTER);
    style6.setFont(font6);


    XSSFRow header4 = sheet.createRow(6);
    header4.createCell(0).setCellValue("Spending Date");
    header4.createCell(1).setCellValue("Spending");
    header4.createCell(2).setCellValue("Item Name");
    header4.createCell(3).setCellValue("Receipt Number");

    XSSFRow row6 = sheet.getRow(6);
    row6.getCell(0).setCellStyle(style6);
    row6.getCell(1).setCellStyle(style6);
    row6.getCell(2).setCellStyle(style6);
    row6.getCell(3).setCellStyle(style6);
/////////////////////////////////////////////////////////////////////////////////////////////
    XSSFCellStyle styleindex = workbook.createCellStyle();
    Font fontindex = workbook.createFont();
    fontindex.setBold(true);
    fontindex.setFontName(HSSFFont.FONT_ARIAL);
    fontindex.setColor(IndexedColors.WHITE.getIndex());
    styleindex.setFillForegroundColor(IndexedColors.SKY_BLUE.getIndex());
    styleindex.setFillPattern(FillPatternType.SOLID_FOREGROUND);
    styleindex.setAlignment(CENTER);
    styleindex.setFont(fontindex);


    XSSFCellStyle styleindex1 = workbook.createCellStyle();
    Font fontindex1 = workbook.createFont();
    fontindex1.setBold(true);
    fontindex1.setFontName(HSSFFont.FONT_ARIAL);
    fontindex1.setColor(IndexedColors.WHITE.getIndex());
    styleindex1.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
    styleindex1.setFillPattern(FillPatternType.SOLID_FOREGROUND);
    styleindex1.setAlignment(CENTER);
    styleindex1.setFont(fontindex1);


    int index = 7;
    while (c.moveToNext()) {

        if (index % 2 == 1) {
            XSSFRow row = sheet.createRow(index);

            row.createCell(0).setCellValue(c.getString(irow1));
            row.createCell(1).setCellValue(c.getString(irow2));
            row.createCell(2).setCellValue(c.getString(irow3));
            row.createCell(3).setCellValue(c.getString(irow4));


            XSSFRow rowindex = sheet.getRow(index);
            rowindex.getCell(0).setCellStyle(styleindex);
            rowindex.getCell(1).setCellStyle(styleindex);
            rowindex.getCell(2).setCellStyle(styleindex);
            rowindex.getCell(3).setCellStyle(styleindex);

            index++;

        } else if (index % 2 == 0) {
            XSSFRow row = sheet.createRow(index);

            row.createCell(0).setCellValue(c.getString(irow1));
            row.createCell(1).setCellValue(c.getString(irow2));
            row.createCell(2).setCellValue(c.getString(irow3));
            row.createCell(3).setCellValue(c.getString(irow4));


            XSSFRow rowindex = sheet.getRow(index);
            rowindex.getCell(0).setCellStyle(styleindex1);
            rowindex.getCell(1).setCellStyle(styleindex1);
            rowindex.getCell(2).setCellStyle(styleindex1);
            rowindex.getCell(3).setCellStyle(styleindex1);


            index++;
        }

    }


    sheet.setColumnWidth(0, 4000);
    sheet.setColumnWidth(1, 4000);
    sheet.setColumnWidth(2, 7000);
    sheet.setColumnWidth(3, 4000);


    ActivityCompat.requestPermissions(SendMailXlsx.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},23);
   if (checkpermission()) {

       //SimpleDateFormat formatter = new SimpleDateFormat("yyy-MM-dd-HH:mm:ss");
      // Date date = new Date(System.currentTimeMillis());


       File filepath = Environment.getExternalStorageDirectory();
       File dir = new File(filepath.getAbsolutePath() + "/Budget Reports/");
       File exel = new File(dir, System.currentTimeMillis()+"-Budget_Report.xlsx");
       if (!dir.exists()) {
           dir.mkdirs();
           Toast.makeText(SendMailXlsx.this, "Budget Reports Directory was created in your internal storage device", Toast.LENGTH_LONG).show();

       }

       OutputStream fileOutputStream1 = null;

       try {
           fileOutputStream1 = new FileOutputStream(exel + "");
       } catch (FileNotFoundException e) {
           e.printStackTrace();
       }


       try {
           workbook.write(fileOutputStream1);
       } catch (IOException e) {
           e.printStackTrace();
       }
       try {
           fileOutputStream1.close();
       } catch (IOException e) {
           e.printStackTrace();
       }
       Toast.makeText(SendMailXlsx.this, "Finished!", Toast.LENGTH_LONG).show();

   }
}
///////////////////////////////////////////////////////////////
private boolean checkpermission(){
       int result = ContextCompat.checkSelfPermission(SendMailXlsx.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
      if (result == PackageManager.PERMISSION_GRANTED){
          return true;

      }else {
          return false;
      }

}

}

























