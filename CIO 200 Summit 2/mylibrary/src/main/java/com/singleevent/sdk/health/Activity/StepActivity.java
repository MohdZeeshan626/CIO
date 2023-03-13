package com.singleevent.sdk.health.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.github.lzyzsd.circleprogress.ArcProgress;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.data.Bucket;
import com.google.android.gms.fitness.data.DataSet;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.data.Field;
import com.google.android.gms.fitness.request.DataReadRequest;
import com.google.android.gms.fitness.result.DataReadResponse;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;
import com.singleevent.sdk.health.Activity.LineGraphActivity;
import com.singleevent.sdk.R;

import java.time.YearMonth;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import io.paperdb.Paper;

import static java.text.DateFormat.getDateInstance;
import static java.text.DateFormat.getTimeInstance;
public class StepActivity extends AppCompatActivity {

    private static String TAG = "Reading Step Count";
    EditText txt,txtCal, txtdistance, txtTime;;
    ArcProgress arcProgress;
    String totalSteps;
    private boolean titleboolean;
    private boolean jokeboolean;
    long total;
    double totaldist, km;
    TextView textView;
    CircularProgressBar circularProgressBar,circularProgressBar2,circularProgressBar3;
    float totalcal;
    Task<DataSet> result;
    int totalTime;
    long active;
    Button demo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step);
        txt = (EditText) findViewById(R.id.textView);
        textView = (TextView) findViewById(R.id.str_percent);
        txtCal = (EditText) findViewById(R.id.txt_cal);
        txtdistance = (EditText) findViewById(R.id.txt_distance);
        txtTime = (EditText) findViewById(R.id.txt_totaltime);
        Paper.init(this);

        txt.setEnabled(false);
        txt.setFocusable(false);
        txt.setCursorVisible(false);
        txt.setKeyListener(null);
        txt.setTextColor(Color.BLACK);
        txt.setBackgroundColor(Color.TRANSPARENT);

        txtCal.setEnabled(false);
        txtCal.setFocusable(false);
        txtCal.setCursorVisible(false);
        txtCal.setKeyListener(null);
        txtCal.setTextColor(Color.BLACK);
        txtCal.setBackgroundColor(Color.TRANSPARENT);

        txtdistance.setEnabled(false);
        txtdistance.setFocusable(false);
        txtdistance.setCursorVisible(false);
        txtdistance.setKeyListener(null);
        txtdistance.setTextColor(Color.BLACK);
        txtdistance.setBackgroundColor(Color.TRANSPARENT);

        txtTime.setEnabled(false);
        txtTime.setFocusable(false);
        txtTime.setCursorVisible(false);
        txtTime.setKeyListener(null);
        txtTime.setTextColor(Color.BLACK);
        txtTime.setBackgroundColor(Color.TRANSPARENT);

        circularProgressBar = findViewById(R.id.circularProgressBar);
        circularProgressBar2 = findViewById(R.id.circularProgressBar2);
        circularProgressBar3 = findViewById(R.id.circularProgressBar3);
        arcProgress = (ArcProgress) findViewById(R.id.arc_progress);
        arcProgress.setFinishedStrokeColor(Color.parseColor("#8d80f4"));
        arcProgress.setUnfinishedStrokeColor(Color.parseColor("#f5f6fa"));
        arcProgress.setTextColor(Color.parseColor("#000000"));

        readData();
        readCalData();
        readDistanceData();
        try {
            readHistoryData();
        }catch (Exception e)
        {

        }
//       readTotlMins();
        readTotalActiveMins();


        txt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                readData();
                setProgress();

            }
        });

        txtCal.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                readCalData();
                circularProgressBar.setProgress(totalcal);
                circularProgressBar.setProgressMax(2500);
            }
        });

        txtdistance.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                readDistanceData();

                float d = (float)Math.round(totaldist);
                circularProgressBar2.setProgress(d);
                circularProgressBar2.setProgressMax(50);

            }
        });

        txtTime.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }


            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                readTotalActiveMins();
