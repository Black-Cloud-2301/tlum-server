package com.kltn.individualservice.config._init;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class CollectionInit {

    private final List<DataImporter> importers;

    @Autowired
    public CollectionInit(List<DataImporter> importers) {
        this.importers = importers;
    }

    @PostConstruct
    public void init() {
        importers.forEach(DataImporter::importData);
    }
}
