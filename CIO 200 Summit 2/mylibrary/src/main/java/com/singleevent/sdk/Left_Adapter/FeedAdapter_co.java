package com.singleevent.sdk.Left_Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.ProcessLifecycleOwner;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.net.Uri;
import android.text.SpannableString;
import android.text.util.Linkify;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.gson.Gson;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;
import com.singleevent.sdk.ApiList;
import com.singleevent.sdk.App;
import com.singleevent.sdk.Custom_View.Letter.LetterTileProvider;
import com.singleevent.sdk.Custom_View.Letter.Roundeddrawable;
import com.singleevent.sdk.Custom_View.Util;
import com.singleevent.sdk.Custom_View.VolleyErrorLis;

import com.singleevent.sdk.Left_Adapter.pollingadapters.PollingAnswerAdapter;
import com.singleevent.sdk.Left_Adapter.pollingadapters.PollingResultAdapter;
import com.singleevent.sdk.View.LeftActivity.Feeds;
import com.singleevent.sdk.View.LeftActivity.pollingActivities.PollingActivity;
import com.singleevent.sdk.View.LeftActivity.pollingActivities.PollingResultActivity;
import com.singleevent.sdk.View.RightActivity.group_feed.GroupFeedView;
import com.singleevent.sdk.feeds_class.OnItemClickListener;
import com.singleevent.sdk.gallery_camera.attachMent.Attachment;

import com.singleevent.sdk.model.AppDetails;
import com.singleevent.sdk.model.Feed;
import com.singleevent.sdk.model.Likeinfo;
import com.singleevent.sdk.R;
import com.singleevent.sdk.View.LeftActivity.LikeActivity;
import com.singleevent.sdk.View.LeftActivity.SharePost;
import com.singleevent.sdk.model.Polling.PollingModel;
import com.singleevent.sdk.model.Polling.VoteModel;
import com.singleevent.sdk.pojo.pollingpojo.PollingAnswersPojo;
import com.singleevent.sdk.pojo.pollingpojo.PollingQuestionsPojo;
import com.singleevent.sdk.pojo.pollingpojo.PollingResultPojo;
import com.singleevent.sdk.pojo.pollingpojo.PostVoteForPolling;
import com.singleevent.sdk.service.Health1NetworkController;
import com.singleevent.sdk.utils.Utils;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.paperdb.Paper;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;

import android.widget.VideoView;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by webMOBI on 9/13/2017.
 */

public class FeedAdapter_co extends RecyclerView.Adapter<FeedAdapter_co.MyViewHolder> implements LifecycleObserver {

    public static final String ACTION_LIKE_BUTTON_CLICKED = "action_like_button_button";
    PollingAnswerAdapter pollingAnswerAdapter;
    PollingResultAdapter pollingResultAdapter;
    List<PollingQuestionsPojo> questionsPojos = new ArrayList<>();
    private OnItemClickListener likeSelectionListener;
    ArrayList<Feed> feeds = new ArrayList<>();
    ArrayList<Likeinfo> likeinfos = new ArrayList<>();
    Context context;
    LetterTileProvider tileProvider;
    float dpWidth, Iwidth, pwidth;
    Bitmap letterTile;
    Random r;
    protected static String userType = "";
    private String userId;
    protected String intentValue;
    AppDetails appDetails;
    ProgressBar loading;
    int n;
    int count_polls = 0;
    boolean is_share, check;
    View itemView;
    List<VoteModel.Datum> voteData = new ArrayList<>();
    List<PollingModel.Poll> pollList = new ArrayList<>();

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView username, datetime, postmsg, comment;
        TextSwitcher likes;
        LinearLayout likeview, commentview, like_commet_ll, shareview;
        Attachment postimage;
        VideoView feedvideo;
        YouTubePlayerView youtube_player_view, youtube_player_view_group;
        ImageView profilepic, like;
        TextView likedetails;
        ImageView iv_more;
        CardView cardView, card3_yt, card2_exo, play_custom_card, play_custom_card_yt,card4_image;
        ImageView play_button, feedimage;
        SimpleExoPlayerView exoPlayerView;
        SimpleExoPlayer exoPlayer;
        ImageView imageView;
        TextView txt_description;
        TextView txt_title;
        LinearLayout layout_preview;
        CircularProgressIndicator progress_bar;
        Button poll;
        RecyclerView answer_recycler, result_recycler;

