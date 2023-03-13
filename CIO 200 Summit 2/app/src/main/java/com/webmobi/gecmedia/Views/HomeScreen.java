    package com.webmobi.gecmedia.Views;

    import android.annotation.SuppressLint;
    import android.content.Context;
    import android.content.Intent;
    import android.os.Bundle;
    import androidx.annotation.NonNull;
    import androidx.annotation.Nullable;
    import androidx.constraintlayout.widget.ConstraintLayout;
    import com.google.android.material.appbar.AppBarLayout;
    import com.google.android.material.bottomnavigation.BottomNavigationView;
    import com.google.android.material.appbar.CollapsingToolbarLayout;
    import androidx.coordinatorlayout.widget.CoordinatorLayout;
    import androidx.fragment.app.Fragment;
    import androidx.fragment.app.FragmentManager;
    import androidx.appcompat.app.AppCompatActivity;
    import androidx.appcompat.widget.PopupMenu;
    import android.text.SpannableString;
    import android.util.Log;
    import android.view.ContextThemeWrapper;
    import android.view.Menu;
    import android.view.MenuItem;
    import android.view.View;
    import android.widget.FrameLayout;
    import android.widget.ImageView;
    import android.widget.LinearLayout;
    import android.widget.RelativeLayout;
    import android.widget.TextView;

    import com.singleevent.sdk.Custom_View.Util;
    import com.webmobi.AppUpdateChecker;
    import com.webmobi.gecmedia.Config.ApiList;
    import com.webmobi.gecmedia.Models.NearbyInterface;
    import com.webmobi.gecmedia.R;
    import com.webmobi.gecmedia.Views.fragment.ContactUsFragment;
    import com.webmobi.gecmedia.Views.fragment.EventFragment;
    import com.webmobi.gecmedia.Views.fragment.MoreFragment;
    import com.webmobi.gecmedia.Views.fragment.NearByFragment;

    import java.io.File;
    import java.util.List;

    import io.paperdb.Paper;

    /**
     * Created by Admin on 4/20/2017.
     */

    public class HomeScreen extends AppCompatActivity implements NearbyInterface, View.OnClickListener,
            BottomNavigationView.OnNavigationItemSelectedListener {
        private static final String TAG = HomeScreen.class.getSimpleName();
        PopupMenu popup;
        private FrameLayout container;
        AppBarLayout appBarLayout;
        ImageView Showmore/*, pguide*/;
        TextView search;
        ConstraintLayout pguide;
        RelativeLayout ll_toolbar_content,event_text;
        NearByFragment nearByFragment;
        PopularActivity popularFragment;
        EventFragment homefragment;
        String filename = "app.json";
        File eventDir, jsonFile, descFile;
        private String dir, dir_prev;
        EventFragment hfragment;
        MoreFragment moreFragment;
        ContactUsFragment contactUsFragment;
        private BottomNavigationView bottomNavigationView;
        TextView textViewToolBar, toolbar_title;
        CollapsingToolbarLayout collapsingToolbarLayout;
        ImageView user_login;
        LinearLayout toolbar1, toolbar2;
        View sec_action_bar;
        CoordinatorLayout main_content;
        String event_id, app_id;

        @Override
        protected void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            Paper.init(this);
            setContentView(R.layout.activity_main2);

            AppUpdateChecker appUpdateChecker = new AppUpdateChecker(HomeScreen.this);  //pass the activity in constructure
            appUpdateChecker.checkForUpdate(false);
            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                event_id = extras.getString("EVENT_ID");
                app_id = extras.getString("appid");
                try {
                    if (event_id != null && !event_id.equals("")) {
                        callEvent();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                event_id = "";
                app_id = "";
            }

            dir = getFilesDir() + File.separator + "EventsDownload" + File.separator;
            dir_prev = getFilesDir() + File.separator + "PreviewDownload" + File.separator;
            //   pguide = (ConstraintLayout) findViewById(R.id.pguide);
            //Showmore = (ImageView) findViewById(R.id.showmore);
            user_login = (ImageView) findViewById(R.id.title_right_ic);
            appBarLayout = (AppBarLayout) findViewById(R.id.MyAppbar);
            /*search = (RelativeLayout) findViewById(R.id.search);*/
            toolbar_title = (TextView) findViewById(R.id.title_text);
            search = (TextView) findViewById(R.id.search);
            ll_toolbar_content = (RelativeLayout) findViewById(R.id.ll_toolbar_content);
            //   event_text = (RelativeLayout) findViewById(R.id.event_text);
            textViewToolBar = (TextView) findViewById(R.id.textViewToolBar);
            container = (FrameLayout) findViewById(R.id.frame_container);
            bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);
            collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
            try {
              //  BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);

             }catch(Exception e){}
           // Showmore.setOnClickListener(this);
            search.setOnClickListener(this);
         //   pguide.setOnClickListener(this);
            bottomNavigationView.setOnNavigationItemSelectedListener(this);


            bottomNavigationView.setSelectedItemId(R.id.navigation_events);
            bottomNavigationView.performClick();

            appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
                @Override
                public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {

                }
            });

            user_login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    userLogin();
                }
            });

            ////Checking APP update



            appBarAnimation();
        }

        private void userLogin() {
            Intent i;

            if (Paper.book().read("Islogin", false)) {

                i = new Intent(HomeScreen.this, Profile.class);
                i.setAction(ApiList.loginaction);
                startActivity(i);
            } else {
                i = new Intent(HomeScreen.this, RegActivity.class);
                i.setAction(ApiList.loginaction);
                startActivityForResult(i, 40);
            }
        }

        private void appBarAnimation() {

            appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
                boolean isShow = false;
                int scrollRange = -1;

                @SuppressLint("ResourceAsColor")
                @Override
                public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                    if (scrollRange == -1) {
                        scrollRange = appBarLayout.getTotalScrollRange();
                    }
                    //if the scroll range is "0" then title bar will be visisble
                    if (scrollRange + verticalOffset == 0) {
                        isShow = true;
                       // toolbar_title.setVisibility(View.VISIBLE);
                       // collapsingToolbarLayout.setTitle("webMOBI");
                    } else if (isShow) {
                        isShow = false;
                        toolbar_title.setVisibility(View.GONE);

                    }
                }
            });
        }


        private void replaceFragment(Fragment fragment, String popularfragment) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            androidx.fragment.app.FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.frame_container, fragment, popularfragment);
            transaction.commit();
        }


        @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent data) {

            switch (requestCode) {
                // Check for the integer request code originally supplied to startResolutionForResult().
                case 100:
                    NearByFragment fragment = (NearByFragment) getSupportFragmentManager().findFragmentByTag("nearbyfragment");
                    if (fragment != null) {
                        fragment.onActivityResult(requestCode, resultCode, data);
                    }
                    break;

                case 101:
                    hfragment = (EventFragment) getSupportFragmentManager().findFragmentByTag("homefragment");
                    if (hfragment != null) {
                        hfragment.onActivityResult(requestCode, resultCode, data);
                    }
                    break;

                case 1:
                    hfragment = (EventFragment) getSupportFragmentManager().findFragmentByTag("homefragment");
                    if (hfragment != null) {
                        hfragment.onActivityResult(requestCode, resultCode, data);
                    }
                    break;

                case 40:
                    if (resultCode == RESULT_OK)
                        // setpopmenu();
                        break;

                case 41:
                    if (resultCode == RESULT_OK) {
                        Intent intent = new Intent(HomeScreen.this, RegActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                    }
                    break;

                case 50:
                    ContactUsFragment contactUsFragment = (ContactUsFragment) getSupportFragmentManager().findFragmentByTag("contactusfragment");
                    if (contactUsFragment != null) {
                        contactUsFragment.onActivityResult(requestCode, resultCode, data);
                    }
                    break;

                default:
                    super.onActivityResult(requestCode, resultCode, data);


            }
        }


        @Override
        public void onClick(View view) {
            Intent i;
            switch (view.getId()) {
                /*case R.id.showmore:
                    popup.show();
                    break;*/
                case R.id.search:
    //                i = new Intent(HomeScreen.this, DisCover_Search.class);
                    i = new Intent(HomeScreen.this, SearchActivity.class);
                    startActivity(i);
                    break;
               /* case R.id.pguide:
                    if (Paper.book().read("Islogin", false)) {
                        i = new Intent(HomeScreen.this, PrivateSearch.class);
                        startActivity(i);
                    } else {
                        i = new Intent();
                        i.putExtra("keyMessage", "Please Login to Search or Download the Private Events");
                        i.putExtra("keyAlert", "Login/Register");
                        i.setClassName("com.webmobi.eventsapp", "com.webmobi.eventsapp.Views.TokenExpireAlertReceived");
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);
                    }
                    break;*/
            }

        }

        @Override
        protected void onResume() {
            super.onResume();

            // setpopmenu();
        }

        private void setpopmenu() {
            Context wrapper = new ContextThemeWrapper(this, R.style.PopupMenu);

         //   popup = new PopupMenu(wrapper, Showmore);
            popup.getMenuInflater().inflate(R.menu.main_menu, popup.getMenu());
            Menu popupMenu = popup.getMenu();
            if (Paper.book().read("Islogin", false)) {
                popupMenu.findItem(R.id.login).setVisible(false);
                popupMenu.findItem(R.id.profile).setVisible(true);
            } else {
                popupMenu.findItem(R.id.login).setVisible(true);
                popupMenu.findItem(R.id.profile).setVisible(false);
            }


            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                public boolean onMenuItemClick(MenuItem item) {
                    Intent i;
                    switch (item.getItemId()) {
                        case R.id.login:
                            i = new Intent(HomeScreen.this, RegActivity.class);
                            i.setAction(ApiList.loginaction);
                            startActivityForResult(i, 40);
                            return true;
                        case R.id.profile:
                            i = new Intent(HomeScreen.this, Profile.class);
                            startActivityForResult(i, 41);
                            return true;
                        /*case R.id.favevents:
                            i = new Intent(HomeScreen.this, WishList.class);
                            startActivity(i);
                            return true;
                        case R.id.help:
                            return true;*/
                        default:
                            return true;
                    }

                }
            });
        }


        @Override
        public void disabletab(int pos) {


        }

        @Override
        public void enableall() {

        }


        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_events:
                    Log.v(TAG, "navigation_home");
                    if (homefragment == null)
                        homefragment = new EventFragment();

                    Bundle bundle = new Bundle();
                    bundle.putString("EVENT_ID", event_id);
                    bundle.putString("APP_ID", app_id);
                    homefragment.setArguments(bundle);

                    replaceFragment(homefragment, "homefragment");
                    ll_toolbar_content.setVisibility(View.VISIBLE);
                 //   event_text.setVisibility(View.VISIBLE);
                    textViewToolBar.setVisibility(View.GONE);
                    return true;
                case R.id.navigation_nearby:

                    if (Paper.book().read("Islogin", false)) {

                       /* if (nearByFragment == null)
                            nearByFragment = new NearByFragment();
                        replaceFragment(nearByFragment, "nearbyfragment");
                        ll_toolbar_content.setVisibility(View.VISIBLE);
                        textViewToolBar.setVisibility(View.GONE);*/
                        Intent i = new Intent(getApplicationContext(), Profile.class);
    //                i = new Intent(getActivity(), NewProfile.class);
                        startActivityForResult(i, 41);
                        return true;
                    } else {
                        Intent i = new Intent(HomeScreen.this, RegActivity.class);
                        i.setAction(ApiList.loginaction);
                        startActivityForResult(i, 40);

                        break;
                    }

                case R.id.navigation_contactus:

                    Log.v(TAG, "more");

                    if (Paper.book().read("Islogin", false)) {
                        if (contactUsFragment == null)
                            contactUsFragment = new ContactUsFragment();
                        replaceFragment(contactUsFragment, "contactusfragment");
                        ll_toolbar_content.setVisibility(View.GONE);
                        textViewToolBar.setVisibility(View.VISIBLE);
                        textViewToolBar.setText(Util.applyFontToMenuItem(this, new SpannableString("Contacts")));
                        return true;
                    } else {
                        Intent i = new Intent(HomeScreen.this, RegActivity.class);
                        i.setAction(ApiList.loginaction);
                        startActivityForResult(i, 40);

                        break;
                    }


                case R.id.navigation_more:
                    Log.v(TAG, "more");
                    if (Paper.book().read("Islogin", false)) {

                        if (moreFragment == null)
                            moreFragment = new MoreFragment();
                        replaceFragment(moreFragment, "morefragment");
                        ll_toolbar_content.setVisibility(View.GONE);
                        textViewToolBar.setVisibility(View.VISIBLE);
                        textViewToolBar.setText(Util.applyFontToMenuItem(this, new SpannableString("More ")));
                        return true;
                    } else {
                        Intent i = new Intent(HomeScreen.this, RegActivity.class);
                        i.setAction(ApiList.loginaction);
                        startActivityForResult(i, 40);


                        break;
                    }

            }


            return false;
        }


        @Override
        public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            List<Fragment> fragments = getSupportFragmentManager().getFragments();
            if (fragments != null) {
                for (Fragment fragment : fragments) {
                    fragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
                }
            }
        }

        /// Adding for Multi event view

        public void callEvent(){
            if (homefragment == null)
                homefragment = new EventFragment();

            Bundle bundle = new Bundle();
            bundle.putString("EVENT_ID", event_id);
            bundle.putString("APP_ID", app_id);
            homefragment.setArguments(bundle);

            replaceFragment(homefragment, "homefragment");
            ll_toolbar_content.setVisibility(View.VISIBLE);
            //   event_text.setVisibility(View.VISIBLE);
            textViewToolBar.setVisibility(View.GONE);
        }


    }
