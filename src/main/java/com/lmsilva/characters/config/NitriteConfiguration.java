package com.lmsilva.characters.config;

import org.dizitart.no2.Nitrite;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;

@Configuration
public class NitriteConfiguration {
    @Value("${nitrite.characters.collection:characters}")
    private String collection;

    @Value("${nitrite.characters.dbLocation}")
    private String dbLocation;

    @Bean
    public Nitrite nitrite() {
        if (dbLocation.isEmpty()) {
            dbLocation = System.getProperty("java.io.tmp") + File.separator + "characters.db";
        }

        Nitrite db = Nitrite.builder()
                .compressed()
                .filePath(dbLocation)
                .openOrCreate();

        db.getCollection(collection);

        return db;
    }
}
