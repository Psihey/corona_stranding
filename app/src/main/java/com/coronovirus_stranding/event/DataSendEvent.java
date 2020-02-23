package com.coronovirus_stranding.event;

import com.coronovirus_stranding.model.Example;

public class DataSendEvent {

    private Example example;

    public DataSendEvent(Example example) {
        this.example = example;
    }

    public Example getExample() {
        return example;
    }
}
