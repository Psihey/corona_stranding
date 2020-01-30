package com.example.coron_stranding.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

class UniqueIdField {
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("isSystemMaintained")
    @Expose
    private Boolean isSystemMaintained;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getIsSystemMaintained() {
        return isSystemMaintained;
    }

    public void setIsSystemMaintained(Boolean isSystemMaintained) {
        this.isSystemMaintained = isSystemMaintained;
    }
}
