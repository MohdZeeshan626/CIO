package com.singleevent.sdk.View.LeftActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.singleevent.sdk.R;
import com.singleevent.sdk.View.LeftActivity.trelo.TreloBoard;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import cyd.awesome.material.AwesomeText;

public class AddCard extends AppCompatActivity implements View.OnClickListener{
    EditText cardname,desc;
    RelativeLayout calview;
    TextView pickdate;
    private int mYear, mMonth, mDay;
    int day, month, year1, hour, minutes;
    Spinner spinner,spinner1;
    int spinnerID,spinnerID1;
    HashMap<Integer, String> itemset;
    HashMap<Integer, String> itemset1;
    ArrayList<String> spinner_text;
    ArrayList<String> spinner_text1;
    AwesomeText gclose;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addcard);
        cardname=(EditText)findViewById(R.id.cardname);
        desc=(EditText)findViewById(R.id.desc);
        calview=(RelativeLayout)findViewById(R.id.calview);
        spinner=(Spinner)findViewById(R.id.spinner);
        spinner1=(Spinner)findViewById(R.id.spinner1);
        itemset = new HashMap<Integer, String>();
        itemset1 = new HashMap<Integer, String>();

        pickdate=(TextView)findViewById(R.id.pickdate);
        spinnerID = spinner.getSelectedItemPosition();
        spinner_text = new ArrayList<>();
        spinner_text1 = new ArrayList<>();
        gclose=(AwesomeText)findViewById(R.id.gclose);
        gclose.setOnClickListener(this);
        setSpinnerValues();
        setSpinnerValues1();


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                spinnerID = i;
                //getting agenda id

                String t=(itemset.get(spinnerID));


              //  userListAdapter = new UserListAdapter(EventUsersActivity.this, eventUserList, agenda_id);
              //  recyclerView.setAdapter(userListAdapter);

               // updateView();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                spinnerID1 = i;
                //getting agenda id

                String t=(itemset1.get(spinnerID1));


                //  userListAdapter = new UserListAdapter(EventUsersActivity.this, eventUserList, agenda_id);
                //  recyclerView.setAdapter(userListAdapter);

                // updateView();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
    @Override
    public void onClick(View view)
    {
      if(view.getId()==R.id.gclose){
          Intent i=new Intent(AddCard.this,TreloBoard.class);
          startActivity(i);
      }
    }

    public void calendarClicks(View view) {

        // Get Current Date
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(this, R.style.CalendarDialog,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        day = dayOfMonth;
                        month = monthOfYear;
                        year1 = year;

                        pickdate.setText("Due "+dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                    }


                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }
    private void setSpinnerValues() {
        itemset.put(0, "Select Board");
        spinner_text.add("Select Board");

        for (int i = 0; i < 2; i++) {
            spinner_text.add("webmobi");
        }

        // Initializing an ArrayAdapter
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                this, R.layout.spinneritem1, spinner_text
        );
        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        spinner.setAdapter(spinnerArrayAdapter);

    }
    private void setSpinnerValues1() {
        itemset1.put(0, "Select List");
        spinner_text1.add("Select List");

        for (int i = 0; i < 2; i++) {
            spinner_text1.add("list");
        }

        // Initializing an ArrayAdapter
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                this, R.layout.spinneritem1, spinner_text1
        );
        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        spinner1.setAdapter(spinnerArrayAdapter);
    }



}
