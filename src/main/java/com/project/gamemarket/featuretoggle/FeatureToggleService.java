package com.project.gamemarket.featuretoggle;

import com.project.gamemarket.config.FeatureToggleProperties;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;

@Service
public class FeatureToggleService {

    private final ConcurrentHashMap<String, Boolean> featureToggleMap;


    public FeatureToggleService(FeatureToggleProperties featureToggleProperties) {
        featureToggleMap = new ConcurrentHashMap<>(featureToggleProperties.getToggles());
    }

    public boolean isFeatureEnabled(String featureName) {
        return featureToggleMap.getOrDefault(featureName, false);
    }

    public void setFeatureEnabled(String featureName) {
        featureToggleMap.put(featureName, true);
    }

    public void setFeatureDisable(String featureName) {
        featureToggleMap.put(featureName, false);
    }
}
