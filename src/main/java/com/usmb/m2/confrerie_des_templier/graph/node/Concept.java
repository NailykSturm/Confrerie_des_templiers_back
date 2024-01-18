package com.usmb.m2.confrerie_des_templier.graph.node;

public class Concept extends Node {
    public Concept(String name) {
        super(name);
    }

    @Override
    public boolean isConcept() {
        return true;
    }
}
