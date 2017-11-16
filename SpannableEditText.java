package com.knoxpo.customedittext;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.content.ContentValues.TAG;

/**
 * Created by knoxpo on 9/9/17.
 */

public class SpannableEditText extends AppCompatEditText {

    private Drawable mSpannableBackground;
    private int mSpanPadding;
    private float mSpanHeight;
    private float mSpanFontSize;
    private TextWatcher mTextWatcher;
    private float mTextXPos = -1;

    public SpannableEditText(Context context) {
        super(context);
    }

    public SpannableEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        setSpanDimensions(context, attrs);
    }

    public SpannableEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setSpanDimensions(context, attrs);
    }

    private void setSpanDimensions(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SpannableEditText, 0, 0);
        Resources r = getContext().getResources();

        mSpannableBackground = a.getDrawable(R.styleable.SpannableEditText_spannableBackgraound);

        mSpanPadding = a.getDimensionPixelSize(R.styleable.SpannableEditText_spannablePadding, -1);
        if (mSpanPadding == -1) {
            mSpanPadding = (int) r.getDimension(R.dimen.span_padding);
        }

        mSpanHeight = a.getDimensionPixelSize(R.styleable.SpannableEditText_spannableHeight, -1);
        if (mSpanHeight == -1) {
            mSpanHeight = r.getDimension(R.dimen.span_height);
        }

        mSpanFontSize = a.getDimensionPixelSize(R.styleable.SpannableEditText_spannableFontSize, -1);
        if (mSpanFontSize == -1) {
            mSpanFontSize = r.getDimension(R.dimen.span_font_size);
        }

        mTextWatcher = new RecipientTextWatcher();
        addTextChangedListener(mTextWatcher);

        a.recycle();
    }


    public void setSpanText(String str) {
        Editable editable = getText();
        setSelection(editable.length());

        CharSequence text = createSpannable(str);
        editable.append(text);
        setSelection(getText().length());
    }

    private CharSequence createSpannable(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }

        SpannableString spanText = new SpannableString(str);
        DrawableSpan drawableSpan = constructSpan(str);
        spanText.setSpan(drawableSpan, 0, str.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        drawableSpan.setOriginalText(str);
        return spanText;
    }

    private DrawableSpan constructSpan(String str) {
        if (mSpannableBackground == null) {
            throw new NullPointerException(
                    "Unable to render any chips as setChipDimensions was not called.");
        }

        TextPaint paint = getPaint();
        float defaultSize = paint.getTextSize();
        int defaultColor = paint.getColor();

        Bitmap tmpBitmap;

        tmpBitmap = createDrawable(str, paint);

        Drawable result = new BitmapDrawable(getResources(), tmpBitmap);
        result.setBounds(0, 0, tmpBitmap.getWidth(), tmpBitmap.getHeight());

        DrawableSpan drawableSpan = new VisibleDrawable(result);

        paint.setTextSize(defaultSize);
        paint.setColor(defaultColor);

        return drawableSpan;
    }

    private Bitmap createDrawable(String str, TextPaint paint) {
        Drawable background = mSpannableBackground;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            paint.setColor(getContext().getResources().getColor(android.R.color.black, null));
        } else {
            paint.setColor(getContext().getResources().getColor(android.R.color.black));
        }

        Rect backgroundPadding = new Rect();
        background.getPadding(backgroundPadding);

        int height = (int) mSpanHeight;
        int iconWidth = height - backgroundPadding.top - backgroundPadding.bottom;
        float[] widths = new float[1];
        paint.getTextWidths(" ", widths);

        CharSequence ellipsizedText = ellipsizeText(str, paint,
                calculateAvailableWidth() - iconWidth - widths[0] - backgroundPadding.left
                        - backgroundPadding.right);

        int textWidth = (int) paint.measureText(ellipsizedText, 0, ellipsizedText.length());

        int width = Math.max(iconWidth * 2, textWidth + (mSpanPadding * 2) + iconWidth
                + backgroundPadding.left + backgroundPadding.right);


        // Create the background of the chip.
        Bitmap tmpBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(tmpBitmap);

        // Draw the background drawable
        background.setBounds(height / 4, 0, width, height);
        background.draw(canvas);
        // Draw the text vertically aligned
        float textX = height / 2 + mSpanPadding * 2;


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            paint.setColor(getContext().getResources().getColor(android.R.color.black, null));
        } else {
            paint.setColor(getContext().getResources().getColor(android.R.color.black));
        }
        paint.setAntiAlias(true);

        if (mTextXPos == -1) {
            mTextXPos = getTextYOffset(ellipsizedText.toString(), paint, height);
        }

        canvas.drawText(ellipsizedText, 0, ellipsizedText.length(),
                textX, /*getTextYOffset(ellipsizedText.toString(), paint, height)*/ mTextXPos, paint);

        return tmpBitmap;
    }

    /**
     * Get the max amount of space a chip can take up. The formula takes into
     * account the width of the EditTextView, any view padding, and padding
     * that will be added to the chip.
     */
    private float calculateAvailableWidth() {
        return getWidth() - getPaddingLeft() - getPaddingRight() - (mSpanPadding * 2);
    }

    private CharSequence ellipsizeText(CharSequence text, TextPaint paint, float maxWidth) {
        paint.setTextSize(mSpanFontSize);
        if (maxWidth <= 0 && Log.isLoggable(TAG, Log.DEBUG)) {
            Log.d(TAG, "Max width is negative: " + maxWidth);
        }
        return TextUtils.ellipsize(text, paint, maxWidth,
                TextUtils.TruncateAt.END);
    }

    /**
     * Given a height, returns a Y offset that will draw the text in the middle of the height.
     */
    protected float getTextYOffset(String text, TextPaint paint, int height) {
        Rect bounds = new Rect();
        paint.getTextBounds(text, 0, text.length(), bounds);
        int textHeight = bounds.bottom - bounds.top;
        return height - ((height - textHeight) / 2);
    }

    @Override
    public void removeTextChangedListener(TextWatcher watcher) {
        mTextWatcher = null;
        super.removeTextChangedListener(watcher);
    }

    Spannable getSpannable() {
        return super.getText();
    }

    public void getSpannableText() {
        removeTextChangedListener(mTextWatcher);

        DrawableSpan drawableSpan[] = getSpannable().getSpans(0, getSelectionStart(), DrawableSpan.class);
        Editable editable = getText();
        for (int i = 0; i < drawableSpan.length; i++) {
            Log.d("No of Span :", drawableSpan.length + " " + drawableSpan[i].getOriginalText());
            CharSequence str = drawableSpan[i].getOriginalText();
            str = "{" + str + "}";
            editable.replace(editable.getSpanStart(drawableSpan[i]), editable.getSpanEnd(drawableSpan[i]), str);
            editable.removeSpan(drawableSpan[i]);
        }
    }

    public void setSpannableText(String str) {

        Matcher m = Pattern.compile("\\{([^}]+)\\}").matcher(str);

        Editable editable = getText();

        while (m.find()) {
            Log.d("pattern :", m.groupCount() + " " + m.group());
            String replace = m.group().substring(1, m.group().length() - 1);
            editable.replace(m.start(), m.end(), createSpannable(replace));
            m = Pattern.compile("\\{([^}]+)\\}").matcher(getText().toString());
        }
    }

    private class RecipientTextWatcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            //(i1 - i2 == 1) -> check point for back press from soft keyboard to delete previous char
            if (i1 - i2 == 1) {
                int selPoint = getSelectionStart();
                DrawableSpan drawableSpan[] = getSpannable().getSpans(selPoint, selPoint, DrawableSpan.class);

                if (drawableSpan.length > 0) {
                    Log.d("span : ", "found");
                    Editable editable = getText();
                    getSpannable().removeSpan(drawableSpan[0]);
                    editable.delete(getText().toString().lastIndexOf(" ") + 1, getSelectionStart());
                }
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {
        }
    }
}
