/*Disclaimer: IMPORTANT: This Colworx software is proprietary confidential property owned exclusively by Colworx Tech Inc. ("CTI") Without prior written consent and compensation, Colworx expressly forbids the use, installation, modification or redistribution of this CTI software.
 CTI forbids all parties, without a non-exclusive license, under CTI's copyrights in this original CTI software (the "CTI Software"), to use, reproduce, modify and redistribute the CTI Software, with or without modifications, in source and/or binary forms. In all cases, you must retain this notice and the following text and  Disclaimers in all such redistributions of the CTI Software.
 The CTI Software is provided by CTI on an "AS IS" basis. CTI MAKES NO WARRANTIES, EXPRESS OR IMPLIED, INCLUDING WITHOUT LIMITATION THE IMPLIED WARRANTIES OF NON-INFRINGEMENT, MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE, REGARDING THE CTI SOFTWARE OR ITS USE AND OPERATION ALONE OR IN COMBINATION WITH YOUR PRODUCTS.
 IN NO EVENT SHALL CTI BE LIABLE FOR ANY SPECIAL, INDIRECT, INCIDENTAL OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION). ARISING IN ANY WAY OUT OF THE USE, REPRODUCTION, MODIFICATION AND/OR DISTRIBUTION OF THE APPLE SOFTWARE, HOWEVER CAUSED AND WHETHER UNDER THEORY OF CONTRACT, TORT (INCLUDING NEGLIGENCE), STRICT LIABILITY OR OTHERWISE, EVEN IF CTI HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 Copyright (c) 2015 Colworx Tech Inc. All rights reserved.*/

package com.webmobi.gecmedia.CustomViews;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

import com.webmobi.gecmedia.R;

import java.util.Hashtable;

public class TxtVCustomFonts extends TextView {
    private static final String TAG = "TextView";
    private int mLineY;
    private int mViewWidth;

    public TxtVCustomFonts(Context context) {
        super(context);
        this.setPadding(0, 0, 0, 0);
    }

    public TxtVCustomFonts(Context context, AttributeSet attrs) {
        super(context, attrs);
        setCustomFont(context, attrs);
        this.setPadding(0, 0, 0, 0);
    }

    public TxtVCustomFonts(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setCustomFont(context, attrs);
        this.setPadding(0, 0, 0, 0);
    }


    @Override
    public void setPadding(int left, int top, int right, int bottom) {
        // TODO Auto-generated method stub
        super.setPadding(left + 10, top, right + 10, bottom);
    }

    private void setCustomFont(Context ctx, AttributeSet attrs) {
        TypedArray a = ctx.obtainStyledAttributes(attrs, R.styleable.TextViewFonts);
        String customFont = a.getString(R.styleable.TextViewFonts_customFont);
        setCustomFont(ctx, customFont);
        a.recycle();
    }

    public boolean setCustomFont(Context ctx, String asset) {
        Typeface tf = null;
         Hashtable<String, Typeface> cache = new Hashtable<String, Typeface>();

        try {
        tf = Typeface.createFromAsset(ctx.getAssets(), "fonts/"+asset);
        cache.put(asset, tf);
        } catch (Exception e) {
            Log.e(TAG, "Could not get typeface: "+e.getMessage());
            return false;
        }

        setTypeface(cache.get(asset));  
        return true;
    }
    @SuppressLint("NewApi")
    @Override
    protected void onDraw(Canvas canvas) {
        TextPaint paint = getPaint();
        paint.setAntiAlias(true);
        paint.setColor(getCurrentTextColor());
        paint.drawableState = getDrawableState();
        mViewWidth = getMeasuredWidth();
        String text = getText().toString();
        mLineY = 0;
        mLineY += getTextSize();
        Layout layout = getLayout();

        // layout.getLayout()在4.4.3出现NullPointerException
        if (layout == null) {
            return;
        }

        Paint.FontMetrics fm = paint.getFontMetrics();

        int textHeight = (int) (Math.ceil(fm.descent - fm.ascent));
        textHeight = (int) (textHeight * layout.getSpacingMultiplier() + layout.getSpacingAdd());

        for (int i = 0; i < layout.getLineCount(); i++) {
            int lineStart = layout.getLineStart(i);
            int lineEnd = layout.getLineEnd(i);
            float width = StaticLayout.getDesiredWidth(text, lineStart, lineEnd, getPaint());
            String line = text.substring(lineStart, lineEnd);
            if (needScale(line) && i < layout.getLineCount() - 1) {
                drawScaledText(canvas, lineStart, line, width);
            } else {
                canvas.drawText(line, 0, mLineY, paint);
            }
            mLineY += textHeight;
        }
    }

    private boolean needScale(String line) {
        if (line == null || line.length() == 0) {
            return false;
        } else {
            return line.charAt(line.length() - 1) != '\n';
        }
    }

    private boolean isFirstLineOfParagraph(int lineStart, String line) {
        return line.length() > 3 && line.charAt(0) == ' ' && line.charAt(1) == ' ';
    }

    private void drawScaledText(Canvas canvas, int lineStart, String line, float lineWidth) {
        float x = 0;
        if (isFirstLineOfParagraph(lineStart, line)) {
            String blanks = "  ";
            canvas.drawText(blanks, x, mLineY, getPaint());
            float bw = StaticLayout.getDesiredWidth(blanks, getPaint());
            x += bw;

            line = line.substring(3);
        }

        int gapCount = line.length() - 1;
        int i = 0;
        if (line.length() > 2 && line.charAt(0) == 12288 && line.charAt(1) == 12288) {
            String substring = line.substring(0, 2);
            float cw = StaticLayout.getDesiredWidth(substring, getPaint());
            canvas.drawText(substring, x, mLineY, getPaint());
            x += cw;
            i += 2;
        }

        float d = (mViewWidth - lineWidth) / gapCount;
        for (; i < line.length(); i++) {
            String c = String.valueOf(line.charAt(i));
            float cw = StaticLayout.getDesiredWidth(c, getPaint());
            canvas.drawText(c, x, mLineY, getPaint());
            x += cw + d;
        }
    }

}
