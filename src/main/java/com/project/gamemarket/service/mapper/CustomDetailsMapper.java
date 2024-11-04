package com.project.gamemarket.service.mapper;

import com.project.gamemarket.common.DeviceType;
import com.project.gamemarket.domain.CustomerDetails;
import com.project.gamemarket.dto.customer.CustomerDetailsDto;
import com.project.gamemarket.dto.customer.CustomerDetailsEntry;
import com.project.gamemarket.dto.customer.CustomerDetailsListDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CustomDetailsMapper {


    @Mapping(target = "name", source = "name")
    @Mapping(target = "phoneNumber", source = "phoneNumber")
    @Mapping(target = "email", source = "email")
    @Mapping(target = "region", source = "region")
    @Mapping(target = "deviceTypes", source = "deviceTypes", qualifiedByName = "toDeviceTypeString")
    CustomerDetailsDto toCustomerDetailsDto(CustomerDetails customerDetails);

    default CustomerDetailsListDto toCustomerDetailsListDto(List<CustomerDetails> customerDetails) {
        return CustomerDetailsListDto.builder()
                .customerDetailsEntries(toCustomerDetailsEntry(customerDetails))
                .build();
    }

    List<CustomerDetailsEntry> toCustomerDetailsEntry(List<CustomerDetails> customerDetails);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "phoneNumber", source = "phoneNumber")
    @Mapping(target = "email", source = "email")
    @Mapping(target = "region", source = "region")
    @Mapping(target = "deviceTypes", source = "deviceTypes", qualifiedByName = "toDeviceTypeString")
    CustomerDetailsEntry toCustomerDetailsEntry(CustomerDetails customerDetails);

    @Named("toDeviceTypeString")
    default List<String> toDeviceTypeString(List<DeviceType> devices) {
        return devices.stream().map(device -> device.name().toLowerCase()).toList();
    }
}