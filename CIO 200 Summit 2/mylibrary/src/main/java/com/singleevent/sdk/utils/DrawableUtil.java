package com.singleevent.sdk.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import androidx.core.content.ContextCompat;

import com.singleevent.sdk.R;


/**
 * Created by User on 8/16/2017.
 */

public class DrawableUtil {
    private static int[] colorArray;

    public static Drawable genBackgroundDrawable(Context context, String name) {
        int color = getColor(context, name);
        LayerDrawable layerDrawable = (LayerDrawable) ContextCompat.getDrawable(context, R.drawable.drawable_circle_outline);
        GradientDrawable gradientDrawable = (GradientDrawable) layerDrawable
                .findDrawableByLayerId(R.id.inner_circle);
        gradientDrawable.setColor(color);
        return layerDrawable;
    }

    public static int getColor(Context context, String name) {
        int hash = 0;
        for (int i = 0; i < name.length(); i++) {
            hash = hash * 31 + name.charAt(i);
        }
        hash = name.hashCode();
        if (colorArray == null)
            colorArray = context.getResources().getIntArray(R.array.rainbow);
        int index = hash % colorArray.length;
        if (index < 0) index += colorArray.length;
        return colorArray[index];
    }

    public static int getDrawableForName(String name) {
        String lowerCaseName = name.toLowerCase();
        char c = 0;
        if (lowerCaseName.length() > 0)
            c = lowerCaseName.charAt(0);
        int drawable;
        switch (c) {
            case 'a':
                drawable = R.drawable.alpha_a;
                break;
            case 'b':
                drawable = R.drawable.alpha_b;
                break;
            case 'c':
                drawable = R.drawable.alpha_c;
                break;
            case 'd':
                drawable = R.drawable.alpha_d;
                break;
            case 'e':
                drawable = R.drawable.alpha_e;
                break;
            case 'f':
                drawable = R.drawable.alpha_f;
                break;
            case 'g':
                drawable = R.drawable.alpha_g;
                break;
            case 'h':
                drawable = R.drawable.alpha_h;
                break;
            case 'i':
                drawable = R.drawable.alpha_i;
                break;
            case 'j':
                drawable = R.drawable.alpha_j;
                break;
            case 'k':
                drawable = R.drawable.alpha_k;
                break;
            case 'l':
                drawable = R.drawable.alpha_l;
                break;
            case 'm':
                drawable = R.drawable.alpha_m;
                break;
            case 'n':
                drawable = R.drawable.alpha_n;
                break;
            case 'o':
                drawable = R.drawable.alpha_o;
                break;
            case 'p':
                drawable = R.drawable.alpha_p;
                break;
            case 'q':
                drawable = R.drawable.alpha_q;
                break;
            case 'r':
                drawable = R.drawable.alpha_r;
                break;
            case 's':
                drawable = R.drawable.alpha_s;
                break;
            case 't':
                drawable = R.drawable.alpha_t;
                break;
            case 'u':
                drawable = R.drawable.alpha_u;
                break;
            case 'v':
                drawable = R.drawable.alpha_v;
                break;
            case 'w':
                drawable = R.drawable.alpha_w;
                break;
            case 'x':
                drawable = R.drawable.alpha_x;
                break;
            case 'y':
                drawable = R.drawable.alpha_y;
                break;
            case 'z':
                drawable = R.drawable.alpha_z;
                break;
            default:
                drawable = 0;
        }
        return drawable;
    }
}
