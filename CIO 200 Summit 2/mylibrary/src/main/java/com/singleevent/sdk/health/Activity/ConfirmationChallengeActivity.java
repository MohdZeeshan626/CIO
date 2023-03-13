package com.singleevent.sdk.health.Activity;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.singleevent.sdk.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import androidx.appcompat.app.AppCompatActivity;
import io.paperdb.Paper;

public class ConfirmationChallengeActivity extends AppCompatActivity {

    Button btndone,btn_invitefrnds;
    String cname;
    long startdate,enddate;
    TextView challengename,startend;
    private int year;
    private int month;
    private int day;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation_challenge);
        btndone= findViewById(R.id.btn_done);
        challengename=findViewById(R.id.cname);
        btn_invitefrnds=findViewById(R.id.btn_invitefrnds);
        startend=(TextView)findViewById(R.id.startend);



        if (getIntent().getExtras() == null)
            finish();
        cname=getIntent().getExtras().getString("Challengename");
        startdate=getIntent().getExtras().getLong("startdate");
        enddate=getIntent().getExtras().getLong("end_date");
        if(cname!=null){
            challengename.setText(cname);
        }
        if(startdate!=0l&& enddate!=0l){

            startend.setText(gettime(startdate)+ " - "+gettime(enddate));
        }


        btn_invitefrnds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto:")); // only email apps should handle this
                intent.putExtra(Intent.EXTRA_EMAIL, Paper.book().read("Email", ""));
                intent.putExtra(Intent.EXTRA_SUBJECT, "For Join" + cname+" Challenge");
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
            }
        });



        btndone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(ConfirmationChallengeActivity.this, ChallengeActivity.class);
                startActivity(in);
                finish();

//                Fragment myFragment  = new ChallengeActivity();
//                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
//                fragmentTransaction.add(R.id.fragmentContainer, myFragment);
//                        fragmentTransaction.addToBackStack("youfragment");
//                fragmentTransaction.commit();
            }
        });
    }

    public String gettime(long mill){
        String dateString;

        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(mill);
       SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd");
        String days = myFormat.format(cal.getTime());
        year = cal.get(Calendar.YEAR);
        month = cal.get(Calendar.MONTH);
        day = cal.get(Calendar.DAY_OF_MONTH);
        String mo=getmonth(month);
        String temp=mo+" "+day;
        return temp;
    }
    public String getmonth(int n){
        ArrayList<String> monthArray=new ArrayList<>();
        monthArray.add("Jan");
        monthArray.add("Feb");
        monthArray.add("Mar");
        monthArray.add("Apr");
        monthArray.add("May");
        monthArray.add("Jun");
        monthArray.add("Jul");
        monthArray.add("Aug");
        monthArray.add("Sep");
        monthArray.add("Oct");
        monthArray.add("Nov");
        monthArray.add("Dec");
        return monthArray.get(n);
    }

}
