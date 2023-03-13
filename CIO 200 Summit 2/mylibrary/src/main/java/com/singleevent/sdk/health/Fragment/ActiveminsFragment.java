package com.singleevent.sdk.health.Fragment;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.data.Bucket;
import com.google.android.gms.fitness.data.DataPoint;
import com.google.android.gms.fitness.data.DataSet;
import com.google.android.gms.fitness.data.DataSource;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.data.Field;
import com.google.android.gms.fitness.request.DataReadRequest;
import com.google.android.gms.fitness.result.DataReadResponse;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.singleevent.sdk.R;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.multidex.BuildConfig;
import androidx.recyclerview.widget.RecyclerView;
import io.paperdb.Paper;

import static com.singleevent.sdk.App.TAG;
import static com.singleevent.sdk.health.Fragment.ActivityFragment.dayStringFormat;
import static java.text.DateFormat.getDateInstance;
import static java.text.DateFormat.getTimeInstance;

/**
 * A simple {@link Fragment} subclass.
 */
public class ActiveminsFragment extends Fragment {

    BarChart barChart,barChart_month,barChart_year;
    private ArrayList<String> HmMins ;
    private ArrayList<String> HmTimestamp ;
    int m_totalscores,m_dailyscores;
    TextView d_dailyscore,d_totalscore,mtotalscore,mweekscore,ytotalscore,ymonthscore,txtWeek1,txtWeek2;
    RecyclerView recyclerViewActiveMins;
    private ArrayList<String> monthsactmin ;
    private ArrayList<String> yearlyactmin ;
    private  ArrayList<String> mactsmonth ;
    private  ArrayList<String> mactsyesr ;
    private  ArrayList<String> mDate = new ArrayList<>();
    private  ArrayList<String> mMins = new ArrayList<>();
    private ProgressDialog dialog;
    static ArrayList<Long> startweekmil = new ArrayList<>();
    static ArrayList<Long> endweekmil = new ArrayList<>();
    private  ArrayList<String> mSteps ;
    private  ArrayList<String> weekday ;
    Button btn,btnYear,btnWeek,btnLeft,btnRight,btnLeft1,btnRight1,btnLeft2,btnRight2;;
    static int currentIndex = 0;
    private  ArrayList<String> arr ;
    TextView txtWeek;
    ArrayList<String> monthArray;
    int month, monthcount;
    int year,yearcount;
    int l;
    static HashMap<Integer,Long> startmonthmil = new HashMap<>();
    static HashMap<Integer,Long> endmonthmil = new HashMap<>();
    static HashMap<Integer,Long> startyearmil = new HashMap<>();
    static HashMap<Integer,Long> endyearmil = new HashMap<>();

