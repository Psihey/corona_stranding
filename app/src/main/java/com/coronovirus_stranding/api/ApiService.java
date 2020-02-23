package com.coronovirus_stranding.api;

import com.coronovirus_stranding.model.Example;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService {

    @GET("0MSEUqKaxRlEPj5g/arcgis/rest/services/ncov_cases/FeatureServer/1/query?f=json&where=1%3D1&returnGeometry=false&spatialRel=esriSpatialRelIntersects&outFields=*&orderByFields=Confirmed%20desc&outSR=102100&resultOffset=0&resultRecordCount=100&cacheHint=true")
    Call<Example> getAttributes();
}
