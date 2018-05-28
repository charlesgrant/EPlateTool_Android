package com.mingbikes.plate.entity;

/**
 * Created by cronus-tropix on 17/8/10.
 */

public class BrandEvent {

    public String macAddress;
    public int type;

    public BrandEvent(String _mac, int _type) {

        this.macAddress = _mac;
        this.type = _type;
    }

    public BrandEvent(String _mac) {
        this.macAddress = _mac;
    }
}
