package com.singleevent.sdk.Left_Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;


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
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;
import com.singleevent.sdk.R;
import com.singleevent.sdk.View.LeftActivity.FeedActivity;
import com.singleevent.sdk.pojo.FeedPojo;
import com.singleevent.sdk.utils.Utils;
import com.squareup.picasso.Picasso;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.myViewHolder> {
    List<FeedPojo> feedPojo;
    Context context;
    String url = "";

    public FeedAdapter(List<FeedPojo> feedPojo, Context context) {
        this.feedPojo = feedPojo;
        this.context = context;

    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.feed_row_items, parent, false);
        return new FeedAdapter.myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
        if (feedPojo.get(position).getLink().isEmpty() && !feedPojo.get(position).getContentType().equals("link")) {
            holder.card2.setVisibility(View.GONE);
            holder.card3.setVisibility(View.GONE);
            holder.card4.setVisibility(View.GONE);
            holder.cardView.setVisibility(View.GONE);
            holder.name.setText(feedPojo.get(position).getName());
            holder.text_content.setText(feedPojo.get(position).getWrittenContent());

        } else if (feedPojo.get(position).getContentType().equals("link")) {
            holder.card2.setVisibility(View.GONE);
            holder.card3.setVisibility(View.GONE);
            holder.card4.setVisibility(View.GONE);
            holder.cardView.setVisibility(View.VISIBLE);
            holder.name.setText(feedPojo.get(position).getName());
            holder.text_content.setText(feedPojo.get(position).getWrittenContent());
            String url=holder.extractURL(feedPojo.get(position).getWrittenContent());
            Log.d("check_title", "onBindViewHolder:url value "+url);
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

        } else if (feedPojo.get(position).getContentType().equals("image")) {
            if (feedPojo.get(position).getLink().startsWith("file")) {
                holder.card4.setVisibility(View.VISIBLE);
                holder.card2.setVisibility(View.GONE);
                holder.card3.setVisibility(View.GONE);
                holder.cardView.setVisibility(View.GONE);

                holder.name.setText(feedPojo.get(position).getName());
                holder.text_content.setText(feedPojo.get(position).getWrittenContent());
                String imageUri = feedPojo.get(position).getLink();
                Uri uri = Uri.parse(imageUri);
                Log.d("image_uri_ada", "nextActivity: " + uri);
                holder.posted_image.setImageURI(uri);
            } else {
                holder.card4.setVisibility(View.VISIBLE);
                holder.card2.setVisibility(View.GONE);
                holder.card3.setVisibility(View.GONE);
                holder.cardView.setVisibility(View.GONE);
                Picasso.get().load(feedPojo.get(position).getLink()).into(holder.posted_image);
                holder.name.setText(feedPojo.get(position).getName());
                holder.text_content.setText(feedPojo.get(position).getWrittenContent());
            }

        } else if (feedPojo.get(position).getContentType().equals("video")) {
            if (feedPojo.get(position).getLink().contains("youtube.com")) {
                holder.card2.setVisibility(View.GONE);
                holder.card3.setVisibility(View.VISIBLE);
                holder.card4.setVisibility(View.GONE);
                holder.cardView.setVisibility(View.GONE);

                holder.text_content.setText(feedPojo.get(position).getWrittenContent());
                holder.name.setText(feedPojo.get(position).getName());

                holder.play_custom_card_yt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String videoURL = feedPojo.get(position).getLink();
                        url = videoURL.substring(videoURL.lastIndexOf("=") + 1);
                        holder.play_custom_card_yt.setVisibility(View.GONE);

                        ((FeedActivity) context).addLifeCycleCallBack(holder.youTubePlayerView);
                        holder.youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
                            @Override
                            public void onError(YouTubePlayer youTubePlayer, PlayerConstants.PlayerError error) {
                                super.onError(youTubePlayer, error);
                                Log.d("check785", "onTimelineChanged: " + error);
                            }

                            @Override
                            public void onPlaybackQualityChange(YouTubePlayer youTubePlayer, PlayerConstants.PlaybackQuality playbackQuality) {
                                super.onPlaybackQualityChange(youTubePlayer, playbackQuality);
                                Log.d("check785", "onTimelineChanged: " + "onPlaybackQualityChange");
                            }

                            @Override
                            public void onPlaybackRateChange(YouTubePlayer youTubePlayer, PlayerConstants.PlaybackRate playbackRate) {
                                super.onPlaybackRateChange(youTubePlayer, playbackRate);
                                Log.d("check785", "onTimelineChanged: " + "onPlaybackRateChange");
                            }

                            @Override
                            public void onReady(YouTubePlayer youTubePlayer) {
                                super.onReady(youTubePlayer);
                                youTubePlayer.loadVideo(url, 0f);
                                Log.d("check785", "onTimelineChanged: " + "onReady");
                            }

                            @Override
                            public void onStateChange(YouTubePlayer youTubePlayer, PlayerConstants.PlayerState state) {
                                super.onStateChange(youTubePlayer, state);
                                String stateCheck = state.toString();
                                Log.d("check7855", "onTimelineChanged: " + stateCheck);
                                if (stateCheck.equals("PAUSED")) {
                                    holder.play_custom_card_yt.setVisibility(View.VISIBLE);
                                }

                            }

                        });
                    }
                });
            } else {
                holder.card2.setVisibility(View.VISIBLE);
                holder.card3.setVisibility(View.GONE);
                holder.card4.setVisibility(View.GONE);
                holder.cardView.setVisibility(View.GONE);
                String videoURL = feedPojo.get(position).getLink();
                holder.name.setText(feedPojo.get(position).getName());
                holder.text_content.setText(feedPojo.get(position).getWrittenContent());
                try {

                    BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();


                    TrackSelector trackSelector = new DefaultTrackSelector(new AdaptiveTrackSelection.Factory(bandwidthMeter));


                    holder.exoPlayer = ExoPlayerFactory.newSimpleInstance(context, trackSelector);

                    Uri videouri = Uri.parse(videoURL);


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
                    // below line is used for
                    // handling our errors.
                    Log.e("TAG", "Error : " + e.toString());
                }

            }
        }

        holder.image_unfilled.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.image_unfilled.setVisibility(View.GONE);
                holder.image_filled.setVisibility(View.VISIBLE);
            }
        });
        holder.image_filled.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.image_filled.setVisibility(View.GONE);
                holder.image_unfilled.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public int getItemCount() {
        return feedPojo.size();
    }

    public void filterData(List<FeedPojo> new_list) {
        feedPojo = new_list;
        notifyDataSetChanged();
    }

    public class myViewHolder extends RecyclerView.ViewHolder {
        TextView name, text_content;
        SimpleExoPlayerView exoPlayerView;
        CardView play_custom_card, play_custom_card_yt, card1, card2, card3, card4;
        FrameLayout frame_pause;
        SimpleExoPlayer exoPlayer;
        ImageView image_filled, image_unfilled, posted_image;
        YouTubePlayerView youTubePlayerView;
        private Unbinder unbinder;
//        @BindView(R.id.img_preview)
        ImageView imageView;
//        @BindView(R.id.txt_description)
        TextView txt_description;
//        @BindView(R.id.txt_title)
        TextView txt_title;
//        @BindView(R.id.layout_preview)
        LinearLayout layout_preview;
//        @BindView(R.id.progress_bar)
        CircularProgressIndicator progress_bar;
//        @BindView(R.id.card_view)
        CardView cardView;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            unbinder = ButterKnife.bind(this, itemView);
            frame_pause = itemView.findViewById(R.id.frame_pause);


            imageView = itemView.findViewById(R.id.img_preview);
            txt_description = itemView.findViewById(R.id.txt_description);
            txt_title = itemView.findViewById(R.id.txt_title);
            layout_preview = itemView.findViewById(R.id.layout_preview);
            progress_bar = itemView.findViewById(R.id.progress_bar);
            cardView = itemView.findViewById(R.id.card_view);


            name = itemView.findViewById(R.id.name);
            text_content = itemView.findViewById(R.id.text_content);
            play_custom_card = itemView.findViewById(R.id.play_custom_card);
            card1 = itemView.findViewById(R.id.card1);
            card2 = itemView.findViewById(R.id.card2);
            card3 = itemView.findViewById(R.id.card3);
            card4 = itemView.findViewById(R.id.card4);
            cardView = itemView.findViewById(R.id.card_view);
            play_custom_card = itemView.findViewById(R.id.play_custom_card);
            play_custom_card_yt = itemView.findViewById(R.id.play_custom_card_yt);
            youTubePlayerView = itemView.findViewById(R.id.youtubePlayerView);
            exoPlayerView = itemView.findViewById(R.id.idExoPlayerVIew);
            image_filled = itemView.findViewById(R.id.image_filled);
            image_unfilled = itemView.findViewById(R.id.image_unfilled);
            posted_image = itemView.findViewById(R.id.posted_image);
            text_content.setTextIsSelectable(true);
        }

        public void setVideoPlayer(myViewHolder holder) {
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

        public String extractURL(String str) {

            // Creating an empty ArrayList
            List<String> list= new ArrayList<>();

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

            String urlString="";
            // Print all the URLs stored
            for (String url : list) {
                urlString=url;
                System.out.println(url);
            }
            return urlString;
        }


    }
}
