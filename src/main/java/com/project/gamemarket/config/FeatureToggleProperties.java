package com.project.gamemarket.config;


import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Data
@NoArgsConstructor
@Configuration
@ConfigurationProperties(prefix = "application.feature")
public class FeatureToggleProperties {

    Map<String, Boolean> toggles;

}
