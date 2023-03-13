package com.webmobi.gecmedia.Views;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import com.webmobi.gecmedia.Models.Interestlist;
import com.webmobi.gecmedia.R;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import io.paperdb.Paper;

/**
 * Created by Admin on 12/28/2016.
 */

public class nearby_filter extends AppCompatActivity implements View.OnClickListener {

    Toolbar toolbar;
    private SeekBar seekBar;
    private TextView textView;
    TextView txt_date, txteventtypes;
    LinearLayout dateshow, alleventshow, locationshow;
    RadioGroup radioGroup;
    RadioButton alldate, today, tom, nextweek, nextmonth, choosedate;
    TextView from_date, to_date, location_range;
    LinearLayout dateselection, settingcategories;
    private DatePicker date_picker;
    private int year;
    private int month;
    private int day;
    static final int FromDATE_DIALOG_ID = 100;
    static final int ToDATE_DIALOG_ID = 101;
    String from, to;
    int seekBarProgress = 0;
    CheckBox all;
    ArrayList<Interestlist> countryList;
    String[] interestlist = new String[]{
            "Adhesives & Sealants",
            "Aerospace",
            "Agriculture & Forestry",
            "Air & Water, Mangement",
            "Air, Aviation & Airports",
            "Antiques & Philately",
            "Apparel & Clothing",
            "Architecture & Designing",
            "Astrology",
            "Auto Shows",
            "Automation & Robotics",
            "Automotive",
            "Ayurvedic & Herbal",
            "Baby, Kids & Maternity",
            "Bakery & Confectionery",
            "Banking, Insurance & Finance",
            "Bathroom & Kitchen",
            "Bicycles, Rickshaw",
            "Biotechnology",
            "Book Fairs",
            "Building Construction",
            "Business Services",
            "Cables & Wires",
            "Chemicals & Dyes",
            "Computer Hardware & Software",
            "Consumer & Home Appliances",
            "Consumer Fairs & Carnivals",
            "Cosmetics and Beauty Products",
            "Dies & Moulds",
            "Drugs & Medicines",
            "Education & Training",
            "Electronics & Electrical Goods",
            "Embassies & Consulates",
            "Environment & Waste Management",
            "Fashion Shows",
            "Food & Beverage",
            "Foundry, Casting & Forging",
            "Franchising & Retailing",
            "Furniture",
            "Gems & Jewelry",
            "Gifts",
            "Gifts & Handicrafts",
            "Glass & Glassware",
            "Hand, Machine & Garden Tools",
            "Home Furnishings & Home Textiles",
            "Horticulture & Floriculture",
            "Hospitals & Medical Equipments",
            "Hotel, Restaurant & Catering",
            "Household Consumables",
            "Household Services",
            "HR Consultants",
            "Industrial Products",
            "Internet & Startups",
            "IT & Technology",
            "Knitting & Stitching",
            "Leather & Leather Products",
            "Lifestyle & Fashion",
            "Lights & Lighting",
            "Logistics & transportation",
            "Manufacturing, Fabrication, Repair & Maintenance",
            "Marine & Boat",
            "Meat, Poultry & Seafood",
            "Media & Advertising",
            "Medical & Pharamaceutical",
            "Minerals, Metals & Ores",
            "Miscellaneous",
            "Musical & Organic",
            "Natural Stones",
            "Office & Commerical Supplies",
            "Packing Materials",
            "Paints & Coatings",
            "Paper and Paper Products",
            "Petroleum, Oil & Gas",
            "Pets & Veterinary",
            "Photography & Imaging",
            "Plant, Machinery & Equipment",
            "Plants & Machinery",
            "Plastic & Plastic Products",
            "Power & Energy",
            "Power & Renewable Energy",
            "Printing & Publishing",
            "Railway, Shipping & Aviation",
            "Real Estate",
            "Repair, Maintenance & Cleaning",
            "Research & Development", "Rubber & Rubber Products",
            "Scientific Instruments",
            "Security & Defense",
            "Shipping, Marine & Ports",
            "Solar Energy",
            "Sporting Goods, Toys & Games",
            "Tea & Coffee",
            "Telecom Products & Equipment",
            "Textile, Fabrics & Yarns",
            "Toys & Games",
            "Travel & Tourism",
            "Wedding & Bridal",
            "Wedding & Cutting",
            "Wellness, Health & Fitness",
            "Wine & Spirits"};

