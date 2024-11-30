package com.project.gamemarket.repository.projection;

import com.project.gamemarket.repository.entity.CustomerEntity;

import java.util.UUID;

public interface OrderSummary {
    Long getId();
    String getCart_id();
    Double getTotal_price();
    String getPayment_reference();
    UUID getCustomerReference();

}
