package com.project.gamemarket.dto.customer;


import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

@Value
@Builder(toBuilder = true)
@Jacksonized
public class CustomerDetailsListDto {

    List<CustomerDetailsEntry> customerDetailsEntries;
}
