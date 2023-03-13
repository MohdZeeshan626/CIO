package com.singleevent.sdk.health.Activity;

import androidx.appcompat.app.AppCompatActivity;
import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.singleevent.sdk.health.API.loginApi;
import com.singleevent.sdk.health.Model.loginModel;
import com.singleevent.sdk.R;


public class LoginwithnumberActivity extends AppCompatActivity {
    private static String BASE_URL="https://check1.webmobi.in/api/event/";

    Button verify;
    EditText emailid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginwithnumber);
        verify = (Button)findViewById(R.id.btn_verify);
        emailid = (EditText)findViewById(R.id.edit_email);
        Paper.init(this);


        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                login();

            }
        });

      //  getActionBar().setDisplayHomeAsUpEnabled(true);
       // getActionBar().setDisplayShowHomeEnabled(true);
        //getActionBar().setHomeAsUpIndicator(R.drawable.ic_chevron_left_black_24dp);

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main_health,menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent intent= new Intent(this, MainActivityHealth.class);
            startActivity(intent);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    private void login()
    {
        String eid = emailid.getText().toString();
        String event_id = Paper.book().read("Current_Event_Id","");
        String action = "generate";
        String passkey = "passkey";
        String devic_id = "1234";
        String device_type = "android";
        Paper.book().write("email_id",eid);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        System.out.println("Selecting api type"+BASE_URL);

        loginApi api = retrofit.create(loginApi.class);
        Call<loginModel> call = api.userOtp(event_id,eid,action,passkey,devic_id,device_type);
        System.out.println("check:"+call);
        final ProgressDialog progressDialog = ProgressDialog.show(LoginwithnumberActivity.this,
                "Please wait...", "Proccessing...", true);
        call.enqueue(new Callback<loginModel>() {
            @Override
            public void onResponse(Call<loginModel> call, Response<loginModel> response) {
                progressDialog.dismiss();
                System.out.println("check"+response.body());
                if (response.isSuccessful())
                {
                    loginModel login_model = response.body();
                    if (login_model.isResponse() == true)
                    {
                        Toast.makeText(getApplicationContext(),login_model.getResponseString(),Toast.LENGTH_SHORT).show();
                        Intent in = new Intent(LoginwithnumberActivity.this,PasswordActivity.class);
                        startActivity(in);
                        finish();
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
                Toast.makeText(LoginwithnumberActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}
