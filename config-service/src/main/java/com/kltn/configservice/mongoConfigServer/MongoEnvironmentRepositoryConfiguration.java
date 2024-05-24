package com.kltn.configservice.mongoConfigServer;

import org.springframework.cloud.config.server.environment.EnvironmentRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.core.MongoTemplate;

@Configuration
@Profile("mongo")
public class MongoEnvironmentRepositoryConfiguration {

    private final MongoTemplate mongoTemplate;

    public MongoEnvironmentRepositoryConfiguration(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Bean
    public EnvironmentRepository environmentRepository() {
        return new MongoEnvironmentRepository(mongoTemplate);
    }

}
