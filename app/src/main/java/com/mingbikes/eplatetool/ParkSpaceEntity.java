package com.mingbikes.eplatetool;

import java.io.Serializable;

/**
 * Created by cronus-tropix on 17/7/28.
 */

public class ParkSpaceEntity implements Serializable {

    /**
     * "id": "0956279851eb4d8491cc8a5678901234",
     "name": "陈建浩停车位001",
     "macAddress": "EC:DB:10:38:C9:4C",
     "amount": 0,
     "capacity": 15,
     "status": 1,
     "longitude": "113.346071",
     "latitude": "23.140642",
     "updateDate": 1500379139000,
     "createDate": 1500257282000,
     "location": "五山路尚德大厦141号"
     */

    private String id;
    private String name;
    private int amount;
    private int capacity;
    private int status;
    private String macAddress;
    private String latitude;
    private String longitude;
    private long updateDate;
    private long createDate;
    private String location;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void addAmount(int _amount) {
        this.amount += _amount;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public long getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(long updateDate) {
        this.updateDate = updateDate;
    }

    public long getCreateDate() {
        return createDate;
    }

    public void setCreateDate(long createDate) {
        this.createDate = createDate;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
