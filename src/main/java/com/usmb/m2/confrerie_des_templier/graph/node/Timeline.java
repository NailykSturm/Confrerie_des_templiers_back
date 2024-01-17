package com.usmb.m2.confrerie_des_templier.graph.node;

import java.time.LocalDate;

public class Timeline extends Node{
    private int begin;
    private int end;
    private ETimelineType type;

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

    public ETimelineType getType() {
        return type;
    }

    public void setType(ETimelineType type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Timeline{" +
                "name='" + getName() + '\'' +
                ", begin=" + begin +
                ", end=" + end +
                ", type=" + type +
                '}';
    }
}
