package com.knoxpo.customedittext;

import android.graphics.Canvas;
import android.graphics.Rect;

/**
 * Created by knoxpo on 9/9/17.
 */

public interface DrawableSpan extends BaseSpan {

    Rect getBounds();

    /**
     * Draw the chip.
     */
    void draw(Canvas canvas);

}
