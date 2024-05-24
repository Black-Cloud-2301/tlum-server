package com.kltn.configservice.business.gateway;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.LinkedHashMap;

@Document("gateway")
@Setter @Getter
@AllArgsConstructor
public class Gateway {
    @Id
    private String id;
    private String label;
    private String profile;
    private LinkedHashMap<String, Object> source;
}
