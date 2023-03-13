package com.singleevent.sdk.View.Fragment.Left_Fragment;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;
import androidx.core.view.ViewCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.transition.Scene;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.OvershootInterpolator;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.singleevent.sdk.ApiList;
import com.singleevent.sdk.App;
import com.singleevent.sdk.Custom_View.Error_Dialog;
import com.singleevent.sdk.Custom_View.Util;
import com.singleevent.sdk.Custom_View.VolleyErrorLis;
import com.singleevent.sdk.View.LeftActivity.Attendee_Profile;
import com.singleevent.sdk.databinding.FragmentOrderFormBinding;
import com.singleevent.sdk.model.AppDetails;
import com.singleevent.sdk.model.LocalData;
import com.singleevent.sdk.model.Schedule;
import com.singleevent.sdk.model.TimeSlot;
import com.singleevent.sdk.model.User;
import com.singleevent.sdk.R;

import com.singleevent.sdk.databinding.LayoutFormOrderStep1Binding;
import com.singleevent.sdk.databinding.LayoutFormOrderStep2Binding;
import com.singleevent.sdk.databinding.LayoutOrderConfirmationBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cyd.awesome.material.FontCharacterMaps;
import io.paperdb.Paper;


/**
 * Created by Admin on 8/7/2017.
 */







public class MeetingDialogFragment extends BottomSheetDialogFragment {


    private FragmentOrderFormBinding binding;
    private Transition selectedViewTransition;

    private List<View> dateview = new ArrayList<>();
    private List<View> timeview = new ArrayList<>();

    LocalData ldata = new LocalData();
    String exhibitorEmail = "";

    ArrayList<Schedule> schlist = new ArrayList<>();

    public static final String ID_DATE_SUFFIX = "txt_date";
    public static final String ID_TIME_SUFFIX = "txt_time";

    AppDetails appDetails;
    int timecheckid, index_selected = -1;

    private String userId = "";
    String username = "";
    JSONObject obj = null;
    FileInputStream fis;
    ObjectInputStream ois;

    public static MeetingDialogFragment newInstance(User user) {

        final MeetingDialogFragment orderFragment = new MeetingDialogFragment();
        final Bundle args = new Bundle();
        args.putSerializable("User", user);
        orderFragment.setArguments(args);
        return orderFragment;
    }

    public static MeetingDialogFragment newInstance(User user, String exhibitorDetail) {

        final MeetingDialogFragment orderFragment = new MeetingDialogFragment();
        final Bundle args = new Bundle();
        args.putSerializable("User", user);
        args.putSerializable("ExhibitorEmail", exhibitorDetail);
        orderFragment.setArguments(args);
        return orderFragment;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Paper.init(getActivity());
        schlist = Paper.book().read("schlist");
        appDetails = Paper.book().read("Appdetails");
        if (Paper.book().read("Islogin", false)) {
            userId = Paper.book().read("userId", "");
            username = Paper.book().read("user", "");


        }
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.s_MaterialDialogSheet);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        binding = FragmentOrderFormBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        super.onViewCreated(view, savedInstanceState);
        if (getArguments() == null)
            getDialog().dismiss();


        ldata.setUser((User) getArguments().getSerializable("User"));
        exhibitorEmail = getArguments().getString("ExhibitorEmail", "");