//                readTotlMins();
                circularProgressBar3.setProgress(active);
                circularProgressBar3.setProgressMax(1440);
            }
        });

        demo = (Button)findViewById(R.id.demo);
        demo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(getApplicationContext(), LineGraphActivity.class);
                startActivity(in);
            }
        });
    }

    private void readTotalActiveMins() {


          try {
              Fitness.getHistoryClient(this, GoogleSignIn.getLastSignedInAccount(this))
                      .readDailyTotal(DataType.TYPE_MOVE_MINUTES)
                      .addOnSuccessListener(
                              new OnSuccessListener<DataSet>() {
                                  @Override
                                  public void onSuccess(DataSet dataSet) {
                                      active =
                                              dataSet.isEmpty()
                                                      ? 0
                                                      : dataSet.getDataPoints().get(0).getValue(Field.FIELD_DURATION).asInt();
                                      Log.i(TAG, "Total steps: " + active);
//                                    Toast.makeText(getApplicationContext(),"Total Steps"+total,Toast.LENGTH_SHORT).show();
//                                    String totalSteps = String.valueOf(total);
//                                    textView.setText(totalSteps);


                                      String in = String.valueOf(active);
                                      txtTime.setText(in + " mins");

                                  }
                              })
                      .addOnFailureListener(
                              new OnFailureListener() {
                                  @Override
                                  public void onFailure(@NonNull Exception e) {
                                      Log.w(TAG, "There was a problem getting the step count.", e);
                                  }
                              });
          }catch (Exception e)
          {

          }


    }


    private void readData() {

      try {
          Fitness.getHistoryClient(this, GoogleSignIn.getLastSignedInAccount(this))
                  .readDailyTotal(DataType.TYPE_STEP_COUNT_DELTA)
                  .addOnSuccessListener(
                          new OnSuccessListener<DataSet>() {
                              @Override
                              public void onSuccess(DataSet dataSet) {
                                  if (dataSet.isEmpty()) {
                                      Toast.makeText(getApplicationContext(), "There was a problem getting the step count.", Toast.LENGTH_SHORT).show();

                                  } else {
                                      total =
                                              dataSet.isEmpty()
                                                      ? 0
                                                      : dataSet.getDataPoints().get(0).getValue(Field.FIELD_STEPS).asInt();
//                                    Log.i(TAG, "Total steps: " + total);
//                                    Toast.makeText(getApplicationContext(), "Total Steps" + total, Toast.LENGTH_SHORT).show();
                                      totalSteps = String.valueOf(total);
                                      txt.setText(totalSteps);
                                      Paper.book().write("total_steps", totalSteps);
                                      Log.i(TAG, "totalStep" + total);


                                  }
                              }
                          })
                  .addOnFailureListener(
                          new OnFailureListener() {
                              @Override
                              public void onFailure(@NonNull Exception e) {
                                  Log.w(TAG, "There was a problem getting the step count.", e);
                              }
                          });
      }catch (Exception e)
      {

      }
    }

    private void readCalData() {
        try {
            Fitness.getHistoryClient(this, GoogleSignIn.getLastSignedInAccount(this))
                    .readDailyTotal(DataType.TYPE_CALORIES_EXPENDED)
                    .addOnSuccessListener(
                            new OnSuccessListener<DataSet>() {
                                @Override
                                public void onSuccess(DataSet dataSet) {
                                    totalcal =
                                            dataSet.isEmpty()
                                                    ? 0
                                                    : dataSet.getDataPoints().get(0).getValue(Field.FIELD_CALORIES).asFloat();
//                                Log.i(TAG, "Total Calories: " + totalcal);
//                                Toast.makeText(getApplicationContext(),"Total Calories"+total,Toast.LENGTH_SHORT).show();
                                    ;
                                    String totalCals = String.valueOf(Math.round(totalcal));
                                    txtCal.setText(totalCals + " kcal");
                                    Paper.book().write("total_cal", totalCals);
                                    Log.i(TAG, "totalCal" + totalCals);
                                }
                            })
                    .addOnFailureListener(
                            new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w(TAG, "There was a problem getting the step count.", e);
                                }
                            });
        }catch (Exception e)
        {

        }
    }

    private void readDistanceData() {
        try {
            result = Fitness.getHistoryClient(this, GoogleSignIn.getLastSignedInAccount(this))
                    .readDailyTotal((DataType.TYPE_DISTANCE_DELTA))
                    .addOnSuccessListener(
                            new OnSuccessListener<DataSet>() {
                                @Override
                                public void onSuccess(DataSet dataSet) {
                                    totaldist =
                                            dataSet.isEmpty()
                                                    ? 0
                                                    : dataSet.getDataPoints().get(0).getValue(Field.FIELD_DISTANCE).asFloat();
//                                Log.i(TAG, "Total Distance: " + totaldist);
//                                Toast.makeText(getApplicationContext(),"Total Calories"+total,Toast.LENGTH_SHORT).show();
                                    //converting meters to km
                                    km = totaldist / 1000;
                                    String str = String.format("%.4f", km);

                                    System.out.println("distance" + str);
                                    txtdistance.setText(str + " km");


                                }
                            })
                    .addOnFailureListener(
                            new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w(TAG, "There was a problem getting the step count.", e);
                                }
                            });
        }catch (Exception e)
        {

        }
    }

    private void readTotlMins ()
    {
        try {
            Fitness.getHistoryClient(this, GoogleSignIn.getLastSignedInAccount(this))
                    .readDailyTotal(DataType.AGGREGATE_STEP_COUNT_DELTA)
                    .addOnSuccessListener(
                            new OnSuccessListener<DataSet>() {
                                @Override
                                public void onSuccess(DataSet dataSet) {


                                    Calendar cal = Calendar.getInstance();
                                    Date now = new Date();
                                    cal.setTime(now);
                                    long endTime = cal.getTimeInMillis();
                                    cal.set(Calendar.HOUR_OF_DAY, 0);
                                    cal.set(Calendar.MINUTE, 0);
                                    cal.set(Calendar.SECOND, 0);

                                    long startTime = cal.getTimeInMillis();

                                    java.text.DateFormat dateFormat = getDateInstance();
                                    Log.i(TAG, "Range StartT: " + (startTime));
                                    Log.i(TAG, "Range EndT: " + (endTime));

                                    Log.i(TAG, "Range Start: " + dateFormat.format(startTime));
                                    Log.i(TAG, "Range End: " + dateFormat.format(endTime));

                                    System.out.println();
                                    long i = dataSet.isEmpty() ? 0 : dataSet.getDataPoints().get(0).getEndTime(TimeUnit.MINUTES);
                                    long s = dataSet.isEmpty() ? 0 : dataSet.getDataPoints().get(0).getStartTime(TimeUnit.MINUTES);
                                    active = i - s;

                                    System.out.println("starttime" + dataSet.getDataPoints().get(0).getStartTime(TimeUnit.MILLISECONDS));
                                    System.out.println("endtime" + dataSet.getDataPoints().get(0).getEndTime(TimeUnit.MILLISECONDS));
                                    System.out.println("checking active " + active);
                                    double totalTim = active / 60000;
                                    Math.round(totalTim);
                                    System.out.println("Total Time " + totalTim);
                                    String in = String.valueOf(active);
                                    txtTime.setText(in + " mins");


                                }
                            })
                    .addOnFailureListener(
                            new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w(TAG, "There was a problem getting the step count.", e);
                                }
                            });
        }catch (Exception e)
        {

        }
    }



    private Task<DataReadResponse> readHistoryData() {

            // Begin by creating the query.
            DataReadRequest readRequest = queryFitnessData();

            System.out.println("query Ftness Data");
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
                                    System.out.println("Checking dataResult" + dataReadResponse);

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


    /**
     * Returns a {@link DataReadRequest} for all step count changes in the past week.
     */
    public DataReadRequest queryFitnessData() {
//        DataReadResponse dataReadResult;
        // [START build_read_data_request]
        // Setting a start and end date using a range of 1 week before this moment.
            Calendar cal = Calendar.getInstance();
            Date now = new Date();
            cal.setTime(now);
            long endTime = cal.getTimeInMillis();
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
           cal.add(Calendar.MONTH, -1);
       int mon= Calendar.getInstance().get(Calendar.MONTH);
        //YearMonth yearMonthObject = YearMonth.of(2000,2);
       // int daysInMonth = yearMonthObject.lengthOfMonth();
        long startTime = cal.getTimeInMillis();

            java.text.DateFormat dateFormat = getDateInstance();
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

            System.out.println("Value of readRequest" + readRequest);


            return readRequest;

    }


    public static void printData(DataReadResponse dataReadResult) {
        // [START parse_read_data_result]
        // If the DataReadRequest object specified aggregated data, dataReadResult will be returned
        // as buckets containing DataSets, instead of just DataSets.
//        if (dataReadResult.getBuckets().size() > 0) {
//            Log.i(
//                    TAG, "Number of returned buckets of DataSets is: " + dataReadResult.getBuckets().size());
System.out.println("Invoke PRintData");

            if (dataReadResult.getBuckets().size() > 0) {
                Log.i(TAG, "Number of returned buckets of DataSets is =========: "
                        + dataReadResult.getBuckets().size());
                System.out.println("Number of returned buckets of DataSers is " + dataReadResult.getBuckets().size());


                for (Bucket bucket : dataReadResult.getBuckets()) {
                    Log.i(TAG, bucket.getActivity());
                    long activeTime = bucket.getEndTime(TimeUnit.MINUTES) - bucket.getStartTime(TimeUnit.MINUTES);
                    Log.i(TAG, "Active time ========" + activeTime);
                }

        }

    }




    public void setProgress() {

        double percentageres;
        double e = 100;
        double max = 20000;

        percentageres = (total/max)*e;
        System.out.println("percentage"+percentageres);
        String str = String.format("%.6f", percentageres);
        System.out.println("total"+str);
        int res = (int)percentageres;
        int inum=(int)max;

        textView.setText("You have walked "+res+"% of your goal");
        arcProgress.setProgress(res);
        arcProgress.setMax(20000);







    }
}
