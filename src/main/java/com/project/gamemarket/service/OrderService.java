package com.project.gamemarket.service;

import com.project.gamemarket.domain.order.Order;

import java.util.List;
import java.util.UUID;

public interface OrderService {
    Order placeOrder(Order order);
    Order getOrderByCartId(String cartId);
    List<Order> getAllOrders();
    void deleteOrder(String cartId);

}
