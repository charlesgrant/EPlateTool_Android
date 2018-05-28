package com.mingbikes.plate;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void checkKuqi() throws Exception {

        byte[] data = new byte[]{2, 1, 6, 12, -1, 1, 2, 80, 101, -125, 30, -9, 38, 0, 99, 1, 3, 2, -25, -2, 9, 9, 66, 76};

        ScanData scanData = new ScanData(null, -80, data);

        assertEquals(true, BrandCheckUtil.checkKuqi(scanData));
    }

    @Test
    public void checkYouBike() {
        byte[] data = new byte[] {2, 1, 6, 12, -1, 1, 3, -44, 54, 57, -111, -20, 42, 2, 98, 1, 3, 2, -25, -2, 6, 9, 85, 66};

        ScanData scanData = new ScanData(null, -80, data);

        assertEquals(true, BrandCheckUtil.checkYouBike(scanData));
    }

    @Test
    public void checkOfo() {
//        byte[] data = new byte[] {2, 1, 6, 9, -1, -1, -1, -109, 45, -122, -73, -1, -47, 4, 9, 111, 102, 111, 17, 7, -98, -54, -36, 36, 14, -27, -87, -32, -109, -13, -93, -75, 1, 0, 86, -119, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

        byte[] data = new byte[] {2, 1, 6, 9, -1, -1, -1, 122, -120, 18, -60, 67, -44, 4, 9, 111, 102, 111, 17, 7, -98, -54, -36, 36, 14, -27, -87, -32, -109, -13, -93, -75, 1, 0, 86, -119, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

        ScanData scanData = new ScanData(null, -69, data);

        assertEquals(true, BrandCheckUtil.checkOfo(scanData));
    }

    @Test
    public void checkMobike() {
        byte[] data = new byte[] {2, 1, 6, 12, 9, 109, 98, 95, 68, 86, 79, 106, 73, 76, 55, 51, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0
        };

        ScanData scanData = new ScanData(null, -69, data);

        assertEquals(true, BrandCheckUtil.checkMoBike(scanData));
    }

    @Test
    public void checkMingbike() {
        byte[] data = new byte[] {2, 1, 6, 12, -1, 1, 3, -44, 54, 57, -111, -20, 42, 2, 98, 1, 3, 2, -25, -2, 6, 9, 85, 66};

        ScanData scanData = new ScanData(null, -80, data);

        assertEquals(true, BrandCheckUtil.checkMingBikes(scanData));
    }

    @Test
    public void checkLogic() {
        String rssiStr = "89,78,12,65,45,54,13,11,45,34";
        String[] rssiArray = rssiStr.split(",");

        int max = 0;
        int min = 0;
        int rssiCount = 0;
        int totalRssi = 0;
        if (rssiArray != null && rssiArray.length > 2) {
            for (String rssi : rssiArray) {
                int rssiValue = Integer.valueOf(rssi);
                if (max < rssiValue) max = rssiValue;
                if (min > rssiValue) min = rssiValue;
            }
            for (String rssi : rssiArray) {
                int rssiValue = Integer.valueOf(rssi);
                if (max == rssiValue || min == rssiValue) {
                    continue;
                }
                totalRssi += rssiValue;
                rssiCount += 1;
            }
            try {
                totalRssi = totalRssi / rssiCount;
            } catch (Exception e) {
                throw e;
            }

            System.out.printf("totalRssi:" + totalRssi );
        } else if (rssiArray != null && rssiArray.length == 2) {
            for (String rssi : rssiArray) {
                int rssiValue = Integer.valueOf(rssi);
                totalRssi += rssiValue;
            }
            totalRssi = totalRssi / rssiArray.length;
        }
    }
}