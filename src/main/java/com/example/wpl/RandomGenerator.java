package com.example.wpl;

import java.util.Random;

public class RandomGenerator {
    private static final Random random = new Random();

    // Method to generate a random integer between 0 and 999
    public static int generateRandomNumber() {
        return random.nextInt(1000);
    }

    // Method to generate a random double between 0.0 (inclusive) and 1.0 (exclusive)
    public static double generateRandomDouble() {
        return random.nextDouble();
    }
}
