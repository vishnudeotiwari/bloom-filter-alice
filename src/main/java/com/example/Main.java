package com.example;

import com.google.inject.Guice;
import com.google.inject.Injector;

public class Main {
    public static void main(String[] args) {
        // Record start time
        long startTime = System.nanoTime();

        // Record initial memory usage
        Runtime runtime = Runtime.getRuntime();
        long initialMemory = runtime.totalMemory() - runtime.freeMemory();

        // Create Guice injector and run the program
        Injector injector = Guice.createInjector(new AppModule());
        WordChecker checker = injector.getInstance(WordChecker.class);

        // Test some words
        String[] wordsToCheck = {"alice", "wonderland", "xyzzy", "rabbit"};
        for (String word : wordsToCheck) {
            System.out.println(checker.checkWord(word));
        }

        // Record end time
        long endTime = System.nanoTime();
        double elapsedTimeMs = (endTime - startTime) / 1_000_000.0; // Convert to milliseconds

        // Record final memory usage
        long finalMemory = runtime.totalMemory() - runtime.freeMemory();
        long memoryUsedKB = (finalMemory - initialMemory) / 1024; // Convert to KB

        // Print performance metrics
        System.out.printf("Execution Time: %.2f ms%n", elapsedTimeMs);
        System.out.printf("Memory Used: %d KB%n", memoryUsedKB);
    }
}