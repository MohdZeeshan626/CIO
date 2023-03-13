package com.singleevent.sdk.health.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import io.paperdb.Paper;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.data.DataSet;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.data.Field;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.singleevent.sdk.health.Activity.ChallengeActivity;
import com.singleevent.sdk.health.Activity.LeaderBoardActivity;
import com.singleevent.sdk.R;

public class HomeActivity extends AppCompatActivity {

    FloatingActionButton floatingActionButton;
    LinearLayout linearStep;
    TextView txtSteps,txtCalores,txtWt;
    String Wt;
    private String TAG = "Home Activity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
       // floatingActionButton = (FloatingActionButton)findViewById(R.id.addFab);
        linearStep = (LinearLayout)findViewById(R.id.linear_steps);
        txtSteps = (TextView)findViewById(R.id.txt_steps_taken);
        txtWt = (TextView)findViewById(R.id.txt_weight);
        txtCalores = (TextView)findViewById(R.id.txt_Calories);



        /*floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Features coming soon",Toast.LENGTH_SHORT).show();
            }
        });*/
        Paper.init(this);



        linearStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), StepActivity.class);
                startActivity(i);

            }
        });



        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

//        if (savedInstanceState == null) {
//            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
//                    new ActivityFragment()).commit();
//        }
      //  getActionBar().hide();
        readData();
        readCalData();
//        readWeightData();
    }

//    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
//            new BottomNavigationView.OnNavigationItemSelectedListener() {
//                @Override
//                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//                    Fragment selectedFragment = null;
//                    switch (item.getItemId()) {
//                        case R.id.nav_home:
//                            selectedFragment = new ActivityFragment();
//                            break;
//                        case R.id.nav_friends:
//                            selectedFragment = new LeaderBoardActivity();
//                            break;
//
//                        case R.id.nav_challenge:
//                            selectedFragment = new ChallengeActivity();
//                            break;
//
//                    }
//                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
//                            selectedFragment).commit();
//                    return true;
//                }
//            };



    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {


                  /*  if(item.getItemId() ==R.id.nav_home)
                    {

                    }
*/

                      if(item.getItemId()== R.id.nav_friends) {
                        Intent in = new Intent(getApplicationContext(), LeaderBoardActivity.class);
                        startActivity(in);
                        finish();
                    }

                        else if(item.getItemId()== R.id.nav_challenge) {
                        Intent inte = new Intent(getApplicationContext(), ChallengeActivity.class);
                        startActivity(inte);
                        finish();
                    }


//                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
//                            selectedFragment).commit();
                    return true;
                }
            };

    private void readWeightData() {


      try {
          Fitness.getHistoryClient(this, GoogleSignIn.getLastSignedInAccount(this))
                  .readDailyTotal(DataType.TYPE_WEIGHT)
                  .addOnSuccessListener(
                          new OnSuccessListener<DataSet>() {
                              @Override
                              public void onSuccess(DataSet dataSet) {
                                  if (dataSet.isEmpty()) {
                                      Toast.makeText(getApplicationContext(), "There was a problem getting the step count.", Toast.LENGTH_SHORT).show();

                                  } else {
                                      int total =
                                              dataSet.isEmpty()
                                                      ? 0
                                                      : dataSet.getDataPoints().get(0).getValue(Field.FIELD_WEIGHT).asInt();
//                                    Log.i(TAG, "Total steps: " + total);
//                                    Toast.makeText(getApplicationContext(), "Total Steps" + total, Toast.LENGTH_SHORT).show();
                                      Wt = String.valueOf(total);
                                      txtWt.setText(Wt);
                                      System.out.println("checking wt" + Wt);


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
                                     long total =
                                             dataSet.isEmpty()
                                                     ? 0
                                                     : dataSet.getDataPoints().get(0).getValue(Field.FIELD_STEPS).asInt();
//
                                     String totalSteps = String.valueOf(total);
                                     txtSteps.setText(totalSteps + " Total Steps");
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
                                    double totalcal =
                                            dataSet.isEmpty()
                                                    ? 0
                                                    : dataSet.getDataPoints().get(0).getValue(Field.FIELD_CALORIES).asFloat();

                                    ;
                                    String totalCals = String.valueOf(Math.round(totalcal));
                                    txtCalores.setText(totalCals + " Calories");

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


}
