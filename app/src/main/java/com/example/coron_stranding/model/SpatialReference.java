package com.example.coron_stranding.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

class SpatialReference {
    @SerializedName("wkid")
    @Expose
    private Integer wkid;
    @SerializedName("latestWkid")
    @Expose
    private Integer latestWkid;

    public Integer getWkid() {
        return wkid;
    }

    public void setWkid(Integer wkid) {
        this.wkid = wkid;
    }

    public Integer getLatestWkid() {
        return latestWkid;
    }

    public void setLatestWkid(Integer latestWkid) {
        this.latestWkid = latestWkid;
    }
}
