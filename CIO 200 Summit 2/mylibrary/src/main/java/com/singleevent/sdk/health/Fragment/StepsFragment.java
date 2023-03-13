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
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
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
import com.singleevent.sdk.View.RightActivity.MyMeetingBase;
import com.whiteelephant.monthpicker.MonthPickerDialog;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.Interval;
import org.joda.time.Period;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.temporal.ChronoField;
import java.time.temporal.WeekFields;
import java.util.ArrayList;

import java.util.Calendar;

import java.util.Date;
import java.util.Formatter;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.multidex.BuildConfig;
import io.paperdb.Paper;

import static com.singleevent.sdk.health.Fragment.ActivityFragment.dayStringFormat;
import static java.text.DateFormat.getDateInstance;
import static java.text.DateFormat.getTimeInstance;
import static org.joda.time.DateTimeConstants.SUNDAY;

/**
 * A simple {@link Fragment} subclass.
 */
public class StepsFragment extends Fragment    {

    //WeekWise
    static int currentIndex = 0;
    static ArrayList<String> arr = new ArrayList<>();
    static ArrayList<Long> startweekmil = new ArrayList<>();
    static ArrayList<Long> endweekmil = new ArrayList<>();
    static HashMap<Integer,Long> startmonthmil = new HashMap<>();
    static HashMap<Integer,Long> endmonthmil = new HashMap<>();
    static HashMap<Integer,Long> startyearmil = new HashMap<>();
    static HashMap<Integer,Long> endyearmil = new HashMap<>();
    static ArrayList<Integer> monthsize = new ArrayList<>();

    private static final String TAG ="StepFragment" ;
    BarChart barChart,barChart_month,barChart_year;
    Button btn,btnYear,btnWeek,btnLeft,btnRight,btnLeft1,btnRight1,btnLeft2,btnRight2;

    private ProgressDialog dialog;
    private ArrayList<String> HmSteps ;

