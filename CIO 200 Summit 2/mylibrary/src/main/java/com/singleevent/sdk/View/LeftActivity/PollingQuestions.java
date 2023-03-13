package com.singleevent.sdk.View.LeftActivity;

import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.text.SpannableString;
import android.view.MenuItem;


import com.singleevent.sdk.Custom_View.Util;
import com.singleevent.sdk.Left_Adapter.PollingQuestionsAdapter;
import com.singleevent.sdk.model.AppDetails;
import com.singleevent.sdk.model.Events;
import com.singleevent.sdk.model.Polling.Report;
import com.singleevent.sdk.model.Polling.polling;
import com.singleevent.sdk.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import io.paperdb.Paper;

public class PollingQuestions extends AppCompatActivity {
    private RecyclerView recycler_view;
    private AppDetails appDetails;
    private PollingQuestionsAdapter pollingQuestionsAdapter;
    private PollingQuestionsAdapter pollingQuestionsAdapter1;

    private ArrayList<polling> pollingList = new ArrayList<>();
    private ArrayList<polling> pollingList1 = new ArrayList<>();
    int pos, pos1,questionsize, itempos,itempos1,polltypeid;
    private String title, toJson, answer, pollName, dateTime,polltype;
    private ArrayList<Events> events = new ArrayList<Events>();
    Events e;
    private boolean is_ans_submitted;
    private ArrayList<Report> reportArrayList = new ArrayList<>();
    private ArrayList<Report> reportArrayList1 = new ArrayList<>();
    private ArrayList<Report> finalPollingList = new ArrayList<>();
    private ArrayList<Report> finalPollingList1 = new ArrayList<>();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_pollingquestions );
        Paper.init(this);
        appDetails = Paper.book().read("Appdetails");
        recycler_view = (RecyclerView) findViewById(R.id.recycler_view);

        setpollingitems();




    }

    private void setpollingitems() {
try{
        appDetails = Paper.book().read("Appdetails");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(Color.parseColor(appDetails.getTheme_color()));
        setSupportActionBar(toolbar);


        // getting the data from previous activity

        if (getIntent().getExtras() == null)
            finish();


        pos = getIntent().getExtras().getInt("Tabpos");
        itempos = getIntent().getExtras().getInt("Itempos");
        pollName = getIntent().getExtras().getString("pollName");
        title = getIntent().getExtras().getString("title");
        polltype = getIntent().getExtras().getString("polltype");
        polltypeid = getIntent().getExtras().getInt("polltypeid");


        reportArrayList = (ArrayList<Report>) getIntent().getSerializableExtra("ReportList");
        reportArrayList1 = (ArrayList<Report>) getIntent().getSerializableExtra("ReportList");
        is_ans_submitted = getIntent().getExtras().getBoolean("submitted_answer");

        events = Paper.book().read("Appevents");
        e = events.get(0);

        pollingList.clear();
        pollingList1.clear();
        finalPollingList.clear();
        finalPollingList1.clear();


        if (polltype.equals("global")) {

            for (int j = 0; j < e.getTabs(pos).getItems().length; j++) {
                for (int k = 0; k < e.getTabs(pos).getItems(j).getpollingSize(); k++) {
                    if (e.getTabs(pos).getItems(j).getName().equals(pollName)) {
                        pollingList.add(e.getTabs(pos).getItems(j).getpoll(k));
                    }
                }
            }
        } else {
            for (int j = 0; j < e.getTabs(pos).getItems().length; j++) {
                if (polltypeid > 0) {
                    for (int k = 0; k < e.getTabs(pos).getItems(j).getpollingSize(); k++) {
                        if (e.getTabs(pos).getItems(j).getName().equals(pollName)) {
                            pollingList1.add(e.getTabs(pos).getItems(j).getpoll(k));
                        }
                    }
                }
            }
        }


        /*According to updated polling answers,I have changed arraylist's data*/

        if (!is_ans_submitted) {
            if (polltype.equals("global")) {
                for (int position = 0; position < pollingList.size(); position++) {

                    Report report = new Report(null, pollingList.get(position).getId(),
                            pollingList.get(position).getQuestion(),
                            pollingList.get(position).getAnswer(), pollingList.get(position).getType(),
                            pollingList.get(position).getDetail(), false, "0", "0");

                    finalPollingList.add(report);
                }
            } else {
                for (int position = 0; position < pollingList1.size(); position++) {
                    Report report1 = new Report(null, pollingList1.get(position).getId(),
                            pollingList1.get(position).getQuestion(),
                            pollingList1.get(position).getAnswer(), pollingList1.get(position).getType(),
                            pollingList1.get(position).getDetail(), false, "0", "0");

                    finalPollingList1.add(report1);
                }
            }

        } else {

            /*Changes in Adapter list  */
            if (polltype.equals("global")) {
                for (int position = 0; position < pollingList.size(); position++) {

                    if (checkPollingId(pollingList.get(position).getId())) {
                        int pollresPos;
                        pollresPos = getReportListPosition(pollingList.get(position).getId());

                        /*(true or false) ? (execute this if true) : (execute this if false)*/
                        Report report = new Report(pollresPos != -1 ? reportArrayList.get(pollresPos).getPollresult() : null,
                                pollingList.get(position).getId(), pollingList.get(position).getQuestion(),
                                pollingList.get(position).getAnswer(), pollingList.get(position).getType(),
                                pollingList.get(position).getDetail(), true, reportArrayList.get(pollresPos).getTotal_count(), reportArrayList.get(pollresPos).getPolling_id());

                        finalPollingList.add(report);
                    } else {
                        Report report = new Report(null, pollingList.get(position).getId(),
                                pollingList.get(position).getQuestion(),
                                pollingList.get(position).getAnswer(), pollingList.get(position).getType(),
                                pollingList.get(position).getDetail(), false, "0", "0");

                        finalPollingList.add(report);
                    }

                }
            }//close outer for loop
            else {
                for (int position = 0; position < pollingList1.size(); position++) {

                    if (checkPollingId(pollingList1.get(position).getId())) {
                        int pollresPos;
                        pollresPos = getReportListPosition(pollingList1.get(position).getId());

                        /*(true or false) ? (execute this if true) : (execute this if false)*/
                        Report report1 = new Report(pollresPos != -1 ? reportArrayList1.get(pollresPos).getPollresult() : null,
                                pollingList1.get(position).getId(), pollingList1.get(position).getQuestion(),
                                pollingList1.get(position).getAnswer(), pollingList1.get(position).getType(),
                                pollingList1.get(position).getDetail(), true, reportArrayList1.get(pollresPos).getTotal_count(), reportArrayList1.get(pollresPos).getPolling_id());

                        finalPollingList1.add(report1);
                    } else {
                        Report report1 = new Report(null, pollingList1.get(position).getId(),
                                pollingList1.get(position).getQuestion(),
                                pollingList1.get(position).getAnswer(), pollingList1.get(position).getType(),
                                pollingList1.get(position).getDetail(), false, "0", "0");

                        finalPollingList1.add(report1);
                    }

                }
            }// closing else


        }


        Collections.sort(finalPollingList, new Comparator<Report>() {
            @Override
            public int compare(Report abc1, Report abc2) {
                return Boolean.compare(abc1.getAnsSubmitted(), abc2.getAnsSubmitted());
            }
        });

        Collections.sort(finalPollingList1, new Comparator<Report>() {
            @Override
            public int compare(Report abc1, Report abc2) {
                return Boolean.compare(abc1.getAnsSubmitted(), abc2.getAnsSubmitted());
            }
        });

        /*initialized polling question adapter */
        if (polltype.equals("global")) {
            pollingQuestionsAdapter = new PollingQuestionsAdapter(this, finalPollingList, pos, title, itempos, pollName, polltype, polltypeid);
            recycler_view.setLayoutManager(new LinearLayoutManager(this,
                    LinearLayoutManager.VERTICAL, false));
            recycler_view.setItemAnimator(new DefaultItemAnimator());
            recycler_view.setAdapter(pollingQuestionsAdapter);

            pollingQuestionsAdapter.notifyDataSetChanged();
        } else {
            pollingQuestionsAdapter1 = new PollingQuestionsAdapter(this, finalPollingList1, pos, title, itempos, pollName, polltype, polltypeid);
            recycler_view.setLayoutManager(new LinearLayoutManager(this,
                    LinearLayoutManager.VERTICAL, false));
            recycler_view.setItemAnimator(new DefaultItemAnimator());
            recycler_view.setAdapter(pollingQuestionsAdapter1);

            pollingQuestionsAdapter1.notifyDataSetChanged();

        }
    }catch(Exception e){}

    }

    private int getReportListPosition(int id){

        for (int count=0;count<reportArrayList.size();count++){
            if(polltype.equals("global")){
                if ( reportArrayList.get(count).getPolling_id() != null &&
                        !reportArrayList.get(count).getPolling_id().equals("") &&
                        id == Integer.valueOf(reportArrayList.get(count).getPolling_id())){

                    return count;}}
            else{
                if(reportArrayList1.get(count).getPolling_id() != null &&
                        !reportArrayList1.get(count).getPolling_id().equals("") &&
                        id == Integer.valueOf(reportArrayList1.get(count).getPolling_id())){

                    return count;

            }

            }
        }

        return -1;
    }

    private Boolean checkPollingId(int id){

        for (int count=0;count<reportArrayList.size();count++){
            if(polltype.equals("global")){
                if ( reportArrayList.get(count).getPolling_id() != null &&
                        !reportArrayList.get(count).getPolling_id().equals("") &&
                        id == Integer.valueOf(reportArrayList.get(count).getPolling_id())){

                    return true;}

            }else{
                if ( reportArrayList1.get(count).getPolling_id() != null &&
                        !reportArrayList1.get(count).getPolling_id().equals("") &&
                        id == Integer.valueOf(reportArrayList1.get(count).getPolling_id())){

                    return true;}
            }

        }

        return false;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId()==android.R.id.home){
            finish();
        }
        return true;
    }



    @Override
    protected void onResume() {
        super.onResume();
        SpannableString s = new SpannableString(title);
        setTitle(Util.applyFontToMenuItem(this, s));

    }
}
