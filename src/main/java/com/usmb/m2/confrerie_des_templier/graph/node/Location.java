package com.usmb.m2.confrerie_des_templier.graph.node;

import java.util.HashMap;
import java.util.Map;

public class Location extends Node {
    private Map<String, String> images = new HashMap<String, String>();

    public void addImage(String label, String imgUri) {
        this.images.put(label, imgUri);
    }

    public Map<String, String> getImages() {
        return this.images;
    }

    @Override
    public String toString() {
        String locString = "Location{" +
        "name='" + getName() + '\'' +
        ", images= {";
        for ( Map.Entry<String, String> entry : this.images.entrySet() ) {
            locString += entry.getKey() + "='" + entry.getValue() + "',";
        }
        locString = locString.substring(0, locString.length() - 1);
        locString += "}}";
        return locString;
    }
}