        AddSch();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            selectedViewTransition = TransitionInflater.from(getContext())
                    .inflateTransition(R.transition.transition_selected_view);
        }

        initOrderStepOneView(binding.layoutStep1);
    }


    private void AddSch() {

        binding.layoutStep2.txtLabelTime.setVisibility(View.INVISIBLE);
        binding.layoutStep2.timelist.removeAllViews();
        binding.layoutStep2.datelist.removeAllViews();


        for (int j = 0; j < schlist.size(); j++) {
            RadioButton radioButtonView = (RadioButton) getActivity().getLayoutInflater().inflate(R.layout.radiobutton_view, null);
            radioButtonView.setBackground(Util.setRadiocheckbox(getActivity(), Color.parseColor(appDetails.getTheme_color())));
            radioButtonView.setTextColor(Util.setRadiocheckbox(getActivity()));
            radioButtonView.setTypeface(Util.regulartypeface(getActivity()));
            radioButtonView.setText(Util.preferredDate(schlist.get(j).getName()));
            radioButtonView.setId(j + 100);
            schlist.get(j).setId("txt_date" + j);
            radioButtonView.setTag(schlist.get(j));
            RadioGroup.LayoutParams params_soiled = new RadioGroup.LayoutParams(getContext(), null);
            params_soiled.setMargins(10, 0, 10, 0);
            radioButtonView.setLayoutParams(params_soiled);
            radioButtonView.setOnClickListener(DateButtonListener);
            binding.layoutStep2.datelist.addView(radioButtonView);
        }


    }

    private View.OnClickListener DateButtonListener = new View.OnClickListener() {
        public void onClick(View v) {
            Schedule sch = (Schedule) ((RadioButton) v).getTag();

            if (sch.getTimeslotlength() > 0) {
                binding.layoutStep2.txtLabelTime.setVisibility(View.VISIBLE);
                binding.layoutStep2.timelist.removeAllViews();
                for (int j = 0; j < sch.getTimeslotlength(); j++) {
                    sch.getTimeslot(j).setId("txt_time" + j);
                    TimeSlot slot = sch.getTimeslot(j);
                    RadioButton radioButtonView = (RadioButton) getActivity().getLayoutInflater().inflate(R.layout.radiobutton_view, null);
                    radioButtonView.setBackground(Util.setRadiocheckbox(getActivity(), Color.parseColor(appDetails.getTheme_color())));
                    radioButtonView.setTextColor(Util.setRadiocheckbox(getActivity()));
                    radioButtonView.setTypeface(Util.regulartypeface(getActivity()));
                    radioButtonView.setText(Util.preferredFromTime(slot.getFrom_time()) + " - " + Util.preferredToTime(slot.getTo_time()));
                    radioButtonView.setTag(slot);
                    radioButtonView.setId(j + 200);
                    RadioGroup.LayoutParams params_soiled = new RadioGroup.LayoutParams(getContext(), null);
                    params_soiled.setMargins(10, 0, 10, 0);
                    radioButtonView.setLayoutParams(params_soiled);
                    if (slot.isStatus()) {
                        radioButtonView.setAlpha(.5f);
                        radioButtonView.setClickable(false);
                    } else
                        radioButtonView.setOnClickListener(TimeButtonListener);
                    radioButtonView.setChecked(radioButtonView.getId() == timecheckid);

                    binding.layoutStep2.timelist.addView(radioButtonView);

                }
            }

            ldata.setDate(sch.getName());
            cleardateview();
            cleartimeview();
            transitionSelectedView(v, binding.txtLabelSize, true);
        }
    };


    private View.OnClickListener TimeButtonListener = new View.OnClickListener() {
        public void onClick(View v) {
            TimeSlot slot = (TimeSlot) ((RadioButton) v).getTag();
            ldata.setFromtime(slot.getFrom_time());
            ldata.setTotime(slot.getTo_time());

            cleartimeview();
            transitionSelectedView(v, binding.txtLabelColour, false);

        }
    };

    private void initOrderStepOneView(LayoutFormOrderStep1Binding layoutStep1) {

        // setting fonts and background color

        binding.txtName.setTypeface(Util.boldtypeface(getActivity()));
        binding.txtLabelSize.setTypeface(Util.regulartypeface(getActivity()));
        binding.txtLabelColour.setTypeface(Util.regulartypeface(getActivity()));
        binding.btnGo.setBackground(Util.setdrawable(getContext(), R.drawable.healthpostbut,
                Color.parseColor(appDetails.getTheme_color())));
        binding.txtAction.setText("Go");
        binding.attbgimag.setVisibility(View.GONE);

        binding.txtAction.setTypeface(Util.regulartypeface(getActivity()));
        // binding.imgProduct.setBackground(Util.setdrawable(getActivity(), R.drawable.round_selected, Color.parseColor(appDetails.getTheme_color())));


        binding.txtLabelSize.setVisibility(View.INVISIBLE);
        binding.txtLabelColour.setVisibility(View.INVISIBLE);
        binding.imgProduct.setVisibility(View.INVISIBLE);
        binding.txtPrice.setVisibility(View.VISIBLE);
        layoutStep1.title.addTextChangedListener(new MyTextWatcher(layoutStep1.title));
        layoutStep1.des.addTextChangedListener(new MyTextWatcher(layoutStep1.des));


        cleardateview();
        cleartimeview();

        binding.btnGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!validatetitle()) {
                    return;
                }

                if (!validatedes()) {
                    return;
                }
                ldata.setTitle(binding.layoutStep1.title.getText().toString());
                ldata.setDes(binding.layoutStep1.des.getText().toString());
                binding.txtAction.setText("Book");

                showDeliveryForm();
            }
        });

        binding.txtPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDialog().dismiss();
            }
        });


    }

    private void cleardateview() {


        for (View clonedView : dateview)
            binding.mainContainer.removeView(clonedView);

        dateview.clear();

    }

    private void cleartimeview() {


        for (View clonedView : timeview)
            binding.mainContainer.removeView(clonedView);


        timeview.clear();

    }


    private void showDeliveryForm() {
        //binding.txtPrice.setPixedenStrokeIcon(FontCharacterMaps.Pixeden.PE_BACK_2);
        binding.txtPrice.setVisibility(View.INVISIBLE);
        binding.attbgimag.setVisibility(View.VISIBLE);
        binding.imgProduct.setPixedenStrokeIcon(FontCharacterMaps.Pixeden.PE_BACK_2);
        binding.imgProduct.setVisibility(View.VISIBLE);
        binding.switcher.setDisplayedChild(1);
        initOrderStepTwoView(binding.layoutStep2);

    }

    private void initOrderStepTwoView(final LayoutFormOrderStep2Binding step2Binding) {

        binding.txtLabelSize.setVisibility(View.VISIBLE);
        binding.txtLabelColour.setVisibility(View.VISIBLE);

        binding.txtPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.switcher.setDisplayedChild(0);
                initOrderStepOneView(binding.layoutStep1);
            }
        });

        binding.imgProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.switcher.setDisplayedChild(0);
                initOrderStepOneView(binding.layoutStep1);
            }
        });


        binding.btnGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int checkedRadioButtonId = binding.layoutStep2.datelist.getCheckedRadioButtonId();
                if (checkedRadioButtonId == -1) {
                    Toast.makeText(getActivity(), "Please Select the Date", Toast.LENGTH_LONG).show();
                    return;
                } else {
                    // find the radio button by returned id
                    RadioButton radioButton = (RadioButton) getActivity().findViewById(checkedRadioButtonId);
                }

                checkedRadioButtonId = binding.layoutStep2.timelist.getCheckedRadioButtonId();
                if (checkedRadioButtonId == -1) {
                    Toast.makeText(getActivity(), "Please Select the Time", Toast.LENGTH_LONG).show();
                    return;
                } else {
                    // find the radio button by returned id
                    RadioButton radioButton = (RadioButton) getActivity().findViewById(checkedRadioButtonId);
                }


                SubmitDetails(ldata);
            }
        });

        //check for date and time selected

        int checkedRadioButtonId = binding.layoutStep2.datelist.getCheckedRadioButtonId();
        timecheckid = binding.layoutStep2.timelist.getCheckedRadioButtonId();


        if (timecheckid != -1) {
            index_selected = binding.layoutStep2.timelist.indexOfChild(binding.layoutStep2.timelist.findViewById(timecheckid));
        }

        if (checkedRadioButtonId != -1) {
            View radioButton = binding.layoutStep2.datelist.findViewById(checkedRadioButtonId);
            radioButton.performClick();
        }

        if (timecheckid != -1) {

            binding.layoutStep2.timelist.getChildAt(index_selected).performClick();
        }
    }

    private void SubmitDetails(final LocalData ldata) {

        final ProgressDialog dialog = new ProgressDialog(getActivity(), R.style.MyAlertDialogStyle);
        dialog.setMessage("Booking...");
        dialog.show();
        String tag_string_req = "Book_TimeSlot";
        String url = ApiList.Book_TimeSlot;
        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {
                dialog.dismiss();
                try {
                    System.out.println(response);
                    JSONObject jObj = new JSONObject(response);

                    if (jObj.getBoolean("response")) {
                        changeToConfirmScene(ldata);
                    } else {
                        if (jObj.getString("responseString").equalsIgnoreCase("Invalid token")) {
                            Error_Dialog.show("Session Expired, Please Login", getActivity());
                        } else
                            Error_Dialog.show(jObj.getString("responseString"), getActivity());
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
                if (error instanceof TimeoutError) {
                    Error_Dialog.show("Timeout", getActivity());
                } else if (VolleyErrorLis.isServerProblem(error)) {
                    Error_Dialog.show(VolleyErrorLis.handleServerError(error, getActivity()), getActivity());
                } else if (VolleyErrorLis.isNetworkProblem(error)) {
                    Error_Dialog.show("Please Check Your Internet Connection", getActivity());
                }
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("appid", appDetails.getAppId());
                params.put("userid", userId);
                if (exhibitorEmail.equals("")) {
                    Log.d("id_or_email", "getParams: "+ldata.getUser().getUserid());
                    params.put("client_id", ldata.getUser().getUserid());
                }
                else {
                    params.put("client_id", exhibitorEmail);
                }
                params.put("meeting_date", String.valueOf(ldata.getDate()));
                params.put("from_time", String.valueOf(ldata.getFromtime()));
                params.put("to_time", String.valueOf(ldata.getTotime()));
                params.put("title", ldata.getTitle());
                params.put("description", ldata.getDes());
                params.put("appname", appDetails.getAppName());
                params.put("username", Paper.book().read("username", ""));
                System.out.println(params);
                return params;
            }

        };


        App.getInstance().addToRequestQueue(strReq, tag_string_req);
        strReq.setRetryPolicy(new DefaultRetryPolicy(
                500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));


    }

    private void changeToConfirmScene(LocalData ldata) {


        final LayoutOrderConfirmationBinding confBinding = prepareConfirmationBinding(ldata);
        //confBinding.btnGo1.setBackgroundColor(Color.parseColor(appDetails.getTheme_color()));

        final Scene scene;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

            scene = new Scene(binding.content, ((ViewGroup) confBinding.getRoot()));
            scene.setEnterAction(new Runnable() {
                @Override
                public void run() {
                    ViewCompat.animate(confBinding.txtSubtitle)
                            .scaleX(1).scaleY(1)
                            .setInterpolator(new OvershootInterpolator())
                            .setStartDelay(200)
                            .start();
                }
            });

            final Transition transition = TransitionInflater.from(getContext()).inflateTransition(R.transition.transition_confirmation_view);
            TransitionManager.go(scene, transition);
        }


    }


    private LayoutOrderConfirmationBinding prepareConfirmationBinding(LocalData ldata) {
        LayoutOrderConfirmationBinding confBinding = LayoutOrderConfirmationBinding
                .inflate(LayoutInflater.from(getContext()), binding.mainContainer, false);

        //setting background color

//        confBinding.getRoot().setBackground(new ColorDrawable(ContextCompat.getColor(
//                getContext(), getProduct().color)));


//        confBinding.imgProduct.setImageDrawable(ContextCompat.getDrawable(getContext(), getProduct().image));
        confBinding.personName.setText(getString(R.string.txt_label_conf_name, String.format(ldata.getUser().getFirst_name() + " " + ldata.getUser().getLast_name())));
        confBinding.meetingTitle.setText(getString(R.string.txt_label_conf_title, String.format(ldata.getTitle())));
        confBinding.txtDate.setText(getString(R.string.txt_label_conf_date, String.format(Util.preferredDate(ldata.getDate()))));
        confBinding.txtTime.setText(getString(R.string.txt_label_conf_time, String.format(Util.preferredFromTime(ldata.getFromtime()) + " - " + Util.preferredToTime(ldata.getTotime()))));
        confBinding.btnArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDialog().dismiss();
            }
        });


        return confBinding;
    }


    private void transitionSelectedView(View v, View targetview, boolean isdate) {
        // Create the cloned view from the selected view at the same position
        final View selectionView = createSelectedTextView(v, isdate);


        binding.mainContainer.addView(selectionView);


        // Perform the transition by changing constraint's layout params
        startCloneAnimation(selectionView, targetview);
    }

    private void startCloneAnimation(final View clonedView, final View targetView) {

        clonedView.post(new Runnable() {
            @Override
            public void run() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    TransitionManager.beginDelayedTransition((ViewGroup) binding.getRoot(), selectedViewTransition);
                }

                // Fires the transition
                clonedView.setLayoutParams(SelectedParamsFactory.endParams(clonedView, targetView));
            }
        });

    }


    private View createSelectedTextView(final View v, boolean isdate) {
        final TextView fakeSelectedTextView = new TextView(getContext(), null, R.attr.selectedTextStyle);
        fakeSelectedTextView.setTextColor(Color.parseColor("#90A4AE"));
        fakeSelectedTextView.setTypeface(Util.regulartypeface(getActivity()));
        fakeSelectedTextView.setSingleLine(true);

        String resourceName = "";
        if (isdate) {
            Schedule sch = (Schedule) v.getTag();
            resourceName = sch.getId();
        } else {
            TimeSlot slot = (TimeSlot) v.getTag();
            resourceName = slot.getId();
        }

        if (resourceName.startsWith(ID_DATE_SUFFIX)) {
           // fakeSelectedTextView.setText(getCleanedText(v));
            dateview.add(fakeSelectedTextView);
            fakeSelectedTextView.setLayoutParams(SelectedParamsFactory.startTextParams(binding.layoutStep2.h1, v));
            binding.txtLabelSize.setText("date :"+getCleanedText(v));
        } else if (resourceName.startsWith(ID_TIME_SUFFIX)) {
            //fakeSelectedTextView.setText(getCleanedText(v));
            binding.txtLabelColour.setText("Time :"+getCleanedText(v));
            timeview.add(fakeSelectedTextView);
            fakeSelectedTextView.setLayoutParams(SelectedParamsFactory.startTextParams(binding.layoutStep2.h2, v));

        }

        return fakeSelectedTextView;
    }

    private static class SelectedParamsFactory {


        private static ConstraintLayout.LayoutParams startTextParams(View selectedView, View v) {
            final ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

            setStartState(selectedView, layoutParams, v);
            return layoutParams;
        }

        private static void setStartState(View selectedView, ConstraintLayout.LayoutParams layoutParams, View v) {

            int[] x = new int[2];
            v.getLocationInWindow(x);


            layoutParams.topToTop = ((ViewGroup) selectedView.getParent().getParent()).getId();
            layoutParams.leftToLeft = ((ViewGroup) selectedView.getParent().getParent()).getId();
            layoutParams.setMargins(x[0], (int) selectedView.getY(), 0, 0);

        }

        private static ConstraintLayout.LayoutParams endParams(View v, View targetView) {
            final ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) v.getLayoutParams();

            final int marginLeft = v.getContext().getResources().getDimensionPixelOffset(R.dimen.spacing_medium);

            layoutParams.setMargins(marginLeft, 0, 0, 0);
            layoutParams.topToTop = targetView.getId();
            layoutParams.startToEnd = targetView.getId();
            layoutParams.bottomToBottom = targetView.getId();

            return layoutParams;
        }

    }

    private String getCleanedText(View v) {
        return ((TextView) v).getText().toString().replace("\n", " ");
    }


    private class MyTextWatcher implements TextWatcher {

        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            int i = view.getId();
            if (i == R.id.des) {
                validatedes();

            } else if (i == R.id.title) {
                validatetitle();

            }
        }
    }

    private boolean validatetitle() {

        if (binding.layoutStep1.title.getText().toString().trim().isEmpty()) {
            binding.layoutStep1.etitle.setError("Enter the Valid Title");
            return false;
        } else {
            binding.layoutStep1.etitle.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validatedes() {
        if (binding.layoutStep1.des.getText().toString().trim().isEmpty()) {
            binding.layoutStep1.edetails.setError("Enter the Valid Description");
            return false;
        } else {
            binding.layoutStep1.edetails.setErrorEnabled(false);
        }

        return true;
    }


}



/*
public class MeetingDialogFragment extends BottomSheetDialogFragment {


    private FragmentOrderFormBinding binding;
    private Transition selectedViewTransition;

    private List<View> dateview = new ArrayList<>();
    private List<View> timeview = new ArrayList<>();

    LocalData ldata = new LocalData();
    String exhibitorEmail = "";

    ArrayList<Schedule> schlist = new ArrayList<>();

    public static final String ID_DATE_SUFFIX = "txt_date";
    public static final String ID_TIME_SUFFIX = "txt_time";

    AppDetails appDetails;
    int timecheckid, index_selected = -1;

    private String userId = "";
    String username = "";
    JSONObject obj = null;
    FileInputStream fis;
    ObjectInputStream ois;

    public static MeetingDialogFragment newInstance(User user) {

        final MeetingDialogFragment orderFragment = new MeetingDialogFragment();
        final Bundle args = new Bundle();
        args.putSerializable("User", user);
        orderFragment.setArguments(args);
        return orderFragment;
    }

    public static MeetingDialogFragment newInstance(User user, String exhibitorDetail) {

        final MeetingDialogFragment orderFragment = new MeetingDialogFragment();
        final Bundle args = new Bundle();
        args.putSerializable("User", user);
        args.putSerializable("ExhibitorEmail", exhibitorDetail);
        orderFragment.setArguments(args);
        return orderFragment;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Paper.init(getActivity());
        schlist = Paper.book().read("schlist");
        appDetails = Paper.book().read("Appdetails");
        if (Paper.book().read("Islogin", false)) {
            userId = Paper.book().read("userId", "");
            username = Paper.book().read("user", "");


        }
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.s_MaterialDialogSheet);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        binding = FragmentOrderFormBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        super.onViewCreated(view, savedInstanceState);
        if (getArguments() == null)
            getDialog().dismiss();


        ldata.setUser((User) getArguments().getSerializable("User"));
        exhibitorEmail = getArguments().getString("ExhibitorEmail", "");


        AddSch();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            selectedViewTransition = TransitionInflater.from(getContext())
                    .inflateTransition(R.transition.transition_selected_view);
        }

        initOrderStepOneView(binding.layoutStep1);
    }


    private void AddSch() {

        binding.layoutStep2.txtLabelTime.setVisibility(View.INVISIBLE);
        binding.layoutStep2.timelist.removeAllViews();
        binding.layoutStep2.datelist.removeAllViews();


        for (int j = 0; j < schlist.size(); j++) {
            RadioButton radioButtonView = (RadioButton) getActivity().getLayoutInflater().inflate(R.layout.radiobutton_view, null);
            radioButtonView.setBackground(Util.setRadiocheckbox(getActivity(), Color.parseColor(appDetails.getTheme_color())));
            radioButtonView.setTextColor(Util.setRadiocheckbox(getActivity()));
            radioButtonView.setTypeface(Util.regulartypeface(getActivity()));
            radioButtonView.setText(Util.preferredDate(schlist.get(j).getName()));
            radioButtonView.setId(j + 100);
            schlist.get(j).setId("txt_date" + j);
            radioButtonView.setTag(schlist.get(j));
            RadioGroup.LayoutParams params_soiled = new RadioGroup.LayoutParams(getContext(), null);
            params_soiled.setMargins(10, 0, 10, 0);
            radioButtonView.setLayoutParams(params_soiled);
            radioButtonView.setOnClickListener(DateButtonListener);
            binding.layoutStep2.datelist.addView(radioButtonView);
        }


    }

    private View.OnClickListener DateButtonListener = new View.OnClickListener() {
        public void onClick(View v) {
            Schedule sch = (Schedule) ((RadioButton) v).getTag();

            if (sch.getTimeslotlength() > 0) {
                binding.layoutStep2.txtLabelTime.setVisibility(View.VISIBLE);
                binding.layoutStep2.timelist.removeAllViews();
                for (int j = 0; j < sch.getTimeslotlength(); j++) {
                    sch.getTimeslot(j).setId("txt_time" + j);
                    TimeSlot slot = sch.getTimeslot(j);
                    RadioButton radioButtonView = (RadioButton) getActivity().getLayoutInflater().inflate(R.layout.radiobutton_view, null);
                    radioButtonView.setBackground(Util.setRadiocheckbox(getActivity(), Color.parseColor(appDetails.getTheme_color())));
                    radioButtonView.setTextColor(Util.setRadiocheckbox(getActivity()));
                    radioButtonView.setTypeface(Util.regulartypeface(getActivity()));
                    radioButtonView.setText(Util.preferredFromTime(slot.getFrom_time()) + " - " + Util.preferredToTime(slot.getTo_time()));
                    radioButtonView.setTag(slot);
                    radioButtonView.setId(j + 200);
                    RadioGroup.LayoutParams params_soiled = new RadioGroup.LayoutParams(getContext(), null);
                    params_soiled.setMargins(10, 0, 10, 0);
                    radioButtonView.setLayoutParams(params_soiled);
                    if (slot.isStatus()) {
                        radioButtonView.setAlpha(.5f);
                        radioButtonView.setClickable(false);
                    } else
                        radioButtonView.setOnClickListener(TimeButtonListener);
                    radioButtonView.setChecked(radioButtonView.getId() == timecheckid);

                    binding.layoutStep2.timelist.addView(radioButtonView);

                }
            }

            ldata.setDate(sch.getName());
            cleardateview();
            cleartimeview();
            transitionSelectedView(v, binding.txtLabelSize, true);
        }
    };


    private View.OnClickListener TimeButtonListener = new View.OnClickListener() {
        public void onClick(View v) {
            TimeSlot slot = (TimeSlot) ((RadioButton) v).getTag();
            ldata.setFromtime(slot.getFrom_time());
            ldata.setTotime(slot.getTo_time());

            cleartimeview();
            transitionSelectedView(v, binding.txtLabelColour, false);

        }
    };

    private void initOrderStepOneView(LayoutFormOrderStep1Binding layoutStep1) {

        // setting fonts and background color

        binding.txtName.setTypeface(Util.boldtypeface(getActivity()));
        binding.txtLabelSize.setTypeface(Util.regulartypeface(getActivity()));
        binding.txtLabelColour.setTypeface(Util.regulartypeface(getActivity()));
        binding.btnGo.setBackgroundColor(Color.parseColor(appDetails.getTheme_color()));
        binding.txtAction.setText("Go");
        binding.txtAction.setTypeface(Util.regulartypeface(getActivity()));
        // binding.imgProduct.setBackground(Util.setdrawable(getActivity(), R.drawable.round_selected, Color.parseColor(appDetails.getTheme_color())));


        binding.txtLabelSize.setVisibility(View.INVISIBLE);
        binding.txtLabelColour.setVisibility(View.INVISIBLE);
        binding.imgProduct.setVisibility(View.INVISIBLE);
        binding.txtPrice.setVisibility(View.VISIBLE);
        layoutStep1.title.addTextChangedListener(new MyTextWatcher(layoutStep1.title));
        layoutStep1.des.addTextChangedListener(new MyTextWatcher(layoutStep1.des));


        cleardateview();
        cleartimeview();

        binding.btnGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!validatetitle()) {
                    return;
                }

                if (!validatedes()) {
                    return;
                }
                ldata.setTitle(binding.layoutStep1.title.getText().toString());
                ldata.setDes(binding.layoutStep1.des.getText().toString());
                binding.txtAction.setText("Book");
                showDeliveryForm();
            }
        });

        binding.txtPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDialog().dismiss();
            }
        });


    }

    private void cleardateview() {


        for (View clonedView : dateview)
            binding.mainContainer.removeView(clonedView);

        dateview.clear();

    }

    private void cleartimeview() {


        for (View clonedView : timeview)
            binding.mainContainer.removeView(clonedView);


        timeview.clear();

    }


    private void showDeliveryForm() {
        //binding.txtPrice.setPixedenStrokeIcon(FontCharacterMaps.Pixeden.PE_BACK_2);
        binding.txtPrice.setVisibility(View.INVISIBLE);
        binding.imgProduct.setPixedenStrokeIcon(FontCharacterMaps.Pixeden.PE_BACK_2);
        binding.imgProduct.setVisibility(View.VISIBLE);
        binding.switcher.setDisplayedChild(1);
        initOrderStepTwoView(binding.layoutStep2);

    }

    private void initOrderStepTwoView(final LayoutFormOrderStep2Binding step2Binding) {

        binding.txtLabelSize.setVisibility(View.VISIBLE);
        binding.txtLabelColour.setVisibility(View.VISIBLE);

        binding.txtPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.switcher.setDisplayedChild(0);
                initOrderStepOneView(binding.layoutStep1);
            }
        });

        binding.imgProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.switcher.setDisplayedChild(0);
                initOrderStepOneView(binding.layoutStep1);
            }
        });


        binding.btnGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int checkedRadioButtonId = binding.layoutStep2.datelist.getCheckedRadioButtonId();
                if (checkedRadioButtonId == -1) {
                    Toast.makeText(getActivity(), "Please Select the Date", Toast.LENGTH_LONG).show();
                    return;
                } else {
                    // find the radio button by returned id
                    RadioButton radioButton = (RadioButton) getActivity().findViewById(checkedRadioButtonId);
                }

                checkedRadioButtonId = binding.layoutStep2.timelist.getCheckedRadioButtonId();
                if (checkedRadioButtonId == -1) {
                    Toast.makeText(getActivity(), "Please Select the Time", Toast.LENGTH_LONG).show();
                    return;
                } else {
                    // find the radio button by returned id
                    RadioButton radioButton = (RadioButton) getActivity().findViewById(checkedRadioButtonId);
                }


                SubmitDetails(ldata);
            }
        });

        //check for date and time selected

        int checkedRadioButtonId = binding.layoutStep2.datelist.getCheckedRadioButtonId();
        timecheckid = binding.layoutStep2.timelist.getCheckedRadioButtonId();


        if (timecheckid != -1) {
            index_selected = binding.layoutStep2.timelist.indexOfChild(binding.layoutStep2.timelist.findViewById(timecheckid));
        }

        if (checkedRadioButtonId != -1) {
            View radioButton = binding.layoutStep2.datelist.findViewById(checkedRadioButtonId);
            radioButton.performClick();
        }

        if (timecheckid != -1) {

            binding.layoutStep2.timelist.getChildAt(index_selected).performClick();
        }
    }

    private void SubmitDetails(final LocalData ldata) {

        final ProgressDialog dialog = new ProgressDialog(getActivity(), R.style.MyAlertDialogStyle);
        dialog.setMessage("Booking...");
        dialog.show();
        String tag_string_req = "Book_TimeSlot";
        String url = ApiList.Book_TimeSlot;
        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {
                dialog.dismiss();
                try {
                    System.out.println(response);
                    JSONObject jObj = new JSONObject(response);

                    if (jObj.getBoolean("response")) {
                        changeToConfirmScene(ldata);
                    } else {
                        if (jObj.getString("responseString").equalsIgnoreCase("Invalid token")) {
                            Error_Dialog.show("Session Expired, Please Login", getActivity());
                        } else
                            Error_Dialog.show(jObj.getString("responseString"), getActivity());
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
                if (error instanceof TimeoutError) {
                    Error_Dialog.show("Timeout", getActivity());
                } else if (VolleyErrorLis.isServerProblem(error)) {
                    Error_Dialog.show(VolleyErrorLis.handleServerError(error, getActivity()), getActivity());
                } else if (VolleyErrorLis.isNetworkProblem(error)) {
                    Error_Dialog.show("Please Check Your Internet Connection", getActivity());
                }
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("appid", appDetails.getAppId());
                params.put("userid", userId);
                if (exhibitorEmail.equals(""))
                    params.put("client_id", ldata.getUser().getUserid());
                else
                    params.put("client_id", exhibitorEmail);

                params.put("meeting_date", String.valueOf(ldata.getDate()));
                params.put("from_time", String.valueOf(ldata.getFromtime()));
                params.put("to_time", String.valueOf(ldata.getTotime()));
                params.put("title", ldata.getTitle());
                params.put("description", ldata.getDes());
                params.put("appname", appDetails.getAppName());
                params.put("username", Paper.book().read("username", ""));
                System.out.println(params);
                return params;
            }

        };


        App.getInstance().addToRequestQueue(strReq, tag_string_req);
        strReq.setRetryPolicy(new DefaultRetryPolicy(
                500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));


    }

    private void changeToConfirmScene(LocalData ldata) {


        final LayoutOrderConfirmationBinding confBinding = prepareConfirmationBinding(ldata);
        confBinding.btnGo.setBackgroundColor(Color.parseColor(appDetails.getTheme_color()));

        final Scene scene;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

            scene = new Scene(binding.content, ((ViewGroup) confBinding.getRoot()));
            scene.setEnterAction(new Runnable() {
                @Override
                public void run() {
                    ViewCompat.animate(confBinding.txtSubtitle)
                            .scaleX(1).scaleY(1)
                            .setInterpolator(new OvershootInterpolator())
                            .setStartDelay(200)
                            .start();
                }
            });

            final Transition transition = TransitionInflater.from(getContext()).inflateTransition(R.transition.transition_confirmation_view);
            TransitionManager.go(scene, transition);
        }


    }


    private LayoutOrderConfirmationBinding prepareConfirmationBinding(LocalData ldata) {
        LayoutOrderConfirmationBinding confBinding = LayoutOrderConfirmationBinding
                .inflate(LayoutInflater.from(getContext()), binding.mainContainer, false);

        //setting background color

//        confBinding.getRoot().setBackground(new ColorDrawable(ContextCompat.getColor(
//                getContext(), getProduct().color)));


//        confBinding.imgProduct.setImageDrawable(ContextCompat.getDrawable(getContext(), getProduct().image));
        confBinding.personName.setText(getString(R.string.txt_label_conf_name, String.format(ldata.getUser().getFirst_name() + " " + ldata.getUser().getLast_name())));
        confBinding.meetingTitle.setText(getString(R.string.txt_label_conf_title, String.format(ldata.getTitle())));
        confBinding.txtDate.setText(getString(R.string.txt_label_conf_date, String.format(Util.preferredDate(ldata.getDate()))));
        confBinding.txtTime.setText(getString(R.string.txt_label_conf_time, String.format(Util.preferredFromTime(ldata.getFromtime()) + " - " + Util.preferredToTime(ldata.getTotime()))));
        confBinding.btnArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDialog().dismiss();
            }
        });


        return confBinding;
    }


    private void transitionSelectedView(View v, View targetview, boolean isdate) {
        // Create the cloned view from the selected view at the same position
        final View selectionView = createSelectedTextView(v, isdate);


        binding.mainContainer.addView(selectionView);


        // Perform the transition by changing constraint's layout params
        startCloneAnimation(selectionView, targetview);
    }

    private void startCloneAnimation(final View clonedView, final View targetView) {

        clonedView.post(new Runnable() {
            @Override
            public void run() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    TransitionManager.beginDelayedTransition((ViewGroup) binding.getRoot(), selectedViewTransition);
                }

                // Fires the transition
                clonedView.setLayoutParams(SelectedParamsFactory.endParams(clonedView, targetView));
            }
        });

    }


    private View createSelectedTextView(final View v, boolean isdate) {
        final TextView fakeSelectedTextView = new TextView(getContext(), null, R.attr.selectedTextStyle);
        fakeSelectedTextView.setTextColor(Color.parseColor("#90A4AE"));
        fakeSelectedTextView.setTypeface(Util.regulartypeface(getActivity()));
        fakeSelectedTextView.setSingleLine(true);

        String resourceName = "";
        if (isdate) {
            Schedule sch = (Schedule) v.getTag();
            resourceName = sch.getId();
        } else {
            TimeSlot slot = (TimeSlot) v.getTag();
            resourceName = slot.getId();
        }

        if (resourceName.startsWith(ID_DATE_SUFFIX)) {
            fakeSelectedTextView.setText(getCleanedText(v));
            dateview.add(fakeSelectedTextView);
            fakeSelectedTextView.setLayoutParams(SelectedParamsFactory.startTextParams(binding.layoutStep2.h1, v));
        } else if (resourceName.startsWith(ID_TIME_SUFFIX)) {
            fakeSelectedTextView.setText(getCleanedText(v));
            timeview.add(fakeSelectedTextView);
            fakeSelectedTextView.setLayoutParams(SelectedParamsFactory.startTextParams(binding.layoutStep2.h2, v));

        }

        return fakeSelectedTextView;
    }

    private static class SelectedParamsFactory {


        private static ConstraintLayout.LayoutParams startTextParams(View selectedView, View v) {
            final ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

            setStartState(selectedView, layoutParams, v);
            return layoutParams;
        }

        private static void setStartState(View selectedView, ConstraintLayout.LayoutParams layoutParams, View v) {

            int[] x = new int[2];
            v.getLocationInWindow(x);


            layoutParams.topToTop = ((ViewGroup) selectedView.getParent().getParent()).getId();
            layoutParams.leftToLeft = ((ViewGroup) selectedView.getParent().getParent()).getId();
            layoutParams.setMargins(x[0], (int) selectedView.getY(), 0, 0);

        }

        private static ConstraintLayout.LayoutParams endParams(View v, View targetView) {
            final ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) v.getLayoutParams();

            final int marginLeft = v.getContext().getResources().getDimensionPixelOffset(R.dimen.spacing_medium);

            layoutParams.setMargins(marginLeft, 0, 0, 0);
            layoutParams.topToTop = targetView.getId();
            layoutParams.startToEnd = targetView.getId();
            layoutParams.bottomToBottom = targetView.getId();

            return layoutParams;
        }

    }

    private String getCleanedText(View v) {
        return ((TextView) v).getText().toString().replace("\n", " ");
    }


    private class MyTextWatcher implements TextWatcher {

        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            int i = view.getId();
            if (i == R.id.des) {
                validatedes();

            } else if (i == R.id.title) {
                validatetitle();

            }
        }
    }

    private boolean validatetitle() {

        if (binding.layoutStep1.title.getText().toString().trim().isEmpty()) {
            binding.layoutStep1.etitle.setError("Enter the Valid Title");
            return false;
        } else {
            binding.layoutStep1.etitle.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validatedes() {
        if (binding.layoutStep1.des.getText().toString().trim().isEmpty()) {
            binding.layoutStep1.edetails.setError("Enter the Valid Description");
            return false;
        } else {
            binding.layoutStep1.edetails.setErrorEnabled(false);
        }

        return true;
    }


}*/




/// new UI update
