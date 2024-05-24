package com.kltn.configservice.business.gateway;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface GatewayRepository extends MongoRepository<Gateway, String> {

    Optional<Gateway> findFirstByProfile(String profile);

    Boolean existsByProfile(String profile);
}
