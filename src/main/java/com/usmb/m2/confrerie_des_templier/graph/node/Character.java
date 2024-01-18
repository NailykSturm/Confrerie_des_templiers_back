package com.usmb.m2.confrerie_des_templier.graph.node;

import java.util.HashMap;

public class Character extends Node {
    private String img;
    
    public void setImg(String img) {
        this.img = img;
    }

    @Override
    public String toString() {
        return "Timeline{" +
                "name='" + getName() + '\'' +
                ", img=" + img +
                '}';
    }

    @Override
    public HashMap<String, Object> toJson() {
        HashMap<String, Object> json = super.toJson();
        json.put("img", this.img);
        json.put("type", "Character");
        return json;
    }
}
