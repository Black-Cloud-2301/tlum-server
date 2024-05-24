package com.kltn.configservice.business.controller;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.kltn.configservice.business.gateway.GatewayService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/gateway")
public class GatewayController {

    private final GatewayService gatewayService;

    public GatewayController(GatewayService gatewayService) {
        this.gatewayService = gatewayService;
    }

    @PostMapping("/addRoute/{profile}")
    ArrayNode addRoute(@PathVariable String profile, String routeId, String path) {
        return gatewayService.addRoute(profile, routeId, path);
    }
}

