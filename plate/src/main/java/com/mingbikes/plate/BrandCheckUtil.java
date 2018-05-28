package com.mingbikes.plate;

/**
 * Created by cronus-tropix on 17/8/10.
 */

public class BrandCheckUtil {

    public static final int MING_BIKE_RSSI = 70;
    public static final int MO_BIKE_RSSI = 65;
    public static final int OFO_BIKE_RSSI = 68;
    public static final int U_BIKE_RSSI = 90;
    public static final int KU_QI_BIKE_RSSI = 90;

    protected static boolean checkMingBikes(ScanData scanData) {

        byte[] data = scanData.scanRecord;

        if (data[3] == (byte) 12 && data[5] == 0x01 && data[6] == 0x02
                && data[13] == (byte) 6
                && data[20] == (byte) 0x09 && data[21] == 0x09 && data[22] == (byte) 78) {

            if (scanData.rssi < MING_BIKE_RSSI) {
                return true;
            } else {
                return false;
            }
        } else if (data[3] == (byte) 12 && data[5] == 0x01 && data[6] == 0x03
                && data[13] == (byte) 6
                && data[20] == (byte) 9 && data[21] == 0x09 && data[22] == (byte) 88) {

            if (scanData.rssi < MING_BIKE_RSSI) {
                return true;
            } else {
                return false;
            }
        } else if (data[3] == (byte) 12 && data[5] == 0x01 && data[6] == 0x03
                && data[13] == (byte) 6
                && data[20] == (byte) 10 && data[21] == 0x09 && data[22] == (byte) 88) {

            if (scanData.rssi < MING_BIKE_RSSI) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    protected static boolean checkOfo(ScanData scanData) {

        byte[] data = scanData.scanRecord;

        if (data[3] == 0x09 && data[13] == 0x04 && data[14] == 0x09) {

            if (scanData.rssi < OFO_BIKE_RSSI) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    protected static boolean checkMoBike(ScanData scanData) {

        byte[] data = scanData.scanRecord;

        if (data[3] == (byte) 12 && data[4] == 0x09) {

            if (scanData.rssi < MO_BIKE_RSSI) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    protected static boolean checkYouBike(ScanData scanData) {

        byte[] data = scanData.scanRecord;

        if (data[3] == (byte) 14 && data[5] == 0x01 && data[6] == 0x02
                && data[13] == 0x06
                && data[20] == 0x09 && data[21] == 0x09 && data[22] == (byte) 78) {
            if (scanData.rssi < U_BIKE_RSSI) {
                return true;
            } else {
                return false;
            }

        } else if (data[5] == 0x01 && data[6] == 0x03
                && data[13] == 0x02
                && data[20] == 0x06 && data[21] == 0x09 && data[22] == (byte) 85) {
            if (scanData.rssi < U_BIKE_RSSI) {
                return true;
            } else {
                return false;
            }

        } else if (data[5] == 0x01 && data[6] == 0x03
                && data[13] == 0x02
                && data[20] == 0x09 && data[21] == 0x09 && data[22] == (byte) 88) {
            if (scanData.rssi < U_BIKE_RSSI) {
                return true;
            } else {
                return false;
            }

        } else {
            return false;
        }
    }

    protected static boolean checkKuqi(ScanData scanData) {

        byte[] data = scanData.scanRecord;

        if (data[3] == (byte) 12 && data[5] == 0x01 && data[6] == 0x02
                && data[13] == 0x06
                && data[20] == 0x07 && data[21] == 0x09 && data[22] == (byte) 67) {
            if (scanData.rssi < KU_QI_BIKE_RSSI) {
                return true;
            } else {
                return false;
            }

        } else if (data[3] == (byte) 12 && data[5] == 0x01 && data[6] == 0x02
                && data[13] == 0x00
                && data[20] == (byte) 9 && data[21] == (byte) 9 && data[22] == (byte) 66) {
            if (scanData.rssi < KU_QI_BIKE_RSSI) {
                return true;
            } else {
                return false;
            }

        } else {
            return false;
        }
    }
}
