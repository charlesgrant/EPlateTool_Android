package com.mingbikes.eplatetool;

import android.text.TextUtils;

import java.util.Calendar;

/**
 * Created by cronus-tropix on 17/7/31.
 */

public class TimeUtil {

    /**
     * 获取时间字符串
     *
     * @param time      10位数
     * @param separator 分割(年/月/日)字符
     * @return
     */
    public static String getTime(long time, String separator, boolean isShowDate, boolean showYear, boolean showHour) {
        if (TextUtils.isEmpty(separator))
            separator = "/";

        String timeStr = String.valueOf(time);
        if (timeStr.length() == 10) {
            time = time * 1000;
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int min = calendar.get(Calendar.MINUTE);
        //        int second = calendar.get(Calendar.SECOND);

        if (showHour)
            return (isShowDate ? (showYear ? year + separator : "") + (month < 10 ? "0" + month : month) + separator + day + " " : "") + (hour < 10 ? "0" + hour : hour) + ":" + (min < 10 ? "0" + min : min);
        else
            return (showYear ? year + separator : "") + (month < 10 ? "0" + month : month) + separator + (day < 10 ? "0" + day : day);
    }
}
