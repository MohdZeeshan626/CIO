package com.singleevent.sdk.View.LeftActivity.askAquestion;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.singleevent.sdk.ApiList;
import com.singleevent.sdk.Custom_View.Error_Dialog;
import com.singleevent.sdk.Custom_View.Util;
import com.singleevent.sdk.Custom_View.VolleyErrorLis;
import com.singleevent.sdk.model.Agenda.Agendadetails;
import com.singleevent.sdk.model.AppDetails;
import com.singleevent.sdk.R;
import com.singleevent.sdk.View.LeftActivity.askAquestion.adapter.AskAQueAdapter;
import com.singleevent.sdk.View.LeftActivity.askAquestion.model.EventQuestionModel;
import com.singleevent.sdk.View.RecyclerItemClickListener;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import io.paperdb.Paper;

/**
 * Created by webMOBI on 2/13/2018.
 */

public class AskAQuestionActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = AskAQuestionActivity.class.getSimpleName();
    private AppDetails appDetails;
    private Context context;
    private AskAQueAdapter askAQueAdapter;
    private EventQuestionModel eventQuestionModel;
    private ArrayList<EventQuestionModel> questionsList ;
    private TextView addYourQues;
    RequestQueue queue;
    private Agendadetails item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_ask_ques);
        Paper.init(this);
        context = AskAQuestionActivity.this;
        questionsList = new ArrayList<>();
        appDetails = Paper.book().read("Appdetails");

        item = (Agendadetails) getIntent().getSerializableExtra("Agendaview");

        askAQueAdapter = new AskAQueAdapter( this, questionsList );
        queue = Volley.newRequestQueue(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        RecyclerView recycler_view =(RecyclerView)findViewById( R.id.recyclerView);
        addYourQues =(TextView)findViewById(R.id.addYourQues);

        toolbar.setBackgroundColor(Color.parseColor(appDetails.getTheme_color()));
        addYourQues.setBackground(Util.setdrawable(this,R.drawable.rectanglebackground,
                Color.parseColor(appDetails.getTheme_color())));

        setSupportActionBar(toolbar);

        recycler_view.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,
                false));
        //recycler_view.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recycler_view.setAdapter(askAQueAdapter);
        recycler_view.addOnItemTouchListener(new RecyclerItemClickListener(this, recycler_view,
                new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (!questionsList.get(position).isUser_vote()){
                    questionsList.get(position).setUser_vote(false);
                    getAgandaVote( questionsList.get(position).getAgenda_id(),
                            questionsList.get(position).getQuestion_id());

                }
               }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));

        addYourQues.setOnClickListener(this);
        /*askAQueAdapter.setOnCardClickListner(this);*/
        getAgendaQuestions();
    }

    private void getAgandaVote(final int aganda_id,final int question_id) {

        final ProgressDialog dialog = new ProgressDialog(this,R.style.MyAlertDialogStyle);
        dialog.setMessage("Loading...");
        dialog.show();

        String tag_string_req = "add_question";
        String url = ApiList.Vote_Agenda_Question;

        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {
                dialog.hide();
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.getBoolean("response")){
                        Error_Dialog.show(object.getString("responseString"),
                                AskAQuestionActivity.this );

                        getAgendaQuestions();

                    }else {
                        dialog.hide();
                        Error_Dialog.show(object.getString("responseString"),
                                AskAQuestionActivity.this );
                    }

                } catch (JSONException e) {
                    dialog.hide();
                    e.printStackTrace();

                }


            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.hide();
                Log.d(TAG,error.toString() );

            }
        }){

            @Override
            public Map<String, String> getParams() {
                HashMap<String, String> params = new HashMap<String, String>();
                params.put("userid", Paper.book().read("userId", ""));
                params.put("appid",appDetails.getAppId());
                params.put("agenda_id", String.valueOf(aganda_id));
                params.put("question_id", String.valueOf(question_id));
                return params;
            }
        };


       queue.add(strReq);
    }

    private void getAgendaQuestions() {

        String url = ApiList.Agenda_Questions+"appid="+appDetails.getAppId()+"&agenda_id="+item.getAgendaId()
                +"&userid="+ Paper.book().read("userId");
            String tag_string_req = "Agenda_question";

        final ProgressDialog dialog = new ProgressDialog(context,R.style.MyAlertDialogStyle);
        dialog.setMessage("Loading...");
        dialog.show();

            StringRequest strReq = new StringRequest(Request.Method.GET,
                    url, new Response.Listener<String>() {


                @Override
                public void onResponse(String response) {
                    dialog.hide();
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.getBoolean("response")){
                            JSONArray jsonArray = object.getJSONArray("questions");
                            questionsList.clear();
                            for (int i = 0; i < jsonArray.length();i++ ){
                                Log.d(TAG,jsonArray.length()+"" );
                                JSONObject obje = jsonArray.getJSONObject(i);
                                eventQuestionModel = new EventQuestionModel();
                                eventQuestionModel.setQuestion_id(obje.getInt("question_id"));
                                eventQuestionModel.setQuestion(obje.getString("question").trim());
                                eventQuestionModel.setAgenda_id(obje.getInt("agenda_id"));
                                eventQuestionModel.setAppid(obje.getString("appid"));
                                eventQuestionModel.setUserid(obje.getString("userid"));
                              //  eventQuestionModel.setVote();
                                eventQuestionModel.setUp_votes(obje.getInt("up_votes"));
                                eventQuestionModel.setUser_vote(obje.getBoolean("user_vote"));
                                questionsList.add(eventQuestionModel);


                            }
                            askAQueAdapter.notifyDataSetChanged();


                        }else {
                            dialog.hide();
                            Error_Dialog.show(object.getString("responseString"),
                                    AskAQuestionActivity.this );
                        }

                    } catch (JSONException e) {
                        dialog.hide();
                        e.printStackTrace();

                    }


                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    dialog.hide();
                    if (error instanceof TimeoutError) {
                        Error_Dialog.show("Timeout", AskAQuestionActivity.this);
                    } else if (VolleyErrorLis.isServerProblem(error)) {
                        Error_Dialog.show(VolleyErrorLis.handleServerError(error, context), AskAQuestionActivity.this);
                    } else if (VolleyErrorLis.isNetworkProblem(error)) {
                        Error_Dialog.show("Please Check Your Internet Connection", AskAQuestionActivity.this);
                    }

                }
            });
        queue.add(strReq);

        }


    @Override
    protected void onResume() {
        super.onResume();

        SpannableString s = new SpannableString("Session Feed");
        setTitle(Util.applyFontToMenuItem(this, s));

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.addYourQues){

            openDialogToAddQuestion();
        }
    }

    private void openDialogToAddQuestion() {
        final Dialog dialog = new Dialog(AskAQuestionActivity.this,
                R.style.MaterialDialogSheet);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawableResource(R.color.transparent);
        dialog.setContentView(R.layout.dialog_add_questions);
        dialog.setCancelable(true);
        dialog.show();

        TextView canceltxt = (TextView) dialog.findViewById(R.id.canceltxt);
        TextView savetxt = (TextView) dialog.findViewById(R.id.savetxt);
        savetxt.setBackground(Util.setdrawable(this, R.drawable.act_button_border,
                Color.parseColor(appDetails.getTheme_color())));
        canceltxt.setBackground(Util.setdrawable(this, R.drawable.act_button_border,
                Color.parseColor(appDetails.getTheme_color())));

        final TextView textLimit =(TextView) dialog.findViewById(R.id.textLimit);
        canceltxt.setTypeface(Util.regulartypeface(context));
        savetxt.setTypeface(Util.regulartypeface(context));

        final EditText questions = (EditText) dialog.findViewById(R.id.questions);
        questions.setTypeface(Util.regulartypeface(context));

        canceltxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        savetxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!questions.getText().toString().isEmpty()) {


                    if (questions.getText().toString().trim().length() < 8 ){

                        Toast.makeText(context, Util.applyFontToMenuItem(context,
                                new SpannableString("Question should be more than 8 characters.")),
                                Toast.LENGTH_SHORT).show();
                    }else {
                        getNewQuestion( questions.getText().toString() );
                        dialog.dismiss();
                    }

                }


            }
        });
          TextWatcher mTextEditorWatcher = new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //This sets a textview to the current length


                textLimit.setText(String.valueOf(1000 - s.length()));
            }

            public void afterTextChanged(Editable s) {
            }
        };
        questions.addTextChangedListener(mTextEditorWatcher);


    }

    private void getNewQuestion(final String question ) {

        final ProgressDialog dialog = new ProgressDialog(this,R.style.MyAlertDialogStyle);
        dialog.setMessage("Loading...");
        dialog.show();

        String tag_string_req = "add_question";
        String url = ApiList.Add_Agenda_Question;

        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {
                dialog.hide();
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.getBoolean("response")){
                        Error_Dialog.show(object.getString("responseString"),
                                AskAQuestionActivity.this );
                        getAgendaQuestions();

                    }else {
                        dialog.hide();
                        Error_Dialog.show(object.getString("responseString"),
                                AskAQuestionActivity.this );
                    }

                } catch (JSONException e) {
                    dialog.hide();
                    e.printStackTrace();

                }


            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.hide();
                if (error instanceof TimeoutError) {
                    Error_Dialog.show("Timeout", AskAQuestionActivity.this);
                } else if (VolleyErrorLis.isServerProblem(error)) {
                    Error_Dialog.show(VolleyErrorLis.handleServerError(error, context), AskAQuestionActivity.this);
                } else if (VolleyErrorLis.isNetworkProblem(error)) {
                    Error_Dialog.show("Please Check Your Internet Connection", AskAQuestionActivity.this);
                }


            }
        }){

            @Override
            public Map<String, String> getParams() {
                HashMap<String, String> params = new HashMap<String, String>();
                params.put("userid", Paper.book().read("userId", ""));
                params.put("appid", appDetails.getAppId());
                params.put("agenda_id", String.valueOf(item.getAgendaId()));
                params.put("question",question);
                return params;
            }
        };


        queue.add(strReq);
    }
}
