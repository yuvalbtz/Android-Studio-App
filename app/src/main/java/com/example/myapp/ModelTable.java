package com.example.myapp;

public class ModelTable {


String date,details,receiptnum;
Integer emp_id;
Float Amount;

    public Float getMainbudget() {
        return mainbudget;
    }

    public void setMainbudget(Float mainbudget) {
        this.mainbudget = mainbudget;
    }

    Float mainbudget;

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getReceiptnum() {
        return receiptnum;
    }

    public void setReceiptnum(String receiptnum) {
        this.receiptnum = receiptnum;
    }

    public Integer getEmp_id() {
        return emp_id;
    }

    public void setEmp_id(Integer emp_id) {
        this.emp_id = emp_id;
    }

    public Float getAmount() {
        return Amount;
    }

    public void setAmount(Float amount) {
        Amount = amount;
    }



    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
