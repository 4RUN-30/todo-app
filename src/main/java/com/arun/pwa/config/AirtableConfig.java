package com.arun.pwa.config;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AirtableConfig {

    private static final Logger logger = LoggerFactory.getLogger(AirtableConfig.class);

    @Value("${airtable.base-id}")
    private String baseId;

    @Value("${airtable.table-name}")
    private String tableName;

    @Value("${airtable.token}")
    private String token;

    public String getBaseId() {
        return baseId;
    }

    public String getTableName() {
        return tableName;
    }

    public String getToken() {
        return token;
    }

    // Log non-secret parts of configuration after bean creation
    @Override
    public String toString() {
        return "AirtableConfig{baseId='" + baseId + "', tableName='" + tableName + "'}";
    }

    // Log configuration once the bean is ready
    @PostConstruct
    public void init() {
        logger.info("Loaded airtable configuration: {}", this.toString());
    }
}
