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

import com.makeramen.roundedimageview.RoundedImageView;
import com.singleevent.sdk.Custom_View.Letter.LetterTileProvider;
import com.singleevent.sdk.Custom_View.Letter.Roundeddrawable;
import com.singleevent.sdk.model.AppDetails;
import com.singleevent.sdk.R;
import com.singleevent.sdk.agora.openvcall.model.GroupInfo;
import com.tubb.smrv.SwipeHorizontalMenuLayout;

import java.util.List;
import java.util.Random;

import io.paperdb.Paper;
public class Group_List_Adapter extends RecyclerView.Adapter<Group_List_Adapter.ViewHolder> {

    private static Context context1;
    private List<GroupInfo> eventUserList;
    AppDetails appDetails;
    static LetterTileProvider tileProvider;
    public String userid;
    // Provide a suitable constructor (depends on the kind of dataset)
    public Group_List_Adapter(Context context, List<GroupInfo> myDataset) {
        eventUserList = myDataset;
        Group_List_Adapter.context1 = context;
        Paper.init(context);
//        userid = DataBaseStorage.decrypt(Paper.book().read("userId",""));
        userid = Paper.book().read("userId", "");
        appDetails = Paper.book().read("Appdetails",null);
    }

    //after search update list
    public void updateList(List<GroupInfo> list){

        eventUserList = list;
        notifyDataSetChanged();
    }

    // Create new views (invoked by the layout manager)
    @Override
    public Group_List_Adapter.ViewHolder onCreateViewHolder(final ViewGroup parent,
                                                                 int viewType) {
        // create a new view
        View v = LayoutInflater.from(context1)
                .inflate(R.layout.group_list, parent, false);

        return new Group_List_Adapter.ViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final Group_List_Adapter.ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        final GroupInfo user = eventUserList.get(position);

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
        return eventUserList.size();
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        Context context;
        GroupInfo user;
        TextView authorName,alpha_header;
       // Button invite;
       // TextView tvDesignation;
        RoundedImageView profilepic;
        AppDetails appDetails;
        static Group_List_Adapter.OnCardClickListner onCardClickListner;
        SwipeHorizontalMenuLayout sml;


        ViewHolder(View v) {
            super(v);
            appDetails = Paper.book().read("Appdetails");


          //  alpha_header = (TextView)v.findViewById(R.id.alpha_header);
            authorName = (TextView) v.findViewById(R.id.book_title);

            profilepic = (RoundedImageView) v.findViewById(R.id.profilepic);
            sml = (SwipeHorizontalMenuLayout) v.findViewById(R.id.sml);
          //  invite=(Button)v.findViewById(R.id.invite);
           // invite.setOnClickListener(this);



            v.setOnClickListener(this);
            sml.setOnClickListener(this);

        }

        void setItem(GroupInfo user) {
            tileProvider = new LetterTileProvider(context1);
            this.user = user;
            authorName.setText(user.getGroup_name());

            Random r;
            r = new Random();
            DisplayMetrics displayMetrics;
            int color= Color.argb(255, r.nextInt(256), r.nextInt(256), r.nextInt(256));
            final float dpWidth; displayMetrics = context1.getResources().getDisplayMetrics();
            dpWidth = displayMetrics.widthPixels * 0.15F;
            RelativeLayout.LayoutParams imgParams = (RelativeLayout.LayoutParams) profilepic.getLayoutParams();
            imgParams.width = (int) dpWidth;
            imgParams.height = (int) dpWidth;
            Bitmap letterTile = tileProvider.getLetterTile(user.getGroup_name().trim(), "key", (int) dpWidth, (int) dpWidth,color);
            profilepic.setImageBitmap((letterTile));
            profilepic.setCornerRadius(8, 8, 8, 8);


              /*  if (URLUtil.isValidUrl(user.getProfile_pic()))
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
                Bitmap letterTile = tileProvider.getLetterTile(user.getGroup_name().trim(), "key", (int) dpWidth, (int) dpWidth, user.getColor());
                profilepic.setImageDrawable(new Roundeddrawable(letterTile));
            }*/
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

    public void setOnCardClickListner(Group_List_Adapter.OnCardClickListner onCardClickListner) {
        Group_List_Adapter.ViewHolder.onCardClickListner = onCardClickListner;
    }

    public interface OnCardClickListner {
        void OnItemLongClicked(View view, GroupInfo user, int position);
        void OnItemClick(View view, GroupInfo user, int position);
    }
}
