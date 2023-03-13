package com.singleevent.sdk.health.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.FitnessOptions;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.singleevent.sdk.health.API.PasswordApi;
import com.singleevent.sdk.health.API.loginApi;
import com.singleevent.sdk.health.Model.loginModel;
import com.singleevent.sdk.health.Model.passwordModel;
import com.singleevent.sdk.R;

public class PasswordActivity extends AppCompatActivity {

    private static String BASE_URL="https://check1.webmobi.in/api/event/";

    private static final int REQUEST_OAUTH_REQUEST_CODE = 0x1001;
    public static final String TAG = "StepCounter";
    FitnessOptions fitnessOptions;
    Button btncon;
    EditText ed1,ed2,ed3,ed4,ed5,ed6;
    TextView resend;
    String otp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
  //      getSupportActionBar().setDisplayShowHomeEnabled(true);
    //    getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_chevron_left_black_24dp);
        GoogleSignin();
        btncon = (Button)findViewById(R.id.btn_continue);
        ed1 = (EditText)findViewById(R.id.otp1);
        ed2 = (EditText)findViewById(R.id.otp2);
        ed3 = (EditText)findViewById(R.id.otp3);
        ed4 = (EditText)findViewById(R.id.otp4);
        ed5 = (EditText)findViewById(R.id.otp5);
        ed6 = (EditText)findViewById(R.id.otp6);


        ed1.addTextChangedListener(new GenericTextWatcher(ed2, ed1));
        ed2.addTextChangedListener(new GenericTextWatcher(ed3, ed1));
        ed3.addTextChangedListener(new GenericTextWatcher(ed4, ed2));
        ed4.addTextChangedListener(new GenericTextWatcher(ed5, ed3));
        ed5.addTextChangedListener(new GenericTextWatcher(ed6, ed4));
        ed6.addTextChangedListener(new GenericTextWatcher(ed6, ed5));
        resend = (TextView)findViewById(R.id.resend_code);
        Paper.init(this);
        resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resend_code();
            }
        });

        btncon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                passwordOtp();
