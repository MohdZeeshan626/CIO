package com.webmobi.gecmedia.Views;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;

import com.webmobi.gecmedia.Models.Interestlist;
import com.webmobi.gecmedia.R;
import com.webmobi.gecmedia.Views.Adapter.InterestAdapter;

import java.util.ArrayList;

/**
 * Created by Admin on 3/27/2017.
 */

public class InterestActivity extends AppCompatActivity {


    ListView listview;
    CheckBox all;
    InterestAdapter adapter;
    ArrayList<Interestlist> interestlists;

    ImageView interest;

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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.act_interest);

        interest = (ImageView) findViewById(R.id.interest);

        listview = (ListView) findViewById(R.id.listview);

        all = (CheckBox) findViewById(R.id.allevent);


        interestlists = new ArrayList<Interestlist>();

        for (int i = 0; i < interestlist.length; i++) {
            Interestlist ilist = new Interestlist(interestlist[i], false);
            interestlists.add(ilist);
        }
        adapter = new InterestAdapter(InterestActivity.this, interestlists);
        listview.setAdapter(adapter);


        all.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    View view = null;
                    for (int i = 0; i < adapter.getCount(); i++) {
                        view = adapter.getView(i, view, listview);
                        CheckBox cb = (CheckBox) view.findViewById(R.id.checkBox);
                        cb.setChecked(true);
                        Interestlist country = (Interestlist) cb.getTag();
                        country.setSelected(true);
                        adapter.notifyDataSetChanged();

                    }
                } else {
                    View view = null;
                    for (int i = 0; i < adapter.getCount(); i++) {
                        view = adapter.getView(i, view, listview);
                        CheckBox cb = (CheckBox) view.findViewById(R.id.checkBox);
                        cb.setChecked(false);
                        Interestlist country = (Interestlist) cb.getTag();
                        country.setSelected(false);
                        adapter.notifyDataSetChanged();
                    }
                }
            }
        });

    }
}
