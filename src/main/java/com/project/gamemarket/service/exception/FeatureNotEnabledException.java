package com.project.gamemarket.service.exception;

public class FeatureNotEnabledException extends RuntimeException{
    public static final String FEATURE_NOT_ENABLED_MESSAGE = "Feature %s not enabled";

    public FeatureNotEnabledException(String name) { super(String.format(FEATURE_NOT_ENABLED_MESSAGE, name)); }
}
