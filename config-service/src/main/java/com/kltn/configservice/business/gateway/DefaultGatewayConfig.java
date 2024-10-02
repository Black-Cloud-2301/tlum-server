package com.kltn.configservice.business.gateway;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;

@Component
public class DefaultGatewayConfig {
    private final ObjectMapper objectMapper;

    public DefaultGatewayConfig(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    private ObjectNode defaultGateway() {
        ObjectNode gatewayNode = newObjectNode();
        gatewayNode.set("routes",defaultRoutes());
        gatewayNode.set("discovery", discovery());
        gatewayNode.set("default-filters", defaultFilters());
        gatewayNode.set("global-cors", globalCors());

        String listApiSecured = "/login, /refresh, /users/create";

        return newObjectNode()
                .put("list-api-secured", listApiSecured)
                .set("spring", newObjectNode()
                        .set("cloud", newObjectNode()
                                .set("gateway", gatewayNode)));
    }

    private ArrayNode defaultRoutes() {
        return newArrayNode()
                .add(generateRoute("config-service", "config"))
                .add(generateRoute("auth-service", "auth"))
                .add(generateRoute("file-service", "file"))
                .add(generateRoute("individual-service", "individual"))
                .add(generateRoute("notification", "notification"));
    }

    private ObjectNode discovery() {
        return newObjectNode()
                .set("locator", newObjectNode()
                        .put("enabled", true)
                        .put("lower-case-service-id", true));
    }

    private ArrayNode defaultFilters() {
        return newArrayNode().add("DedupeResponseHeader=Access-Control-Allow-Credentials Access-Control-Allow-Origin");
    }

    private ObjectNode globalCors() {
        return newObjectNode()
                .set("corsConfigurations", newObjectNode()
                        .set("[/**]", newObjectNode()
                                .put("allowedOrigins", "*")
                                .put("allowedMethods", "*")
                                .put("allowedHeaders", "*")));
    }

    ObjectNode generateRoute(String routeId, String path) {
        ObjectNode route = newObjectNode();
        route.put("id", routeId)
                .put("uri", "lb://" + routeId);

        ArrayNode predicates = newArrayNode().add("Path=/" + (path != null ? path : routeId) + "/**");
        ArrayNode filters = newArrayNode().add("RewritePath=/" + (path != null ? path : routeId) + "/(?<segment>.*),/${segment}");

        route.set("predicates", predicates);
        route.set("filters", filters);

        return route;
    }

    public LinkedHashMap<String, Object> defaultGatewayMap() {
        return objectMapper.convertValue(defaultGateway(), new TypeReference<>() {});
    }

    private ObjectNode newObjectNode() {
        return objectMapper.createObjectNode();
    }

    private ArrayNode newArrayNode() {
        return objectMapper.createArrayNode();
    }
}
