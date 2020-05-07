package com.example.attendanceapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;


public class TakeAttendance extends AppCompatActivity {

    String tablename, mode, subject, year, cr_date;
    Button showUI, submit;
    EditText roll_from, roll_to, absent_roll;
    TextView date;
    String absentRolls;
    AutoCompleteTextView subjects, years;
    DatePickerDialog dp;
    RadioGroup rg;
    int selID,  endroll, startroll;
    RadioButton rb;
    AttendData data;
    CommonMethods cm = new CommonMethods();
    DataBaseOp db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_attendance);
        try {
            showUI = findViewById(R.id.btn_ui);
            roll_from = findViewById(R.id.from_roll);
            roll_to = findViewById(R.id.to_roll);
            absent_roll = findViewById(R.id.absent_roll);
            submit = findViewById(R.id.submit);
            subjects = findViewById(R.id.subject);
            years = findViewById(R.id.year);
            date = findViewById(R.id.date);
            rg = findViewById(R.id.radiogrp);
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            Date d = new Date();
            cr_date = formatter.format(d);
            date.setText("Date -> " + cr_date);
            years.setAdapter(cm.getYearAdpt(getApplicationContext()));
            years.setThreshold(1);

            subjects.setAdapter(cm.getSubjectAdpt(getApplicationContext()));
            subjects.setThreshold(1);

            data = new AttendData();
            data.subj = subjects.getText().toString();
            data.year = years.getText().toString();
            data.rollFrom = roll_from.getText().toString();
            data.rollTo = roll_to.getText().toString();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        try {
            absent_roll.setText(getIntent().getExtras().getString("absentRolls").toString());
            data = (AttendData) getIntent().getSerializableExtra("Data");
            subjects.setText(data.subj);
            years.setText(data.year);
            roll_from.setText(data.rollFrom);
            roll_to.setText(data.rollTo);
        } catch (Exception e) {
            e.printStackTrace();
            subjects.setText(data.subj);
            years.setText(data.year);
            roll_from.setText(data.rollFrom);
            roll_to.setText(data.rollTo);
//            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();

        }
//        getSubjectList();
        showUI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    int start = Integer.parseInt(roll_from.getText().toString());
                    int end = Integer.parseInt(roll_to.getText().toString());

                    if (start > end) {
                        Toast.makeText(getApplicationContext(), "Roll_No. from cannot be Greater", Toast.LENGTH_SHORT).show();
                    } else {
                        data.subj = subjects.getText().toString();
                        data.year = years.getText().toString();
                        data.rollFrom = roll_from.getText().toString();
                        data.rollTo = roll_to.getText().toString();

                        Intent i = new Intent(getApplicationContext(), CreateBtnUI.class);
                        i.putExtra("roll_from", roll_from.getText().toString());
                        i.putExtra("roll_to", roll_to.getText().toString());
                        i.putExtra("Data", data);
                        i.putExtra("absent_rolls", absent_roll.getText().toString());
                        startActivity(i);
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Plz input Roll No correctly", Toast.LENGTH_SHORT).show();
                }
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    getAllDetails();
                    db = new DataBaseOp(getApplicationContext());
//                db.chooseTable(year);
                    db.insertData(year, subject, mode, absentRolls, cr_date, startroll, endroll);
                    Toast.makeText(getApplicationContext(), "Data Stored", Toast.LENGTH_SHORT).show();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Enter data correctly...", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }




    public void getAllDetails()
    {
        subject = subjects.getText().toString();
        year = getYear();
        selID = rg.getCheckedRadioButtonId();
        rb = findViewById(selID);
        mode = rb.getText().toString();
        absentRolls = absent_roll.getText().toString();
        startroll = Integer.parseInt(roll_from.getText().toString());
        endroll = Integer.parseInt(roll_to.getText().toString());
    }
    public String getYear()
    {
        if(years.getText().toString().equals("1st Year"))
        {
            year = "first_year";
        }
        if(years.getText().toString().equals("2nd Year"))
        {
            year = "second_year";
        }
        if(years.getText().toString().equals("3rd Year"))
        {
            year = "third_year";
        }
        return year;
    }

//    @Override
//    public void onSaveInstanceState(Bundle savedInstanceState) {
//        super.onSaveInstanceState(savedInstanceState);
//        savedInstanceState.putString("year", years.getText().toString());
//        savedInstanceState.putString("subject", subjects.getText().toString());
//        savedInstanceState.putString("date", dateBox.getText().toString());
////        savedInstanceState.putString("mode", rb.getText().toString());
//        savedInstanceState.putString("startRoll", roll_from.getText().toString());
//        savedInstanceState.putString("endRoll", roll_to.getText().toString());
//    }
//
//    @Override
//    public void onRestoreInstanceState(Bundle savedInstanceState) {
//        super.onRestoreInstanceState(savedInstanceState);
//        years.setText(savedInstanceState.getString("year"));
//        subjects.setText(savedInstanceState.getString("subject"));
//        dateBox.setText(savedInstanceState.getString("date"));
//        roll_from.setText(savedInstanceState.getString("rollFrom"));
//        roll_to.setText(savedInstanceState.getString("rollTo"));
//    }
}

class AttendData implements Serializable
{
    public String subj, year,rollFrom, rollTo;
}