    private TextView txtSearch;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nearbyfilter);
        Paper.init(this);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        all = (CheckBox) findViewById(R.id.allevent);
        setSupportActionBar(toolbar);
        settingcategories = (LinearLayout) findViewById(R.id.listview);
        txt_date = (TextView) findViewById(R.id.txt_date);
        location_range = (TextView) findViewById(R.id.txt_locationrange);
        txteventtypes = (TextView) findViewById(R.id.txt_eventtypes);
        dateshow = (LinearLayout) findViewById(R.id.dateshow);
        locationshow = (LinearLayout) findViewById(R.id.locationrange);
        alleventshow = (LinearLayout) findViewById(R.id.eventshow);
        txt_date.setOnClickListener(this);
        txteventtypes.setOnClickListener(this);
        location_range.setOnClickListener(this);
        date_picker = (DatePicker) findViewById(R.id.datapicker);
        radioGroup = (RadioGroup) findViewById(R.id.radiodatfilter);
        alldate = (RadioButton) findViewById(R.id.alldates);
        today = (RadioButton) findViewById(R.id.today);
        tom = (RadioButton) findViewById(R.id.Tomorrow);
        nextweek = (RadioButton) findViewById(R.id.NextWeek);
        nextmonth = (RadioButton) findViewById(R.id.NextMonth);
        choosedate = (RadioButton) findViewById(R.id.ChooseDate);
        dateselection = (LinearLayout) findViewById(R.id.dateselection);
        from_date = (TextView) findViewById(R.id.from_date);
        to_date = (TextView) findViewById(R.id.to_date);
        dateselection.setVisibility(View.GONE);
        String selected = getIntent().getExtras().getString("dateselected");
        setradiobutton(selected);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.alldates:
                        if (dateselection.getVisibility() == View.VISIBLE)
                            dateselection.setVisibility(View.GONE);
                        break;
                    case R.id.today:
                        if (dateselection.getVisibility() == View.VISIBLE)
                            dateselection.setVisibility(View.GONE);
                        break;
                    case R.id.Tomorrow:
                        if (dateselection.getVisibility() == View.VISIBLE)
                            dateselection.setVisibility(View.GONE);
                        break;
                    case R.id.NextWeek:
                        if (dateselection.getVisibility() == View.VISIBLE)
                            dateselection.setVisibility(View.GONE);
                        break;
                    case R.id.NextMonth:
                        if (dateselection.getVisibility() == View.VISIBLE)
                            dateselection.setVisibility(View.GONE);
                        break;
                    case R.id.ChooseDate:
                        dateselection.setVisibility(View.VISIBLE);
                        break;


                }
            }
        });
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd yyyy");
        if (!getIntent().getExtras().getBoolean("type")) {
            long date = System.currentTimeMillis();
            String dateString = sdf.format(date);
            from_date.setText(dateString);
            to_date.setText(dateString);
            Calendar now = Calendar.getInstance();
            year = now.get(Calendar.YEAR);
            month = now.get(Calendar.MONTH);
            day = now.get(Calendar.DAY_OF_MONTH);
        } else {
            String dateString;
            from = getIntent().getExtras().getString("fromdate");
            to = getIntent().getExtras().getString("todate");
            dateString = sdf.format(new Date(from));
            from_date.setText(dateString);
            dateString = sdf.format(new Date(to));
            to_date.setText(dateString);
            Date date = new Date(from);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            year = cal.get(Calendar.YEAR);
            month = cal.get(Calendar.MONTH);
            day = cal.get(Calendar.DAY_OF_MONTH);
        }

        date_picker.init(year, month, day, null);
        from_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showDialog(FromDATE_DIALOG_ID);

            }
        });
        to_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showDialog(ToDATE_DIALOG_ID);

            }
        });


        countryList = new ArrayList<Interestlist>();
        Interestlist il;
        for (int i = 0; i < interestlist.length; i++) {
            il = new Interestlist(interestlist[i], false);
            countryList.add(il);
        }

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        for (int i = 0; i < countryList.size(); i++) {
            final Interestlist country = countryList.get(i);
            final View child = inflater.inflate(R.layout.act_interestitems, null);
            TextView eventname = (TextView) child.findViewById(R.id.name);
            CheckBox cb = (CheckBox) child.findViewById(R.id.checkBox);
            eventname.setText(country.getName());
            eventname.setTextColor(Color.parseColor("#000000"));
            cb.setChecked(country.isSelected());
            cb.setTag(country);
            cb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CheckBox cb = (CheckBox) v;
                    Interestlist country = (Interestlist) cb.getTag();
                    country.setSelected(cb.isChecked());
                }
            });
            settingcategories.addView(child);
        }

        setlist(getIntent().getExtras().getString("categoryselected"));

        all.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    View view = null;
                    for (int i = 0; i < settingcategories.getChildCount(); i++) {
                        view = settingcategories.getChildAt(i);
                        CheckBox cb = (CheckBox) view.findViewById(R.id.checkBox);
                        cb.setChecked(true);
                        Interestlist country = (Interestlist) cb.getTag();
                        country.setSelected(true);

                    }
                } else {
                    View view = null;
                    for (int i = 0; i < settingcategories.getChildCount(); i++) {
                        view = settingcategories.getChildAt(i);
                        CheckBox cb = (CheckBox) view.findViewById(R.id.checkBox);
                        cb.setChecked(false);
                        Interestlist country = (Interestlist) cb.getTag();
                        country.setSelected(false);
                    }
                }
            }
        });


        seekBar = (SeekBar) findViewById(R.id.seekBar);
        textView = (TextView) findViewById(R.id.radius);

        seekBar.setThumb(getResources().getDrawable(R.drawable.seekar));
        seekBar.setProgress(Paper.book().read("SettingProgress",25));
        seekBar.post(new Runnable() {
            @Override
            public void run() {
                seekBar.setProgress(Paper.book().read("SettingProgress",25));
            }
        });
        seekBarProgress = Paper.book().read("SettingProgress",25);
        textView.setText("Set Search Radius - " + Paper.book().read("SettingProgress",25) + " kms " + "/" + Math.round(Paper.book().read("SettingProgress",25) * 0.621371) + "miles");
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                seekBarProgress = progress;
                if (progress <= 25) {
                /* if seek bar value is lesser than min value then set min value to seek bar */
                    seekBarProgress = 25;
                }
                textView.setText("Set Search Radius - " + seekBarProgress + " kms " + "/" + Math.round(seekBarProgress * 0.621371) + "miles");
