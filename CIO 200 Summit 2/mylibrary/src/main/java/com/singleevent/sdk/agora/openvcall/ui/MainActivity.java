package com.singleevent.sdk.agora.openvcall.ui;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.ActionBar;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.singleevent.sdk.View.LeftActivity.AgoraClass;
import com.singleevent.sdk.model.Agenda.Agendadetails;
import com.singleevent.sdk.model.AppDetails;
import com.singleevent.sdk.R;
import com.singleevent.sdk.agora.openvcall.model.ConstantApp;

import io.paperdb.Paper;

public class MainActivity extends BaseActivity {

    public final static Logger log = LoggerFactory.getLogger(MainActivity.class);

    String c_name,role,group_id,agenda_id;
    Agendadetails agendadetails;
    AppDetails appDetails;
    Boolean isAdmin=false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appDetails = Paper.book().read("Appdetails", null);
         Paper.init(this);
        Bundle extras = getIntent().getExtras();
        if (extras == null)
            finish();

         c_name = extras.getString("Channel_name");
         role=extras.getString("Role");
         group_id=extras.getString("Group_id");
         agenda_id=extras.getString("Agenda_id");
        agendadetails = (Agendadetails) getIntent().getSerializableExtra("AgendaList"); //Obtaining data



        System.out.print("Channel Name is"+c_name);
        setContentView(R.layout.agoramain);

        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            ab.setCustomView(R.layout.ard_agora_actionbar);
        }
        forwardToRoom();
    }

    @Override
    public void initUIandEvent() {

    }/* EditText v_channel = (EditText) findViewById(R.id.channel_name1);

        v_channel.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                boolean isEmpty = TextUtils.isEmpty(s.toString());
                findViewById(R.id.button_join1).setEnabled(!isEmpty);
            }
        });
*//*
        Spinner encryptionSpinner = (Spinner) findViewById(R.id.encryption_mode);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.encryption_mode_values, R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        encryptionSpinner.setAdapter(adapter);

        encryptionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                vSettings().mEncryptionModeIndex = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        encryptionSpinner.setSelection(vSettings().mEncryptionModeIndex);

        String lastChannelName = vSettings().mChannelName;
        if (!TextUtils.isEmpty(lastChannelName)) {
            v_channel.setText(lastChannelName);
            v_channel.setSelection(lastChannelName.length());
        }

        EditText v_encryption_key = (EditText) findViewById(R.id.encryption_key);
        String lastEncryptionKey = vSettings().mEncryptionKey;
        if (!TextUtils.isEmpty(lastEncryptionKey)) {
            v_encryption_key.setText(lastEncryptionKey);
        }*/

    @Override
    public void deInitUIandEvent() {
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        MenuInflater inflater = getMenuInflater();
        //  inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        int i=item.getItemId();

       return false;
    }

    public void onClickJoin(View view) {
        forwardToRoom();
    }

    public void forwardToRoom() {
        EditText v_channel = (EditText) findViewById(R.id.channel_name1);
        Button b=(Button) findViewById(R.id.button_join1) ;
        String channel =c_name;
       // vSettings().mChannelName = channel;




        /*Intent i = new Intent(MainActivity.this, CallActivity.class);
        i.putExtra(ConstantApp.ACTION_KEY_CHANNEL_NAME, channel);
        i.putExtra(ConstantApp.ACTION_KEY_USER_ROLE,role);
        i.putExtra("group_id",group_id);
        i.putExtra("Agenda_ID",agenda_id);
        startActivity(i);
        finish();*/


        Bundle args = new Bundle();
        args.putString(ConstantApp.ACTION_KEY_CHANNEL_NAME,channel);
        args.putString("Role",role);
        args.putString(ConstantApp.ACTION_KEY_USER_ROLE,role);
        args.putString("group_id",group_id);
        args.putString("Agenda_ID",agenda_id);
        args.putSerializable("AgendaDetails",agendadetails);
        Intent i = new Intent(MainActivity.this, CallActivity.class);
        i.putExtras(args);
        //i.setAction("com.agenda");
        startActivity(i);
        finish();

    }


    @Override
    public void permissionGranted() {

    }


}
