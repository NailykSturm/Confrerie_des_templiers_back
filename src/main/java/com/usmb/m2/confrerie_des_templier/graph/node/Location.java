package com.usmb.m2.confrerie_des_templier.graph.node;

import java.util.HashMap;
import java.util.Map;

public class Location extends Node {
    private final Map<String, String> images = new HashMap<>();

    public void addImage(String label, String imgUri) {
        this.images.put(label, imgUri);
    }

    public Map<String, String> getImages() {
        return this.images;
    }

    @Override
    public String toString() {
        StringBuilder locString = new StringBuilder("Location{" +
                "name='" + getName() + '\'' +
                ", images= {");
        for ( Map.Entry<String, String> entry : this.images.entrySet() ) {
            locString.append(entry.getKey()).append("='").append(entry.getValue()).append("',");
        }
        locString = new StringBuilder(locString.substring(0, locString.length() - 1));
        locString.append("}}");
        return locString.toString();
    }

    @Override
    public HashMap<String, Object> toJson() {
        HashMap<String, Object> json = super.toJson();
        json.put("images", this.images);
        json.put("type", "Location");
        return json;
    }
}
