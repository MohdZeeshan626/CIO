package com.singleevent.sdk.Left_Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import androidx.recyclerview.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.singleevent.sdk.Custom_View.Letter.LetterTileProvider;
import com.singleevent.sdk.Custom_View.Letter.Roundeddrawable;
import com.singleevent.sdk.model.AppDetails;
import com.singleevent.sdk.model.Likeinfo;
import com.singleevent.sdk.R;

import com.tubb.smrv.SwipeHorizontalMenuLayout;
import java.util.List;
import java.util.Random;

import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;

import android.webkit.URLUtil;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;

import io.paperdb.Paper;


public class Like_User_Adapter extends RecyclerView.Adapter<Like_User_Adapter.ViewHolder> {

    private static Context context1;
    private List<Likeinfo> eventUserList;
    AppDetails appDetails;
    static LetterTileProvider tileProvider;
    public String userid;
    // Provide a suitable constructor (depends on the kind of dataset)
    public Like_User_Adapter(Context context, List<Likeinfo> myDataset) {
        eventUserList = myDataset;
        Like_User_Adapter.context1 = context;
        Paper.init(context);
//        userid = DataBaseStorage.decrypt(Paper.book().read("userId",""));
        userid = Paper.book().read("userId", "");
        appDetails = Paper.book().read("Appdetails",null);
    }

    //after search update list
    public void updateList(List<Likeinfo> list){

        eventUserList = list;
        notifyDataSetChanged();
    }

    // Create new views (invoked by the layout manager)
    @Override
    public Like_User_Adapter.ViewHolder onCreateViewHolder(final ViewGroup parent,
                                                           int viewType) {
        // create a new view
        View v = LayoutInflater.from(context1)
                .inflate(R.layout.likelist, parent, false);

        return new Like_User_Adapter.ViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final Like_User_Adapter.ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        final Likeinfo user = eventUserList.get(position);

        // - replace the contents of the view with that element
        holder.setItem(user);

        //new logic
      /*  if (position==0){
            holder.alpha_header.setVisibility(View.VISIBLE);
            holder.alpha_header.setText(user.getGroup_name().substring(0,1));
        }else if (!user.getGroup_name().substring(0,1).equalsIgnoreCase(eventUserList.get(position-1).getGroup_name().substring(0,1))){
            holder.alpha_header.setVisibility(View.VISIBLE);
            holder.alpha_header.setText(user.getGroup_name().substring(0,1));
        }else {
            holder.alpha_header.setVisibility(View.GONE);

        }*/
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        if(eventUserList.size()>0)
            return eventUserList.size();
        else
            return 0;
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        Context context;
        Likeinfo user;
        TextView authorName,alpha_header;
        // Button invite;
        // TextView tvDesignation;
        ImageView profilepic;
        AppDetails appDetails;
        static Like_User_Adapter.OnCardClickListner onCardClickListner;
        SwipeHorizontalMenuLayout sml;


        ViewHolder(View v) {
            super(v);
            appDetails = Paper.book().read("Appdetails");


            //  alpha_header = (TextView)v.findViewById(R.id.alpha_header);
            authorName = (TextView) v.findViewById(R.id.book_title);

            profilepic = (ImageView) v.findViewById(R.id.profilepic);
            sml = (SwipeHorizontalMenuLayout) v.findViewById(R.id.sml);
            //  invite=(Button)v.findViewById(R.id.invite);
            // invite.setOnClickListener(this);



            v.setOnClickListener(this);
            sml.setOnClickListener(this);

        }

        void setItem(Likeinfo user) {
            tileProvider = new LetterTileProvider(context1);
            this.user = user;
            authorName.setText(user.getUsername());

            Random r;
            r = new Random();
            DisplayMetrics displayMetrics;
            int color= Color.argb(255, r.nextInt(256), r.nextInt(256), r.nextInt(256));
            final float dpWidth; displayMetrics = context1.getResources().getDisplayMetrics();
            dpWidth = displayMetrics.widthPixels * 0.15F;
            RelativeLayout.LayoutParams imgParams = (RelativeLayout.LayoutParams) profilepic.getLayoutParams();
            imgParams.width = (int) dpWidth;
            imgParams.height = (int) dpWidth;
          /*  Bitmap letterTile = tileProvider.getLetterTile(user.getName().trim(), "key", (int) dpWidth, (int) dpWidth,color);
            profilepic.setImageDrawable(new Roundeddrawable(letterTile));
*/


            if (URLUtil.isValidUrl(user.getProfile_pic()))
                Glide.with(context1.getApplicationContext())

                        .load(user.getProfile_pic())
                        .asBitmap()
                        .placeholder(R.drawable.round_user)
                        .error(R.drawable.round_user)
                        .into(new BitmapImageViewTarget(profilepic) {
                            @Override
                            protected void setResource(Bitmap resource) {
                                RoundedBitmapDrawable drawable = RoundedBitmapDrawableFactory.create(context1.getResources(),
                                        Bitmap.createScaledBitmap(resource, (int) dpWidth, (int) dpWidth, false));
                                drawable.setCircular(true);
                                profilepic.setImageDrawable(drawable);
                            }
                        });
            else{
                Bitmap letterTile = tileProvider.getLetterTile(user.getUsername().trim(), "key", (int) dpWidth, (int) dpWidth, color);
                profilepic.setImageDrawable(new Roundeddrawable(letterTile));
            }
        }

        @Override
        public void onClick(View v) {
            System.out.println("Item clicked pos " + getAdapterPosition());

            int position = getAdapterPosition();
           /* if (v.getId()==authorName.getId()){

            }*/
            onCardClickListner.OnItemClick( v, user , position );



        }

        @Override
        public boolean onLongClick(View v) {
            int position = getAdapterPosition();
            onCardClickListner.OnItemLongClicked(v, user, position);
            return true;
        }
    }

    public void setOnCardClickListner(Like_User_Adapter.OnCardClickListner onCardClickListner) {
        Like_User_Adapter.ViewHolder.onCardClickListner = onCardClickListner;
    }

    public interface OnCardClickListner {
        void OnItemLongClicked(View view, Likeinfo user, int position);
        void OnItemClick(View view, Likeinfo user, int position);
    }
}
