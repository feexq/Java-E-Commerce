package com.project.gamemarket.service.impl;


import com.project.gamemarket.domain.order.Order;
import com.project.gamemarket.domain.order.OrderEntry;
import com.project.gamemarket.repository.CustomerRepository;
import com.project.gamemarket.repository.OrderRepository;
import com.project.gamemarket.repository.ProductRepository;
import com.project.gamemarket.repository.entity.CustomerEntity;
import com.project.gamemarket.repository.entity.OrderEntity;
import com.project.gamemarket.repository.entity.OrderEntryEntity;
import com.project.gamemarket.repository.entity.ProductEntity;
import com.project.gamemarket.repository.projection.OrderSummary;
import com.project.gamemarket.service.OrderService;
import com.project.gamemarket.service.exception.CustomerNotFoundException;
import com.project.gamemarket.service.exception.OrderNotFoundException;
import com.project.gamemarket.service.exception.ProductNotFoundException;
import com.project.gamemarket.service.mapper.OrderMapper;
import jakarta.persistence.PersistenceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

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

        try {
            return orderMapper.toOrderEntity(orderRepository.save(orderEntity));
        } catch (Exception e) {
            throw new PersistenceException(e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Order getOrderByCartId(String cartId) {
        return orderRepository.naturalId(cartId)
                .map(orderMapper::toOrderEntity)
                .orElseThrow(() -> new OrderNotFoundException(cartId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Order> getAllOrders() {
        return orderRepository.findAllCustom().stream()
                .map(orderMapper::toOrderFromOrderSummary)
                .toList();
    }

    @Override
    @Transactional
    public void deleteOrder(String cartId) {
        try {
            orderRepository.deleteByNaturalId(cartId);
        } catch (Exception e) {
            log.error("Error while deleting product with id {}", cartId);
            throw new PersistenceException(e);
        }
    }

}
