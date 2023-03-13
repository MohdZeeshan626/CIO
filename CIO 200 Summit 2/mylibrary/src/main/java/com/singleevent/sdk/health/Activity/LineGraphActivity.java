package com.singleevent.sdk.health.Activity;


import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;


import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
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


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.multidex.BuildConfig;

import static java.text.DateFormat.getDateInstance;
import static java.text.DateFormat.getTimeInstance;

public class LineGraphActivity extends AppCompatActivity {


    private static final int MAX_X_VALUE = 7;
    private static final int MAX_Y_VALUE = 50;
    private static final int MIN_Y_VALUE = 5;
    private static final String SET_LABEL = "App Downloads";
    private static final String[] DAYS = { "SUN", "MON", "TUE", "WED", "THU", "FRI", "SAT" };
    private static final String TAG = "BarChart" ;
    private BarChart chart;
    // ArrayList<BarEntry> dataVal = new ArrayList<>();
    ArrayList<BarEntry> visitors  = new ArrayList<>();
    int currentIndex = 0;
    DataPoint dp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line_graph);
        chart = findViewById(R.id.barchart_chart);

//        BarDataSet barDataSet = new BarDataSet(dataVal,"");
//        BarData barData = new BarData();
//        barData.addDataSet(barDataSet);
//
//        chart.setData(barData);
//        chart.invalidate();

        readHistoryData();


