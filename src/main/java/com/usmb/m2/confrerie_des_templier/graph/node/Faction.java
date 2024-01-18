package com.usmb.m2.confrerie_des_templier.graph.node;

import java.util.HashMap;

public class Faction extends Node {
    private String img;
    private String description;
    
    public void setImg(String img) {
        this.img = img;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Timeline{" +
                "name='" + getName() + '\'' +
                ", img=" + img +
                ", description=" + description +
                '}';
    }

    @Override
    public HashMap<String, Object> toJson() {
        HashMap<String, Object> json = super.toJson();
        json.put("description", this.description);
        json.put("img", this.img);
        json.put("type", "Faction");
        return json;
    }
}
