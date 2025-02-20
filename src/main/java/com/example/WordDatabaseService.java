package com.example;

import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.regex.Pattern;
import javax.inject.Inject;

public class WordDatabaseService implements DatabaseService {
    private final BloomFilter<String> bloomFilter;

    @Inject
    public WordDatabaseService() {
        // Initialize Bloom filter: 100K expected words, 1% false positive rate
        this.bloomFilter = BloomFilter.create(
            Funnels.stringFunnel(StandardCharsets.UTF_8),
            100_000,
            0.01
        );
        loadWordsFromFile();
    }

    private void loadWordsFromFile() {
        try {
            URL url = new URL("https://www.gutenberg.org/cache/epub/11/pg11.txt");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
                String line;
                // Split by non-word characters (e.g., spaces, punctuation)
                Pattern wordSplitter = Pattern.compile("\\W+");
                while ((line = reader.readLine()) != null) {
                    String[] words = wordSplitter.split(line);
                    for (String word : words) {
                        if (!word.isEmpty()) {
                            bloomFilter.put(word.toLowerCase()); // Case-insensitive
                        }
                    }
                }
            }
            conn.disconnect();
            System.out.println("Loaded words into Bloom filter.");
        } catch (Exception e) {
            System.err.println("Error loading file: " + e.getMessage());
        }
    }

    @Override
    public boolean isUsernameTaken(String word) {
        return bloomFilter.mightContain(word.toLowerCase());
    }
}