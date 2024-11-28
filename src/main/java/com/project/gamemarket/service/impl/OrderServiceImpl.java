package com.project.gamemarket.service.impl;


import com.project.gamemarket.domain.ProductDetails;
import com.project.gamemarket.domain.order.Order;
import com.project.gamemarket.domain.order.OrderEntry;
import com.project.gamemarket.repository.CustomerRepository;
import com.project.gamemarket.repository.OrderRepository;
import com.project.gamemarket.repository.ProductRepository;
import com.project.gamemarket.repository.entity.CustomerEntity;
import com.project.gamemarket.repository.entity.OrderEntity;
import com.project.gamemarket.repository.entity.OrderEntryEntity;
import com.project.gamemarket.repository.entity.ProductEntity;
import com.project.gamemarket.service.OrderService;
import com.project.gamemarket.service.exception.CustomerNotFoundException;
import com.project.gamemarket.service.exception.OrderNotFoundException;
import com.project.gamemarket.service.exception.ProductNotFoundException;
import com.project.gamemarket.service.mapper.OrderMapper;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.PersistenceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {

    private final OrderMapper orderMapper;
    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;


    @Override
    @Transactional
    public Order placeOrder(Order order) {
        CustomerEntity customerEntity = customerRepository.naturalId(order.getCustomerId())
                .orElseThrow(() -> new CustomerNotFoundException(order.getCustomerId()));

        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setCart_id(order.getCartId());
        orderEntity.setCustomer(customerEntity);

        List<OrderEntryEntity> orderEntries = new ArrayList<>();
        double totalPrice = 0.0;

        for (OrderEntry entry : order.getEntries()) {
            ProductEntity productEntity = productRepository.findByTitleIgnoreCase(entry.getGameType().getTitle())
                    .orElseThrow(() -> new ProductNotFoundException(entry.getGameType().getTitle()));

            OrderEntryEntity orderEntryEntity = new OrderEntryEntity();
            orderEntryEntity.setProductEntity(productEntity);
            orderEntryEntity.setQuantity(entry.getQuantity());
            orderEntryEntity.setPrice(productEntity.getPrice() * entry.getQuantity());
            orderEntryEntity.setOrder_id(orderEntity);

            orderEntries.add(orderEntryEntity);
            totalPrice += orderEntryEntity.getPrice();
        }

        orderEntity.setTotal_price(totalPrice);
        orderEntity.setOrder_entries(orderEntries);
        orderEntity.setPayment_reference("test_payment_reference");

        OrderEntity savedOrder = orderRepository.save(orderEntity);

        return Order.builder()
                .cartId(savedOrder.getCart_id())
                .customerId(savedOrder.getCustomer().getCustomerReference())
                .total(savedOrder.getTotal_price())
                .entries(savedOrder.getOrder_entries().stream()
                        .map(entry -> OrderEntry.builder()
                                .gameType(ProductDetails.builder()
                                        .title(entry.getProductEntity().getTitle())
                                        .build())
                                .quantity(entry.getQuantity())
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public Order getOrderByCartId(String cartId) {
        return orderRepository.naturalId(cartId)
                .map(orderMapper::toOrder)
                .orElseThrow(() -> new OrderNotFoundException(cartId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Order> getAllOrders() {
        return orderMapper.toOrderList(orderRepository.findAll());
    }

    @Override
    @Transactional
    public void deleteOrder(Long id) {
        try {
            orderRepository.deleteById(id);
        } catch (Exception e) {
            log.error("Error while deleting product with id {}", id);
            throw new PersistenceException(e);
        }
    }

}
