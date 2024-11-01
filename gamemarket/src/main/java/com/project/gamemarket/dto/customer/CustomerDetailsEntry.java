package com.project.gamemarket.dto.customer;

import com.project.gamemarket.common.DeviceType;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import java.util.List;


@Value
@Builder
@Jacksonized
public class CustomerDetailsEntry {

    Long id;
    String name;
    String phoneNumber;
    String email;
    String region;
    List<String> deviceTypes;
}
