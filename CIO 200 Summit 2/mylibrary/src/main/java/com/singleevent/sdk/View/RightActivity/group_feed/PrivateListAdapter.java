package com.singleevent.sdk.View.RightActivity.group_feed;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.recyclerview.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.makeramen.roundedimageview.RoundedImageView;
import com.singleevent.sdk.Custom_View.Letter.LetterTileProvider;
import com.singleevent.sdk.Custom_View.Letter.Roundeddrawable;
import com.singleevent.sdk.Custom_View.Util;
import com.singleevent.sdk.model.AppDetails;
import com.singleevent.sdk.model.User;
import com.singleevent.sdk.R;
import com.tubb.smrv.SwipeHorizontalMenuLayout;

import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;

public class PrivateListAdapter extends RecyclerView.Adapter<PrivateListAdapter.ViewHolder> {

    private static Context context1;
    Context temp;
    private List<User> eventUserList;
    AppDetails appDetails;
    static LetterTileProvider tileProvider;
    public String userid;
    public static ArrayList<String> tempuid=new ArrayList<>();

    // Provide a suitable constructor (depends on the kind of dataset)
    public PrivateListAdapter(Context context, List<User> myDataset) {

        eventUserList = myDataset;
        PrivateListAdapter.context1 = context;
        Paper.init(context);
//        userid = DataBaseStorage.decrypt(Paper.book().read("userId",""));
        userid = Paper.book().read("userId", "");
        appDetails = Paper.book().read("Appdetails",null);
    }

    //after search update list
    public void updateList(List<User> list){

        eventUserList = list;
        notifyDataSetChanged();
    }

    // Create new views (invoked by the layout manager)
    @Override
    public PrivateListAdapter.ViewHolder onCreateViewHolder(final ViewGroup parent,
                                                                 int viewType) {
        // create a new view
        View v = LayoutInflater.from(context1)
                .inflate(R.layout.private_group_attendee, parent, false);

        return new PrivateListAdapter.ViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final PrivateListAdapter.ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        final User user = eventUserList.get(position);

        // - replace the contents of the view with that element
        holder.setItem(user);

        //new logic
        if (position==0){
            holder.alpha_header.setVisibility(View.GONE);
            holder.alpha_header.setText(user.getFirst_name().substring(0,1));
        }else if (!user.getFirst_name().substring(0,1).equalsIgnoreCase(eventUserList.get(position-1).getFirst_name().substring(0,1))){
            holder.alpha_header.setVisibility(View.GONE);
            holder.alpha_header.setText(user.getFirst_name().substring(0,1));
        }else {
            holder.alpha_header.setVisibility(View.GONE);

        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return eventUserList.size();
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        Context context;
        User user;
        TextView authorName,alpha_header,city;
        TextView tvDesignation;
        RoundedImageView profilepic;
        AppDetails appDetails;
        Button invite;


        static PrivateListAdapter.OnCardClickListner onCardClickListner;
        SwipeHorizontalMenuLayout sml;


        ViewHolder(View v) {
            super(v);
            appDetails = Paper.book().read("Appdetails");


            alpha_header = (TextView)v.findViewById(R.id.alpha_header);
            authorName = (TextView) v.findViewById(R.id.book_title);
         //   city = (TextView) v.findViewById(R.id.ucity);
            profilepic = (RoundedImageView) v.findViewById(R.id.profilepic);
            tvDesignation = (TextView)v.findViewById(R.id.author_name);
            sml = (SwipeHorizontalMenuLayout) v.findViewById(R.id.sml);
            invite=(Button)v.findViewById(R.id.invite);
            invite.setBackground(Util.setrounded(Color.parseColor(appDetails.getTheme_color())));


            invite.setOnClickListener(this);
            v.setOnClickListener(this);
            sml.setOnClickListener(this);

        }

        void setItem(User user) {
            tileProvider = new LetterTileProvider(context1);
            this.user = user;
            authorName.setText(user.getFirst_name()+" "+user.getLast_name());
          /*  if(city!=null && !city.equals(""))
            {
                city.setText(user.getCity());
            }*/
            if (user.getDesignation()!=null) {
                tvDesignation.setText(user.getDesignation());
                tvDesignation.setVisibility(user.getDesignation().trim().isEmpty() ? View.GONE : View.VISIBLE);
            }

            DisplayMetrics displayMetrics;
            final float dpWidth; displayMetrics = context1.getResources().getDisplayMetrics();
            dpWidth = displayMetrics.widthPixels * 0.15F;
            RelativeLayout.LayoutParams imgParams = (RelativeLayout.LayoutParams) profilepic.getLayoutParams();
            imgParams.width = (int) dpWidth;
            imgParams.height = (int) dpWidth;

            if(null!= user.getProfile_pic() && !user.getProfile_pic().isEmpty() ){

                if (URLUtil.isValidUrl(user.getProfile_pic()))
                    Glide.with(context1.getApplicationContext())

                            .load(user.getProfile_pic())
                            .asBitmap()
                            .placeholder(R.drawable.round_user)
                            .error(R.drawable.round_user)
                            .into(profilepic);
                profilepic.setCornerRadius(8, 8, 8, 8);

                /*new BitmapImageViewTarget(profilepic) {
                                @Override
                                protected void setResource(Bitmap resource) {
                                    RoundedBitmapDrawable drawable = RoundedBitmapDrawableFactory.create(context1.getResources(),
                                            Bitmap.createScaledBitmap(resource, (int) dpWidth, (int) dpWidth, false));
                                    drawable.setCircular(true);
                                    profilepic.setImageDrawable(drawable);
                                }
                            });*/
            }else{
                Bitmap letterTile = tileProvider.getLetterTile(user.getFirst_name().trim(), "key", (int) dpWidth, (int) dpWidth, user.getColor());
                profilepic.setImageBitmap((letterTile));
                profilepic.setCornerRadius(8, 8, 8, 8);

            }
        }

        @Override
        public void onClick(View v) {
            System.out.println("Item clicked pos " + getAdapterPosition());

            int position = getAdapterPosition();
            Privategroup p=new Privategroup();
            if (v.getId()==invite.getId()){
                try {
                    invite.setText("Invited");
                    invite.setBackground(Util.setrounded(Color.LTGRAY));

                    if(tempuid.contains(user.getUserid())){

                    }else{
                        tempuid.add(user.getUserid());
                    }

                    //invite.setBackgroundColor(Color.parseColor("#32000000"));
                    p.getId(user.getUserid());
                  /*  Intent intent = new Intent(context1, CreateGroup.class);
                    context1.startActivity(intent);*/
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
            Paper.book().write("tempuserid",tempuid);
           // onCardClickListner.OnItemClick( v, user , position );


        }

        @Override
        public boolean onLongClick(View v) {
            int position = getAdapterPosition();
            onCardClickListner.OnItemLongClicked(v, user, position);
            return true;
        }
    }

    public void setOnCardClickListner(PrivateListAdapter.OnCardClickListner onCardClickListner) {
        PrivateListAdapter.ViewHolder.onCardClickListner = onCardClickListner;
    }

    public interface OnCardClickListner {
        void OnItemLongClicked(View view, User user, int position);
        void OnItemClick(View view, User user, int position);
    }
}
