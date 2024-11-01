package com.project.gamemarket.service.exception;


public class KeyActivationFailedProcessActivation extends RuntimeException {

    public static final String KEY_S_ACTIVATION_FAILED_PROCESS_ACTIVATION = "Key %s activation failed";

    public KeyActivationFailedProcessActivation(String key) { super(String.format(KEY_S_ACTIVATION_FAILED_PROCESS_ACTIVATION, key)); }
}
