package com.usmb.m2.confrerie_des_templier.DTO;

public class EdgeDTO {
    public final int id1;
    public final int id2;
    public final String label;
    public double weight;
    public EdgeDTO(int id1, int id2, String label, double weight) {
        this.id1 = id1;
        this.id2 = id2;
        this.label = label;
        this.weight = weight;
    }
}
