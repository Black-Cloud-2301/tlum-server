package com.kltn.configservice.business.auth;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface AuthRepository extends MongoRepository<Auth, String> {
    Boolean existsByProfile(String profile);
}
