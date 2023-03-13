package com.singleevent.sdk.View.LeftActivity.trelo;

import android.animation.Animator;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;
import com.singleevent.sdk.Custom_View.Error_Dialog;
import com.singleevent.sdk.Custom_View.Util;
import com.singleevent.sdk.model.AppDetails;
import com.singleevent.sdk.R;
import com.singleevent.sdk.View.LeftActivity.AddCard;
import com.singleevent.sdk.View.RightActivity.Notification;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;


public class TreloHome extends AppCompatActivity implements View.OnClickListener{

    TextView boards,highlights;
    LinearLayout fablayout1, fablayout2, ll_buttons;
    FloatingActionButton fab;
    boolean isFABOpen;
    double CellWidth, ImgWidth, fab1, fab2;
    AppDetails appDetails;
    private NavigationView navigationview;
    private float navdpWidth;
    private DrawerLayout drawerLayout;
    ImageView noti;
    ImageView title_left_ic,showevents;
    Toolbar toolbar;


    @Override
    public void onCreate(@Nullable Bundle savedInstance){
        super.onCreate(savedInstance);
        Paper.init(this);
        appDetails = Paper.book().read("Appdetails");
        setContentView(R.layout.trelohome);
        boards=(TextView)findViewById(R.id.boards);
        noti=(ImageView)findViewById(R.id.noti);
        toolbar=(Toolbar)findViewById(R.id.toolbar);
        showevents=(ImageView)findViewById(R.id.showevents);
        highlights=(TextView)findViewById(R.id.highlights);
        title_left_ic=(ImageView)findViewById(R.id.title_left_ic);
        navigationview = (NavigationView) findViewById(R.id.navigation_view);
        navigationview.setBackgroundColor(Color.parseColor(appDetails.getTheme_color()));
        navigationview.setItemBackground(makeSelector(Color.parseColor(appDetails.getTheme_selected())));
        drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        boards.setTextColor(Color.parseColor("#FFFFFF"));
        boards.setBackgroundColor(Color.parseColor("#0883AD"));
        boards.setOnClickListener(this);
        highlights.setOnClickListener(this);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(appDetails.getTheme_color())));
        fablayout1 = (LinearLayout) findViewById(R.id.fabLayout1);
        fablayout2 = (LinearLayout) findViewById(R.id.fabLayout2);
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        ImgWidth = (displayMetrics.widthPixels) * 0.20;
        fab1 = (displayMetrics.widthPixels) * 0.15;
        fab2 = fab1 * 2;

        fab.setOnClickListener(this);
        fablayout1.setOnClickListener(this);
        fablayout2.setOnClickListener(this);
        noti.setOnClickListener(this);
        showevents.setOnClickListener(this);

        DrawerLayout.LayoutParams params;
        //left nav
        params = (DrawerLayout.LayoutParams) navigationview.getLayoutParams();
        params.width = (int) navdpWidth;
        navigationview.setLayoutParams(params);


        View header = navigationview.getHeaderView(0);

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(TreloHome.this, drawerLayout, toolbar, R.string.openDrawer, R.string.closeDrawer) {

            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank

                super.onDrawerOpened(drawerView);
            }
        };


        title_left_ic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               try{ if(drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    //drawer is open
                    drawerLayout.closeDrawer(Gravity.LEFT);
                    setleftnavigationitems();
                    //CLOSE Nav Drawer!

                }
                else
                    setleftnavigationitems();
                drawerLayout.openDrawer(GravityCompat.START);

            }catch (Exception e)
               {
               }
            }

        });


        TextView uname=(TextView)header.findViewById(R.id.eventtitle);
        TextView uid = (TextView) header.findViewById(R.id.eventlocation);
        CircleImageView logo=(CircleImageView)header.findViewById(R.id.logo);
        uname.setText("Narendra");
        uid.setText("narendr356");




        setleftnavigationitems();
        navigationview.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                String itemname;
                Menu menu = navigationview.getMenu();
                int n=item.getItemId();
                int gid=item.getGroupId();
                //    menu.getItem(n).setActionView(MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);

                menu.getItem(n).setActionView(null);

                //  int secondLastItem = menu.size()-2;

               /* int lastitem = menu.size() - 1;
                if (lastitem == item.getItemId())
                    itemname = "RefreshApp";
*/
                switch (item.getItemId()) {


                    case 1:
                        // Error_Dialog.show("No Events Found",getActivity());
                       // drawerLayout.closeDrawers();



                        break;
                    case 2:


                        break;
                    case 3:
                        // Error_Dialog.show("No Events Found",getActivity());
                       // drawerLayout.closeDrawers();

                        break;
                    default:

                }
                return true;
            }
        });

    }
    private void setleftnavigationitems() {
        final Menu menu = navigationview.getMenu();
        menu.clear();
        menu.add("Spaces");
        menu.getItem(0).setActionView(null);
    hideleftnaviagtionitem();
}
    private void hideleftnaviagtionitem() {
        final Menu menu = navigationview.getMenu();

        int size = menu.size();
        menu.add(1,1,0,"Boards");
        menu.getItem(1).setIcon(R.drawable.logo);
        menu.getItem(1).setCheckable(true);
        menu.getItem(1).setActionView(null);



        menu.add(2, 2, 0, "Home");
        menu.getItem(2).setIcon(new IconDrawable(this, FontAwesomeIcons.fa_plus).colorRes(R.color.white));
        menu.getItem(2).setCheckable(true);
        menu.getItem(2).setActionView(null);

        ////// Adding for left multi event
        try {

            // parsemulti(Paper.book().read("Multi_info"));

        }catch (Exception e)
        {
            e.printStackTrace();
        }


        for (int i = 0; i < menu.size() - 1; i++) {
            MenuItem subMenuItem = menu.getItem(i);
            // setting font roboto
            Util.applyFontToMenuItem(subMenuItem, getApplicationContext());
           /* if (getMenuId(e.getTabs(i).getType()) == 0) {
                menu.getItem(i).setVisible(false);
            }*/
        }
        MenuItem subMenuItem = menu.getItem(menu.size() - 1);
        Util.applyFontToMenuItem(subMenuItem, getApplicationContext());
    }


    @Override
    public void onClick(View view)
    {
        if(view.getId()==R.id.boards)
        {
            highlights.setTextColor(Color.parseColor("#757575"));
            highlights.setBackgroundColor(Color.parseColor("#FFFFFF"));
            boards.setTextColor(Color.parseColor("#FFFFFF"));
            boards.setBackgroundColor(Color.parseColor("#0883AD"));

            Error_Dialog.show("OK fine",TreloHome.this);
        }
       else if(view.getId()==R.id.highlights)
        {
            boards.setTextColor(Color.parseColor("#757575"));
            boards.setBackgroundColor(Color.parseColor("#FFFFFF"));
            highlights.setTextColor(Color.parseColor("#FFFFFF"));
            highlights.setBackgroundColor(Color.parseColor("#0883AD"));
            Error_Dialog.show("OK Highlights",TreloHome.this);
        }
        else if(view.getId()==R.id.noti)
        {
          Intent i=new Intent(TreloHome.this, Notification.class);
          startActivity(i);
        }
       else if (view.getId() == R.id.fab) {
            if (!isFABOpen)
                showFABMenu();
            else
                closeFABMenu();

        } else if (view.getId() == R.id.fabLayout1) {
          //  updatesorting.putExtra("Is_Sortbytime", true);
            //LocalBroadcastManager.getInstance(this).sendBroadcast(updatesorting);
          //  performFullAgenda();
            closeFABMenu();
        }
        else if(view.getId()==R.id.fabLayout2){
        //    updatesorting.putExtra("Is_Sortbytime", false);
         //   LocalBroadcastManager.getInstance(this).sendBroadcast(updatesorting);

            closeFABMenu();


        }
        else if(view.getId()==R.id.showevents);
        {
            Intent i=new Intent(TreloHome.this, AddCard.class);
            startActivity(i);
        }

    }

    private void showFABMenu() {
        isFABOpen = true;
        fablayout1.setVisibility(View.VISIBLE);
        fablayout2.setVisibility(View.VISIBLE);
   //     frame.setAlpha(.35F);

//        fab.animate().rotationBy(135);
        fablayout1.animate().translationY(-(int) fab1);
        fablayout2.animate().translationY(-(int) fab2);
    }

    private void closeFABMenu() {
        isFABOpen = false;

//        fab.animate().rotationBy(-135);
        fablayout1.animate().translationY(0);
        fablayout2.animate().translationY(0).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                if (!isFABOpen) {
                    fablayout1.setVisibility(View.GONE);
                    fablayout2.setVisibility(View.GONE);
                 //   frame.setAlpha(1F);
                }
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
    }

    public static StateListDrawable makeSelector(int color) {
        StateListDrawable res = new StateListDrawable();
        res.setExitFadeDuration(400);
        res.setAlpha(15);
        res.addState(new int[]{android.R.attr.state_checked}, new ColorDrawable(color));
        res.addState(new int[]{-android.R.attr.state_checked}, new ColorDrawable(Color.TRANSPARENT));
        return res;
    }


}
