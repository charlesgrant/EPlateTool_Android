package com.mingbikes.plate.entity;

/**
 * Created by cronus-tropix on 17/7/28.
 */

public class BrandEntity {

    /**
     * {
     "id": "44f17829693a4822bfe16b4bef0ce397",
     "name": "OFO",
     "status": 1,
     "logo": "static/image/icon/ofo.png",
     "createDate": 1499938510000,
     "updateDate": null
     }
     */

    private String id;
    private String name;
    private int status;
    private String logo;
    private long createDate;
    private long updateDate;
    private int parkCount = 0;

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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public long getCreateDate() {
        return createDate;
    }

    public void setCreateDate(long createDate) {
        this.createDate = createDate;
    }

    public long getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(long updateDate) {
        this.updateDate = updateDate;
    }

    public int getParkCount() {
        return parkCount;
    }

    public void setParkCount(int parkCount) {
        this.parkCount = parkCount;
    }
}
