package com.project.gamemarket.service.mapper;

import com.project.gamemarket.common.DeviceType;
import com.project.gamemarket.domain.CustomerDetails;
import com.project.gamemarket.domain.ProductDetails;
import com.project.gamemarket.dto.customer.CustomerDetailsDto;
import com.project.gamemarket.dto.customer.CustomerDetailsEntry;
import com.project.gamemarket.dto.customer.CustomerDetailsListDto;
import com.project.gamemarket.repository.entity.CustomerEntity;
import com.project.gamemarket.repository.entity.ProductEntity;
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

    List<CustomerDetails> toCustomerDetailsList(List<CustomerEntity> customerEntities);

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

    @Mapping(target = "customerReference", expression = "java(java.util.UUID.randomUUID())")
    @Mapping(target = "device_type", source = "deviceTypes")
    CustomerEntity toCustomerEntity(CustomerDetails customerDetails);

    @Mapping(target = "deviceTypes", source = "device_type")
    CustomerDetails toCustomerDetails(CustomerEntity entity);

    @Mapping(source = "name", target = "name")
    @Mapping(source = "phoneNumber", target = "phoneNumber")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "region", target = "region")
    @Mapping(source = "deviceTypes", target = "deviceTypes", qualifiedByName = "toDeviceTypesFromString")
    CustomerDetails toCustomerDetails(CustomerDetailsDto customerDetailsDto);

    @Named("toDeviceTypesFromString")
    default List<DeviceType> toDeviceTypesFromString(List<String> deviceTypes) {
        return deviceTypes.stream().map(DeviceType::fromName).toList();
    }

}