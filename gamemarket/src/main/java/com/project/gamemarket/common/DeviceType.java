package com.project.gamemarket.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum DeviceType {

    PC("PC"),
    MOBILE("Mobile"),
    CONSOLE("Console"),
    NINTENDO_SWITCH("NintendoSwitch"),;

    private final String name;

    public static DeviceType fromName(String name) {
        for (DeviceType device : values()) {
            if (device.name.equalsIgnoreCase(name)) {
                return device;
            }
        }
        throw new IllegalArgumentException(String.format("Device type '%s' not found", name));
    }
}
