package com.usmb.m2.confrerie_des_templier.graph.node;

import java.util.HashMap;

public class Timeline extends Node{
    private int begin;
    private int end;

    public int getBegin() {
        return begin;
    }

    public void setBegin(int begin) {
        this.begin = begin;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    @Override
    public String toString() {
        return "Timeline{" +
                "name='" + getName() + '\'' +
                ", begin=" + begin +
                ", end=" + end +
                '}';
    }

    @Override
    public HashMap<String, Object> toJson() {
        HashMap<String, Object> json = super.toJson();
        json.put("begin", this.begin);
        json.put("end", this.end);
        json.put("type", "Timeline");
        return json;
    }
}
