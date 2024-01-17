package com.usmb.m2.confrerie_des_templier.graph.node;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

public class Support extends Node {
    private String img;
    private LocalDate dateSortie = null;

    public void setImg(String img) {
        this.img = img;
    }

    public void setDateSortie(String dateSortie) {
        this.dateSortie = LocalDate.parse(dateSortie);
    }

    @Override
    public String toString() {
        return "Support{" +
                "name='" + getName() + '\'' +
                ", date=" + (dateSortie != null ? dateSortie.format(DateTimeFormatter.ISO_DATE) : "") +
                ", img='" + img + '\'' +
                '}';
    }

    @Override
    public HashMap<String, Object> toJson() {
        HashMap<String, Object> json = super.toJson();
        json.put("date", this.dateSortie.format(DateTimeFormatter.ISO_DATE));
        json.put("img", this.img);
        return json;
    }
}
