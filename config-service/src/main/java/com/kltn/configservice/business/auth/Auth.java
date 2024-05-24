package com.kltn.configservice.business.auth;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("auth-service")
public class Auth {
    @Id
    String id;
    String label;
    String profile;
    Source source;

    public Auth(String id, String label, String profile, Long jwtExpiration, Integer refreshExpirationDay, Integer seed, String jwtSecret) {
        this.id = id;
        this.label = label;
        this.profile = profile;
        this.source = new Source(jwtExpiration, refreshExpirationDay, seed, jwtSecret);
    }

    static class Source {
        AuthToken authToken;

        Source(Long jwtExpiration, Integer refreshExpirationDay, Integer seed, String jwtSecret) {
            this.authToken = new AuthToken(jwtExpiration, refreshExpirationDay, seed, jwtSecret);
        }
    }

    static class AuthToken {
        Long jwtExpiration;
        Integer refreshExpirationDay;
        Key key;
        String jwtSecret;

        AuthToken(Long jwtExpiration, Integer refreshExpirationDay, Integer key, String jwtSecret) {
            this.jwtExpiration = jwtExpiration;
            this.refreshExpirationDay = refreshExpirationDay;
            this.key = new Key(key);
            this.jwtSecret = jwtSecret;
        }
    }

    static class Key {
        Integer seed;

        Key(Integer seed) {
            this.seed = seed;
        }
    }
}
