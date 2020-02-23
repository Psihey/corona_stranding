package com.coronovirus_stranding.fragment;

import com.coronovirus_stranding.model.AttributesModel;

import java.util.List;
import java.util.Map;

public interface SendDataListener {

    List<AttributesModel> getData();

    List<AttributesModel> getCoordinates();

    Map<String,Integer> getStatistic();
}
