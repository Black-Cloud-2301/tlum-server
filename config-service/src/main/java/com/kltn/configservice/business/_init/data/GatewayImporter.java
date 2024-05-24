package com.kltn.configservice.business._init.data;

import com.kltn.configservice.business._init.DataImporter;
import com.kltn.configservice.business.gateway.DefaultGatewayConfig;
import com.kltn.configservice.business.gateway.Gateway;
import com.kltn.configservice.business.gateway.GatewayRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class GatewayImporter extends DataImporter {

    private final DefaultGatewayConfig defaultGatewayConfig;
    private final GatewayRepository gatewayRepository;

    @Value("${config.current-profile}")
    private String profile;

    public GatewayImporter(DefaultGatewayConfig defaultGatewayConfig, GatewayRepository gatewayRepository) {
        this.defaultGatewayConfig = defaultGatewayConfig;
        this.gatewayRepository = gatewayRepository;
    }

    @Override
    public void importData() {
        if (!gatewayRepository.existsByProfile(profile)) {
            Gateway config = new Gateway(null, "", profile, defaultGatewayConfig.defaultGatewayMap());
            gatewayRepository.save(config);
        }
    }
}
