package com.singleevent.sdk.health.Fragment;


import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import com.singleevent.sdk.health.Adapter.HorizontalRecyclerViewAdapter;
import com.singleevent.sdk.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.multidex.BuildConfig;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.paperdb.Paper;
import ru.tinkoff.scrollingpagerindicator.ScrollingPagerIndicator;

import static java.text.DateFormat.getDateInstance;
import static java.text.DateFormat.getTimeInstance;

public class ActivityFragment extends Fragment {

    private static String TAG = "Steps History";
    private static String TAG1 = "Active History";

    ScrollingPagerIndicator recyclerIndicator;
        RecyclerView recyclerView;

    //vars
    private  ArrayList<String> mDate = new ArrayList<>();
    private  ArrayList<String> mStepsweek = new ArrayList<>();
    private  ArrayList<String> mStepsmonth = new ArrayList<>();
    private  ArrayList<String> mStepsyesr = new ArrayList<>();
    private  ArrayList<String> mMiles = new ArrayList<>();
    private  ArrayList<String> mMins = new ArrayList<>();

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.fragment_activity, container, false);
        recyclerView =v. findViewById(R.id.recyclerview_activity);
         recyclerIndicator = v.findViewById(R.id.indicator);


       try {
           initData();
       }catch (Exception e)
       {

       }

        return v;


    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void initData() throws ParseException {
     //   readHistoryData("week");
       // readHistoryData("month");

        //readHistoryData("year");
        //steps jul 10,-150
        readDistanceHistoryData(); //100 mts
        readActiveHistoryData(); // 0
//        mSteps.add("3,450");
//        mMiles.add("0.2");
//        mMins.add("2");

//        mSteps.add("5,450");
//        mMiles.add("0");
//        mMins.add("10");

//        mSteps.add("8,450");
//        mMiles.add("0.6");
//        mMins.add("16");

//        mSteps.add("8,000");
//        mMiles.add("0.1");
//        mMins.add("2");

//        mSteps.add("10,450");
//        mMiles.add("0.1");
//        mMins.add("2");

//        mMiles.add("0.1");
//        mMins.add("1");

//        mMiles.add("0.1");
//        mMins.add("2");
//
//        mMiles.add("20");
//        mMins.add("65");

//        initHorizontal();

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public Task<DataReadResponse> readHistoryData(String type) throws ParseException {
        // Begin by creating the query.
        String temp=type;
        DataReadRequest readRequest = queryFitnessData(type);

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
                                printData(dataReadResponse,type);
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
    public  DataReadRequest queryFitnessData(String h) throws ParseException {
        // [START build_read_data_request]
        // Setting a start and end date using a range of 1 week before this moment.
        long startTime=0l;
        Calendar cal = Calendar.getInstance();
        Date now = new Date();
        cal.setTime(now);
        cal.setTimeInMillis(0);
        cal.set(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DATE), 23, 59, 59);
        cal.getTime();
        long endTime = cal.getTimeInMillis();
        cal.set(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DATE));
//        Log.i(TAG,"EndDate"+cal.getTime());
//        cal.getTime();
        LocalDate s= LocalDate.now().with ( ChronoField.DAY_OF_MONTH , 1 );


        if (h.equalsIgnoreCase("week")) {
            cal.add(Calendar.WEEK_OF_YEAR, -1);
            startTime = cal.getTimeInMillis();

            //startTime = cal.getTimeInMillis();
        }//1596479400000l
        else if (h.equalsIgnoreCase("month")) {
            cal.add(Calendar.MONTH, -1);
            startTime= datetomill(s);
            // startTime = cal.getTimeInMillis();
        } else if (h.equalsIgnoreCase("year")){

            Date d = new Date();
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

            cal.add(Calendar.YEAR, -1);
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

    public  Long datetomill(LocalDate s) throws ParseException {
        String myDate = s.toString();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = sdf.parse(myDate);
        long start = date.getTime();
        return start;
    }

    public  void printData(DataReadResponse dataReadResult,String type) {
        // [START parse_read_data_result]
        // If the DataReadRequest object specified aggregated data, dataReadResult will be returned
        // as buckets containing DataSets, instead of just DataSets.

        if (dataReadResult.getBuckets().size() > 0) {
            Log.i(TAG, "Number of returned buckets of DataSets is: " + dataReadResult.getBuckets().size());
            for (Bucket bucket : dataReadResult.getBuckets()) {
                List<DataSet> dataSets = bucket.getDataSets();
                for (DataSet dataSet : dataSets) {
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
    private  void dumpDataSet(DataSet dataSet,String type) {
        Log.i(TAG, "Data returned for Data type: " + dataSet.getDataType().getName());
        DateFormat dateFormat = getTimeInstance();

        java.text.DateFormat dateFormat3 = getDateInstance(DateFormat.FULL);
        for (DataPoint dp : dataSet.getDataPoints()) {

            Log.i(TAG, "Data point:");
            Log.i(TAG, "\tType: " + dp.getDataType().getName());
            Log.i(TAG, "\tStart: Date: "+"==" +dateFormat3.format(dp.getStartTime(TimeUnit.MILLISECONDS)));
//            mDate.add(dateFormat3.format(dp.getEndTime(TimeUnit.MILLISECONDS)));
            mDate.add(dayStringFormat(dp.getEndTime(TimeUnit.MILLISECONDS)));
            Log.i(TAG, "\tEnd: Date: "+"==" + dateFormat3.format(dp.getEndTime(TimeUnit.MILLISECONDS)));
            for (Field field : dp.getDataType().getFields()) {
                Log.i(TAG, "\tField: " + field.getName() + " Value: " + dp.getValue(field));
                System.out.println("Checking size mSteps ::"+mMiles.size());
                if(type.equalsIgnoreCase("week")) {
                    mStepsweek.add(dp.getValue(field).toString());
                }
               /* else if(type.equalsIgnoreCase("month"))
                {
                    mStepsmonth.add(dp.getValue(field).toString());
                }
                else if(type.equalsIgnoreCase("year"))
                {
                    mStepsyesr.add(dp.getValue(field).toString());
                }*/
            }
        }
        Paper.book().write("Week_steps",mStepsweek);
       // Paper.book().write("Year_data",mStepsyesr);
        initHorizontal();
    }

    ///Adding to get day
    public static String dayStringFormat(long msecs) {
        GregorianCalendar cal = new GregorianCalendar();

        cal.setTime(new Date(msecs));

        int dow = cal.get(Calendar.DAY_OF_WEEK);

        switch (dow) {
            case Calendar.MONDAY:
                return "Mon";
            case Calendar.TUESDAY:
                return "Tue";
            case Calendar.WEDNESDAY:
                return "Wed";
            case Calendar.THURSDAY:
                return "Thu";
            case Calendar.FRIDAY:
                return "Fri";
            case Calendar.SATURDAY:
                return "Sat";
            case Calendar.SUNDAY:
                return "Sun";
        }

        return "Unknown";
    }

    //Distance
    private Task<DataReadResponse> readDistanceHistoryData() {
        // Begin by creating the query.
        DataReadRequest readRequest = DistancequeryFitnessData();

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
                                DistanceprintData(dataReadResponse);
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


    public  DataReadRequest DistancequeryFitnessData() {
        // [START build_read_data_request]
        // Setting a start and end date using a range of 1 week before this moment.
        Calendar cal = Calendar.getInstance();
        Date now = new Date();
        cal.setTime(now);
        cal.setTimeInMillis(0);
        cal.set(Calendar.getInstance().get(Calendar.YEAR),Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DATE),23,59,59);
        cal.getTime();

        long endTime = cal.getTimeInMillis();
        cal.set(Calendar.getInstance().get(Calendar.YEAR),Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DATE));
//        Log.i(TAG,"EndDate"+cal.getTime());
//        cal.getTime();

        cal.add(Calendar.WEEK_OF_YEAR, -1);
        long startTime = cal.getTimeInMillis();



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
                        .aggregate(DataType.TYPE_DISTANCE_DELTA, DataType.AGGREGATE_DISTANCE_DELTA)
                        // Analogous to a "Group By" in SQL, defines how data should be aggregated.
                        // bucketByTime allows for a time span, whereas bucketBySession would allow
                        // bucketing by "sessions", which would need to be defined in code.
                        .bucketByTime(1, TimeUnit.DAYS)
                        .setTimeRange(startTime, endTime, TimeUnit.MILLISECONDS)
                        .build();
        // [END build_read_data_request]

        return readRequest;
    }


    public  void DistanceprintData(DataReadResponse dataReadResult) {
        // [START parse_read_data_result]
        // If the DataReadRequest object specified aggregated data, dataReadResult will be returned
        // as buckets containing DataSets, instead of just DataSets.
        if (dataReadResult.getBuckets().size() > 0) {
            Log.i(
                    TAG, "Number of returned buckets of DataSets is: " + dataReadResult.getBuckets().size());
            for (Bucket bucket : dataReadResult.getBuckets()) {
                List<DataSet> dataSets = bucket.getDataSets();
                for (DataSet dataSet : dataSets) {
                    if (dataSet.getDataPoints().isEmpty()) {

                        float checZero = 0;
                        long startTime = bucket.getEndTime(TimeUnit.MILLISECONDS);
                        long endTime = bucket.getEndTime(TimeUnit.MILLISECONDS);
                        DataSource dataSource = new DataSource.Builder()
                                .setAppPackageName(BuildConfig.APPLICATION_ID)
                                .setDataType(DataType.TYPE_DISTANCE_DELTA)
                                .setStreamName("move mins")
                                .setType(DataSource.TYPE_RAW)
                                .build();
                        dataSet = DataSet.create(dataSource);
                        // For each data point, specify a start time, end time, and the data value -- in this case,
                        // the number of new steps.
                        DataPoint dataPoint =
                                dataSet.createDataPoint()
                                        .setTimeInterval(startTime, endTime, TimeUnit.MILLISECONDS);
                        dataPoint.getValue(Field.FIELD_DISTANCE).setFloat(checZero);
                        dataSet.add(dataPoint);


                    }
                    DistancedumpDataSet(dataSet);
                }
            }

        } else if (dataReadResult.getDataSets().size() > 0) {
            Log.i(TAG, "Number of returned DataSets is: " + dataReadResult.getDataSets().size());
            for (DataSet dataSet : dataReadResult.getDataSets()) {
                DistancedumpDataSet(dataSet);
            }
        }
        // [END parse_read_data_result]
    }

    // [START parse_dataset]
    private  void DistancedumpDataSet(DataSet dataSet) {
        Log.i(TAG, "Data returned for Data type: " + dataSet.getDataType().getName());
        DateFormat dateFormat = getTimeInstance();

        java.text.DateFormat dateFormat3 = getDateInstance(DateFormat.FULL);
        for (DataPoint dp : dataSet.getDataPoints()) {

            Log.i(TAG, "Data point:");
            Log.i(TAG, "\tType: " + dp.getDataType().getName());
            Log.i(TAG, "\tStart: Date: "+"==" +dateFormat3.format(dp.getStartTime(TimeUnit.MILLISECONDS)));
            Log.i(TAG, "\tEnd: Date: "+"==" + dateFormat3.format(dp.getEndTime(TimeUnit.MILLISECONDS)));
            for (Field field : dp.getDataType().getFields()) {
                double km ;
                km= dp.getValue(field).asFloat();
                double m = km/1000;
                String str = String.format("%.4f", m);
                Log.i(TAG, "\tField: " + field.getName() + " Value: " + str);
               System.out.println("Checking size mMiles ::"+mMiles.size());
                mMiles.add(str);
            }
            if(mMiles.size()>0)
            {
                Paper.book().write("Weekly_Miled",mMiles);
            }
        }
//        initHorizontal();
    }


    //ActiveMins

    private Task<DataReadResponse> readActiveHistoryData() {
        // Begin by creating the query.
        DataReadRequest readRequest = ActivequeryFitnessData();

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
                                ActiveprintData(dataReadResponse);
                            }
                        })
                .addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.e(TAG1, "There was a problem reading the data.", e);
                            }
                        });
    }

    public  DataReadRequest ActivequeryFitnessData() {
        // [START build_read_data_request]
        // Setting a start and end date using a range of 1 week before this moment.
        Calendar cal = Calendar.getInstance();
        Date now = new Date();
        cal.setTime(now);
        cal.setTimeInMillis(0);
        cal.set(Calendar.getInstance().get(Calendar.YEAR),Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DATE),23,59,59);
        cal.getTime();

        long endTime = cal.getTimeInMillis();
        cal.set(Calendar.getInstance().get(Calendar.YEAR),Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DATE));
