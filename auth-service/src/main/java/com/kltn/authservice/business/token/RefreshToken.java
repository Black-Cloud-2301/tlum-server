package com.kltn.authservice.business.token;

import com.kltn.authservice.business.user.User;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Setter
@Getter
@AllArgsConstructor
@Document("refresh-token")
@CompoundIndex(def = "{'userId': 1}", unique = true)
public class RefreshToken {
    @Id
    String id;
    String token;
    @Indexed(expireAfterSeconds = 0)
    Instant expiryDate;
    @DBRef
    User user;
    String ip;
}
