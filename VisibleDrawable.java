package com.knoxpo.customedittext;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.style.DynamicDrawableSpan;
import android.text.style.ImageSpan;

/**
 * Created by knoxpo on 9/9/17.
 */

public class VisibleDrawable extends ImageSpan implements DrawableSpan {
    private final SimpleBaseSpan mDelegate;

    public VisibleDrawable(final Drawable drawable) {
        this(drawable, DynamicDrawableSpan.ALIGN_BOTTOM);
    }

    public VisibleDrawable(final Drawable drawable,
                                final int verticalAlignment) {
        super(drawable, verticalAlignment);
        mDelegate = new SimpleBaseSpan();
    }

    @Override
    public Rect getBounds() {
        return getDrawable().getBounds();
    }

    @Override
    public void draw(Canvas canvas) {
        getDrawable().draw(canvas);
    }

    @Override
    public void setOriginalText(String text) {
        mDelegate.setOriginalText(text);
    }

    @Override
    public CharSequence getOriginalText() {
        return mDelegate.getOriginalText();
    }
}