    private ArrayList<String> HmTimestamp ;
    private ArrayList<String> monthsteps;
    private ArrayList<String> yearlysteps ;
    int monthdata,yeardata,monthtotal,monthweekly,yeartotal,yearmonth;
  private  TextView textView,txtYear,txtWeek,txtWeek1,txtWeek2,txtWeekBy,totalscore,dailyscore,mtotalscore,mweekscore,ytotalscore,ymonthscore;
  int totalscores,dailyscores;
    ArrayList<String> monthArray;
    private  ArrayList<String> mDate ;
    private  ArrayList<String> mSteps ;
    private  ArrayList<String> mStepsmonth ;
    private  ArrayList<String> mStepsyesr ;
    private  ArrayList<String> weekday ;
    String bn = "testing";
    int month,year;
    int monthcount,yearcount;
    ProgressDialog progressDialog;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_steps, container, false);
        barChart = (BarChart)v. findViewById(R.id.barchartStep_week);
        totalscore=(TextView)v.findViewById(R.id.totalscore);
        dailyscore=(TextView)v.findViewById(R.id.dailyscore);
        mtotalscore=(TextView)v.findViewById(R.id.totalscore1);
        mweekscore=(TextView)v.findViewById(R.id.dailyscore1);
        ytotalscore=(TextView)v.findViewById(R.id.totalscore2);
        ymonthscore=(TextView)v.findViewById(R.id.dailyscore2);


        barChart_month = (BarChart)v.findViewById(R.id.barchartStep_month);
        barChart_year = (BarChart)v.findViewById(R.id.barchartStep_year);
        textView = (TextView)v.findViewById(R.id.testing_txt);
        txtWeek = (TextView)v.findViewById(R.id.testing_week_txt);
        btnLeft = v.findViewById(R.id.left_btn);

        txtYear = (TextView)v.findViewById(R.id.testing_year);
        btn = (Button) v.findViewById(R.id.testing_month);
        btnYear = (Button)v.findViewById(R.id.btn_year);
        btnRight = v.findViewById(R.id.right_btn);
        txtWeek1 = (TextView)v.findViewById(R.id.testing_week_txt1);
        btnLeft1 = v.findViewById(R.id.left_btn1);
        btnRight1 = v.findViewById(R.id.right_btn1);
        btnLeft2 = v.findViewById(R.id.left_btn2);
        btnRight2 = v.findViewById(R.id.right_btn2);
        txtWeek2=(TextView)v.findViewById(R.id.testing_week_txt2);
        dialog = new ProgressDialog(getContext(), R.style.MyAlertDialogStyle);
        dialog.setMessage("Syncing data....");
        dialog.show();
        btnRight.setVisibility(View.INVISIBLE);
        btnRight1.setVisibility(View.INVISIBLE);
        btnRight2.setVisibility(View.INVISIBLE);


      mStepsyesr=new ArrayList<>();
        HmSteps=new ArrayList<>();
        monthsteps=new ArrayList<>();
        HmTimestamp=new ArrayList<>();
        mStepsmonth= new ArrayList<>();

        yearlysteps=new ArrayList<>();
        monthArray=new ArrayList<>();
        mDate=new ArrayList<>();
        weekday= new ArrayList<>();

        try {
            readHistoryData("month",0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            readHistoryData("year",0);
        } catch (Exception e) {
            e.printStackTrace();
        }

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
        yeartotal=0;
        yearmonth=0;
        ///
        Date date = new Date();
        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
         month= localDate.getMonthValue();
         monthcount=month;
        txtWeek1.setText(monthArray.get(month-1));


        Calendar calendar = Calendar.getInstance();
        int in = calendar.get(Calendar.MONTH);
        DateFormat simple = new SimpleDateFormat(" MMM");
//        Date res = new Date(in);
        String MonthString = simple.format(calendar.getTime()).toString();
        textView.setText(MonthString);
        /*Calendar cyear = Calendar.getInstance();
        cyear.add(Calendar.YEAR, 1);
        year= cyear.get(Calendar.YEAR);*/


        String yearString = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
        year=Integer.parseInt(yearString);
        System.out.println("THIS IS CURRENT YEAR"+year);
        yearcount=year;
       // txtYear.setText(yearString);
        txtWeek2.setText(yearString);


//        week();

    btn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            demo();
        }
    });


    btnYear.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            year();
        }
    });

        //WeekWise Function Call
        weekbby();


    btnLeft.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            try {
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

    // Adding UI method
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void initUI()
    {


//    monthArray = {"Jan","Frb"}

        ArrayList<BarEntry> visitors  = new ArrayList<>();
        HmSteps= Paper.book().read("Weekly_Steps",null);
        dailyscores=0;
        totalscores=0;
        if(HmSteps!=null)
        {
            for(int i=0; i<HmSteps.size(); i++)
            {
               if((Integer.parseInt(HmSteps.get(i)))!=0) {
                   visitors.add(new BarEntry(i, Float.parseFloat(HmSteps.get(i))));
                   totalscores += Integer.parseInt(HmSteps.get(i));
               }
               else{
                   visitors.add(new BarEntry(i, null));
               }
            }
            dailyscores=totalscores/7;
        }
        if(dailyscores>0)
        {
            dailyscore.setText(String.valueOf(dailyscores));
        }
        if(totalscores>0)
        {
            totalscore.setText(String.valueOf(totalscores));
        }

       /* visitors.add(new BarEntry(0,420));
        visitors.add(new BarEntry(1,120));
        visitors.add(new BarEntry(2,850));
        visitors.add(new BarEntry(3,1050));
        visitors.add(new BarEntry(4,5620));
        visitors.add(new BarEntry(5,920));
        visitors.add(new BarEntry(6,1220));*/


        BarDataSet barDataSet = new BarDataSet(visitors,"By WEEK");
        barDataSet.setColors(Color.parseColor("#ea6749"));
        barDataSet.setValueTextColor(Color.BLACK);
        barDataSet.setValueTextSize(16f);


        BarData barData = new BarData(barDataSet);

        barChart.setFitBars(true);
        barChart.setData(barData);
        barChart.getDescription().setText("Bar Chart");
        barChart.animateY(2000);

        final ArrayList<String> xAxisLabel = new ArrayList<>();
        HmTimestamp= Paper.book().read("TimeStamp",null);
        if(visitors!=null)
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

        //  final DayOfWeek firstDayOfWeek = WeekFields.of(Locale.getDefault()).getFirstDayOfWeek();
        //  final DayOfWeek lastDayOfWeek = DayOfWeek.of(((firstDayOfWeek.getValue() + 5) % DayOfWeek.values().length) + 1);
        // Paper.book("1").read("startweekbyyear");
        //Month
        ArrayList<BarEntry> month  = new ArrayList<>();



        monthsteps=Paper.book().read("Month_data",null);
        int l=monthsteps.size()/7;
        int count=0;
        monthtotal=0;
        monthweekly=0;
        if(monthsteps!=null) {
            for (int i = 0; i < monthsteps.size(); i++) {
                monthtotal = monthtotal + (Integer.parseInt(monthsteps.get(i)));
                monthdata = monthdata + (Integer.parseInt(monthsteps.get(i)));
                if (i % 7 == 0 && count < l) {
                   if(monthdata!=0) {
                       month.add(new BarEntry(count, monthdata));
                       monthdata = 0;
                       count++;
                   }
                } else if (count >= l && i == monthsteps.size() - 1) {

                    if(monthdata!=0) {
                        month.add(new BarEntry(count, monthdata));
                        monthdata = 0;
                    }
                }


            }
        }
     try {
         monthweekly = monthtotal / count;
     }catch (Exception e){}
        mweekscore.setText(String.valueOf(monthweekly));
        mtotalscore.setText(String.valueOf(monthtotal));

        /*month.add(new BarEntry(0,420));
        month.add(new BarEntry(1,120));
        month.add(new BarEntry(2,850));
        month.add(new BarEntry(3,1050));
        month.add(new BarEntry(4,5620));
        month.add(new BarEntry(5,920));
        month.add(new BarEntry(6,1220));
        month.add(new BarEntry(7,220));
        month.add(new BarEntry(8,820));
        month.add(new BarEntry(9,690));
        month.add(new BarEntry(10,6220));
        month.add(new BarEntry(11,890));*/
        //  month.add(new BarEntry(12,8520));



        BarDataSet barDataSet_month = new BarDataSet(month,"MONTH/WEEK");
        barDataSet_month.setColors(Color.parseColor("#ea6749"));
        barDataSet_month.setValueTextColor(Color.BLACK);
        barDataSet_month.setValueTextSize(12f);

      /*  leftAxis.setAxisMaximum(20000);
        leftAxis.setAxisMinimum(0);*/
        //int maxCapacity = 1000;
        /*LimitLine ll = new LimitLine(maxCapacity, "Max Capacity");
        barChart_month.getAxisLeft().addLimitLine(ll);*/

        BarData barData_month = new BarData(barDataSet_month);

        barChart_month.setFitBars(true);
        barData_month.setBarWidth(0.5f);
        barChart_month.setData(barData_month);
        barChart_month.getDescription().setText("Bar Chart");
        barChart_month.animateY(2000);


        final ArrayList<String> xAxisLabel_month = new ArrayList<>();

       /* YearMonth yearMonthObject = YearMonth.of(2000,9);
        int daysInMonth = yearMonthObject.lengthOfMonth();
        int n=daysInMonth/5;
*/




         xAxisLabel_month.add("9" + "/" + "1");
        xAxisLabel_month.add("9" + "/" + "8");
        xAxisLabel_month.add("9" + "/" + "14");
        xAxisLabel_month.add("9" + "/" + "21");
        xAxisLabel_month.add("9" + "/" + "28");
        //   xAxisLabel_month.add("9" + "/" + "26");
        // xAxisLabel_month.add("9" + "/" + "31");
       /* }else if(daysInMonth==30)
        {
            xAxisLabel_month.add("9" + "/" + "1");
            xAxisLabel_month.add("9" + "/" + "8");
            xAxisLabel_month.add("9" + "/" + "14");
            xAxisLabel_month.add("9" + "/" + "21");
            xAxisLabel_month.add("9" + "/" + "28");
        //    xAxisLabel_month.add("9" + "/" + "26");
        }
        else if(daysInMonth==30)
        {
            xAxisLabel_month.add("9" + "/" + "1");
            xAxisLabel_month.add("9" + "/" + "8");
            xAxisLabel_month.add("9" + "/" + "14");
            xAxisLabel_month.add("9" + "/" + "21");
            xAxisLabel_month.add("9" + "/" + "28");
          //  xAxisLabel_month.add("9" + "/" + "26");
        }*/
        //   }
        /*xAxisLabel_month.add("Jul");
        xAxisLabel_month.add("Aug");
        xAxisLabel_month.add("Sept");
        xAxisLabel_month.add("Oct");
        xAxisLabel_month.add("Nov");
        xAxisLabel_month.add("Dec");
*/
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

        ArrayList<BarEntry> year  = new ArrayList<>();
        yearlysteps=Paper.book().read("Year_data",null);
        int monthcount=0;
       /*  for(int i=0; i<12; i++)
         {
//             YearMonth y = YearMonth.of(2020,i);
  //           monthsize.add(y.lengthOfMonth());
         }*/
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
        if(yearlysteps!=null) {
            for (int i = 0; i < yearlysteps.size(); i++)
            {
                //   int days = DateTime.DaysInMonth(2018,05);
                // int days = System.DateTime.DaysInMonth(year, month);

                yeartotal=yeartotal+(Integer.parseInt(yearlysteps.get(i)));
                yeardata=yeardata+(Integer.parseInt(yearlysteps.get(i)));
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
                else if(i==yearlysteps.size()-1 && i%30!=0 && i%31!=0){
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
        {}
        ytotalscore.setText(String.valueOf(yeartotal));
        ymonthscore.setText(String.valueOf(yearmonth));
        dialog.hide();
     /*   year.add(new BarEntry(0,420));
        year.add(new BarEntry(1,120));
        year.add(new BarEntry(2,850));
        year.add(new BarEntry(3,1050));
        year.add(new BarEntry(4,5620));
        year.add(new BarEntry(5,920));
        year.add(new BarEntry(6,1220));
        year.add(new BarEntry(7,220));
        year.add(new BarEntry(8,820));
        year.add(new BarEntry(9,690));
        year.add(new BarEntry(10,6220));*/


        BarDataSet barDataSet_year = new BarDataSet(year,"YEAR");
        barDataSet_year.setColors(Color.parseColor("#ea6749"));
        barDataSet_year.setValueTextColor(Color.BLACK);
        barDataSet_year.setValueTextSize(16f);
        BarData barData_year = new BarData(barDataSet_year);
        barChart_year.setFitBars(true);
        barData_year.setBarWidth(0.3f);
        barChart_year.setData(barData_year);
        barChart_year.getDescription().setText("Bar Chart");
        barChart_year.animateY(2000);

//        ValueFormatter xAxisFormatter = new DayAxisValueFormatter(barChart);

        XAxis xAxis_year = barChart_year.getXAxis();


        xAxis_year.setPosition(XAxis.XAxisPosition.BOTTOM);

        xAxis_year.setDrawGridLines(false);
        xAxis_year.setGranularity(1f); // only intervals of 1 day
        xAxis_year.setLabelCount(12);


//        xAxis.setValueFormatter(xAxisFormatter);
        barChart_year.getXAxis().setValueFormatter(new IndexAxisValueFormatter(xAxisLabel_year));
    }

    private void demo() {
        final Calendar today = Calendar.getInstance();

        MonthPickerDialog.Builder builder = new MonthPickerDialog.Builder(getContext(), new MonthPickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(int selectedMonth, int selectedYear) {

                Calendar c = Calendar.getInstance();
                c.set(Calendar.YEAR, selectedYear);
                c.set(Calendar.MONTH, selectedMonth);

                String currentMonth = monthArray.get(selectedMonth);
                String currentDateString = DateFormat.getDateInstance(DateFormat.MONTH_FIELD).format(c.getTime());


                System.out.println("current"+currentDateString);
                System.out.println("check" +currentMonth);

                Log.d(TAG, "selectedMonth : " + selectedMonth + " selectedYear : " + selectedYear);
                textView.setText(currentMonth);
                Toast.makeText(getContext(), "Date set with month" + selectedMonth + " year " + selectedYear, Toast.LENGTH_SHORT).show();
            }
        }, today.get(Calendar.YEAR), today.get(Calendar.MONTH));

        builder.setActivatedMonth(today.get(Calendar.MONTH))
                .setMinYear(2019)
                .setActivatedYear(2021)
                .setMaxYear(2022)
                .setMinMonth(Calendar.JANUARY)
                .setTitle("Select trading month")
                .setMonthRange(Calendar.JANUARY, Calendar.DECEMBER)
                // .setMaxMonth(Calendar.OCTOBER)
                // .setYearRange(1890, 1890)
                // .setMonthAndYearRange(Calendar.FEBRUARY, Calendar.OCTOBER, 1890, 1890)
                .showMonthOnly()
                // .showYearOnly()  Calendar calendar = Calendar.getInstance(); // this would default to now
                //calendar.add(Calendar.DAY_OF_MONTH, -5).
                //hi
                .setOnMonthChangedListener(new MonthPickerDialog.OnMonthChangedListener() {
                    @Override
                    public void onMonthChanged(int selectedMonth) {
                        Log.d(TAG, "Selected month : " + selectedMonth);
                         Toast.makeText(getContext(), " Selected month : " + selectedMonth, Toast.LENGTH_SHORT).show();
                        Formatter fmtt = new Formatter();
                        Calendar cal = Calendar.getInstance();
                        fmtt = new Formatter();
                        fmtt.format("%tb", cal);
                        String datee = fmtt + " " + selectedMonth + ", ";
                        System.out.println("Checking dated"+datee);
                        textView.setText(datee);
//
                    }
                })
                .setOnYearChangedListener(new MonthPickerDialog.OnYearChangedListener() {
                    @Override
                    public void onYearChanged(int selectedYear) {
                        Log.d(TAG, "Selected year : " + selectedYear);
                         Toast.makeText(getContext(), " Selected year : " + selectedYear, Toast.LENGTH_SHORT).show();
                    }
                })
                .build()
                .show();


    }

    private void year()
    {

        final Calendar today = Calendar.getInstance();

        MonthPickerDialog.Builder builder = new MonthPickerDialog.Builder(getContext(),
                new MonthPickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(int selectedMonth, int selectedYear) {
//                year.setText(Integer.toString(selectedYear));
//                choosenYear = selectedYear;

                Log.d(TAG, "selectedMonth : " + selectedMonth + " selectedYear : " + selectedYear);

                String year_selected = String.valueOf(selectedYear);
                txtYear.setText(year_selected);
                Toast.makeText(getContext(), "Date set with month" + selectedMonth + " year " + selectedYear, Toast.LENGTH_SHORT).show();
            }
        }, today.get(Calendar.YEAR), 0);

        builder.showYearOnly()
                .setYearRange(1990, 2050)
                .build()
                .show();
        }
    private void weekbby()
    {
     SimpleDateFormat df = new SimpleDateFormat(" dd.MMM.yyyy");
        Period weekPeriod = new Period().withWeeks(1);
        DateTime startDate = new DateTime(2021, 1, 1, 0, 0, 0, 0);
        System.out.println("Check startDate:" + startDate);
        while (startDate.getDayOfWeek() != DateTimeConstants.MONDAY) {
            startDate = startDate.plusDays(1);
        }
        DateTime endDate = new DateTime(2022, 1, 1, 0, 0, 0, 0);
        while (endDate.getDayOfWeek() != DateTimeConstants.SUNDAY) {
            endDate = endDate.plusDays(1);
        }
        Interval i = new Interval(startDate, weekPeriod);
        int index = 0;
        while (i.getStart().isBefore(endDate)) {
            long currentDateInMilli = new DateTime().getMillis();
            System.out.println("checking for the current" + i.getStart().getMillis());
            if (i.getStart().getMillis() <= currentDateInMilli && currentDateInMilli <= i.getEnd().getMillis()) {
                currentIndex = index;
                Paper.book().write("Current_Index",index);
//                System.out.println("startdate=: "+i.getStart().getMillis() +" current date :"
//                        + new DateTime(currentDateInMilli)  +" end date: "+i.getEnd().getMillis());
            }
            System.out.println("week : " + i.getStart().getWeekOfWeekyear()
                    + " start: " + df.format(i.getStart().toDate())
                    + " ending: " + df.format(i.getEnd().minusMillis(1).toDate()));
            arr.add(df.format(i.getStart().toDate())
                    + " -- " + df.format(i.getEnd().minusMillis(1).toDate()));
            Paper.book(df.format(i.getStart().toDate())).write("startWeekwisedata",i.getStart().getMillis());
            Paper.book(df.format(i.getEnd().toDate())).write("endWeekwisedata",i.getEnd().getMillis());
            startweekmil.add((i.getStart().getMillis()));
            endweekmil.add((i.getEnd().getMillis()));
            Paper.book(String.valueOf(i.getStart().getWeekOfWeekyear())).write("startweekbyyear",i.getStart().getMillis());
            Paper.book(String.valueOf(i.getStart().getWeekOfWeekyear())).write("endweekbyyear",i.getEnd().getMillis());

            i = new Interval(i.getStart().plus(weekPeriod), weekPeriod);

            index++;
        }
        Paper.book().write("startweekname",startweekmil);
        Paper.book().write("endweekname",endweekmil);
        Paper.book().write("Week_array",arr);
        System.out.println("Array List==" + arr.get(currentIndex));
        String dim = arr.get(currentIndex);
        System.out.println("Checkng dim Current index"+dim);
        txtWeek.setText(dim);
    }


///////////// Adding code to get history data
@RequiresApi(api = Build.VERSION_CODES.O)
public Task<DataReadResponse> readHistoryData(String type,int index) throws Exception,NullPointerException {
    // Begin by creating the query.
    String temp=type;
    DataReadRequest readRequest = queryFitnessData(type,index);

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
                            try{printData(dataReadResponse,type);}catch (Exception e){}
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
    public  DataReadRequest queryFitnessData(String h,int index) throws Exception {
        // [START build_read_data_request]
        // Setting a start and end date using a range of 1 week before this moment.
        long startTime=0l;
        long endTime=0l;
        Calendar cal = Calendar.getInstance();
        Date now = new Date();
        cal.setTime(now);
        cal.setTimeInMillis(0);
        cal.set(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DATE), 23, 59, 59);
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


            //startTime= datetomill(s);
            // startTime = cal.getTimeInMillis();
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


               Calendar calendarEnd = Calendar.getInstance();
               calendarEnd.set(Calendar.YEAR, year);
               calendarEnd.set(Calendar.MONTH, 11);
               calendarEnd.set(Calendar.DAY_OF_MONTH, 31);
               startTime = calendarStart.getTimeInMillis();
               endTime = cal.getTimeInMillis();


               // returning the last date
               Date endDate = calendarEnd.getTime();

               cal.add(Calendar.YEAR, -1);
           }
           else{
               endTime=endyearmil.get(index);
               startTime=startyearmil.get(index);

           }
            //  startTime = cal.getTimeInMillis();
        }


//        ArrayList<Long> dates = new ArrayList<>();
//        dates.add(startTime);

        java.text.DateFormat dateFormat = getDateInstance(DateFormat.FULL);

//        Log.i(TAG, "Range : " + dateFormat.format(dates));
//        Log.i(TAG, "Size : " + dates.size());
        Log.i(TAG, "Range Start: " + dateFormat.format(startTime));
        Log.i(TAG, "Range End: " + dateFormat.format(endTime));

        DataReadRequest readRequest =
                new DataReadRequest.Builder()
                        // The data request can specify multiple data types to return, effectively
                        // combining multiple data queries into one call.
                        // In this example, it's very unlikely that the request is for several hundred
                        // datapoints each consisting of a few steps and a timestamp.  The more likely
                        // scenario is wanting to see how many steps were walked per day, for 7 days.
                        .aggregate(DataType.TYPE_STEP_COUNT_DELTA, DataType.AGGREGATE_STEP_COUNT_DELTA)
                        // Analogous to a "Group By" in SQL, defines how data should be aggregated.
                        // bucketByTime allows for a time span, whereas bucketBySession would allow
                        // bucketing by "sessions", which would need to be defined in code.
                        .bucketByTime(1, TimeUnit.DAYS)
                        .setTimeRange(startTime, endTime, TimeUnit.MILLISECONDS)//1596220200000//1597084200000
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
    public  void printData(DataReadResponse dataReadResult, String type) {
        // [START parse_read_data_result]
        // If the DataReadRequest object specified aggregated data, dataReadResult will be returned
        // as buckets containing DataSets, instead of just DataSets.

        if (dataReadResult.getBuckets().size() > 0) {
          //  Log.i(TAG, "Number of returned buckets of DataSets is: " + dataReadResult.getBuckets().size());
            for (Bucket bucket : dataReadResult.getBuckets()) {
                System.out.println("BUCKET FULL "+dataReadResult);
                List<DataSet> dataSets = bucket.getDataSets();
                for (DataSet dataSet : dataSets) {
                    System.out.println("DATASET"+dataSet);
//                    Log.i(TAG, "Data point:: size:"+dataSets.get(0));
//                    Log.i(TAG, "Size:: " + dataReadResult.getBuckets().get(2));


                    if (dataSet.getDataPoints().isEmpty()) {

                        int checZero = 0;
                        long startTime = bucket.getEndTime(TimeUnit.MILLISECONDS);
                        long endTime = bucket.getEndTime(TimeUnit.MILLISECONDS);
                        DataSource dataSource = new DataSource.Builder()
                                .setAppPackageName(BuildConfig.APPLICATION_ID)
                                .setDataType(DataType.TYPE_STEP_COUNT_DELTA)
                                .setStreamName("move mins")
                                .setType(DataSource.TYPE_RAW)
                                .build();
                        dataSet = DataSet.create(dataSource);
                        // For each data point, specify a start time, end time, and the data value -- in this case,
                        // the number of new steps.
                        DataPoint dataPoint =
                                dataSet.createDataPoint()
                                        .setTimeInterval(startTime, endTime, TimeUnit.MILLISECONDS);
                        dataPoint.getValue(Field.FIELD_STEPS).setInt(checZero);
                        dataSet.add(dataPoint);


                    }
                    dumpDataSet(dataSet,type);

                }

            }
        } else if (dataReadResult.getDataSets().size() > 0) {
            Log.i(TAG, "Number of returned DataSets is: " + dataReadResult.getDataSets().size());
            for (DataSet dataSet : dataReadResult.getDataSets()) {
                dumpDataSet(dataSet,type);
            }
        }
        // [END parse_read_data_result]
    }

    // [START parse_dataset]
    @RequiresApi(api = Build.VERSION_CODES.O)
    private  void dumpDataSet(DataSet dataSet, String type) {
        Log.i(TAG, "Data returned for Data type: " + dataSet.getDataType().getName());
        DateFormat dateFormat = getTimeInstance();


        java.text.DateFormat dateFormat3 = getDateInstance(DateFormat.FULL);
        for (DataPoint dp : dataSet.getDataPoints()) {

          //  Log.i(TAG, "Data point:");
          //  Log.i(TAG, "\tType: " + dp.getDataType().getName());
            //Log.i(TAG, "\tStart: Date: "+"==" +dateFormat3.format(dp.getStartTime(TimeUnit.MILLISECONDS)));
//            mDate.add(dateFormat3.format(dp.getEndTime(TimeUnit.MILLISECONDS)));
            mDate.add(dayStringFormat(dp.getEndTime(TimeUnit.MILLISECONDS)));
          //  Log.i(TAG, "\tEnd: Date: "+"==" + dateFormat3.format(dp.getEndTime(TimeUnit.MILLISECONDS)));
            for (Field field : dp.getDataType().getFields()) {
           //     Log.i(TAG, "\tField: " + field.getName() + " Value: " + dp.getValue(field));
                 System.out.println("Checking steps ::"+dp.getValue(field));
            try {
                if (type.equalsIgnoreCase("week")) {
                    mSteps.add(dp.getValue(field).toString());
                    GregorianCalendar cal = new GregorianCalendar();

                    cal.setTime(new Date(dp.getStartTime(TimeUnit.MICROSECONDS)));

                    int dow = cal.get(Calendar.DAY_OF_WEEK);
                    System.out.println("Mill to Dyas="+dow);
                }
                else if (type.equalsIgnoreCase("month")) {
                    mStepsmonth.add(dp.getValue(field).toString());
                } else if (type.equalsIgnoreCase("year")) {
                    mStepsyesr.add(dp.getValue(field).toString());
                }
            }catch (Exception e)
            {

            }
            }
        }
        if(mSteps!=null) {
            Paper.book().write("Weekly_Steps", mSteps);
        }
        if(mStepsmonth!=null) {
            Paper.book().write("Month_data", mStepsmonth);
        }
        if(mStepsyesr!=null) {
            Paper.book().write("Year_data", mStepsyesr);
        }
        initUI();

    }

   @RequiresApi(api = Build.VERSION_CODES.O)
   public void  getNextMonth(int n) throws Exception {

        if(n<month) {
            btnRight1.setVisibility(View.VISIBLE);
        }
       txtWeek1.setText(monthArray.get(n));
       mStepsmonth=new ArrayList<>();
       Calendar calendar = Calendar.getInstance();
       calendar.add(Calendar.MONTH,  -(month-n));
       calendar.set(Calendar.DATE, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
       Long nextMonthFirstDay = calendar.getTimeInMillis();
       startmonthmil.put(n,nextMonthFirstDay);
       calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
       Long nextMonthLastDay = calendar.getTimeInMillis();
       endmonthmil.put(n,nextMonthLastDay);
      try {
       readHistoryData("month",n);
      }catch (Exception e){}
   }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private  void getPreviousWeek() throws Exception {
        btnRight.setVisibility(View.VISIBLE);
        currentIndex--;
        String dim = arr.get(currentIndex);
        txtWeek.setText(dim);
        System.out.println(arr.get(currentIndex));
        startweekmil.get(currentIndex);
        endweekmil.get(currentIndex);
        mSteps =new ArrayList<>();
        try {
        readHistoryData("week",currentIndex);
        }catch (Exception e){}
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    private  void getNextWeek() throws Exception {
          currentIndex++;
        String dim = arr.get(currentIndex);
        txtWeek.setText(dim);
        System.out.println(arr.get(currentIndex));
        mSteps =new ArrayList<>();
        Date date = new Date();
        long timeMilli = date.getTime();
        if(startweekmil.get(currentIndex)<=timeMilli) {
            btnRight.setVisibility(View.VISIBLE);
            try {
            readHistoryData("week", currentIndex);
            }catch (Exception e){}
        }else{
            btnRight.setVisibility(View.INVISIBLE);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void getPreviousMonth(int n) throws Exception {

        btnRight1.setVisibility(View.VISIBLE);
        txtWeek1.setText(monthArray.get(n-1));


        //System.out.println(arr.get(n));
       // startmonthmil.get(n);
       // endmonthmil.get(n);
        mStepsmonth=new ArrayList<>();



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
        try {
        readHistoryData("month",n);
        }catch (Exception e){}
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

        mStepsyesr=new ArrayList<>();
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
        try {
        readHistoryData("year",n);
        }catch(Exception e){
        }
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
        mStepsyesr=new ArrayList<>();

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
        try {
        readHistoryData("year",n);
        }catch (Exception e){}
    }



//// Adding API to get month and year data in fragment itself





}