//                session.setProgress(seekBarProgress);
            }

            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            public void onStopTrackingTouch(SeekBar seekBar) {

            }

        });
    }

    private void setlist(String sec) {
        View view = null;
        switch (sec) {
            case "All Event":
                all.setChecked(true);
                for (int i = 0; i < settingcategories.getChildCount(); i++) {

                    view = settingcategories.getChildAt(i);
                    CheckBox cb = (CheckBox) view.findViewById(R.id.checkBox);
                    cb.setChecked(true);
                    Interestlist country = (Interestlist) cb.getTag();
                    country.setSelected(true);
                }
                break;
            default:

                try {
                    JSONArray array = new JSONArray(getIntent().getExtras().getString("jsonarray"));

                    for (int j = 0; j < array.length(); j++) {
                        for (int i = 0; i < settingcategories.getChildCount(); i++) {
                            view = settingcategories.getChildAt(i);
                            CheckBox cb = (CheckBox) view.findViewById(R.id.checkBox);
                            Interestlist country = (Interestlist) cb.getTag();
                            try {
                                if (array.get(j).toString().equalsIgnoreCase(country.getName())) {
                                    country.setSelected(true);
                                    cb.setChecked(true);
                                    break;
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    private void setradiobutton(String selected) {

        switch (selected) {
            case "All Dates":
                alldate.setChecked(true);
                break;
            case "Today":
                today.setChecked(true);
                break;
            case "Tomorrow":
                tom.setChecked(true);
                break;
            case "Next Week":
                nextweek.setChecked(true);
                break;
            case "Next Month":
                nextmonth.setChecked(true);
                break;
            default:
                choosedate.setChecked(true);
                dateselection.setVisibility(View.VISIBLE);
                break;
        }

    }


    @Override

    protected Dialog onCreateDialog(int id) {

        switch (id) {

            case FromDATE_DIALOG_ID:
                return new DatePickerDialog(this, datePickerListener, year, month, day);
            case ToDATE_DIALOG_ID:
                return new DatePickerDialog(this, datePickerListener1, year, month, day);
        }

        return null;

    }


    private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {

        public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
            Calendar c = Calendar.getInstance();
            c.set(selectedYear, selectedMonth, selectedDay);

            SimpleDateFormat sdf = new SimpleDateFormat("MMM dd yyyy");
            String formattedDate = sdf.format(c.getTime());
            from_date.setText(formattedDate);
//            date_picker.init(year, month, day, null);

        }

    };

    private DatePickerDialog.OnDateSetListener datePickerListener1 = new DatePickerDialog.OnDateSetListener() {

        public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
            Calendar c = Calendar.getInstance();
            c.set(selectedYear, selectedMonth, selectedDay);

            SimpleDateFormat sdf = new SimpleDateFormat("MMM dd yyyy");
            String formattedDate = sdf.format(c.getTime());
            Date date1 = new Date(formattedDate);
            Date date2 = new Date(from_date.getText().toString());
            to_date.setText(formattedDate);

            if (date1.compareTo(date2) > 0) {
                System.out.println("Date1 is after Date2");
            } else if (date1.compareTo(date2) < 0) {
                Toast.makeText(getApplicationContext(), "Select Future Date", Toast.LENGTH_LONG).show();
            }

        }

    };


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.nearby_menu, menu);
        View v = (View) menu.findItem(R.id.apply).getActionView();

        /** Get the edit text from the action view */
        txtSearch = (TextView) v.findViewById(R.id.filterapply);
        txtSearch.setEnabled(true);
        txtSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendingdata();

            }
        });
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;

            case R.id.clear:
                all.setChecked(false);
                clearinterest();
                setradiobutton("All Dates");
                seekBar.setProgress(Paper.book().read("SettingProgress",25));
                txt_date.performClick();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void sendingdata() {
        Intent output;
        String text;
        StringBuilder f = new StringBuilder();
        JSONArray hh1 = new JSONArray();
        int selectedId = radioGroup.getCheckedRadioButtonId();
        Paper.book().write("SettingProgress",seekBarProgress);
        output = new Intent();
        switch (selectedId) {
            case R.id.alldates:
                text = "All Dates";
                output.putExtra("type", false);
                output.putExtra("selected", text);
                break;
            case R.id.today:
                text = "Today";
                output.putExtra("type", false);
                output.putExtra("selected", text);
                break;
            case R.id.Tomorrow:
                text = "Tomorrow";
                output.putExtra("type", false);
                output.putExtra("selected", text);
                break;
            case R.id.NextWeek:
                text = "Next Week";
                output.putExtra("type", false);
                output.putExtra("selected", text);
                break;
            case R.id.NextMonth:
                text = "Next Month";
                output.putExtra("type", false);
                output.putExtra("selected", text);
                break;
            case R.id.ChooseDate:
                text = "From " + from_date.getText().toString() + " To " + to_date.getText().toString();
                output.putExtra("type", true);
                output.putExtra("fromdate", from_date.getText().toString());
                output.putExtra("todate", to_date.getText().toString());
                output.putExtra("selected", text);
                break;
        }
        for (int i = 0; i < countryList.size(); i++) {
            Interestlist country = countryList.get(i);
            if (country.isSelected()) {
                hh1.put(country.getName());
                f.append(country.getName());
            }
        }

        if (hh1.length() > 0) {
            if (hh1.length() == interestlist.length)
                output.putExtra("selcategory", "All Event");
            else
                output.putExtra("selcategory", f.toString());
            output.putExtra("array", hh1.toString());
            setResult(RESULT_OK, output);
            finish();
        } else
            Toast.makeText(getApplicationContext(), "Please Select the Interest", Toast.LENGTH_LONG).show();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txt_date:
                if (dateshow.getVisibility() == View.VISIBLE) {
                    txt_date.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.icon_showmore, 0);
                    txt_date.setSelected(false);
                    dateshow.setVisibility(View.GONE);
                } else {
                    txt_date.setSelected(true);
                    txt_date.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.icon_showless, 0);
                    dateshow.setVisibility(View.VISIBLE);

                    if (alleventshow.getVisibility() == View.VISIBLE) {
                        txteventtypes.setSelected(false);
                        txteventtypes.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.icon_showless, 0);
                        alleventshow.setVisibility(View.GONE);
                    }
                    if (locationshow.getVisibility() == View.VISIBLE) {
                        location_range.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.icon_showless, 0);
                        locationshow.setVisibility(View.GONE);
                        location_range.setSelected(false);
                    }

                }

                break;
            case R.id.txt_eventtypes:

                if (alleventshow.getVisibility() == View.VISIBLE) {
                    txteventtypes.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.icon_showmore, 0);
                    alleventshow.setVisibility(View.GONE);
                    txteventtypes.setSelected(false);
                } else {
                    txteventtypes.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.icon_showless, 0);
                    alleventshow.setVisibility(View.VISIBLE);
                    txteventtypes.setSelected(true);


                    if (dateshow.getVisibility() == View.VISIBLE) {
                        txt_date.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.icon_showless, 0);
                        txt_date.setSelected(false);
                        dateshow.setVisibility(View.GONE);
                    }
                    if (locationshow.getVisibility() == View.VISIBLE) {
                        location_range.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.icon_showless, 0);
                        locationshow.setVisibility(View.GONE);
                        location_range.setSelected(false);
                    }
                }


                break;

            case R.id.txt_locationrange:


                if (locationshow.getVisibility() == View.VISIBLE) {
                    location_range.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.icon_showmore, 0);
                    locationshow.setVisibility(View.GONE);
                    location_range.setSelected(false);
                } else {
                    location_range.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.icon_showless, 0);
                    locationshow.setVisibility(View.VISIBLE);
                    location_range.setSelected(true);


                    if (dateshow.getVisibility() == View.VISIBLE) {
                        txt_date.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.icon_showless, 0);
                        txt_date.setSelected(false);
                        dateshow.setVisibility(View.GONE);
                    }
                    if (alleventshow.getVisibility() == View.VISIBLE) {
                        txteventtypes.setSelected(false);
                        txteventtypes.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.icon_showless, 0);
                        alleventshow.setVisibility(View.GONE);
                    }
                }

                break;
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        setTitle("");
    }


    @Override
    protected void onPause() {
        super.onPause();
    }

    private void clearinterest() {
        View view = null;
        for (int i = 0; i < settingcategories.getChildCount(); i++) {
            view = settingcategories.getChildAt(i);
            CheckBox cb = (CheckBox) view.findViewById(R.id.checkBox);
            cb.setChecked(false);
            Interestlist country = (Interestlist) cb.getTag();
            country.setSelected(false);
        }

    }
}