    int monthdata,yeardata,monthtotal,monthweekly,yeartotal,yearmonth;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_activemins, container, false);

        barChart = (BarChart)v. findViewById(R.id.barchartMins);
        barChart_month = (BarChart)v.findViewById(R.id.barchartMins_Monthh);
        barChart_year = (BarChart)v.findViewById(R.id.barchartMins_year);
        d_dailyscore=(TextView)v.findViewById(R.id.d_dailyscore);
        d_totalscore=(TextView)v.findViewById(R.id.d_totalscore);
        mtotalscore=(TextView)v.findViewById(R.id.d_totalscore1);
        mweekscore=(TextView)v.findViewById(R.id.d_dailyscore1);
        ytotalscore=(TextView)v.findViewById(R.id.d_totalscore2);
        ymonthscore=(TextView)v.findViewById(R.id.d_dailyscore2);
        monthsactmin=new ArrayList<>();
        yearlyactmin=new ArrayList<>();
        HmMins=new ArrayList<>();
        HmTimestamp=new ArrayList<>();
        mactsmonth=new ArrayList<>();
        mactsyesr=new ArrayList<>();
        monthArray=new ArrayList<>();
        weekday=new ArrayList<>();
        btnLeft = v.findViewById(R.id.left_btn);
        btnRight = v.findViewById(R.id.right_btn);
        txtWeek = (TextView)v.findViewById(R.id.testing_week_txt);
        txtWeek1 = (TextView)v.findViewById(R.id.testing_week_txt1);
        btnLeft1 = v.findViewById(R.id.left_btn1);
        btnRight1 = v.findViewById(R.id.right_btn1);
        btnLeft2 = v.findViewById(R.id.left_btn2);
        btnRight2 = v.findViewById(R.id.right_btn2);
        txtWeek2=(TextView)v.findViewById(R.id.testing_week_txt2);
        dialog = new ProgressDialog(getContext(), R.style.MyAlertDialogStyle);
        dialog.setMessage("Syncing data....");
        dialog.show();

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
        weekday.add("Mon");
        weekday.add("Tue");
        weekday.add("Wed");
        weekday.add("Thu");
        weekday.add("Fri");
        weekday.add("Sat");
        weekday.add("Sun");
        Date date = new Date();
        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        month= localDate.getMonthValue();
        monthcount=month;
        txtWeek1.setText(monthArray.get(month-1));


        try {
            readActiveHistoryData("month",0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            readActiveHistoryData("year",0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        int in=Paper.book().read("Current_Index",0);
        currentIndex=in;
        arr=new ArrayList<>();
        arr=Paper.book().read("Week_array");
        txtWeek.setText(arr.get(currentIndex));
        btnRight.setVisibility(View.INVISIBLE);
        btnRight1.setVisibility(View.INVISIBLE);
        btnRight2.setVisibility(View.INVISIBLE);



        Calendar calendar = Calendar.getInstance();
        int ind = calendar.get(Calendar.MONTH);
        DateFormat simple = new SimpleDateFormat(" MMM");
//        Date res = new Date(in);
        String MonthString = simple.format(calendar.getTime()).toString();
        // textView.setText(MonthString);
        /*Calendar cyear = Calendar.getInstance();
        cyear.add(Calendar.YEAR, 1);
        year= cyear.get(Calendar.YEAR);*/


        String yearString = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
        year=Integer.parseInt(yearString);
        System.out.println("THIS IS CURRENT YEAR"+year);
        yearcount=year;
        // txtYear.setText(yearString);
        txtWeek2.setText(yearString);





        btnLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    btnRight.setVisibility(View.VISIBLE);
                    getPreviousWeek();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        btnRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    getNextWeek();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
        btnLeft1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    txtWeek1.setText(monthArray.get(monthcount-1));
                    getPreviousMonth(monthcount-1);
                    monthcount--;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        btnRight1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    if(monthcount<month) {
                        getNextMonth(monthcount + 1);
                        txtWeek1.setText(monthArray.get(monthcount));
                        monthcount++;
                    }else{
                        btnRight1.setVisibility(View.INVISIBLE);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        btnLeft2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    txtWeek2.setText(String.valueOf(yearcount-1));
                    dialog = new ProgressDialog(getContext(), R.style.MyAlertDialogStyle);
                    dialog.setMessage("Syncing data....");
                    dialog.show();
                    getPreviousYear(yearcount-1);

                    yearcount--;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        btnRight2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    if(yearcount+1!=year&& yearcount<year) {
                        dialog = new ProgressDialog(getContext(), R.style.MyAlertDialogStyle);
                        dialog.setMessage("Syncing data....");
                        dialog.show();
                        yearcount++;
                        txtWeek2.setText(String.valueOf(yearcount));
                        getNextYear(yearcount);

                    }else{
                        dialog = new ProgressDialog(getContext(), R.style.MyAlertDialogStyle);
                        dialog.setMessage("Syncing data....");
                        dialog.show();
                        txtWeek2.setText(String.valueOf(year));
                        btnRight2.setVisibility(View.INVISIBLE);
                        getNextYear(yearcount);
                        yearcount=year;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });



        return v;

    }



    @RequiresApi(api = Build.VERSION_CODES.O)
    private  void getPreviousWeek() throws Exception {
        btnRight.setVisibility(View.VISIBLE);

        startweekmil=Paper.book().read("startweekname");
        endweekmil=Paper.book().read("endweekname");

        currentIndex--;
        String dim = arr.get(currentIndex);
        txtWeek.setText(dim);
        System.out.println(arr.get(currentIndex));
        startweekmil.get(currentIndex);
        endweekmil.get(currentIndex);
        mSteps =new ArrayList<>();
        readActiveHistoryData("week",currentIndex);
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    private  void getNextWeek() throws Exception {
        currentIndex++;
        startweekmil=Paper.book().read("startweekname");
        endweekmil=Paper.book().read("endweekname");
        String dim = arr.get(currentIndex);
        txtWeek.setText(dim);
        System.out.println(arr.get(currentIndex));
        mSteps =new ArrayList<>();
        Date date = new Date();
        long timeMilli = date.getTime();
        if(startweekmil.get(currentIndex)<=timeMilli) {
            btnRight.setVisibility(View.VISIBLE);
            readActiveHistoryData("week", currentIndex);
        }else{
            btnRight.setVisibility(View.INVISIBLE);
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void initUI(){
        ArrayList<BarEntry> visitors  = new ArrayList<>();
        HmMins= Paper.book().read("Weekly_Mins",null);
        m_totalscores=0;
        m_dailyscores=0;
        if(HmMins!=null)
        {
            for(int i=0; i<HmMins.size(); i++) {
                if ((Integer.parseInt(HmMins.get(i)))!= 0) {
                    visitors.add(new BarEntry(i, Float.parseFloat(HmMins.get(i))));
                    m_totalscores += Integer.parseInt(HmMins.get(i));
                }
                else{
                    visitors.add(new BarEntry(i, 0));
                }
            }
            m_dailyscores=m_totalscores/7;
        }
        if(m_dailyscores>0)
        {
            //String temp=df.format(m_dailyscores);
            d_dailyscore.setText(String.valueOf(m_dailyscores));
        }
        if(m_totalscores>0)
        {
            d_totalscore.setText(String.valueOf(m_totalscores));
        }
       /* visitors.add(new BarEntry(0,420));
        visitors.add(new BarEntry(1,120));
        visitors.add(new BarEntry(2,850));
        visitors.add(new BarEntry(3,1050));
        visitors.add(new BarEntry(4,5620));
        visitors.add(new BarEntry(5,920));
        visitors.add(new BarEntry(6,1220));*/




        BarDataSet barDataSet = new BarDataSet(visitors,"By WEEK");
        barDataSet.setColors(Color.parseColor("#f4c475"));
        barDataSet.setValueTextColor(Color.BLACK);
        barDataSet.setValueTextSize(12f);


        BarData barData = new BarData(barDataSet);

        barChart.setFitBars(true);
        barChart.setData(barData);
        barChart.getDescription().setText("Bar Chart");
        barChart.animateY(2000);

        final ArrayList<String> xAxisLabel = new ArrayList<>();
      /*  xAxisLabel.add("Mon");
        xAxisLabel.add("Tue");
        xAxisLabel.add("Wed");
        xAxisLabel.add("Thu");
        xAxisLabel.add("Fri");
        xAxisLabel.add("Sat");
        xAxisLabel.add("Sun");*/
        HmTimestamp= Paper.book().read("TimeStamp",null);
        if(HmMins!=null)
        {
            for(int i=0; i<7; i++)
            {
                xAxisLabel.add(weekday.get(i));
            }


        }

//        ValueFormatter xAxisFormatter = new DayAxisValueFormatter(barChart);

        XAxis xAxis = barChart.getXAxis();


        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f); // only intervals of 1 day
        xAxis.setLabelCount(7);

//        xAxis.setValueFormatter(xAxisFormatter);
        barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(xAxisLabel));



        //Month
        ArrayList<BarEntry> month  = new ArrayList<>();
        monthsactmin=Paper.book().read("Monthly_AMins",null);

        if(monthsactmin!=null){
            l=monthsactmin.size()/7;}
        int count=0;
        monthtotal=0;
        monthweekly=0;

        if(monthsactmin!=null) {
            for (int i = 0; i < monthsactmin.size(); i++) {
                monthtotal = monthtotal + (Integer.parseInt(monthsactmin.get(i)));
                monthdata = monthdata + (Integer.parseInt(monthsactmin.get(i)));
                if(monthdata!=0) {
                    if (i % 7 == 0 && count < l) {

                        month.add(new BarEntry(count, monthdata));
                        monthdata = 0;
                        count++;
                    }
                }

                else if (count >= l && i == monthsactmin.size() - 1) {
                    if(monthdata!=0) {
                        month.add(new BarEntry(count, monthdata));
                        monthdata = 0;
                    }
                }
            }
        }
        try {
            monthweekly = monthtotal / count;
        }catch (Exception e)
        {}
        mweekscore.setText(String.valueOf(monthweekly));
        mtotalscore.setText(String.valueOf(monthtotal));

        //Month


        BarDataSet barDataSet_month = new BarDataSet(month,"MONTH");
        barDataSet_month.setColors(Color.parseColor("#f4c475"));
        barDataSet_month.setValueTextColor(Color.BLACK);
        barDataSet_month.setValueTextSize(12f);


        BarData barData_month = new BarData(barDataSet_month);

        barChart_month.setFitBars(true);
        barData_month.setBarWidth(0.5f);
        barChart_month.setData(barData_month);
        barChart_month.getDescription().setText("Bar Chart");
        barChart_month.animateY(2000);

        final ArrayList<String> xAxisLabel_month = new ArrayList<>();
        xAxisLabel_month.add("9" + "/" + "1");
        xAxisLabel_month.add("9" + "/" + "8");
        xAxisLabel_month.add("9" + "/" + "14");
        xAxisLabel_month.add("9" + "/" + "21");
        xAxisLabel_month.add("9" + "/" + "28");


//        ValueFormatter xAxisFormatter = new DayAxisValueFormatter(barChart);

        XAxis xAxis_month = barChart_month.getXAxis();


        xAxis_month.setPosition(XAxis.XAxisPosition.BOTTOM);

        xAxis_month.setDrawGridLines(false);
        xAxis_month.setGranularity(1f); // only intervals of 1 day
        xAxis_month.setLabelCount(6);
        xAxis_month.setSpaceMax(4);
//        xAxis.setValueFormatter(xAxisFormatter);
        barChart_month.getXAxis().setValueFormatter(new IndexAxisValueFormatter(xAxisLabel_month));


        //year
        final ArrayList<String> xAxisLabel_year = new ArrayList<>();
        yearlyactmin=Paper.book().read("Yearly_AMins",null);
        int monthcount=0;

        ArrayList<BarEntry> year  = new ArrayList<>();

        xAxisLabel_year.add("Jan");
        xAxisLabel_year.add("Feb");
        xAxisLabel_year.add("Mar");
        xAxisLabel_year.add("Apr");
        xAxisLabel_year.add("May");
        xAxisLabel_year.add("Jun");
        xAxisLabel_year.add("Jul");
        xAxisLabel_year.add("Aug");
        xAxisLabel_year.add("Sep");
        xAxisLabel_year.add("Oct");
        xAxisLabel_year.add("Nov");
        xAxisLabel_year.add("Dec");

        yeartotal=0;
        yearmonth=0;
        if(yearlyactmin!=null) {
            for (int i = 0; i < yearlyactmin.size(); i++)
            {
                //   int days = DateTime.DaysInMonth(2018,05);
                // int days = System.DateTime.DaysInMonth(year, month);

                yeartotal=yeartotal+(Integer.parseInt(yearlyactmin.get(i)));
                yeardata=yeardata+(Integer.parseInt(yearlyactmin.get(i)));
                if(i==30)
                {
                    if(yeardata!=0) {
                        //  xAxisLabel_year.add("Jan");
                        year.add(new BarEntry(0, yeardata));
                        yeardata = 0;
                        monthcount=1;
                    }
                }
                else if(i==59) {
                    if(yeardata!=0) {
                        //xAxisLabel_year.add("Feb");
                        year.add(new BarEntry(1, yeardata));
                        yeardata = 0;
                        monthcount=2;
                    }
                }
                else if(i==90){
                    if(yeardata!=0) {
                        // xAxisLabel_year.add("Mar");

                        year.add(new BarEntry(2, yeardata));
                        yeardata = 0;
                        monthcount=3;
                    }
                }
                else if(i==120){
                    if(yeardata!=0) {
                        //xAxisLabel_year.add("Apr");
                        year.add(new BarEntry(3, yeardata));
                        yeardata = 0;
                        monthcount=4;

                    }
                }
                else if(i==151){
                    if(yeardata!=0) {
                        //xAxisLabel_year.add("May");
                        year.add(new BarEntry(4, yeardata));
                        yeardata = 0;
                        monthcount=5;
                    }
                }
                else if(i==181){
                    if(yeardata!=0) {
                        //xAxisLabel_year.add("Jun");
                        year.add(new BarEntry(5, yeardata));
                        yeardata = 0;
                        monthcount=6;
                    }
                }
                else if(i==212){
                    if(yeardata!=0) {
                        //xAxisLabel_year.add("Jul");
                        year.add(new BarEntry(6, yeardata));
                        yeardata = 0;
                        monthcount=7;
                    }
                }
                else if(i==242){
                    if(yeardata!=0) {
                        // xAxisLabel_year.add("Aug");
                        year.add(new BarEntry(7, yeardata));
                        yeardata = 0;
                        monthcount=8;
                    }
                }
                else if(i==273){
                    if(yeardata!=0) {
                        //xAxisLabel_year.add("Sep");
                        year.add(new BarEntry(8, yeardata));
                        yeardata = 0;
                        monthcount=9;
                    }
                }  else if(i==303){
                    if(yeardata!=0) {
                        // xAxisLabel_year.add("Oct");
                        year.add(new BarEntry(9, yeardata));
                        yeardata = 0;
                        monthcount=10;
                    }
                }
                else if(i==334){
                    if(yeardata!=0) {
                        //xAxisLabel_year.add("Nov");
                        year.add(new BarEntry(10, yeardata));
                        yeardata = 0;
                        monthcount=11;
                    }
                }
                else if(i==364){
                    if(yeardata!=0) {
                        //  xAxisLabel_year.add("Dec");
                        year.add(new BarEntry(11, yeardata));
                        yeardata = 0;
                        monthcount=12;
                    }
                }
                else if(i==yearlyactmin.size()-1 && i%30!=0 && i%31!=0){
                    year.add(new BarEntry(monthcount , yeardata));
                    yeardata=0;
                }
                /*else{
                    if(yeardata!=0) {
                        int temp=monthcount;
                        String m=monthArray.get(monthcount+1);
                        xAxisLabel_year.add(m);
                        year.add(new BarEntry(temp-1, yeardata));
                        yeardata = 0;
                        monthcount=0;
                    }
                }*/

            }
        }

        try {
            yearmonth = yeartotal / monthcount;
        }catch (Exception e)
        {

        }
        //  String temp = df.format(yeartotal);
        //String temp1 = df.format(yearmonth);
        ytotalscore.setText(String.valueOf(yeartotal));
        ymonthscore.setText(String.valueOf(yearmonth));
        dialog.hide();
        BarDataSet barDataSet_year = new BarDataSet(year, "YEAR");
        barDataSet_year.setColors(Color.parseColor("#509cfa"));
        barDataSet_year.setValueTextColor(Color.BLACK);
        barDataSet_year.setValueTextSize(12f);
        BarData barData_year = new BarData(barDataSet_year);
        barChart_year.setFitBars(true);
        barData_year.setBarWidth(0.3f);
        barChart_year.setData(barData_year);
        barChart_year.getDescription().setText("Bar Chart");
        barChart_year.animateY(2000);


       /* xAxisLabel_year.add("2010");
        xAxisLabel_year.add("2011");
        xAxisLabel_year.add("2012");
        xAxisLabel_year.add("2013");
        xAxisLabel_year.add("2014");
        xAxisLabel_year.add("2015");
        xAxisLabel_year.add("2016");
        xAxisLabel_year.add("2017");
        xAxisLabel_year.add("2018");
        xAxisLabel_year.add("2019");
        xAxisLabel_year.add("2020");*/


//        ValueFormatter xAxisFormatter = new DayAxisValueFormatter(barChart);

        XAxis xAxis_year = barChart_year.getXAxis();
        xAxis_year.setPosition(XAxis.XAxisPosition.BOTTOM);

        xAxis_year.setDrawGridLines(false);
        xAxis_year.setGranularity(1f); // only intervals of 1 day
        xAxis_year.setLabelCount(12);
//        xAxis.setValueFormatter(xAxisFormatter);
        barChart_year.getXAxis().setValueFormatter(new IndexAxisValueFormatter(xAxisLabel_year));
    }
    /// Adding API call to get Active miniute
    @RequiresApi(api = Build.VERSION_CODES.O)
    private Task<DataReadResponse> readActiveHistoryData(String type,int index) throws Exception {
        // Begin by creating the query.
        DataReadRequest readRequest = ActivequeryFitnessData(type,index);

        // Invoke the History API to fetch the data with the query
        return Fitness.getHistoryClient(getContext(), GoogleSignIn.getLastSignedInAccount(getContext()))
                .readData(readRequest)
                .addOnSuccessListener(
                        new OnSuccessListener<DataReadResponse>() {
                            @Override
                            public void onSuccess(DataReadResponse dataReadResponse) {
                                // For the sake of the sample, we'll print the data so we can see what we just
                                // added. In general, logging fitness information should be avoided for privacy
                                // reasons.
                                ActiveprintData(dataReadResponse,type);
                            }
                        })
                .addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.e(TAG, "There was a problem reading the data.", e);
                            }
                        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public  void ActiveprintData(DataReadResponse dataReadResult, String type) {
        // [START parse_read_data_result]
        // If the DataReadRequest object specified aggregated data, dataReadResult will be returned
        // as buckets containing DataSets, instead of just DataSets.
        int checZero = 0;
        if (dataReadResult.getBuckets().size() > 0) {
            Log.i(TAG, "Number of returned buckets of DataSets is: " + dataReadResult.getBuckets().size());
            for (Bucket bucket : dataReadResult.getBuckets()) {

                List<DataSet> dataSets = bucket.getDataSets();


                for (DataSet dataSet : dataSets) {


                    if (dataSet.getDataPoints().isEmpty()) {


                        long startTime = bucket.getEndTime(TimeUnit.MILLISECONDS);
                        long endTime = bucket.getEndTime(TimeUnit.MILLISECONDS);
                        DataSource dataSource = new DataSource.Builder()
                                .setAppPackageName(BuildConfig.APPLICATION_ID)
                                .setDataType(DataType.TYPE_MOVE_MINUTES)
                                .setStreamName("move mins")
                                .setType(DataSource.TYPE_RAW)
                                .build();
                        dataSet = DataSet.create(dataSource);
                        // For each data point, specify a start time, end time, and the data value -- in this case,
                        // the number of new steps.
                        DataPoint dataPoint =
                                dataSet.createDataPoint()
                                        .setTimeInterval(startTime, endTime, TimeUnit.MILLISECONDS);
                        dataPoint.getValue(Field.FIELD_DURATION).setInt(checZero);
                        dataSet.add(dataPoint);


                    }

                    Log.i(TAG, "Get Data Points==Point::" + dataSet.getDataPoints());

                    ActivedumpDataSet(dataSet,type);

                }
            }






        } else if (dataReadResult.getDataSets().size() > 0) {

            for (DataSet dataSet : dataReadResult.getDataSets()) {
                ActivedumpDataSet(dataSet,type);
            }
        }
        // [END parse_read_data_result]
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public  DataReadRequest ActivequeryFitnessData(String h,int index) throws Exception {
        // [START build_read_data_request]
        // Setting a start and end date using a range of 1 week before this moment.
        long startTime=0l;
        long endTime=0l;
        Calendar cal = Calendar.getInstance();
        Date now = new Date();
        cal.setTime(now);
        cal.setTimeInMillis(0);
        cal.set(Calendar.getInstance().get(Calendar.YEAR),Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DATE),23,59,59);
        cal.getTime();
        if(index==0) {
            endTime = cal.getTimeInMillis();
        }

        cal.set(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DATE));
//        Log.i(TAG,"EndDate"+cal.getTime());
//        cal.getTime();
        LocalDate s= LocalDate.now().with ( ChronoField.DAY_OF_MONTH , 1 );


        if (h.equalsIgnoreCase("week")) {
            cal.add(Calendar.WEEK_OF_YEAR, -1);
            if(index==0) {
                endTime = cal.getTimeInMillis();
                startTime = cal.getTimeInMillis();
            }
            else{
                startTime=startweekmil.get(index);
                endTime=endweekmil.get(index);
            }

            //  startTime = cal.getTimeInMillis();
        }//1596479400000l
        else if (h.equalsIgnoreCase("month")) {

            if(index==0) {
                cal.add(Calendar.MONTH, -1);
                /// endTime = cal.getTimeInMillis();
                startTime= datetomill(s);
            }
            else{
                // cal.add(Calendar.MONTH, -(month-index));
                endTime=endmonthmil.get(index);
                startTime=startmonthmil.get(index);
            }
        } else if (h.equalsIgnoreCase("year")){
            if(index==0) {
                Date d = new Date();
                DateFormat dateFormat = new SimpleDateFormat("yyyy");
                String format = dateFormat.format(d);
                // System.out.println("Current date and time = " + format);
                //System.out.printf("Four-digit Year = %TY\n",d);
                int year = Integer.parseInt(format);
                Calendar calendarStart = Calendar.getInstance();
                calendarStart.set(Calendar.YEAR, year);
                calendarStart.set(Calendar.MONTH, 0);
                calendarStart.set(Calendar.DAY_OF_MONTH, 1);
                // returning the first date
                startTime = calendarStart.getTimeInMillis();
                endTime = cal.getTimeInMillis();
                Calendar calendarEnd = Calendar.getInstance();
                calendarEnd.set(Calendar.YEAR, year);
                calendarEnd.set(Calendar.MONTH, 11);
                calendarEnd.set(Calendar.DAY_OF_MONTH, 31);


                // returning the last date
                Date endDate = calendarEnd.getTime();

                cal.add(Calendar.YEAR, -1);
            }
            else{
                endTime=endyearmil.get(index);
                startTime=startyearmil.get(index);

            }

          /*  Date d = new Date();
            DateFormat dateFormat = new SimpleDateFormat("yyyy");
            String format = dateFormat.format(d);
            // System.out.println("Current date and time = " + format);
            //System.out.printf("Four-digit Year = %TY\n",d);
            int year=Integer.parseInt(format);
            Calendar calendarStart=Calendar.getInstance();
            calendarStart.set(Calendar.YEAR,year);
            calendarStart.set(Calendar.MONTH,0);
            calendarStart.set(Calendar.DAY_OF_MONTH,1);
            // returning the first date
            startTime=calendarStart.getTimeInMillis();

            Calendar calendarEnd=Calendar.getInstance();
            calendarEnd.set(Calendar.YEAR,year);
            calendarEnd.set(Calendar.MONTH,11);
            calendarEnd.set(Calendar.DAY_OF_MONTH,31);

            // returning the last date
            Date endDate=calendarEnd.getTime();

            cal.add(Calendar.YEAR, -1);*/
            //  startTime = cal.getTimeInMillis();
        }


//        ArrayList<Long> dates = new ArrayList<>();
//        dates.add(startTime);

        java.text.DateFormat dateFormat = getDateInstance(DateFormat.FULL);
        Log.i(TAG, "Range Start: " + dateFormat.format(startTime));
        Log.i(TAG, "Range End: " + dateFormat.format(endTime));

        DataReadRequest readRequest =
                new DataReadRequest.Builder()
                        // The data request can specify multiple data types to return, effectively
                        // combining multiple data queries into one call.
                        // In this example, it's very unlikely that the request is for several hundred
                        // datapoints each consisting of a few steps and a timestamp.  The more likely
                        // scenario is wanting to see how many steps were walked per day, for 7 days.
                        .aggregate(DataType.TYPE_MOVE_MINUTES, DataType.TYPE_MOVE_MINUTES)
                        // Analogous to a "Group By" in SQL, defines how data should be aggregated.
                        // bucketByTime allows for a time span, whereas bucketBySession would allow
                        // bucketing by "sessions", which would need to be defined in code.
                        .bucketByTime(1, TimeUnit.DAYS)
                        .setTimeRange(startTime, endTime, TimeUnit.MILLISECONDS)
                        .enableServerQueries()
                        .build();
        // [END build_read_data_request]

        return readRequest;
    }


    public  Long datetomill(LocalDate s) throws Exception {
        String myDate = s.toString();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = sdf.parse(myDate);
        long start = date.getTime();
        return start;
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public  void DistanceprintData(DataReadResponse dataReadResult, String type) {
        // [START parse_read_data_result]
        // If the DataReadRequest object specified aggregated data, dataReadResult will be returned
        // as buckets containing DataSets, instead of just DataSets.
        int checZero = 0;
        if (dataReadResult.getBuckets().size() > 0) {
            Log.i(TAG, "Number of returned buckets of DataSets is: " + dataReadResult.getBuckets().size());
            for (Bucket bucket : dataReadResult.getBuckets()) {

                List<DataSet> dataSets = bucket.getDataSets();


                for (DataSet dataSet : dataSets) {


                    if (dataSet.getDataPoints().isEmpty()) {


                        long startTime = bucket.getEndTime(TimeUnit.MILLISECONDS);
                        long endTime = bucket.getEndTime(TimeUnit.MILLISECONDS);
                        DataSource dataSource = new DataSource.Builder()
                                .setAppPackageName(BuildConfig.APPLICATION_ID)
                                .setDataType(DataType.TYPE_MOVE_MINUTES)
                                .setStreamName("move mins")
                                .setType(DataSource.TYPE_RAW)
                                .build();
                        dataSet = DataSet.create(dataSource);
                        // For each data point, specify a start time, end time, and the data value -- in this case,
                        // the number of new steps.
                        DataPoint dataPoint =
                                dataSet.createDataPoint()
                                        .setTimeInterval(startTime, endTime, TimeUnit.MILLISECONDS);
                        dataPoint.getValue(Field.FIELD_DURATION).setInt(checZero);
                        dataSet.add(dataPoint);

                    }


                    Log.i(TAG, "Get Data Points==Point::" + dataSet.getDataPoints());

                    ActivedumpDataSet(dataSet,type);

                }
            }






        } else if (dataReadResult.getDataSets().size() > 0) {

            for (DataSet dataSet : dataReadResult.getDataSets()) {
                ActivedumpDataSet(dataSet,type);
            }
        }
        // [END parse_read_data_result]
    }
    // [START parse_dataset]

    @RequiresApi(api = Build.VERSION_CODES.O)
    private  void ActivedumpDataSet(DataSet dataSet,String type) {
        Log.i(TAG, "Data returned for Data type: " + dataSet.getDataType().getName());
        DateFormat dateFormat = getTimeInstance();

        java.text.DateFormat dateFormat3 = getDateInstance(DateFormat.FULL);
        for (DataPoint dp : dataSet.getDataPoints()) {

            Log.i(TAG, "Data point:");
            Log.i(TAG, "\tType: " + dp.getDataType().getName());
            Log.i(TAG, "\tStart: Date: "+"==" +dateFormat3.format(dp.getStartTime(TimeUnit.MILLISECONDS)));
            Log.i(TAG, "\tEnd: Date: "+"==" + dateFormat3.format(dp.getEndTime(TimeUnit.MILLISECONDS)));
            //   mDate.add(dateFormat3.format(dp.getEndTime(TimeUnit.MILLISECONDS)));
            // mDate.add(dayStringFormat(dp.getEndTime(TimeUnit.MILLISECONDS)));
            for (Field field : dp.getDataType().getFields()) {
                Log.i(TAG, "\tField: " + field.getName() + " Value: " + dp.getValue(field));

                //   System.out.println("Checking size mMins ::"+mMins.size());
                // mMins.add(dp.getValue(field).toString());

                //Paper.book().write("Active_minutes",mMins);

                if(type.equalsIgnoreCase("week")){
                    mSteps.add(dp.getValue(field).toString());
                }
                if(type.equalsIgnoreCase("month"))
                {
                    mactsmonth.add(dp.getValue(field).toString());

                }
                else if(type.equalsIgnoreCase("year"))
                {
                    mactsyesr.add(dp.getValue(field).toString());
                }
            }
            if(mSteps!=null)
            {
                Paper.book().write("Weekly_Mins",mSteps);
            }
            if(mactsmonth.size()>0)
            {
                Paper.book().write("Monthly_AMins",mactsmonth);
            }
            if(mactsyesr.size()>0)
            {
                Paper.book().write("Yearly_AMins",mactsyesr);
            }
        }
        try{ initUI();}catch (Exception e){

        }
    }
//        initHorizontal();


    @RequiresApi(api = Build.VERSION_CODES.O)
    public void  getNextMonth(int n) throws Exception {

        if(n<month) {
            btnRight1.setVisibility(View.VISIBLE);
        }
        txtWeek1.setText(monthArray.get(n));
        mactsmonth=new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH,  -(month-n));
        calendar.set(Calendar.DATE, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
        Long nextMonthFirstDay = calendar.getTimeInMillis();
        startmonthmil.put(n,nextMonthFirstDay);
        calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        Long nextMonthLastDay = calendar.getTimeInMillis();
        endmonthmil.put(n,nextMonthLastDay);
        readActiveHistoryData("month",n);
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void getPreviousMonth(int n) throws Exception {
        btnRight1.setVisibility(View.VISIBLE);
        txtWeek1.setText(monthArray.get(n-1));
        //System.out.println(arr.get(n));
        // startmonthmil.get(n);
        // endmonthmil.get(n);
        mactsmonth=new ArrayList<>();
        Calendar aCalendar = Calendar.getInstance();
// add -1 month to current month
        aCalendar.add(Calendar.MONTH, -(month-n));
// set DATE to 1, so first date of previous month
        aCalendar.set(Calendar.DATE, 1);
        Long firstDateOfPreviousMonth = aCalendar.getTimeInMillis();
        startmonthmil.put(n,firstDateOfPreviousMonth);
// set actual maximum date of previous month
        aCalendar.set(Calendar.DATE, aCalendar.getActualMaximum(Calendar.DAY_OF_MONTH));
//read it
        Long lastDateOfPreviousMonth = aCalendar.getTimeInMillis();
        endmonthmil.put(n,lastDateOfPreviousMonth);
        readActiveHistoryData("month",n);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void  getNextYear(int n) throws Exception {

        int temp=0;
        startyearmil=new HashMap<>();
        endyearmil=new HashMap<>();
        if(n==year) {
            btnRight2.setVisibility(View.INVISIBLE);
        }
        if(year!=n) {
            temp = year - n;
            temp = temp - 1;
        }
        else{
            temp=-1;
        }
        System.out.println("NEXT YEAR"+temp);

        mactsyesr=new ArrayList<>();
        Date d = new Date();
        DateFormat dateFormat = new SimpleDateFormat("yyyy");
        String format = dateFormat.format(d);
        // System.out.println("Current date and time = " + format);
        //System.out.printf("Four-digit Year = %TY\n",d);
        //int year = Integer.parseInt(format);
        Calendar calendarStart = Calendar.getInstance();
        calendarStart.set(Calendar.YEAR, n+1);
        calendarStart.set(Calendar.MONTH, 0);
        calendarStart.set(Calendar.DAY_OF_MONTH, 1);
        // returning the first date
        startyearmil.put(n,calendarStart.getTimeInMillis());


        Calendar calendarEnd = Calendar.getInstance();
        calendarEnd.set(Calendar.YEAR,  n+1);
        calendarEnd.set(Calendar.MONTH, 11);
        calendarEnd.set(Calendar.DAY_OF_MONTH, 31);
        endyearmil.put(n,calendarEnd.getTimeInMillis());
        readActiveHistoryData("year",n);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void getPreviousYear(int n) throws Exception {

        btnRight2.setVisibility(View.VISIBLE);
        int temp=year-n;
        temp=temp+1;
        System.out.println("PREVIOUS YEAR"+temp);
        //  txtWeek2.setText(monthArray.get(n-1));

        //System.out.println(arr.get(n));
        // startmonthmil.get(n);
        // endmonthmil.get(n);
        mactsyesr=new ArrayList<>();

        Date d = new Date();
        DateFormat dateFormat = new SimpleDateFormat("yyyy");
        String format = dateFormat.format(d);
        // System.out.println("Current date and time = " + format);
        //System.out.printf("Four-digit Year = %TY\n",d);
        //int year = Integer.parseInt(format);
        Calendar calendarStart = Calendar.getInstance();
        calendarStart.add(Calendar.YEAR, -temp);
        calendarStart.add(Calendar.MONTH, 0);
        calendarStart.add(Calendar.DAY_OF_MONTH, 1);
        // returning the first date
        startyearmil.put(n,calendarStart.getTimeInMillis());


        Calendar calendarEnd = Calendar.getInstance();
        calendarEnd.add(Calendar.YEAR,  -temp);
        calendarEnd.add(Calendar.MONTH, 11);
        calendarEnd.add(Calendar.DAY_OF_MONTH, 31);
        endyearmil.put(n,calendarEnd.getTimeInMillis());
        readActiveHistoryData("year",n);
    }



}



