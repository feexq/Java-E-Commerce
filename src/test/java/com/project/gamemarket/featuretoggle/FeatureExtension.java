package com.project.gamemarket.featuretoggle;

import com.project.gamemarket.featuretoggle.anotation.DisableFeature;
import com.project.gamemarket.featuretoggle.anotation.EnableFeature;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.core.env.Environment;
import org.springframework.test.context.junit.jupiter.SpringExtension;

public class FeatureExtension implements BeforeEachCallback, AfterEachCallback {

    @Override
    public void beforeEach(ExtensionContext context) {

        context.getTestMethod().ifPresent(method -> {

            FeatureToggleService featureToggleService = getFeatureToggleService(context);

            if (method.isAnnotationPresent(EnableFeature.class)) {
                EnableFeature enabledFeatureToggleAnnotation = method.getAnnotation(EnableFeature.class);
                featureToggleService.setFeatureEnabled(enabledFeatureToggleAnnotation.value().getFeatureName());
            } else if (method.isAnnotationPresent(DisableFeature.class)) {
                DisableFeature disabledFeatureToggleAnnotation = method.getAnnotation(DisableFeature.class);
                featureToggleService.setFeatureDisable(disabledFeatureToggleAnnotation.value().getFeatureName());
            }
        });
    }

    @Override
    public void afterEach(ExtensionContext context) {
        context.getTestMethod().ifPresent(method -> {
            String featureName = null;

            if (method.isAnnotationPresent(EnableFeature.class)) {
                EnableFeature enabledFeatureToggleAnnotation = method.getAnnotation(EnableFeature.class);
                featureName = enabledFeatureToggleAnnotation.value().getFeatureName();
            } else if (method.isAnnotationPresent(DisableFeature.class)) {
                DisableFeature disabledFeatureToggleAnnotation = method.getAnnotation(DisableFeature.class);
                featureName = disabledFeatureToggleAnnotation.value().getFeatureName();
            }
            if (featureName != null) {
                FeatureToggleService featureToggleService = getFeatureToggleService(context);
                if (getFeatureNamePropertyAsBoolean(context, featureName)) {
                    featureToggleService.setFeatureEnabled(featureName);
                } else {
                    featureToggleService.setFeatureDisable(featureName);
                }
            }
        });
    }

    private boolean getFeatureNamePropertyAsBoolean(ExtensionContext context, String featureName) {
        Environment environment = SpringExtension.getApplicationContext(context).getEnvironment();
        return environment.getProperty("application.feature.toggles." + featureName, Boolean.class, Boolean.FALSE);
    }

    private FeatureToggleService getFeatureToggleService(ExtensionContext context) {
        return SpringExtension.getApplicationContext(context).getBean(FeatureToggleService.class);
    }
}
