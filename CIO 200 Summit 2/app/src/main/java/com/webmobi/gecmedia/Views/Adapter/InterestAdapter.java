package com.webmobi.gecmedia.Views.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.webmobi.gecmedia.Models.Interestlist;
import com.webmobi.gecmedia.R;

import java.util.ArrayList;

/**
 * Created by webmodi on 8/12/2016.
 */
public class InterestAdapter extends BaseAdapter implements Filterable {

    ValueFilter valueFilter;
    Context context;
    ArrayList<Interestlist> eventsarray = new ArrayList<>();
    ArrayList<Interestlist> mStringFilterList;

    public InterestAdapter(Context context, ArrayList<Interestlist> eventsarray) {
        this.context = context;
        this.eventsarray = eventsarray;
        mStringFilterList = eventsarray;
    }

    @Override
    public Filter getFilter() {
        if (valueFilter == null) {
            valueFilter = new ValueFilter();
        }
        return valueFilter;
    }

    private class ViewHolder {
        CheckBox checked;
        TextView txtname;

    }

    @Override
    public int getCount() {
        return eventsarray.size();
    }

    @Override
    public Interestlist getItem(int position) {
        return eventsarray.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;

        if (convertView == null) {
            LayoutInflater vi = (LayoutInflater) context.getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
            convertView = vi.inflate(R.layout.act_interestitems, null);
            holder = new ViewHolder();
            holder.txtname = (TextView) convertView.findViewById(R.id.name);
            holder.checked = (CheckBox) convertView.findViewById(R.id.checkBox);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Interestlist country = eventsarray.get(position);
        holder.txtname.setText(country.getName());
        holder.checked.setChecked(country.isSelected());
        holder.checked.setTag(country);
        holder.checked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBox cb = (CheckBox) v;
                Interestlist country = (Interestlist) cb.getTag();
                country.setSelected(cb.isChecked());
            }
        });

        return convertView;
    }


    private class ValueFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();

            if (constraint != null && constraint.length() > 0) {
                ArrayList<Interestlist> filterList = new ArrayList<>();
                for (int i = 0; i < mStringFilterList.size(); i++) {
                    if ((mStringFilterList.get(i).getName().toUpperCase())
                            .contains(constraint.toString().toUpperCase())) {

                        Interestlist speakerFilter = new Interestlist(mStringFilterList.get(i)
                                .getName(), false);

                        filterList.add(speakerFilter);
                    }
                }
                results.count = filterList.size();
                results.values = filterList;
            } else {
                results.count = mStringFilterList.size();
                results.values = mStringFilterList;
            }
            return results;

        }

        @Override
        protected void publishResults(CharSequence constraint,
                                      FilterResults results) {
            eventsarray = (ArrayList<Interestlist>) results.values;
            notifyDataSetChanged();
        }

    }

    public ArrayList<Interestlist> setSelected() {
        return eventsarray;
    }
}