//        demo();
//        BarData data = createChartData();
//        configureChartAppearance();
//        prepareChartData(data);

    }
    private void configureChartAppearance() {
        chart.getDescription().setEnabled(false);
        chart.setDrawValueAboveBar(false);

        XAxis xAxis = chart.getXAxis();
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return DAYS[(int) value];
            }
        });

        YAxis axisLeft = chart.getAxisLeft();
        axisLeft.setGranularity(10f);
        axisLeft.setAxisMinimum(0);

        YAxis axisRight = chart.getAxisRight();
        axisRight.setGranularity(10f);
        axisRight.setAxisMinimum(0);
    }

    private BarData createChartData() {
        ArrayList<BarEntry> values = new ArrayList<>();
        for (int i = 0; i < MAX_X_VALUE; i++) {
            float x = i;
            float y = new Util().randomFloatBetween(MIN_Y_VALUE, MAX_Y_VALUE);
            values.add(new BarEntry(x, y));
        }

        BarDataSet set1 = new BarDataSet(values, SET_LABEL);

        ArrayList<IBarDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1);

        BarData data = new BarData(dataSets);

        return data;
    }
    private void prepareChartData(BarData data) {
        data.setValueTextSize(12f);
        chart.setData(data);
        chart.invalidate();
    }


    private Task<DataReadResponse> readHistoryData() {
        // Begin by creating the query.
        DataReadRequest readRequest = queryFitnessData();

        // Invoke the History API to fetch the data with the query
        return Fitness.getHistoryClient(this, GoogleSignIn.getLastSignedInAccount(this))
                .readData(readRequest)
                .addOnSuccessListener(
                        new OnSuccessListener<DataReadResponse>() {
                            @Override
                            public void onSuccess(DataReadResponse dataReadResponse) {
                                // For the sake of the sample, we'll print the data so we can see what we just
                                // added. In general, logging fitness information should be avoided for privacy
                                // reasons.
                                printData(dataReadResponse);
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

    public  DataReadRequest queryFitnessData() {
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
                        .setTimeRange(startTime, endTime, TimeUnit.MILLISECONDS)
                        .build();
        // [END build_read_data_request]

        return readRequest;
    }



    public  void printData(DataReadResponse dataReadResult) {
        int index = 0;
        // [START parse_read_data_result]
        // If the DataReadRequest object specified aggregated data, dataReadResult will be returned
        // as buckets containing DataSets, instead of just DataSets.

        if (dataReadResult.getBuckets().size() > 0) {
            Log.i(TAG, "Number of returned buckets of DataSets is: " + dataReadResult.getBuckets().size());

//            for (Bucket bucket : dataReadResult.getBuckets()) {

            for (index = 0; index < dataReadResult.getBuckets().size(); index++) {
                Bucket bucket=dataReadResult.getBuckets().get(index);
//                Bucket bucket = (Bucket) dataReadResult.getBuckets();
                List<DataSet> dataSets = bucket.getDataSets();

                for (DataSet dataSet : dataSets) {


                    Log.i(TAG, "BucketS" + dataReadResult.getBuckets());

//bucket ->Datasets ->start,endtime,->datapoint -> main value stored //active mins,total steps,total, fieldvalue
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


                    dumpDataSet(dataSet, index);


                }
            }


        } else if (dataReadResult.getDataSets().size() > 0) {
            Log.i(TAG, "Number of returned DataSets is: " + dataReadResult.getDataSets().size());
            for (DataSet dataSet : dataReadResult.getDataSets()) {
                dumpDataSet(dataSet, index);
            }
        }

        // [END parse_read_data_result]
    }

    // [START parse_dataset]
    private  void dumpDataSet(DataSet dataSet,int i) {
        Log.i(TAG, "Data returned for Data type: " + dataSet.getDataType().getName());
        DateFormat dateFormat = getTimeInstance();


        java.text.DateFormat dateFormat3 = getDateInstance(DateFormat.FULL);
//        java.text.DateFormat dateFormat4 = getDateInstance(DateFormat.DAY_OF_WEEK_FIELD);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat ("EEE dd");


//        for (DataPoint dp : dataSet.getDataPoints()) {
        for(int h=0; h<dataSet.getDataPoints().size(); h++) {
            dp = dataSet.getDataPoints().get(h);
        }
        Log.i(TAG, "Data point:");
        Log.i(TAG, "Data point:::"+dp);

        Log.i(TAG, "\tType: " + dp.getDataType().getName());
        Log.i(TAG, "\tStart: Date: " + "==" + dateFormat3.format(dp.getStartTime(TimeUnit.MILLISECONDS)));
        Log.i(TAG, "\tEnd: Date: " + "==" + dateFormat3.format(dp.getEndTime(TimeUnit.MILLISECONDS)));
        for (Field field : dp.getDataType().getFields()) {


            Log.i(TAG, "\tField: " + field.getName() + " Value: " + dp.getValue(field));
            long totalsteps = dp.getValue(field).asInt();



            for (i=0;i<dataSet.getDataPoints().size();i++)
            {
                if(currentIndex==6)
                {
                    currentIndex = 0;
                }
                visitors.add(new BarEntry(currentIndex,dataSet.getDataPoints().get(0).getValue(field).asInt()));
                currentIndex = currentIndex+1;


            }
//                visitors.add(new BarEntry(0,dp.getValue(field).asInt()));
//                visitors.add(new BarEntry(1,120));
//                visitors.add(new BarEntry(2,850));
//                visitors.add(new BarEntry(3,1050));
//                visitors.add(new BarEntry(4,5620));
//                visitors.add(new BarEntry(5,920));
//                visitors.add(new BarEntry(6,1220));


            BarDataSet barDataSet = new BarDataSet(visitors,"By WEEK");
            barDataSet.setColors(Color.parseColor("#ea6749"));
            barDataSet.setValueTextColor(Color.BLACK);
            barDataSet.setValueTextSize(12f);


            BarData barData = new BarData(barDataSet);

            chart.setFitBars(true);
            chart.setData(barData);
            chart.getDescription().setText("Bar Chart");
            chart.animateY(2000);

            final ArrayList<String> xAxisLabel = new ArrayList<>();



            DateFormat simple_endDate = new SimpleDateFormat("M/dd ");
            Date res_endDate = new Date(dataSet.getDataPoints().get(0).getEndTime(TimeUnit.MILLISECONDS));


            xAxisLabel.add(simple_endDate.format(res_endDate).toString());
            xAxisLabel.add(simple_endDate.format(res_endDate).toString());
            xAxisLabel.add(simple_endDate.format(res_endDate).toString());
            xAxisLabel.add(simple_endDate.format(res_endDate).toString());
            xAxisLabel.add(simple_endDate.format(res_endDate).toString());
            xAxisLabel.add(simple_endDate.format(res_endDate).toString());
            xAxisLabel.add(simple_endDate.format(res_endDate).toString());



//                xAxisLabel.add("Mon");
//                xAxisLabel.add("Tue");
//                xAxisLabel.add("Wed");
//                xAxisLabel.add("Thu");
//                xAxisLabel.add("Fri");
//                xAxisLabel.add("Sat");
//                xAxisLabel.add("Sun");

//        ValueFormatter xAxisFormatter = new DayAxisValueFormatter(chart);

            XAxis xAxis = chart.getXAxis();


            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

            xAxis.setDrawGridLines(false);
            xAxis.setGranularity(1f); // only intervals of 1 day
            xAxis.setLabelCount(7);
//        xAxis.setValueFormatter(xAxisFormatter);
            chart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(xAxisLabel));





//            }
        }
//Bucket->dataset->dataPoint
    }


    private void demo()
    {
//        ArrayList<BarEntry> visitors  = new ArrayList<>();
        visitors.add(new BarEntry(0,420));
        visitors.add(new BarEntry(1,120));
        visitors.add(new BarEntry(2,850));
        visitors.add(new BarEntry(3,1050));
        visitors.add(new BarEntry(4,5620));
        visitors.add(new BarEntry(5,920));
        visitors.add(new BarEntry(6,1220));


        BarDataSet barDataSet = new BarDataSet(visitors,"By WEEK");
        barDataSet.setColors(Color.parseColor("#ea6749"));
        barDataSet.setValueTextColor(Color.BLACK);
        barDataSet.setValueTextSize(12f);


        BarData barData = new BarData(barDataSet);

        chart.setFitBars(true);
        chart.setData(barData);
        chart.getDescription().setText("Bar Chart");
        chart.animateY(2000);

        final ArrayList<String> xAxisLabel = new ArrayList<>();
        xAxisLabel.add("Mon");
        xAxisLabel.add("Tue");
        xAxisLabel.add("Wed");
        xAxisLabel.add("Thu");
        xAxisLabel.add("Fri");
        xAxisLabel.add("Sat");
        xAxisLabel.add("Sun");

//        ValueFormatter xAxisFormatter = new DayAxisValueFormatter(chart);

        XAxis xAxis = chart.getXAxis();


        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f); // only intervals of 1 day
        xAxis.setLabelCount(7);
//        xAxis.setValueFormatter(xAxisFormatter);
        chart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(xAxisLabel));
    }

//    private ArrayList<BarEntry> dataVals(){
//        ArrayList<BarEntry> dataVal = new ArrayList<>();
//        dataVal.add(new BarEntry(0,3));
//        dataVal.add(new BarEntry(1,4));
//        dataVal.add(new BarEntry(15,63));
//        dataVal.add(new BarEntry(50,20));
//
//
//        return dataVal;
//    }
}