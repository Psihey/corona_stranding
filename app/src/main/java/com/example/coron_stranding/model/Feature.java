package com.example.coron_stranding.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Feature {

    @SerializedName("attributes")
    @Expose
    private AttributesModel attributes;

    public AttributesModel getAttributes() {
        return attributes;
    }

    public void setAttributes(AttributesModel attributes) {
        this.attributes = attributes;
    }
}
