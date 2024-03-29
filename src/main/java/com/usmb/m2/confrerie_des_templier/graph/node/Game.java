package com.usmb.m2.confrerie_des_templier.graph.node;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

public class Game extends Node {
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate date;
    private String img;

    public LocalDate getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = LocalDate.parse(date);
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
                ", date=" + date.format(DateTimeFormatter.ISO_DATE) +
                ", img='" + img + '\'' +
                '}';
    }

    @Override
    public HashMap<String, Object> toJson() {
        HashMap<String, Object> json = super.toJson();
        json.put("date", this.date.format(DateTimeFormatter.ISO_DATE));
        json.put("img", this.img);
        return json;
    }
}
