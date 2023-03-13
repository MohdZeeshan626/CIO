package com.webmobi.gecmedia.Views.fragment.adapter;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.singleevent.sdk.utils.DrawableUtil;
import com.webmobi.gecmedia.R;
import com.webmobi.gecmedia.Views.fragment.model.ContactsModel;

import java.util.ArrayList;

/**
 * Created by webMOBI on 12/11/2017.
 */

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ContactsViewHolder> {


    private ArrayList<ContactsModel> value;
    private Context context;


    public ContactsAdapter(Context context, ArrayList<ContactsModel> values) {
        this.context = context;
        this.value = new ArrayList<>(values);

    }

    @Override
    public ContactsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.recycler_contacts_item, parent, false);

        return new ContactsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ContactsViewHolder holder, int position) {

        ContactsModel contactsModel = value.get(position);
        if (contactsModel.getName() != null) {
            holder.tv_name.setText("Name: " + contactsModel.getName());
            holder.iv_scanCard.setBackground(DrawableUtil.genBackgroundDrawable(context, contactsModel.getName()));
//            System.out.println("Contacts Model : " + contactsModel.getImageUrl());
            String image_url = contactsModel.getImageUrl();
            holder.iv_scanCard.setImageResource(DrawableUtil.getDrawableForName(contactsModel.getName()));

            }


        holder.tv_desc.setText("Description: " + contactsModel.getContact_info());
    }

    @Override
    public int getItemCount() {
        return value.size();
    }

    static class ContactsViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_scanCard;
        TextView tv_name, tv_desc;

        ContactsViewHolder(View itemView) {
            super(itemView);

            iv_scanCard = (ImageView) itemView.findViewById(R.id.iv_scanCard);
            tv_desc = (TextView) itemView.findViewById(R.id.tv_desc);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);


        }
    }


}
