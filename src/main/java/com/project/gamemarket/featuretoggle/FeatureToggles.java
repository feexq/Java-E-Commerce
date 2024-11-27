package com.project.gamemarket.featuretoggle;

import lombok.Getter;

@Getter
public enum FeatureToggles {

    KEY_ACTIVATION("key-activation"),
    SUMMER_SALE("summer-sale"),
    HALLOWEEN("halloween");

    private final String featureName;

    FeatureToggles(String featureName) {
        this.featureName = featureName;
    }
}
