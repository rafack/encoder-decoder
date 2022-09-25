package com.Encoder_Decoder;

import java.util.Arrays;

public enum Algorithms {
    GOLOMB(0, "Golomb"),
    ELIASGAMMA(1,"Elias-Gamma"),
    FIBONACCI(2,"Fibonacci"),
    UNARIA(3,"Unária"),
    DELTA(4,"Delta");

    private int identifier;
    private String name;

    Algorithms(final int identifier, final String name) {
        this.identifier = identifier;
        this.name = name;
    }

    public int getIdentifier() {
        return identifier;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return this.getName();
    }

    public static Algorithms getValueByName(String name) {
        return Arrays.stream(Algorithms.values())
                .filter(codingType -> codingType.getName().equals(name))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("CodingType not found: " + name));
    }
}