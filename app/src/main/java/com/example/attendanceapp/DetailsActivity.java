package com.example.attendanceapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class DetailsActivity extends AppCompatActivity {
    TableLayout tl;
    String year, subject, mode, date;
    TableRow tr;
    TextView rollText, statusText;
    int start_roll, end_roll;

    String[]  data;
    DataBaseOp db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        tl = findViewById(R.id.table);
        db = new DataBaseOp(getApplicationContext());
        try
        {
            year = getIntent().getStringExtra("year");
            subject = getIntent().getStringExtra("subject");
            mode = getIntent().getStringExtra("mode");
            date = getIntent().getStringExtra("date");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        try {
            data = db.getDateData(year, subject, mode, date);
            makeReport();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "No attendance taken on this day", Toast.LENGTH_SHORT).show();
        }
    }
    public void makeReport()
    {
        // data = [AbsentRolls, StartRoll, EndRoll]
        start_roll = Integer.parseInt(data[1]);
        end_roll = Integer.parseInt(data[2]);
        for (int i = start_roll; i <= end_roll; i++)
        {
            tr = new TableRow(getApplicationContext());
            tr.setGravity(View.TEXT_ALIGNMENT_CENTER);
            rollText = new TextView(getApplicationContext());
            rollText.setText("    "+i);
            rollText.setTextSize(20);
            tr.addView(rollText);

            statusText = new TextView(getApplicationContext());
            if(checkAbsent(""+i, data[0]))
            {
                statusText.setText("    A");
                statusText.setBackgroundResource(R.drawable.absent_cell);
            }
            else
            {
                statusText.setText("    P");
                statusText.setBackgroundResource(R.drawable.present_cell);
            }
            statusText.setTextSize(20);
            tr.addView(statusText);

            tl.addView(tr);
        }

    }

    boolean checkAbsent(String roll, String absentrolls)
    {
        String[] rolls = absentrolls.split(",");
        for(int i=0; i<rolls.length;i++)
        {
            if(roll.equals(rolls[i]))
                return true;
        }
        return false;
    }

}
