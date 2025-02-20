package com.example;

import com.google.inject.AbstractModule;

public class AppModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(DatabaseService.class).to(WordDatabaseService.class);
        bind(WordChecker.class);
    }
}