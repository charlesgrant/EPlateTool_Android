package com.mingbikes.plate;

/**
 * Created by charles on 17/8/8.
 */

public class ByteUtils {

    public static String[] toHexStringArray(byte[] values) {
        if (values == null) {
            return new String[0];
        }

        String[] array = new String[values.length];

        int max = values.length - 1;
        if (max == -1) {
            return new String[0];
        }
        try {
            for (int i = 0; ; i++) {
                if (i > max) {
                    break;
                }
                array[i] = Integer.toHexString(values[i]).toUpperCase();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return array;
    }
}
