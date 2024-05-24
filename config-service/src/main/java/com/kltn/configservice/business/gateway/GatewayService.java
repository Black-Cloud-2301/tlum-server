package com.kltn.configservice.business.gateway;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class GatewayService {
    private final GatewayRepository gatewayRepository;
    private final ObjectMapper objectMapper;
    private final DefaultGatewayConfig defaultGatewayConfig;

    public GatewayService(GatewayRepository gatewayRepository, ObjectMapper objectMapper, DefaultGatewayConfig defaultGatewayConfig) {
        this.gatewayRepository = gatewayRepository;
        this.objectMapper = objectMapper;
        this.defaultGatewayConfig = defaultGatewayConfig;
    }

    public ArrayNode addRoute(String profile, String routeId, String path) {
        return updateRoutes(profile, routes -> {
            for (JsonNode node : routes) {
                if (node.get("id").asText().equals(routeId)) {
                    throw new GatewayException("routeExist", "Route already exist");
                }
            }

            ObjectNode routeNode = defaultGatewayConfig.generateRoute(routeId, path);
            routes.add(routeNode);
            return routes;
        });
    }


    private ArrayNode updateRoutes(String profile, Function<ArrayNode, ArrayNode> updateFunction) {
        Gateway gateway = gatewayRepository.findFirstByProfile(profile).orElseThrow(() -> new GatewayException("profileNotExist", "Profile is not exist"));
        JsonNode node = objectMapper.convertValue(gateway.getSource(), JsonNode.class);
        ArrayNode routes = (ArrayNode) node.get("spring").get("cloud").get("gateway").get("routes");

        routes = updateFunction.apply(routes);

        ObjectNode gatewayObjectNode = (ObjectNode) node.get("spring").get("cloud").get("gateway");
        gatewayObjectNode.set("routes", routes);

        gateway.setSource(objectMapper.convertValue(node, new TypeReference<>() {
        }));
        gatewayRepository.save(gateway);

        return routes;
    }
}
