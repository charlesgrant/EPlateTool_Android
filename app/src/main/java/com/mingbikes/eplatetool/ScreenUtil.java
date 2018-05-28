package com.mingbikes.eplatetool;

import android.content.res.Resources;

/**
 * Created by cronus-tropix on 17/7/29.
 */

public class ScreenUtil {

    private static float density = 0;

    /**
     * dp转换为px
     */
    public static int dpToPx(float dp) {
        if (density == 0) {
            density = Resources.getSystem().getDisplayMetrics().density;
        }
        return (int) (dp * density + 0.5f);
    }
}
