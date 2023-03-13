package com.singleevent.sdk.View.Fragment.Left_Fragment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.Nullable;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import android.text.Editable;
import android.text.Html;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.singleevent.sdk.ApiList;
import com.singleevent.sdk.App;
import com.singleevent.sdk.Custom_View.Error_Dialog;
import com.singleevent.sdk.Custom_View.Letter.LetterTileProvider;
import com.singleevent.sdk.Custom_View.Letter.Roundeddrawable;
import com.singleevent.sdk.Custom_View.Util;
import com.singleevent.sdk.Custom_View.VolleyErrorLis;
import com.singleevent.sdk.View.LeftActivity.Attendee;
import com.singleevent.sdk.View.RightActivity.admin.adminSurvey.adapter.AttendeeUserListAdapter;
import com.singleevent.sdk.gallery_camera.attachMent.Attachment;
import com.singleevent.sdk.Left_Adapter.CommentAdapter;
import com.singleevent.sdk.model.AppDetails;
import com.singleevent.sdk.model.Comments;
import com.singleevent.sdk.model.Feed;
import com.singleevent.sdk.R;
import com.singleevent.sdk.model.User;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import cyd.awesome.material.AwesomeText;
import io.paperdb.Paper;
import io.socket.emitter.Emitter;

import static android.content.Context.INPUT_METHOD_SERVICE;
import static java.util.Locale.filter;

/**
 * Created by webMOBI on 9/15/2017.
 */

public class CommentFragment extends DialogFragment implements View.OnClickListener ,AttendeeUserListAdapter.OnCardClickListner {


    private static io.socket.client.Socket Socket;
    int dpWidth;
    Context context;
    private float Iwidth;
    ArrayList<com.singleevent.sdk.model.Comments> feeds = new ArrayList<>();
    CommentAdapter adapter;
    AppDetails appDetails;
    Feed feed;
    FrameLayout content;
    LinearLayout commentlistlayout;
    ConstraintLayout footer;
    AwesomeText close;
    TextView tcomment;
    Attachment postimage;
    SimpleExoPlayerView cpostvideo;
    ProgressBar loading;
    ListView commentlist;

    ImageView addimage;
    EditText ecomment;
    ImageView sendbutton;

