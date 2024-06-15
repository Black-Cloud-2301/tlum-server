package com.kltn.configservice.business.file;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface FileRepository extends MongoRepository<File, String> {
    Boolean existsByProfile(String profile);
}
