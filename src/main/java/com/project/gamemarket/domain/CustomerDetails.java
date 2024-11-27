package com.project.gamemarket.domain;

import com.project.gamemarket.common.DeviceType;
import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class CustomerDetails {

    Long id;
    String name;
    String phoneNumber;
    String email;
    String region;
    List<DeviceType> deviceTypes;
}
