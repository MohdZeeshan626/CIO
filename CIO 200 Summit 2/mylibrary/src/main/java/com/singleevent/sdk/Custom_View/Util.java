package com.singleevent.sdk.Custom_View;

import android.app.Dialog;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import androidx.core.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.singleevent.sdk.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by Admin on 5/22/2017.
 */

public class Util {

    private static int screenHeight = 0;


    public static void applyFontToMenuItem(MenuItem mi, Context context) {
        Typeface font = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Regular.otf");
        SpannableString mNewTitle = new SpannableString(mi.getTitle());
        mNewTitle.setSpan(new CustomTypefaceSpan("", font), 0, mNewTitle.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        mi.setTitle(mNewTitle);
    }

    public static SpannableString applyFontToMenuItem(Context context, SpannableString string) {
        Typeface font = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Regular.otf");
        string.setSpan(new CustomTypefaceSpan("", font), 0, string.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return string;
    }

    public static ColorStateList textcolordate(Context context) {
        ColorStateList myColorStateList = new ColorStateList(
                new int[][]{
                        new int[]{android.R.attr.state_selected},
                        new int[]{-android.R.attr.state_selected}
                },
                new int[]{
                        context.getResources().getColor(R.color.white),
                        context.getResources().getColor(R.color.black)
                }
        );
        return myColorStateList;
    }

    public static ColorStateList textcolorday(Context context, int color) {
        ColorStateList myColorStateList = new ColorStateList(
                new int[][]{
                        new int[]{android.R.attr.state_selected},
                        new int[]{-android.R.attr.state_selected}
                },
                new int[]{
                        color,
                        context.getResources().getColor(R.color.black)
                }
        );
        return myColorStateList;
    }


    public static StateListDrawable setImageButtonState(Context context, int icon, int newColor) {

        Drawable mDrawable = ContextCompat.getDrawable(context, icon).mutate();
//        mDrawable.setColorFilter(new PorterDuffColorFilter(newColor, PorterDuff.Mode.MULTIPLY));

        Bitmap bitmap = Bitmap.createBitmap(mDrawable.getIntrinsicWidth(), mDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas myCanvas = new Canvas(bitmap);

        mDrawable.setColorFilter(newColor, PorterDuff.Mode.SRC_IN);
        mDrawable.setBounds(0, 0, mDrawable.getIntrinsicWidth(), mDrawable.getIntrinsicHeight());
        mDrawable.draw(myCanvas);
        mDrawable = new BitmapDrawable(context.getResources(), bitmap);


        StateListDrawable states = new StateListDrawable();

        states.addState(new int[]{android.R.attr.state_selected}, mDrawable);
        states.addState(new int[]{-android.R.attr.state_selected}, context.getResources().getDrawable(R.drawable.round_unselected));

        return states;
    }

    public static Drawable setdrawable(Context context, int icon, int newColor) {

        Drawable mDrawable = ContextCompat.getDrawable(context, icon).mutate();
        mDrawable.setColorFilter(new PorterDuffColorFilter(newColor, PorterDuff.Mode.SRC_IN));

        return mDrawable;
    }

    public static StateListDrawable setcheckbox(Context context, int icon, int newColor) {

        Drawable mDrawable = ContextCompat.getDrawable(context, icon).mutate();
        mDrawable.setColorFilter(new PorterDuffColorFilter(newColor, PorterDuff.Mode.SRC_IN));

        StateListDrawable states = new StateListDrawable();

        states.addState(new int[]{android.R.attr.state_checked}, mDrawable);
        states.addState(new int[]{-android.R.attr.state_checked},
                context.getResources().getDrawable(android.R.drawable.checkbox_off_background));

        return states;
    }

    /*public static StateListDrawable setsurveycheckbox(Context context, int icon, int newColor) {

        Drawable mDrawable = ContextCompat.getDrawable(context, icon).mutate();
//        mDrawable.setColorFilter(new PorterDuffColorFilter(newColor, PorterDuff.Mode.SRC_IN));

        Bitmap bitmap = Bitmap.createBitmap(mDrawable.getIntrinsicWidth(), mDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas myCanvas = new Canvas(bitmap);

        mDrawable.setColorFilter(newColor, PorterDuff.Mode.SRC_IN);
        mDrawable.setBounds(0, 0, mDrawable.getIntrinsicWidth(), mDrawable.getIntrinsicHeight());
        mDrawable.draw(myCanvas);
        mDrawable = new BitmapDrawable(context.getResources(), bitmap);

        StateListDrawable states = new StateListDrawable();

        states.addState(new int[]{android.R.attr.state_checked}, mDrawable);
        states.addState(new int[]{-android.R.attr.state_checked}, context.getResources().getDrawable(R.drawable.n2_ic_checkbox_unchecked));

        return states;
    }*/


    public static StateListDrawable setsurveycheckbox(Context context, int icon, int newColor) {

        Drawable mDrawable = ContextCompat.getDrawable(context, icon).mutate();
        mDrawable.setColorFilter(new PorterDuffColorFilter(newColor, PorterDuff.Mode.SRC_IN));

        StateListDrawable states = new StateListDrawable();

        states.addState(new int[]{android.R.attr.state_checked}, mDrawable);
        states.addState(new int[]{-android.R.attr.state_checked}, context.getResources().getDrawable(android.R.drawable.checkbox_off_background));

        return states;
    }

    public static Typeface regulartypeface(Context context) {
        Typeface custom_font = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Regular.otf");
        return custom_font;
    }

    public static Typeface lighttypeface(Context context) {
        Typeface custom_font = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Regular.otf");
        return custom_font;
    }

    public static Typeface boldtypeface(Context context) {
        Typeface custom_font = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Bold.ttf");
        return custom_font;
    }

    public static Bitmap scaleBitmap(Bitmap bitmap, int newWidth, int newHeight) {
        Bitmap scaledBitmap = Bitmap.createBitmap(newWidth, newHeight, Bitmap.Config.ARGB_8888);

        float scaleX = newWidth / (float) bitmap.getWidth();
        float scaleY = newHeight / (float) bitmap.getHeight();
        float pivotX = 0;
        float pivotY = 0;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(scaleX, scaleY, pivotX, pivotY);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bitmap, 0, 0, new Paint(Paint.FILTER_BITMAP_FLAG));

        return scaledBitmap;
    }

    public static GradientDrawable setshape(int newColor, int bordercolor) {
        GradientDrawable shape = new GradientDrawable();
        shape.setShape(GradientDrawable.RECTANGLE);
        shape.setCornerRadii(new float[]{8, 8, 8, 8, 0, 0, 0, 0});
        shape.setColor(newColor);
        shape.setStroke(3, bordercolor);
        return shape;

    }

    public static GradientDrawable setroundedshape(int newColor) {
        GradientDrawable shape = new GradientDrawable();
        shape.setShape(GradientDrawable.RECTANGLE);
//        shape.setCornerRadii(new float[]{35, 35, 35, 35, 0, 0, 0, 0});
        shape.setColor(newColor);
        return shape;

    }

    public static String setColorAlpha(int percentage, String colorCode) {
        double decValue = ((double) percentage / 100) * 255;
        String rawHexColor = colorCode.replace("#", "");
        StringBuilder str = new StringBuilder(rawHexColor);

        if (Integer.toHexString((int) decValue).length() == 1)
            str.insert(0, "#0" + Integer.toHexString((int) decValue));
        else
            str.insert(0, "#" + Integer.toHexString((int) decValue));


        return str.toString();
    }

    public static String getColoredSpanned(String text, String color) {
        String input = "<font color=" + color + ">" + text + "</font>";
        return input;
    }

    public static String calheader(long senttime) {

        //calc time and adding header
        Calendar today = Calendar.getInstance();
        Calendar gettingtime = Calendar.getInstance();
        gettingtime.setTimeInMillis(senttime);
        SimpleDateFormat dateformat1 = new SimpleDateFormat(" EEE, MMM d,yyyy");
        SimpleDateFormat dateformat2 = new SimpleDateFormat(" h:mm a");
        Calendar Yesterday = Calendar.getInstance();
        Yesterday.add(Calendar.DATE, -1);

        String header;
        String result;

        if ((dateformat1.format(today.getTime()).equals(dateformat1.format(gettingtime.getTime())))) {
            header = "Today "+" at"+dateformat2.format(gettingtime.getTime());
        } else if ((dateformat1.format(Yesterday.getTime()).equals(dateformat1.format(gettingtime.getTime())))) {
            header = "Yesterday"+" at" + dateformat2.format(gettingtime.getTime());
        } else {
            header = dateformat1.format(gettingtime.getTime()) + " at" + dateformat2.format(gettingtime.getTime());
        }
//        result = header + " at" + dateformat2.format(gettingtime.getTime());

        return header;
    }

    public static Dialog showinternt(Context context) {

        LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);


        View view = li.inflate(R.layout.act_noconnectiondialog, null);


        final Dialog mBottomSheetDialog = new Dialog(context, R.style.MaterialDialogSheet);

        mBottomSheetDialog.setContentView(view);
        mBottomSheetDialog.setCanceledOnTouchOutside(false);
        mBottomSheetDialog.setCancelable(false);
        mBottomSheetDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        mBottomSheetDialog.getWindow().setGravity(Gravity.BOTTOM);

        return mBottomSheetDialog;
    }

    public static int getScreenHeight(Context c) {
        if (screenHeight == 0) {
            WindowManager wm = (WindowManager) c.getSystemService(Context.WINDOW_SERVICE);
            Display display = wm.getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            screenHeight = size.y;
        }

        return screenHeight;
    }

    public static GradientDrawable setrounded(int newColor) {
        GradientDrawable shape = new GradientDrawable();
        shape.setShape(GradientDrawable.RECTANGLE);
        shape.setCornerRadius(50);
        shape.setColor(newColor);
        shape.setStroke(2, newColor);
        return shape;

    }



    public static ColorStateList setRadiocheckbox(Context context) {

        ColorStateList myColorStateList = new ColorStateList(
                new int[][]{
                        new int[]{android.R.attr.state_checked},
                        new int[]{-android.R.attr.state_checked}
                },
                new int[]{
                        context.getResources().getColor(R.color.white),
                        context.getResources().getColor(R.color.blue_grey_400)
                }
        );
        return myColorStateList;

    }

    public static StateListDrawable setRadiocheckbox(Context context, int newColor) {


        GradientDrawable checkedshape = new GradientDrawable();
        checkedshape.setShape(GradientDrawable.RECTANGLE);
        checkedshape.setCornerRadius(12);
        checkedshape.setColor(newColor);

        GradientDrawable uncheckedshape = new GradientDrawable();
        uncheckedshape.setShape(GradientDrawable.RECTANGLE);
        uncheckedshape.setCornerRadius(12);
        uncheckedshape.setStroke(1, context.getResources().getColor(R.color.blue_grey_400));
        uncheckedshape.setColor(context.getResources().getColor(R.color.transparent));

        StateListDrawable states = new StateListDrawable();

        states.addState(new int[]{android.R.attr.state_checked}, checkedshape);
        states.addState(new int[]{-android.R.attr.state_checked}, uncheckedshape);

        return states;
    }

    public static String preferredDate(long senttime) {

        SimpleDateFormat myFormat = new SimpleDateFormat(" dd MMM ");
        myFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        return myFormat.format(senttime);

    }

    public static String preferredFromTime(long senttime) {

        SimpleDateFormat myFormat = new SimpleDateFormat("h ");
        myFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        return myFormat.format(senttime);
    }

    public static String preferredToTime(long senttime) {


        SimpleDateFormat myFormat = new SimpleDateFormat("h a");
        myFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        return myFormat.format(senttime);

    }


    public static String TimeToTime(long senttime) {

        SimpleDateFormat myFormat = new SimpleDateFormat("h:mm a");
        myFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        return myFormat.format(senttime);

    }
    public static long getTime_for_meeting(long time) {
        Log.e("TAG", "getTime_for_meeting: time="+time);
        SimpleDateFormat sdf = new SimpleDateFormat();
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        sdf.format(new Date(time));

        SimpleDateFormat sdf2 = new SimpleDateFormat();
        sdf2.setTimeZone(TimeZone.getTimeZone("GMT"));

        try {
            Log.e("TAG", "date2: time="+sdf.format(new Date(time)));
            Date date2 = sdf.parse(sdf.format(new Date(time)));
            return date2.getTime()-((5*3600000)+(3600000/2));//5.5 hrs difference
        }catch (Exception e){
            return -1;
        }
//        SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/YY HH:mm:ss");
//        Date date = sdf.parse(date_string);
//        long millis = date.getTime();
//        return millis;
    }

}