//                Intent intent = new Intent(PasswordActivity.this,HomeActivity.class);
//                startActivity(intent);
            }
        });


    }

    public void GoogleSignin()
    {
         fitnessOptions =
                FitnessOptions.builder()
                        .addDataType(DataType.TYPE_STEP_COUNT_CUMULATIVE)
                        .addDataType(DataType.TYPE_STEP_COUNT_DELTA)
                        .addDataType(DataType.TYPE_CALORIES_EXPENDED)
                      //  .addDataType(DataType.TYPE_DISTANCE_CUMULATIVE)
                        .addDataType(DataType.TYPE_DISTANCE_DELTA,FitnessOptions.ACCESS_READ)
                      //  .addDataType(DataType.TYPE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_WRITE)
                      //  .addDataType(DataType.AGGREGATE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_WRITE)
                        .addDataType(DataType.TYPE_MOVE_MINUTES)
                        .addDataType(DataType.TYPE_WEIGHT)
                        .build();
        if (!GoogleSignIn.hasPermissions(GoogleSignIn.getLastSignedInAccount(this), fitnessOptions)) {
            GoogleSignIn.requestPermissions(
                    this,
                    REQUEST_OAUTH_REQUEST_CODE,
                    GoogleSignIn.getLastSignedInAccount(this),
                    fitnessOptions);
        } else {
            subscribe();
        }
    }

        @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_OAUTH_REQUEST_CODE) {
                subscribe();
            }
        }
    }
    public void subscribe() {
        // To create a subscription, invoke the Recording API. As soon as the subscription is
        // active, fitness data will start recording.
        Fitness.getRecordingClient(this, GoogleSignIn.getLastSignedInAccount(this))
                .subscribe(DataType.TYPE_ACTIVITY_SEGMENT)
                .addOnCompleteListener(
                        new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Log.i(TAG, "Successfully subscribed!");
                                    Toast.makeText(getApplicationContext(),"Successfully subscribed!",Toast.LENGTH_SHORT).show();

//                                    for (Subscription sc : subscriptions) {
//                                        DataType dt = sc.getDataType();
//                                        Log.i(TAG, "Active subscription for data type: " + dt.getName());
//                                    }
                                } else {
                                    Log.w(TAG, "There was a problem subscribing.", task.getException());
                                    Toast.makeText(getApplicationContext(),"There was a problem subscribing.",Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
    }

    private void resend_code()

    {
        String eid =Paper.book().read("email_id");
        String event_id = Paper.book().read("Current_Event_Id","");
        String action = "generate";
        String passkey = "passkey";
        String devic_id = "1234";
        String device_type = "android";


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        System.out.println("Selecting api type"+BASE_URL);

        loginApi api = retrofit.create(loginApi.class);
        Call<loginModel> call = api.userOtp(event_id,eid,action,passkey,devic_id,device_type);
        final ProgressDialog progressDialog = ProgressDialog.show(PasswordActivity.this,
                "Please wait...", "Proccessing...", true);
        call.enqueue(new Callback<loginModel>() {
            @Override
            public void onResponse(Call<loginModel> call, Response<loginModel> response) {
                progressDialog.dismiss();
                if (response.isSuccessful())
                {
                    loginModel login_model = response.body();
                    if (login_model.isResponse() == true)
                    {
                        Toast.makeText(getApplicationContext(),login_model.getResponseString(),Toast.LENGTH_SHORT).show();
//                        Intent in = new Intent(PasswordActivity.this,PasswordActivity.class);
//                        startActivity(in);
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(),login_model.getResponseString(),Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<loginModel> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(PasswordActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void passwordOtp()
    {

        otp = ed1.getText().toString()+ed2.getText().toString()+ed3.getText().toString()+ed4.getText().toString()+ed5.getText().toString()+ed6.getText().toString();

        System.out.println("checking passkey"+otp);
        String eid =Paper.book().read("email_id");
        String event_id = Paper.book().read("Current_Event_Id","");
        String action = "verify";
        String passkey = otp;
        String devic_id = "1234";
        String device_type = "android";


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        PasswordApi api = retrofit.create(PasswordApi.class);
        Call<passwordModel> call = api.passwordOtp(event_id,eid,action,passkey,devic_id,device_type);
        final ProgressDialog progressDialog = ProgressDialog.show(PasswordActivity.this,
                "Please wait...", "Proccessing...", true);
        call.enqueue(new Callback<passwordModel>() {
            @Override
            public void onResponse(Call<passwordModel> call, Response<passwordModel> response) {
                progressDialog.dismiss();
                if (response.isSuccessful())
                {
                   passwordModel lgm = response.body();
                   if (lgm.isResponse() == true)
                   {
                       Paper.book().write("ISLOGINSTATUS",true);
                       Intent intent = new Intent(PasswordActivity.this,HomeActivity.class);
                        startActivity(intent);
                        finish();

                   }
                   else
                   {
                       Toast.makeText(getApplicationContext(),lgm.getResponseString().toString(),Toast.LENGTH_SHORT).show();
                       System.out.println("insufficient data"+lgm.getData());
                   }
                }
            }

            @Override
            public void onFailure(Call<passwordModel> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(PasswordActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main_health,menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent intent= new Intent(this, LoginwithnumberActivity.class);
            startActivity(intent);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    public class GenericTextWatcher implements TextWatcher {
        private EditText etPrev;
        private EditText etNext;

        public GenericTextWatcher(EditText etNext, EditText etPrev) {
            this.etPrev = etPrev;
            this.etNext = etNext;
        }

        @Override
        public void afterTextChanged(Editable editable) {
            String text = editable.toString();
            if (text.length() == 1)
                etNext.requestFocus();
            else if (text.length() == 0)
                etPrev.requestFocus();
        }

        @Override
        public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
        }

        @Override
        public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
        }
    }

}