    List<User> userview;
    AttendeeUserListAdapter bookListAdapter;
    RecyclerView userlist;
    ScrollView scrollview;
    StringBuffer mentioneduser;
    String fuid,funm,fprpic;
    SimpleExoPlayer cexoPlayer;
    public static CommentFragment newInstance(io.socket.client.Socket mSocket, Feed feed, int id) {

        Socket = mSocket;

        final CommentFragment orderFragment = new CommentFragment();
        final Bundle args = new Bundle();
        args.putInt("postid", id);
        args.putParcelable("feed", feed);
        orderFragment.setArguments(args);
        return orderFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
        Paper.init(getActivity());
        appDetails = Paper.book().read("Appdetails");
        setStyle(DialogFragment.STYLE_NORMAL, R.style.MY_DIALOG);
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        dpWidth = displayMetrics.widthPixels;

        userview = new ArrayList<>();
        userview = Paper.book(appDetails.getAppId()).read("OfflineAttendeeList",new ArrayList<User>());
        if(userview==null || userview.size()==0)
        {
            getuser();
        }
        getuser();


        if (getArguments() == null)
            getDialog().dismiss();

        feed = getArguments().getParcelable("feed");


        Socket.on("post_comment_ack", onComment);
        Socket.on("confirm_post_checkin", onConfirmCheckin);
        Socket.on("new_post_comment", onNewPostComment);
        //
        JSONObject json = new JSONObject();
        try {
            json.put("userid", Paper.book().read("userId", ""));
            json.put("appid", appDetails.getAppId());
            json.put("post_id", feed.getId());
            Socket.emit("post_checkin", json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onStart() {
        super.onStart();
        Dialog d = getDialog();
        if (d != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            d.getWindow().setLayout(width, height);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        // binding = FragmentCommentBinding.inflate(inflater, container, false);
        View view = getLayoutInflater().inflate(R.layout.fragment_comment, container, false);

        // content = (FrameLayout)view.findViewById(R.id.content);
        commentlistlayout = (LinearLayout) view.findViewById(R.id.commentlistlayout);
        footer=(ConstraintLayout)view.findViewById(R.id.footer);
       // footer.setBackgroundColor(Color.parseColor(appDetails.getTheme_color()));
        close = (AwesomeText) view.findViewById(R.id.close);
        tcomment = (TextView) view.findViewById(R.id.tcomment);
        postimage = (Attachment) view.findViewById(R.id.postimage);
        cpostvideo=(SimpleExoPlayerView) view.findViewById(R.id.cpostvideo);
        loading = (ProgressBar) view.findViewById(R.id.loading);
        addimage = (ImageView) view.findViewById(R.id.addimage);
        userlist = (RecyclerView) view.findViewById(R.id.listview);
        ecomment = (EditText) view.findViewById(R.id.ecomment);
        sendbutton = (ImageView) view.findViewById(R.id.sendbutton);
        scrollview=(ScrollView)view.findViewById(R.id.scrollview);
        //return binding.getRoot();
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

      /*  binding.close.setOnClickListener(this);
        Iwidth = dpWidth * 0.10F;
        feeds = new ArrayList<>();
        adapter = new CommentAdapter(feeds, context, dpWidth);
        binding.commentlist.setAdapter(adapter);
        binding.tcomment.setTypeface(Util.boldtypeface(context));

        binding.ecomment.setOnKeyListener(keyListener);
        binding.ecomment.addTextChangedListener(watcher);*/


        //

        close.setOnClickListener(this);
        Iwidth = dpWidth * 0.10F;
        feeds = new ArrayList<>();
       /* adapter = new CommentAdapter(feeds, context, dpWidth);
        commentlist.setAdapter(adapter);*/
        //adding list of comments using for loop
        addcommentlist(feeds, context, dpWidth);
        tcomment.setTypeface(Util.boldtypeface(context));

        ecomment.setOnKeyListener(keyListener);
        ecomment.addTextChangedListener(watcher);


        //setting posted image in imageview
        if (feed.attachmentlength() <= 0) {
            postimage.setVisibility(View.GONE);
            cpostvideo.setVisibility(View.GONE);
        }
        else if(feed.getAttachments().get(0).getType().equalsIgnoreCase("video/mp4")){
            postimage.setVisibility(View.GONE);
            cpostvideo.setVisibility(View.VISIBLE);
            String result = feed.getAttachments().get(0).getPath().substring(0, feed.getAttachments().get(0).getPath().indexOf("?"));

            try {

                // bandwisthmeter is used for
                // getting default bandwidth


                BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();

                // track selector is used to navigate between
                // video using a default seekbar.
                TrackSelector trackSelector = new DefaultTrackSelector(new AdaptiveTrackSelection.Factory(bandwidthMeter));

                // we are adding our track selector to exoplayer.
                cexoPlayer = ExoPlayerFactory.newSimpleInstance(context, trackSelector);

                // we are parsing a video url
                // and parsing its video uri.
                Uri videouri = Uri.parse(result);

                // we are creating a variable for datasource factory
                // and setting its user agent as 'exoplayer_view'
                DefaultHttpDataSourceFactory dataSourceFactory = new DefaultHttpDataSourceFactory("exoplayer_video");

                // we are creating a variable for extractor factory
                // and setting it to default extractor factory.
                ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();

                // we are creating a media source with above variables
                // and passing our event handler as null,
                MediaSource mediaSource = new ExtractorMediaSource(videouri, dataSourceFactory, extractorsFactory, null, null);

                // inside our exoplayer view
                // we are setting our player
                cpostvideo.setPlayer(cexoPlayer);

                // we are preparing our exoplayer
                // with media source.
                cexoPlayer.prepare(mediaSource);

                // we are setting our exoplayer
                // when it is ready.
                cexoPlayer.setPlayWhenReady(false);

                // holder.exoPlayerView.buildDrawingCache();

                cexoPlayer.addListener(new ExoPlayer.EventListener() {
                    @Override
                    public void onTimelineChanged(Timeline timeline, Object manifest) {

                    }

                    @Override
                    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

                    }

                    @Override
                    public void onLoadingChanged(boolean isLoading) {

                    }

                    @Override
                    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                        if (playWhenReady && playbackState == cexoPlayer.STATE_READY) {
                            // media actually playing
                        } else if (playWhenReady) {
                            if(cexoPlayer.getDuration()==cexoPlayer.getCurrentPosition()){
                                cexoPlayer.seekTo(0);
                            }
                            // might be idle (plays after prepare()),
                            // buffering (plays when data available)
                            // or ended (plays when seek away from end)
                        } else {
                            // player paused in any state
                        }
                    }

                    @Override
                    public void onPlayerError(ExoPlaybackException error) {

                    }

                    @Override
                    public void onPositionDiscontinuity() {

                    }

                    @Override
                    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

                    }
                });



            } catch (Exception e) {
                // below line is used for
                // handling our errors.
                Log.e("TAG", "Error : " + e.toString());
            }
        }
        else {
            cpostvideo.setVisibility(View.GONE);
            postimage.setVisibility(View.VISIBLE);
            postimage.setItemList(feed.getAttachments());
        }


        //clciklistener for send button
        setinactiveimage();
        sendbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ecomment.getText().length()>0) {
                attemptSend();
                InputMethodManager imm = (InputMethodManager) context.getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);//to hide the input keyboard
                }
            }
        });

        Glide.with(context.getApplicationContext())
                .load(R.drawable.camera)
                .asBitmap()
                .placeholder(R.drawable.camera)
                .error(R.drawable.camera)
                .into(new BitmapImageViewTarget(addimage) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        addimage.setImageBitmap(Util.scaleBitmap(resource, (int) Iwidth, (int) Iwidth));
                    }
                });

        LoadComment(getArguments().getInt("postid"));

    }
    private EditText.OnKeyListener keyListener = new View.OnKeyListener() {
        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {

            // If the event is a key-down event on the "enter" button
            if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                    (keyCode == KeyEvent.KEYCODE_ENTER)) {
                // Perform action on key press

                EditText editText = (EditText) v;

                if (v == ecomment) {
                    if(ecomment.getText().length()>0)
                    attemptSend();
                }

                ecomment.setText("");

                return true;
            }
            return false;

        }
    };


    private void getuser() {

        final ProgressDialog dialog = new ProgressDialog(context, R.style.MyAlertDialogStyle);
        dialog.setMessage("Loading");
        dialog.show();
        String tag_string_req = "Login";
        String url = ApiList.Users + appDetails.getAppId()+"&userid="+Paper.book().read("userId", "")+"&admin_flag=attendee_option";






        StringRequest strReq = new StringRequest(Request.Method.GET,





                url, new Response.Listener<String>() {




            @Override
            public void onResponse(String response) {
                dialog.dismiss();



                try {

                    JSONObject jObj = new JSONObject(response);


                    if (jObj.getBoolean("response")) {
                        userview.clear();

                        parseuser(jObj.getJSONObject("responseString").getJSONArray("users"));
                    } else {
                        if (jObj.getString("responseString").equalsIgnoreCase("Invalid token")) {
                            Intent i = new Intent();
                            i.putExtra("keyMessage", "Session Expired, Please Login ");
                            i.putExtra("keyAlert", "Session Expired");
                            i.setClassName(context, "com.singleevent.sdk.View.TokenExpireAlertReceived");
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(i);

                        } else
                            Error_Dialog.show(jObj.getString("responseString"), getActivity());
                    }

                } catch (JSONException e) {
                    e.printStackTrace();

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();

                if (error instanceof TimeoutError) {
                    Error_Dialog.show("Timeout", getActivity());
                } else if (VolleyErrorLis.isServerProblem(error)) {
                    Error_Dialog.show(VolleyErrorLis.handleServerError(error, context), getActivity());
                } else if (VolleyErrorLis.isNetworkProblem(error)) {
                    //tv_checkInternet.setVisibility(View.VISIBLE);

                    userview.clear();
                    userview = Paper.book(appDetails.getAppId()).read("OfflineAttendeeList",new ArrayList<User>());
                    if (userview.size() > 0) {
                        bookListAdapter = new AttendeeUserListAdapter(context,userview);
                        userlist.setAdapter(bookListAdapter);
                        bookListAdapter.notifyDataSetChanged();
                        //showview(true);
                    } else{}
                    //  showview(false);
                }
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("token", Paper.book().read("token", ""));
                return headers;
            }
        };
        App.getInstance().addToRequestQueue(strReq, tag_string_req);
        strReq.setRetryPolicy(new DefaultRetryPolicy(
                500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));
    }
    private void parseuser(JSONArray responseString) {


        try {
            userview.clear();
            Gson gson = new Gson();

            Random r = new Random();

            for (int i = 0; i < responseString.length(); i++) {
                String eventString = responseString.getJSONObject(i).toString();
                User obj = gson.fromJson(eventString, User.class);
                obj.setColor(Color.argb(255, r.nextInt(256), r.nextInt(256), r.nextInt(256)));

                userview.add(obj);

            }

            Collections.sort(userview, new Comparator<User>() {
                @Override
                public int compare(User o1, User o2) {
                    return o1.getFirst_name().compareToIgnoreCase(o2.getFirst_name());
                }
            });
            Paper.book(appDetails.getAppId()).write("OfflineAttendeeList",userview);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        if (userview.size() > 0) {
            // bookListAdapter.notifyDataSetChanged();
            // showview(true);
        } else{}
        // showview(false);
    }
    private void attemptSend() {
        Type type = new TypeToken<Feed>() {
        }.getType();
        String selected_post = new Gson().toJson(feed, type);

        String tmp_userid = Paper.book().read("userId", "");
        String tmp_usr = Paper.book().read("username", "");
        String tmp_profile_pic = Paper.book().read("profile_pic", "");
        String msg = ecomment.getText().toString().trim();
        Socket.emit("post_comment", appDetails.getAppId(), feed.getId(),
                tmp_userid,
                tmp_usr, tmp_profile_pic, "comment",
                System.currentTimeMillis(), msg, selected_post);
       /* Socket.emit("post_comment", appDetails.getAppId(), feed.getId(),
                fuid,
                funm, fprpic, "comment",
                System.currentTimeMillis(), msg, selected_post);
*/
       // Socket.emit("message", fuid, funm, Paper.book().read("userId", ""), Paper.book().read("username"), appDetails.getAppId(), appDetails.getAppName(), "single", System.currentTimeMillis(), msg, "message");
        //  mSocket.emit("message", user.getSender_id(), user.getSender_name(), UserID, Paper.book().read("username"), appid, appname, "single", System.currentTimeMillis(), msg, "message");
        ecomment.setText("");


    }

    //new method to add comment list in linearlayout

    private void addcommentlist(ArrayList<Comments> com_list, Context context, float dpWidth) {
        try {
            commentlistlayout.removeAllViews();
            LayoutInflater lv = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            for (int i = 0; i < com_list.size(); i++) {

                try {
                    final Comments comment = com_list.get(i);
                    View child = lv.inflate(R.layout.comments_view, null);
                    TextView username = (TextView) child.findViewById(R.id.username);
                    TextView datetime = (TextView) child.findViewById(R.id.datetime);
                    TextView postmsg = (TextView) child.findViewById(R.id.postmsg);
                    TextView posttime = (TextView) child.findViewById(R.id.posttime);
                    final ImageView profilepic = (ImageView) child.findViewById(R.id.profilepic);

                    // setting image width
                    LetterTileProvider tileProvider;
                    tileProvider = new LetterTileProvider(context);
                    final float pwidth;
                    Bitmap letterTile;
                    Random r;
                    r = new Random();

                    pwidth = dpWidth * 0.12F;
                    RelativeLayout.LayoutParams imgParams = (RelativeLayout.LayoutParams) profilepic.getLayoutParams();
                    imgParams.width = (int) pwidth;
                    imgParams.height = (int) pwidth;
                    profilepic.setLayoutParams(imgParams);


                    username.setTypeface(Util.boldtypeface(context));
                    datetime.setTypeface(Util.lighttypeface(context));
                    postmsg.setTypeface(Util.regulartypeface(context));
                    posttime.setTypeface(Util.regulartypeface(context));

                    username.setText(comment.getName());
                    String s =Util.calheader(comment.getCommentedon());
                    String[] split = s.split("at");
                    String firstSubString = split[0];
                   try{String secondSubString = split[1];
                       posttime.setText(secondSubString);
                   }catch (Exception e){}
                    datetime.setText(firstSubString);



                    // It is used to set foreground color.
                    if(comment.getCommenttxt().contains("@")) {
                        String str = comment.getCommenttxt();
                        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(str);
                        SpannableStringBuilder builder = new SpannableStringBuilder();
                        int end=0;

                        for (int i1 = 0; i1 < str.length(); i1++) {

                            if (str.charAt(i1) == '@') {

                                System.out.println("START AT" + i);
                                for (int j = i1; j < str.length(); j++) {
                                    if (str.length() == j + 1) {
                                        SpannableString redSpannable = new SpannableString(str.substring(i1, j + 1));
                                        redSpannable.setSpan(new ForegroundColorSpan(Color.parseColor("#0080ff")), 0, redSpannable.length(), 0);
                                        builder.append(redSpannable);
                                        end = 1;
                                        break;
                                    } else if (str.charAt(j) == ' ') {
                                        SpannableString redSpannable = new SpannableString(str.substring(i1, j + 1));
                                        redSpannable.setSpan(new ForegroundColorSpan(Color.parseColor("#0080ff")), 0, redSpannable.length(), 0);
                                        builder.append(redSpannable);
                                        i1 = j;

                                        System.out.println("END" + j);
                                        break;

                                    } else {

                                    }
                                }

                            } else {
                                if (end != 1) {
                                    SpannableString redSpannable = new SpannableString(str.substring(i1, i1 + 1));
                                    redSpannable.setSpan(new ForegroundColorSpan(Color.parseColor("#ffb1b2b5")), 0, redSpannable.length(), 0);
                                    builder.append(redSpannable);
                                }
                            }
                        }
                        postmsg.setText(builder);

                    } else {
                        postmsg.setText(comment.getCommenttxt());
                    }



                    // Loading the Profile_Pic

                    if (comment.getProfilepic().equalsIgnoreCase("")) {
                        int color = Color.argb(255, r.nextInt(256), r.nextInt(256), r.nextInt(256));
                        letterTile = tileProvider.getLetterTile(comment.getName(), "key", (int) pwidth, (int) pwidth, color);
                        profilepic.setImageDrawable(new Roundeddrawable(letterTile));

                    }
                    else {
                        Glide.with(context.getApplicationContext())
                                .load(comment.getProfilepic())
                                .asBitmap()
                                .placeholder(R.drawable.default_user)
                                .error(R.drawable.default_user)
                                .into(new BitmapImageViewTarget(profilepic) {
                                    @Override
                                    protected void setResource(Bitmap resource) {
                                        RoundedBitmapDrawable drawable = RoundedBitmapDrawableFactory.create(getActivity().getResources(),
                                                Bitmap.createScaledBitmap(resource, (int) pwidth, (int) pwidth, false));
                                        drawable.setCircular(true);
                                        profilepic.setImageDrawable(drawable);
                                    }
                                });
                    }

                    commentlistlayout.addView(child);
                }

                catch (Exception e) {
                }
            }


        }catch (Exception e){

        }
    }

    private void updateViewAfterAddingComment() {

    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.close) {
            getDialog().dismiss();

        }
    }

    private final TextWatcher watcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {


            if(charSequence.toString().contains("@")) {
                filter(charSequence.toString());
            }

          /*  if (ecomment.getText().toString().equals("")) {

            } else {

                setinactiveimage();

            }*/
        }

        @Override
        public void afterTextChanged(Editable editable) {
            if (editable.length() == 0) {
                userlist.setVisibility(View.GONE);
                scrollview.setVisibility(View.VISIBLE);
                setinactiveimage();
            } else {
                setactiveimage();
            }
        }
    };
    public void filter(String text) {
        List<User> temp = new ArrayList();
        String te=text.substring(text.lastIndexOf("@") + 1);

        for (User d : userview) {

            if((d.getFirst_name().toLowerCase().contains( te.toLowerCase())|| d.getLast_name().toLowerCase().contains(te.toLowerCase()))
            ){
                temp.add(d);
            }
        }
        //update recyclerview
        if(temp.size()>0) {
            scrollview.setVisibility(View.GONE);
            bookListAdapter = new AttendeeUserListAdapter(context, temp);
            userlist.setVisibility(View.VISIBLE);
            userlist.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL,
                    false));
            userlist.setAdapter(bookListAdapter);

            bookListAdapter.setOnCardClickListner(this);
        }
        bookListAdapter.updateList(temp);


        //  bookListAdapter.updateList(temp);
    }


    private void setinactiveimage() {

        Glide.with(context.getApplicationContext())
                .load(R.drawable.send_inactive)
                .asBitmap()
                .placeholder(R.drawable.send_inactive)
                .error(R.drawable.send_inactive)
                .into(new BitmapImageViewTarget(sendbutton) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        sendbutton.setImageBitmap(Util.scaleBitmap(resource, (int) Iwidth, (int) Iwidth));
                    }
                });
    }


    private void setactiveimage() {

        Glide.with(context.getApplicationContext())
                .load(R.drawable.send_comment)
                .asBitmap()
                .placeholder(R.drawable.send_comment)
                .error(R.drawable.send_comment)
                .into(new BitmapImageViewTarget(sendbutton) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        sendbutton.setImageBitmap(Util.scaleBitmap(resource, (int) Iwidth, (int) Iwidth));
                    }
                });
    }

    @Override
    public void onResume() {
        super.onResume();
    }


    private Emitter.Listener onComment = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        JSONObject response = (JSONObject) args[0];
                        if (response.getBoolean("response")) {
                            com.singleevent.sdk.model.Comments c = new com.singleevent.sdk.model.Comments((String) args[4], (String) args[5], (long) args[7], (String) args[8]);
                            feeds.add(c);
                            // adapter.notifyDataSetChanged();
                            addcommentlist(feeds, context, dpWidth);
                            tcomment.setText("Total Comments - " + feeds.size());

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_LONG).show();
                    }


                }
            });
        }
    };

    private Emitter.Listener onConfirmCheckin = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    System.out.println("Confirm Comment Checkin");

                }
            });
        }
    };

    private Emitter.Listener onNewPostComment = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    com.singleevent.sdk.model.Comments c = new com.singleevent.sdk.model.Comments((String) args[3], (String) args[4], (long) args[6], (String) args[7]);
                    feeds.add(c);
                    //adapter.notifyDataSetChanged();
                    addcommentlist(feeds, context, dpWidth);
                    tcomment.setText("Total Comments - " + feeds.size());


                }
            });
        }
    };


    private void LoadComment(int postid) {

        String tag_string_req = "Get_Feed";
        String url = ApiList.Get_Comments + appDetails.getAppId() + "&post_id=" + postid + "&action=comment";

        loading.setVisibility(View.VISIBLE);
        StringRequest strReq = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {
                loading.setVisibility(View.GONE);
                JSONObject jObj = null;
                try {
                    jObj = new JSONObject(response);
                    if (jObj.getBoolean("response")) {
                        parseresult(jObj);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                loading.setVisibility(View.GONE);
                if (error instanceof TimeoutError) {
                    Error_Dialog.show("Timeout", (AppCompatActivity) getActivity());
                } else if (VolleyErrorLis.isServerProblem(error)) {
                    Error_Dialog.show(VolleyErrorLis.handleServerError(error, getActivity()), (AppCompatActivity) getActivity());
                } else if (VolleyErrorLis.isNetworkProblem(error)) {
                    Error_Dialog.show("Please Check Your Internet Connection", (AppCompatActivity) getActivity());
                }

            }
        });


        App.getInstance().addToRequestQueue(strReq, tag_string_req);
        strReq.setRetryPolicy(new DefaultRetryPolicy(
                500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));

    }

    private void parseresult(JSONObject args) {

        feeds.clear();
        Gson gson = new Gson();

        try {
            JSONArray jaarray = args.getJSONArray("comments");
            for (int i = 0; i < jaarray.length(); i++) {
                String eventString = jaarray.getJSONObject(i).toString();
                com.singleevent.sdk.model.Comments eobj = gson.fromJson(eventString, com.singleevent.sdk.model.Comments.class);
                feeds.add(eobj);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (feeds.size() > 0) {
            //adapter.notifyDataSetChanged();
            addcommentlist(feeds, context, dpWidth);
            tcomment.setText("Total Comments - " + feeds.size());
        } else
            tcomment.setText("Comment");


    }

    @Override
    public void onStop() {
        super.onStop();
        Socket.off("post_comment_ack", onComment);
        Socket.off("confirm_post_checkin", onConfirmCheckin);
        Socket.off("new_post_comment", onNewPostComment);
    }

    private String decodeStringValue(String decodestr) {
        String result = null;

        try {
            result = new String(Base64.decode(decodestr, Base64.NO_WRAP), "UTF-16");
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }
        return result;
    }

    @Override
    public void OnItemLongClicked(View view, User user, int position) {

    }

    @Override
    public void OnItemClick(View view, User user, int position) {
        String username= user.getFirst_name();
        if(!ecomment.getText().toString().trim().equalsIgnoreCase("")) {
            String text = ecomment.getText().toString().trim();
            int x = findLastIndex(ecomment.getText().toString().trim(), '@');
            if(x!=0 && x-1!=0){
                String temp = text.substring(0, x - 1);
                ecomment.setText(temp +" "+ "@" + username);}
            else
                ecomment.setText("@" + username);
            //ecomment.getText().toString().trim().substring(0, ecomment.getText().toString().trim().indexOf("@"));

        }
        else{
            ecomment.setText("@" + username);
        }
        userlist.setVisibility(View.GONE);
        scrollview.setVisibility(View.VISIBLE);

        fuid=user.getUserid();
        funm=user.getFirst_name();
        fprpic=user.getProfile_pic();

    }
    static int findLastIndex(String str, Character x)
    {
        int index = -1;
        for (int i = 0; i < str.length(); i++)
            if (str.charAt(i) == x)
                index = i;
        return index;
    }
}




