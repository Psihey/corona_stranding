package com.example.coron_stranding.event;

import com.example.coron_stranding.model.Example;

public class DataSendEvent {

    private Example example;

    public DataSendEvent(Example example) {
        this.example = example;
    }

    public Example getExample() {
        return example;
    }
}
