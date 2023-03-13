package com.singleevent.sdk.health.Activity;


import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.singleevent.sdk.ApiList;
import com.singleevent.sdk.App;
import com.singleevent.sdk.Custom_View.Error_Dialog;
import com.singleevent.sdk.Custom_View.Util;
import com.singleevent.sdk.Custom_View.VolleyErrorLis;
import com.singleevent.sdk.R;
import com.singleevent.sdk.health.Model.ChallengeList;
import com.singleevent.sdk.health.Model.GetChallengeList;
import com.singleevent.sdk.model.AppDetails;
import com.singleevent.sdk.model.JoinUser;
import com.singleevent.sdk.model.User_Details;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.Formatter;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import androidx.appcompat.app.AppCompatActivity;
import io.paperdb.Paper;
import retrofit2.http.Body;

public class CreateChallengeActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, DatePickerDialog.OnDateSetListener {

    private static final String TAG = "MainActivity";

    Spinner spinner;
    Button btnNext,btndate_picker;
    TextView txtDate;
    AppDetails appDetails;
    int selectdays=0;
    String startdate;
    String type="steps";

    private DatePickerDialog.OnDateSetListener mDateSetListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_challenge);
        spinner = findViewById(R.id.spinnerDays);
        btnNext = findViewById(R.id.btn_next);
        txtDate = (TextView)findViewById(R.id.date_text);
        appDetails = Paper.book().read("Appdetails");
       /* if(getIntent().getExtras()!=null){
            type=getIntent().getExtras().getString("type");
        }*/

        //getChallenge();
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(txtDate.getText().length()==0)
                {
                    Toast.makeText(getApplicationContext(),"Please Select Start Date.",Toast.LENGTH_LONG).show();                }
                else{
                    Intent in = new Intent(CreateChallengeActivity.this,ChallengeDetailsActivity.class);
                    in.putExtra("selectdate",startdate);
                    in.putExtra("selectdays",selectdays);
                    in.putExtra("type",type);
                    startActivity(in);
                    finish();
                }
            }
        });


        btndate_picker = (Button)findViewById(R.id.date_picker);
     //   btndate_picker.setBackground(Util.setrounded(Color.parseColor(appDetails.getTheme_color())));
       // btnNext.setBackground(Util.setrounded(Color.parseColor(appDetails.getTheme_color())));
        btndate_picker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();

            }
        });


        // Spinner click listener
        spinner.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);

       // spinner.setBackground((Util.setrounded(Color.parseColor("#7b7769"))));
       /* GradientDrawable drawable1 = (GradientDrawable) spinner.getBackground();
        drawable1.setStrOkHttpClientoke (4, Color.parseColor ("#7b7769"));*/

        // Spinner Drop down elements
        List<String> days = new ArrayList<String>();
        days.add("1 Day");
        days.add("2 Days");
        days.add("3 Days");
        days.add("4 Days");
        days.add("5 Days");
        days.add("6 Days");
        days.add("7 Days");
        days.add("8 Days");
        days.add("9 Days");
        days.add("10 Days");
        days.add("11 Days");
        days.add("12 Days");
        days.add("13 Days");
        days.add("14 Days");
        days.add("15 Days");
        days.add("16 Days");
        days.add("17 Days");
        days.add("18 Days");
        days.add("19 Days");
        days.add("20 Days");
        days.add("21 Days");
        days.add("22 Days");
        days.add("23 Days");
        days.add("24 Days");
        days.add("25 Days");
        days.add("26 Days");
        days.add("27 Days");
        days.add("28 Days");
        days.add("29 Days");
        days.add("30 Days");
        days.add("31 Days");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, days);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);
        // Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        // getSupportActionBar().setDisplayShowHomeEnabled(true);
        // getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_chevron_left_black_24dp);

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent intent= new Intent(this, ChallengeActivity.class);
            startActivity(intent);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        String item = parent.getItemAtPosition(position).toString();

        // Showing selected spinner item
        selectdays=position+1;
      //  Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
    }
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }
    public void showDatePickerDialog(){


        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
//
//        Formatter fmt = new Formatter();
//        Calendar cal = Calendar.getInstance();
//        fmt = new Formatter();
//        fmt.format("%tb", cal);
//
//        String date = fmt + " " + dayOfMonth + ", " + year;
        try {
            Calendar c = Calendar.getInstance();
            c.set(Calendar.YEAR, year);
            c.set(Calendar.MONTH, month);
            c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            String currentDateString = DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());

            txtDate.setText(currentDateString);
            btndate_picker.setText("EDIT");
            java.util.Date date = new Date(currentDateString);
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            String format = formatter.format(date);
            System.out.println(format);
            startdate=format;
        }catch (Exception e){}
    }
    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }


}
