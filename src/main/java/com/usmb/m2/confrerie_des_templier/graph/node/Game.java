package com.usmb.m2.confrerie_des_templier.graph.node;

import java.util.Arrays;
import java.util.Date;

public class Game extends Node {
    private Date date;
    private String img;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    @Override
    public String toString() {
        return "Game{" +
                "name='" + getName() + '\'' +
                ", aliases=" + Arrays.toString(getAliases()) +
                ", date=" + date +
                ", img='" + img + '\'' +
                '}';
    }
}
