package com.example;

import javax.inject.Inject;

public class WordChecker {
    private final DatabaseService dbService;

    @Inject
    public WordChecker(DatabaseService dbService) {
        this.dbService = dbService;
    }

    public String checkWord(String word) {
        if (dbService.isUsernameTaken(word)) {
            return "Word '" + word + "' might be in the file (possible false positive).";
        }
        return "Word '" + word + "' is definitely not in the file.";
    }
}