        public MyViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.img_preview);
            txt_description = itemView.findViewById(R.id.txt_description);
            txt_title = itemView.findViewById(R.id.txt_title);
            cardView = itemView.findViewById(R.id.card_view);
            card3_yt = itemView.findViewById(R.id.card3);
            card2_exo = itemView.findViewById(R.id.card2);
            play_custom_card = itemView.findViewById(R.id.play_custom_card);
            play_custom_card_yt = itemView.findViewById(R.id.play_custom_card_yt);
            card4_image = itemView.findViewById(R.id.card4);
            poll = itemView.findViewById(R.id.poll);
            answer_recycler = itemView.findViewById(R.id.answer_recycler);
            result_recycler = itemView.findViewById(R.id.result_recycler);
            layout_preview = itemView.findViewById(R.id.layout_preview);
            progress_bar = itemView.findViewById(R.id.progress_bar);
            username = (TextView) itemView.findViewById(R.id.username);
            datetime = (TextView) itemView.findViewById(R.id.datetime);
            postmsg = (TextView) itemView.findViewById(R.id.postmsg);
            likes = (TextSwitcher) itemView.findViewById(R.id.likescount);
            likedetails = (TextView) itemView.findViewById(R.id.likedetails);
            comment = (TextView) itemView.findViewById(R.id.commentscount);
            profilepic = (ImageView) itemView.findViewById(R.id.profilepic);
            postimage = (Attachment) itemView.findViewById(R.id.postimage);
            feedvideo = (VideoView) itemView.findViewById(R.id.feedposting);
            feedimage = (ImageView) itemView.findViewById(R.id.feedimage);
            exoPlayerView = (SimpleExoPlayerView) itemView.findViewById(R.id.idExoPlayerVIew);

            play_button = (ImageView) itemView.findViewById(R.id.play_button);
            youtube_player_view = (YouTubePlayerView) itemView.findViewById(R.id.youtube_player_view);
            youtube_player_view_group = (YouTubePlayerView) itemView.findViewById(R.id.youtube_player_view);

            like = (ImageView) itemView.findViewById(R.id.likes);
            iv_more = (ImageView) itemView.findViewById(R.id.iv_more);
            likeview = (LinearLayout) itemView.findViewById(R.id.likeview);
            commentview = (LinearLayout) itemView.findViewById(R.id.commentview);
            if (!is_share) {
                shareview = (LinearLayout) itemView.findViewById(R.id.shareview);
            }

            like_commet_ll = (LinearLayout) itemView.findViewById(R.id.like_comment_layout);

            answer_recycler.setLayoutManager(new LinearLayoutManager(itemView.getContext()));
            result_recycler.setLayoutManager(new LinearLayoutManager(itemView.getContext()));

        }


        public String extractURL(String str) {

            // Creating an empty ArrayList
            List<String> list = new ArrayList<>();

            // Regular Expression to extract
            // URL from the string
            String regex
                    = "\\b((?:https?|ftp|file):"
                    + "//[-a-zA-Z0-9+&@#/%?="
                    + "~_|!:, .;]*[-a-zA-Z0-9+"
                    + "&@#/%=~_|])";

            // Compile the Regular Expression
            Pattern p = Pattern.compile(
                    regex,
                    Pattern.CASE_INSENSITIVE);

            // Find the match between string
            // and the regular expression
            Matcher m = p.matcher(str);

            // Find the next subsequence of
            // the input subsequence that
            // find the pattern
            while (m.find()) {

                // Find the substring from the
                // first index of match result
                // to the last index of match
                // result and add in the list
                list.add(str.substring(
                        m.start(0), m.end(0)));
            }

            // IF there no URL present
            if (list.size() == 0) {
                System.out.println("-1");
                return "";
            }

            String urlString = "";
            // Print all the URLs stored
            for (String url : list) {
                urlString = url;
                System.out.println(url);
            }
            return urlString;
        }

        private String getPollId(String value) {
            Pattern pattern = Pattern.compile("\\{([^}]+)\\}");
            Matcher m = pattern.matcher(value);
            String poll_id = "";
            while (m.find()) {
                System.out.println(m.group(1));
                poll_id = m.group(1);
            }
            return poll_id;
        }

        private void shareTextUrl(String url, String title) {
            Intent share = new Intent(android.content.Intent.ACTION_SEND);
            share.setType("text/plain");
            share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);

            // Add data to the intent, the receiving app will decide
            // what to do with it.
            share.putExtra(Intent.EXTRA_SUBJECT, title);
            if (title.length() > 0) {
                share.putExtra(Intent.EXTRA_TEXT, title + "\n\n" + url);
            } else {
                share.putExtra(Intent.EXTRA_TEXT, url);
            }
            itemView.getContext().startActivity(Intent.createChooser(share, "Share!"));
        }

        public void ApiToVote(String vote, String userId, String pollId, int position) {
            clearSharedPref();
            PostVoteForPolling voteForPolling = new PostVoteForPolling(userId, pollId, vote);
            Health1NetworkController.getInstance().getService().vote(voteForPolling).enqueue(new Callback<VoteModel>() {
                @Override
                public void onResponse(Call<VoteModel> call, retrofit2.Response<VoteModel> response) {
                    if (response.code() == 400) {
                        Toast.makeText(context, "You have chosen option which is not present in the list.", Toast.LENGTH_SHORT).show();
                    }
                    if (response.isSuccessful()) {
                        VoteModel voteModel = response.body();
                        if (voteModel.getResponse()) {

                            if (context instanceof Feeds) {
                                ((Feeds) context).recreate();
                            }
                            if (context instanceof GroupFeedView) {
                                ((GroupFeedView) context).recreate();
                            }
                        } else {
                            clearSharedPref();
                        }
                    } else {
                        clearSharedPref();
                    }
                }

                @Override
                public void onFailure(Call<VoteModel> call, Throwable t) {
                    clearSharedPref();
                    t.getStackTrace();
                    Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        }

        private void clearSharedPref() {
            SharedPreferences sharedPreferences = context.getSharedPreferences("poll", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.apply();
        }


        public void setVideoPlayer(MyViewHolder holder) {
            holder.exoPlayer.addListener(new ExoPlayer.EventListener() {
                @Override
                public void onTimelineChanged(Timeline timeline, Object manifest) {
                    Log.d("check78", "onTimelineChanged: " + "onTimelineChanged");
                }

                @Override
                public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
                    Log.d("check78", "onTimelineChanged: " + "onTracksChanged");

                }

                @Override
                public void onLoadingChanged(boolean isLoading) {
                    Log.d("check78", "onTimelineChanged: " + "onLoadingChanged");

                }

                @Override
                public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                    Log.d("check78", "onTimelineChanged: " + "onPlayerStateChanged" + playWhenReady);
                    if (!playWhenReady) {
                        holder.play_custom_card.setVisibility(View.VISIBLE);
                    }
                    holder.exoPlayerView.setUseController(playWhenReady);

                }

                @Override
                public void onPlayerError(ExoPlaybackException error) {
                    Log.d("check78", "onTimelineChanged: " + "onPlayerError");

                }

                @Override
                public void onPositionDiscontinuity() {
                    Log.d("check78", "onTimelineChanged: " + "onPositionDiscontinuity");

                }

                @Override
                public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {
                    Log.d("check78", "onTimelineChanged: " + "onPlaybackParametersChanged");

                }
            });

        }
    }

    private void LoadComment(int postid, int apos) {
        int pos = apos;
        String tag_string_req = "Get_Feed";
        String url = ApiList.Get_Comments + appDetails.getAppId() + "&post_id=" + postid + "&action=like";

        //  loading.setVisibility(View.VISIBLE);
        StringRequest strReq = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {
                // loading.setVisibility(View.GONE);
                JSONObject jObj = null;
                try {
                    jObj = new JSONObject(response);
                    if (jObj.getBoolean("response")) {
                        parseresult(jObj, postid, apos);
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
                    //  Error_Dialog.show("Timeout", );
                } else if (VolleyErrorLis.isServerProblem(error)) {
                    //  Error_Dialog.show(VolleyErrorLis.handleServerError(error, LikeActivity.this), LikeActivity.this);
                } else if (VolleyErrorLis.isNetworkProblem(error)) {
                    //   Error_Dialog.show("Please Check Your Internet Connection", );
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


    private void parseresult(JSONObject args, int id, int pos) {

        likeinfos.clear();
        int adapterpos = pos;
        int pid = id;
        Gson gson = new Gson();

        try {
            JSONArray jaarray = args.getJSONArray("likes");
            for (int i = 0; i < jaarray.length(); i++) {
                String eventString = jaarray.getJSONObject(i).toString();
                Likeinfo eobj = gson.fromJson(eventString, Likeinfo.class);
                likeinfos.add(eobj);
            }
            Paper.book().write("LikeUser" + pid, likeinfos);
            if (likeinfos.size() > 0) {
                try {
                    n = feeds.get(adapterpos).getLikesCount();
                } catch (Exception e) {
                }

                if (n > 0) {

                    try {


                        Intent i = new Intent(context, LikeActivity.class);
                        i.putExtra("totallikes", n);
                        i.putExtra("IDs", pid);

                        context.startActivity(i);
                    } catch (Exception e) {

                    }

                }


            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (likeinfos.size() > 0) {
            //adapter.notifyDataSetChanged();
            // addcommentlist(feeds, context, dpWidth);
            //   tcomment.setText("Total Comments - " + feeds.size());
        } //else
        //   tcomment.setText("Comment");


    }

    public FeedAdapter_co(ArrayList<Feed> feeds, Context context, float dpWidth, OnItemClickListener likeSelectionListener, String intentValue, AppDetails appDetails) {
        this.appDetails = appDetails;
        this.intentValue = intentValue;
        this.feeds = feeds;
        this.context = context;
        this.likeSelectionListener = likeSelectionListener;
        tileProvider = new LetterTileProvider(context);
        this.dpWidth = dpWidth;
        r = new Random();
        String s[] = new String[50];
        s = appDetails.getDisable_items();
        if (s.length > 0 && s != null) {
            for (int i = 0; i < s.length; i++) {
                if (s[i].equalsIgnoreCase("activityfeedshare")) {
                    is_share = true;

                }

            }
        }
        Paper.init(context);
        try {
            userId = Paper.book().read("userId", "");
        } catch (Exception e) {

        }
        if (Paper.book(appDetails.getAppId()).read("admin_flag", "").equals("admin")) {
            userType = "admin";
        } else {
            userType = "none";
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        try {
            if (!is_share) {
                itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.feeds_item, parent, false);
            } else {
                itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.feeds_item1, parent, false);

            }

            ImageView profilepic = (ImageView) itemView.findViewById(R.id.profilepic);

            // setting image width
            pwidth = dpWidth * 0.12F;
            ConstraintLayout.LayoutParams imgParams = (ConstraintLayout.LayoutParams) profilepic.getLayoutParams();
            imgParams.width = (int) pwidth;
            imgParams.height = (int) pwidth;
            profilepic.setLayoutParams(imgParams);
        } catch (Exception e) {
        }
        return new FeedAdapter_co.MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        holder.setIsRecyclable(false);
        try {

            final Feed feed = feeds.get(position);
            holder.username.setTypeface(Util.boldtypeface(context));
            holder.datetime.setTypeface(Util.lighttypeface(context));
            holder.postmsg.setTypeface(Util.regulartypeface(context));
            holder.comment.setTypeface(Util.regulartypeface(context));


            holder.username.setText(feed.getName());
            holder.datetime.setText(Util.calheader(Long.parseLong(feed.getTimeStamp())));
            holder.postmsg.setText(feed.getSubject());
            Linkify.addLinks(holder.postmsg, Linkify.WEB_URLS);
            holder.comment.setText(feed.getCommented() > 0 ? feed.getCommented() + " Comments" : "Comment");


            SpannableString s = new SpannableString(feed.getLikesCount() > 0 ? context.getResources().getQuantityString(R.plurals.likes_count, feed.getLikesCount(), feed.getLikesCount()) : "Like");

            holder.likes.setText(Util.applyFontToMenuItem(context, s));

            holder.like.setImageResource(feed.getLike_status() > 0 ? R.drawable.s_ic_heart : R.drawable.s_ic_heart_wofilled);


            // Loading the Profile_Pic


            if (feed.getProfilePic().equalsIgnoreCase("")) {
                int color = Color.argb(255, r.nextInt(256), r.nextInt(256), r.nextInt(256));
                letterTile = tileProvider.getLetterTile(feed.getName(), "key", (int) pwidth, (int) pwidth, color);
                holder.profilepic.setImageDrawable(new Roundeddrawable(letterTile));

            } else {

                Glide.with(context.getApplicationContext())

                        .load(feed.getProfilePic())
                        .asBitmap()
                        .placeholder(R.drawable.default_user)
                        .error(R.drawable.default_user)
                        .into(new BitmapImageViewTarget(holder.profilepic) {
                            @Override
                            protected void setResource(Bitmap resource) {
                                RoundedBitmapDrawable drawable = RoundedBitmapDrawableFactory.create(context.getResources(),
                                        Bitmap.createScaledBitmap(resource, (int) pwidth, (int) pwidth, false));
                                drawable.setCircular(true);
                                holder.profilepic.setImageDrawable(drawable);
                            }
                        });
            }

            //loading url preview
            if (feed.getSubject().contains("https://")) {
                holder.cardView.setVisibility(View.VISIBLE);
                String url = holder.extractURL(feed.getSubject());
                Utils.getjsoupcontent(url).subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread()).subscribe(result -> {
                            if (result != null) {

                                if (result.title() == null) {
                                    holder.txt_title.setText(result.location());
                                }
                                holder.txt_title.setText(result.title());
                                Elements metaTags = result.getElementsByTag("meta");
                                for (Element element : metaTags) {
                                    if (element.attr("property").equals("og:image")) {
                                        Picasso.get().load(element.attr("content"))
                                                .into(holder.imageView);
                                    } else if (element.attr("name").equals("description")) {
                                        String description = element.attr("content");
                                        Log.d("check_title", "onBindViewHolder: description " + description);
                                        holder.txt_description.setText(element.attr("content"));

                                    } else if (element.attr("property").equals("org:url")) {
                                        holder.cardView.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                String browserUrl = element.attr("content");
                                                Intent i = new Intent(Intent.ACTION_VIEW);
                                                i.setData(Uri.parse(browserUrl));
                                                holder.itemView.getContext().startActivity(i);
                                            }
                                        });
                                    }
                                    holder.progress_bar.setVisibility(View.GONE);
                                    holder.layout_preview.setVisibility(View.VISIBLE);
                                }
                            } else {
                                Toast.makeText(context, "results is null", Toast.LENGTH_SHORT).show();
                                holder.cardView.setVisibility(View.GONE);

                            }

                        },
                        error -> {
                            holder.cardView.setVisibility(View.GONE);
//                        Toast.makeText(context, "no preView Available", Toast.LENGTH_SHORT).show();
                        });

            } else {
                holder.cardView.setVisibility(View.GONE);
            }
            //loading polling
            if (feeds.get(position).getTitle().startsWith("polling{") && feeds.get(position).getTitle().endsWith("}")) {

                if (!feeds.get(position).isAnswerdPoll()) {
                    if (feeds.get(position).getPoll_answersPojoList() != null && feeds.get(position).getPoll_answersPojoList().size() > 0) {
                        holder.poll.setVisibility(View.VISIBLE);
                        holder.answer_recycler.setVisibility(View.VISIBLE);
                        pollingAnswerAdapter = new PollingAnswerAdapter(context, feeds.get(position).getPoll_answersPojoList());
                        holder.answer_recycler.setAdapter(pollingAnswerAdapter);
                    }
                } else {
                    holder.result_recycler.setVisibility(View.VISIBLE);
                    holder.poll.setVisibility(View.GONE);
                    holder.answer_recycler.setVisibility(View.GONE);
                    pollingResultAdapter = new PollingResultAdapter(context, feeds.get(position).getPoll_resultPojos());
                    holder.result_recycler.setAdapter(pollingResultAdapter);
                }

                holder.poll.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SharedPreferences sharedPreferences = context.getSharedPreferences("poll", MODE_PRIVATE);
                        String vote = sharedPreferences.getString("vote", "");
                        if (!vote.isEmpty()) {
                            holder.ApiToVote(vote, userId, holder.getPollId(feeds.get(position).getTitle()), position);
                        } else {
                            Toast.makeText(context, "select any one to poll", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }


            // Loading the Post_Image
            holder.card4_image.setVisibility(feed.attachmentlength() > 0 ? View.VISIBLE : View.GONE);
            holder.postmsg.setVisibility(feed.getSubject().equalsIgnoreCase("") ? View.GONE : View.VISIBLE);

            if (feed.attachmentlength() <= 0) {
                if ((feed.getSubject().startsWith("https://")) && feed.getSubject().contains("youtu") || feed.getSubject().contains("https://youtube")) {
                    holder.postmsg.setVisibility(View.GONE);
                    holder.card4_image.setVisibility(View.GONE);
                    holder.feedvideo.setVisibility(View.GONE);
                    holder.cardView.setVisibility(View.GONE);
                    holder.card2_exo.setVisibility(View.GONE);
                    holder.card3_yt.setVisibility(View.VISIBLE);
                    String result = feed.getSubject();
                    String videoId = extractYTId(result);
                    Log.d("check_id_if_not_null", "onError: " + " Id " + videoId+ "  "+result);

                    ProcessLifecycleOwner.get().getLifecycle().addObserver(holder.youtube_player_view);
                    try {
                        holder.youtube_player_view.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
                            @Override
                            public void onError(@NotNull YouTubePlayer youTubePlayer, PlayerConstants.@NotNull PlayerError error) {
                                super.onError(youTubePlayer, error);
                                Log.d("youtbe_error", "onError: " + " Id " + videoId + error.toString());
                            }

                            @Override
                            public void onVideoId(@NotNull YouTubePlayer youTubePlayer, @NotNull String videoId1) {
                                super.onVideoId(youTubePlayer, videoId1);
                                Log.d("youtbe_error", "videoId1: " + videoId + " Id " + videoId);
                            }

                            @Override
                            public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                                try {
                                    youTubePlayer.cueVideo(videoId, 0);
                                    Log.d("youtbe_error", "onReady: " + "loading" + " Id " + videoId);
                                    youTubePlayer.pause();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                            @Override
                            public void onStateChange(@NotNull YouTubePlayer youTubePlayer, PlayerConstants.@NotNull PlayerState state) {
                                super.onStateChange(youTubePlayer, state);
                                String stateCheck = state.toString();
                                Log.d("check7855", "onTimelineChanged: " + stateCheck);
                                if (stateCheck.equals("PAUSED")) {
                                    holder.play_custom_card_yt.setVisibility(View.VISIBLE);
                                }if(stateCheck.equals("PLAYING")){
                                    holder.play_custom_card_yt.setVisibility(View.GONE);
                                }if(stateCheck.equals("VIDEO_CUED")){
                                    holder.play_custom_card_yt.setVisibility(View.VISIBLE);

                                }
                            }
                        });

                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                }
                // loading vimeo
                else if (feed.getSubject().contains(".mp4") && feed.getSubject().startsWith("https://") && feed.getSubject().contains("progressive.akamaized.net") && feed.getSubject().length() > 20) {
                    holder.cardView.setVisibility(View.GONE);
                    holder.feedvideo.setVisibility(View.GONE);
                    holder.card4_image.setVisibility(View.GONE);
                    holder.card2_exo.setVisibility(View.VISIBLE);
                    holder.card3_yt.setVisibility(View.GONE);
                    String v_url = feed.getSubject();
                    holder.postmsg.setVisibility(View.GONE);

                    try {
                        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();


                        TrackSelector trackSelector = new DefaultTrackSelector(new AdaptiveTrackSelection.Factory(bandwidthMeter));


                        holder.exoPlayer = ExoPlayerFactory.newSimpleInstance(context, trackSelector);

                        Uri videouri = Uri.parse(v_url);


                        DefaultHttpDataSourceFactory dataSourceFactory = new DefaultHttpDataSourceFactory("exoplayer_video");


                        ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();


                        MediaSource mediaSource = new ExtractorMediaSource(videouri, dataSourceFactory, extractorsFactory, null, null);


                        holder.exoPlayerView.setPlayer(holder.exoPlayer);


                        holder.exoPlayer.prepare(mediaSource);


                        holder.play_custom_card.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                holder.play_custom_card.setVisibility(View.GONE);
                                holder.exoPlayer.setPlayWhenReady(true);
                                holder.exoPlayerView.setUseController(true);
                                holder.setVideoPlayer(holder);
                            }
                        });

                    } catch (Exception e) {
                        Log.e("TAG", "Error : " + e.toString());
                    }

                } else {
                    holder.card4_image.setVisibility(View.GONE);
                    holder.feedvideo.setVisibility(View.GONE);
                    holder.card3_yt.setVisibility(View.GONE);
                    holder.card2_exo.setVisibility(View.GONE);
                }


            } else {
                if (feed.getAttachments().get(0).getType().equalsIgnoreCase("video/mp4")) {
                    holder.card4_image.setVisibility(View.GONE);

                    holder.card3_yt.setVisibility(View.GONE);

                    holder.card2_exo.setVisibility(View.VISIBLE);

                    String result = feed.getAttachments().get(0).getPath().substring(0, feed.getAttachments().get(0).getPath().indexOf("?"));

                    try {

                        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();

                        TrackSelector trackSelector = new DefaultTrackSelector(new AdaptiveTrackSelection.Factory(bandwidthMeter));


                        holder.exoPlayer = ExoPlayerFactory.newSimpleInstance(context, trackSelector);

                        Uri videouri = Uri.parse(result);


                        DefaultHttpDataSourceFactory dataSourceFactory = new DefaultHttpDataSourceFactory("exoplayer_video");


                        ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();


                        MediaSource mediaSource = new ExtractorMediaSource(videouri, dataSourceFactory, extractorsFactory, null, null);


                        holder.exoPlayerView.setPlayer(holder.exoPlayer);


                        holder.exoPlayer.prepare(mediaSource);


                        holder.play_custom_card.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                holder.play_custom_card.setVisibility(View.GONE);
                                holder.exoPlayer.setPlayWhenReady(true);
                                holder.exoPlayerView.setUseController(true);
                                holder.setVideoPlayer(holder);
                            }
                        });

                    } catch (Exception e) {

                        Log.e("TAG", "Error : " + e.toString());
                    }

                } else {
                    holder.feedvideo.setVisibility(View.GONE);
                    holder.card3_yt.setVisibility(View.GONE);
                    holder.card2_exo.setVisibility(View.GONE);
                    holder.postimage.setItemList(feed.getAttachments());
                }
            }

            if (intentValue.equals("Reported_Feeds"))
                holder.like_commet_ll.setVisibility(View.GONE);
            else
                holder.like_commet_ll.setVisibility(View.VISIBLE);

            holder.like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int adapterPosition = holder.getAdapterPosition();

                    if (feeds.get(adapterPosition).getLike_status() == 1) {
                        feeds.get(adapterPosition).setLikes(feeds.get(adapterPosition).getLikesCount() - 1);
                        feeds.get(adapterPosition).setLike_status(0);
                        likeSelectionListener.onLike(feeds.get(adapterPosition), 0, adapterPosition);
                    } else {
                        feeds.get(adapterPosition).setLikes(feeds.get(adapterPosition).getLikesCount() + 1);
                        feeds.get(adapterPosition).setLike_status(1);
                        likeSelectionListener.onLike(feeds.get(adapterPosition), 1, adapterPosition);
                    }
                }
            });
            // to get like info
            holder.likes.setOnClickListener(new View.OnClickListener() {
                @Override

                public void onClick(View v) {
                    int adapterPosition = holder.getAdapterPosition();
                    int id = feeds.get(adapterPosition).getId();
                    LoadComment(id, adapterPosition);


                }
            });
            holder.exoPlayerView.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
                @Override
                public void onViewAttachedToWindow(View v) {

                }

                @Override
                public void onViewDetachedFromWindow(View v) {
                    try {

                        holder.exoPlayer.stop();
                    } catch (Exception e) {

                    }


                }
            });




            /*more option*/


            holder.iv_more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    Context wrapper = new ContextThemeWrapper(holder.iv_more.getContext(), R.style.PopupStyle);
                    PopupMenu popup = new PopupMenu(wrapper, v);

                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            int i = item.getItemId();
                            if (i == R.id.edit) {
                                Toast.makeText(v.getContext(), " Edit ", Toast.LENGTH_LONG).show();
                                return true;
                            } else if (i == R.id.delete) {
                                likeSelectionListener.onDeletePost(feed.getId());
                                return true;
                            } else if (i == R.id.report) {
                                likeSelectionListener.onReport(feed, feed.getId());
                                return true;
                            } else if (i == R.id.block) {
                                likeSelectionListener.onBlockUser(feed, feed.getId());
                                return true;
                            } else if (i == R.id.remove) {
                                likeSelectionListener.onDeletePost(feed.getId());
                                return true;
                            } else if (i == R.id.recover) {
                                likeSelectionListener.onRecoverUser(feed, feed.getId());
                                return true;
                            } else {
                                return false;
                            }
                            //return false;
                        }
                    });


                    if (intentValue.equals("Admin_Panel")) {

                        if (userType.equals("admin")) {

                            popup.inflate(R.menu.admin_feed);
                            popup.show();
                        }


                    } else if (intentValue.equals("Reported_Feeds")) {
                        if (userType.equals("admin")) {

                            popup.inflate(R.menu.admin_feed1);
                            popup.show();
                        }


                    } else {
                        if (userId.equals(feed.getUserid())) {
                            popup.inflate(R.menu.feed_menu);
                        } else {
                            popup.inflate(R.menu.report_feed);
                        }
                        popup.show();
                    }

                }
            });


            holder.commentview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    likeSelectionListener.onComment(feed, feed.getId());


                }
            });
            if (!is_share) {
                holder.shareview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        //  likeSelectionListener.onComment(feed, feed.getId());
                        int id = feed.getId();
                        String url = "";
                        String title = "";
                        Intent i = new Intent(context, SharePost.class);
                        if (feed.attachmentlength() > 0) {
                            url = feed.getAttachments().get(0).getPath();
                            title = feed.getSubject();
                            holder.shareTextUrl(url, title);
//                            i.putExtra("url", feed.getAttachments().get(0).getPath());
                        } else {
                            i.putExtra("url", "");
                            title = feed.getSubject();
                            holder.shareTextUrl(title, "");
                        }
//                        i.putExtra("title", feed.getSubject());
//                        holder.itemView.getContext().startActivity(i);

//                context.startActivity(i);


                    }
                });
            }

            holder.profilepic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    likeSelectionListener.onComment(feed, feed.getId());
                }
            });
            holder.username.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    likeSelectionListener.onComment(feed, feed.getId());
                }
            });

            holder.postimage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    likeSelectionListener.onComment(feed, feed.getId());
                }
            });
            holder.itemView.setTag(feed);
        } catch (Exception e) {
        }
    }

    public static String extractYTId(String ytUrl) {
        String vId = null;
//        Pattern pattern = Pattern.compile(
//                "^https?://.*(?:youtu.be/|v/|u/\\w/|embed/|watch?v=)([^#&?]*).*$",
//                Pattern.CASE_INSENSITIVE);

        String regex="http(?:s)?:\\/\\/(?:m.)?(?:www\\.)?youtu(?:\\.be\\/|be\\.com\\/(?:watch\\?(?:feature=youtu.be\\&)?v=|v\\/|embed\\/|user\\/(?:[\\w#]+\\/)+))([^&#?\\n]+)";
        //http(?:s)?:\/\/(?:m.)?(?:www\.)?youtu(?:\.be\/|be\.com\/(?:watch\?(?:feature=youtu.be\&)?v=|v\/|embed\/|user\/(?:[\w#]+\/)+))([^&#?\n]+)
        Pattern pattern = Pattern.compile(
                regex,
                Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(ytUrl);
        if (matcher.matches()) {
            vId = matcher.group(1);
        }
        return vId;
    }

    @Override
    public int getItemCount() {
        return feeds.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
    ///
    ////for prepare video
}
