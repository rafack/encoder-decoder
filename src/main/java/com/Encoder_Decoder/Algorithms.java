package com.Encoder_Decoder;

public enum Algorithms {
    GOLOMB("Golomb"),
    ELIASGAMMA("Elias-Gamma"),
    FIBONACCI("Fibonacci"),
    UNARIA("Unária"),
    DELTA("Delta");

    private String name;

    Algorithms(final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return this.getName();
    }
}