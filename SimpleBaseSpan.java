package com.knoxpo.customedittext;

import android.text.TextUtils;

/**
 * Created by knoxpo on 9/9/17.
 */

public class SimpleBaseSpan implements BaseSpan{

    private CharSequence mOriginalText;

    @Override
    public void setOriginalText(String text) {
        if (TextUtils.isEmpty(text)) {
            mOriginalText = text;
        } else {
            mOriginalText = text.trim();
        }
    }

    @Override
    public CharSequence getOriginalText() {
        return !TextUtils.isEmpty(mOriginalText) ? mOriginalText : null;
    }
}