//        Log.i(TAG,"EndDate"+cal.getTime());
//        cal.getTime();

        cal.add(Calendar.WEEK_OF_YEAR, -1);
        long startTime = cal.getTimeInMillis();



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

    public  void ActiveprintData(DataReadResponse dataReadResult) {
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

                    ActivedumpDataSet(dataSet);

                }
            }






        } else if (dataReadResult.getDataSets().size() > 0) {

            for (DataSet dataSet : dataReadResult.getDataSets()) {
                ActivedumpDataSet(dataSet);
            }
        }
        // [END parse_read_data_result]
    }

    // [START parse_dataset]
    private  void ActivedumpDataSet(DataSet dataSet) {
        Log.i(TAG1, "Data returned for Data type: " + dataSet.getDataType().getName());
        DateFormat dateFormat = getTimeInstance();

        java.text.DateFormat dateFormat3 = getDateInstance(DateFormat.FULL);
        for (DataPoint dp : dataSet.getDataPoints()) {

            Log.i(TAG1, "Data point:");
            Log.i(TAG1, "\tType: " + dp.getDataType().getName());
            Log.i(TAG1, "\tStart: Date: "+"==" +dateFormat3.format(dp.getStartTime(TimeUnit.MILLISECONDS)));
            Log.i(TAG1, "\tEnd: Date: "+"==" + dateFormat3.format(dp.getEndTime(TimeUnit.MILLISECONDS)));
         //   mDate.add(dateFormat3.format(dp.getEndTime(TimeUnit.MILLISECONDS)));
           mDate.add(dayStringFormat(dp.getEndTime(TimeUnit.MILLISECONDS)));
            for (Field field : dp.getDataType().getFields()) {
                Log.i(TAG1, "\tField: " + field.getName() + " Value: " + dp.getValue(field));

                System.out.println("Checking size mMins ::"+mMins.size());
                mMins.add(dp.getValue(field).toString());
            }
            Paper.book().write("Active_minutes",mMins);
        }
//        initHorizontal();
    }



    private  void initHorizontal() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);

        recyclerView.setLayoutManager(layoutManager);
        HorizontalRecyclerViewAdapter adapter = new HorizontalRecyclerViewAdapter( mDate,mStepsweek, mMiles,mMins,getContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setHasFixedSize(true);
        adapter.notifyDataSetChanged();
        recyclerIndicator.attachToRecyclerView(recyclerView);




//        final int radius = getResources().getDimensionPixelSize(R.dimen.radius);
//        final int dotsHeight = getResources().getDimensionPixelSize(R.dimen.dots_height);
//        final int color = ContextCompat.getColor(getContext(), R.color.black);
//        recyclerView.addItemDecoration(new DotsIndicatorDecoration(radius, radius * 4, dotsHeight, color, color));
//        new PagerSnapHelper().attachToRecyclerView(recyclerView);



    }



}