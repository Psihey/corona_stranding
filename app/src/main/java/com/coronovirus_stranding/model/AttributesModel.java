package com.coronovirus_stranding.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AttributesModel {

    @SerializedName("OBJECTID")
    @Expose
    private Integer oBJECTID;
    @SerializedName("Province_State")
    @Expose
    private String provinceState;
    @SerializedName("Country_Region")
    @Expose
    private String countryRegion;
    @SerializedName("Last_Update")
    @Expose
    private Long lastUpdate;
    @SerializedName("Lat")
    @Expose
    private Double lat;
    @SerializedName("Long_")
    @Expose
    private Double _long;
    @SerializedName("Confirmed")
    @Expose
    private Integer confirmed;
    @SerializedName("Deaths")
    @Expose
    private Integer deaths;
    @SerializedName("Recovered")
    @Expose
    private Object recovered;

    private Double distance;

    public AttributesModel(String countryRegion, Double lat, Double _long) {
        this.countryRegion = countryRegion;
        this.lat = lat;
        this._long = _long;
    }

    public AttributesModel(String countryRegion, Double distance) {
        this.countryRegion = countryRegion;
        this.distance = distance;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public Integer getOBJECTID() {
        return oBJECTID;
    }

    public void setOBJECTID(Integer oBJECTID) {
        this.oBJECTID = oBJECTID;
    }

    public String getProvinceState() {
        return provinceState;
    }

    public void setProvinceState(String provinceState) {
        this.provinceState = provinceState;
    }

    public String getCountryRegion() {
        return countryRegion;
    }

    public void setCountryRegion(String countryRegion) {
        this.countryRegion = countryRegion;
    }

    public Long getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Long lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLong() {
        return _long;
    }

    public void setLong(Double _long) {
        this._long = _long;
    }

    public Integer getConfirmed() {
        return confirmed;
    }

    public void setConfirmed(Integer confirmed) {
        this.confirmed = confirmed;
    }

    public Integer getDeaths() {
        return deaths;
    }

    public void setDeaths(Integer deaths) {
        this.deaths = deaths;
    }

    public Object getRecovered() {
        return recovered;
    }

    public void setRecovered(Object recovered) {
        this.recovered = recovered;
    }

}
