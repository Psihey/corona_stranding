package com.example.coron_stranding.model;

import java.util.Comparator;

public class AttributeComparator implements Comparator<AttributesModel> {
    @Override
    public int compare(AttributesModel o1, AttributesModel o2) {
        if (o1.getDistance() < o2.getDistance()) return -1;
        if (o1.getDistance() > o2.getDistance()) return 1;
        return 0;
    }
}
