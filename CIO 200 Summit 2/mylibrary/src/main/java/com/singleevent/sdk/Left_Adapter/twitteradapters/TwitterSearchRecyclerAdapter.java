package com.singleevent.sdk.Left_Adapter.twitteradapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.singleevent.sdk.R;
import com.singleevent.sdk.pojo.twitterpojos.TwitterAdapterModelClass;
import com.squareup.picasso.Picasso;


import java.util.ArrayList;
import java.util.List;

public class TwitterSearchRecyclerAdapter extends RecyclerView.Adapter<TwitterSearchRecyclerAdapter.TwitterSearchViewHolder> {

    private LayoutInflater layoutInflater;
    private Context context;
    private List<TwitterAdapterModelClass> twitterAdapterModelClassArrayList = new ArrayList<>();
    private Twitter_func twitter_func;

    public TwitterSearchRecyclerAdapter(Context context, List<TwitterAdapterModelClass> twitterAdapterModelClasses , Twitter_func twitter_func) {
        this.layoutInflater = LayoutInflater.from(context);
        this.twitterAdapterModelClassArrayList = twitterAdapterModelClasses;
        this.context = context;
        this.twitter_func = twitter_func;
    }

    @NonNull
    @Override
    public TwitterSearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.twitter_search_recycler_view, parent, false);
        return new TwitterSearchViewHolder(view , twitter_func);
    }

    @Override
    public void onBindViewHolder(@NonNull TwitterSearchRecyclerAdapter.TwitterSearchViewHolder holder, int position) {

        holder.tweet.setText(twitterAdapterModelClassArrayList.get(position).getTweet());
        holder.person_name.setText(twitterAdapterModelClassArrayList.get(position).getName());
        holder.person_twitter_id.setText(twitterAdapterModelClassArrayList.get(position).getUsername());
        holder.time.setText(twitterAdapterModelClassArrayList.get(position).getTime());
        holder.twitter_like_count.setText(String.valueOf(twitterAdapterModelClassArrayList.get(position).getLikes_count()));
        holder.twitter_retweet_count.setText(String.valueOf(twitterAdapterModelClassArrayList.get(position).getRetweet_count()));

        if (!twitterAdapterModelClassArrayList.get(position).getProfile_pic_url().equalsIgnoreCase("")) {
            Picasso.get().load(twitterAdapterModelClassArrayList.get(position).getProfile_pic_url()).into(holder.person_photo);
//            Glide.with(context)
//                    .load(twitterAdapterModelClassArrayList.get(position).getProfile_pic_url())
//                    .into(holder.person_photo);
        }

    }

    public void setItems( List<TwitterAdapterModelClass> twitterAdapterModelClassArrayList)
    {
        this.twitterAdapterModelClassArrayList = twitterAdapterModelClassArrayList;
    }

    @Override
    public int getItemCount() {
        return twitterAdapterModelClassArrayList.size();
    }

    public class TwitterSearchViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView person_photo , share , like , retweet;
        private TextView person_name,person_twitter_id , time , tweet , twitter_retweet_count , twitter_like_count;

        public TwitterSearchViewHolder(@NonNull View itemView , Twitter_func twitter_func) {
            super(itemView);
            person_name = itemView.findViewById(R.id.name_person);
            person_twitter_id = itemView.findViewById(R.id.twitter_id_person);
            time = itemView.findViewById(R.id.time);
            tweet = itemView.findViewById(R.id.tweet);
            twitter_retweet_count = itemView.findViewById(R.id.twitter_retweet_count);
            twitter_like_count = itemView.findViewById(R.id.twitter_like_count);
            person_photo = itemView.findViewById(R.id.photo_person);
            share = itemView.findViewById(R.id.twitter_share_image);
            share.setOnClickListener(this::onClick);
            like = itemView.findViewById(R.id.twitter_like_image);
            like.setOnClickListener(this::onClick);
            retweet = itemView.findViewById(R.id.twitter_retweet_image);
            retweet.setOnClickListener(this::onClick);
        }

        @Override
        public void onClick(View v) {
            int id = v.getId();
            if (id == R.id.twitter_like_image) {
                twitter_func.like_on_a_tweet(getAdapterPosition());
            } else if (id == R.id.twitter_retweet_image) {
                twitter_func.retweet_on_a_tweet(getAdapterPosition());
            } else if (id == R.id.twitter_share_image) {
                twitter_func.share_a_tweet(getAdapterPosition());
            }
        }
    }

    public interface Twitter_func
    {
        void like_on_a_tweet ( int a );

        void retweet_on_a_tweet ( int a );

        void share_a_tweet ( int a );
    }

}
