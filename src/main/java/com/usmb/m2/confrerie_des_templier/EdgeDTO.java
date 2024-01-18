package com.usmb.m2.confrerie_des_templier;

public class EdgeDTO {
    public final int id1;
    public final int id2;
    public double weight;
    public EdgeDTO(int id1, int id2, double weight) {
        this.id1 = id1;
        this.id2 = id2;
        this.weight = weight;
    }
}
