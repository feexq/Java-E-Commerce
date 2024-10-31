package com.project.gamemarket.objects;

import com.project.gamemarket.common.DeviceType;
import com.project.gamemarket.domain.CustomerDetails;
import com.project.gamemarket.dto.customer.CustomerDetailsDto;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BuildCustomers {

    public CustomerDetailsDto buildCustomerDetailsDto() {
        return CustomerDetailsDto.builder()
                .name("Jack Spring")
                .email("jacksrping@gmail.com")
                .phoneNumber("+1234567890")
                .region("Ukraine")
                .deviceTypes(List.of("console", "pc"))
                .build();
    }

    public CustomerDetails buildCustomerDetails() {
        return CustomerDetails.builder()
                .id(1L)
                .name("Jack Spring")
                .email("jacksrping@gmail.com")
                .phoneNumber("+1234567890")
                .region("Ukraine")
                .deviceTypes(List.of(DeviceType.CONSOLE, DeviceType.PC))
                .build();
    }

    public CustomerDetails buildInvalidCustomerDetails() {
        return CustomerDetails.builder()
                .name("")
                .email("invalidEmail")
                .phoneNumber("12345")
                .region("A very long region name that exceeds the maximum allowed size of 255 characters, so this will cause a validation failure. Test message to many symbols in this a lot of message that no mean omg where is exception aaaaaaaaaaaaaaaaaaaaaaaaaaaa help help help help help help help help help help help")
                .deviceTypes(List.of(DeviceType.CONSOLE,DeviceType.PC))
                .build();
    }

}
