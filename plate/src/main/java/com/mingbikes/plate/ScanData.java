package com.mingbikes.plate;

import android.bluetooth.BluetoothDevice;

/**
 * Created by cronus-tropix on 17/8/10.
 */

public class ScanData {

    public ScanData(BluetoothDevice device, int rssi, byte[] scanRecord) {
        this.device = device;
        this.rssi = rssi;
        this.scanRecord = scanRecord;
    }

    int rssi;
    BluetoothDevice device;
    byte[] scanRecord;
}
