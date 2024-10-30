package com.project.gamemarket.service.impl;

import com.project.gamemarket.domain.order.Order;
import com.project.gamemarket.domain.order.OrderContext;
import com.project.gamemarket.domain.order.OrderEntry;
import com.project.gamemarket.domain.payment.PaymentTransaction;
import com.project.gamemarket.service.OrderService;
import com.project.gamemarket.service.PaymentService;
import com.project.gamemarket.service.exception.PaymentTransactionFailed;
import com.project.gamemarket.service.mapper.PaymentMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

import static com.project.gamemarket.common.PaymentStatus.FAIL;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final PaymentService paymentService;
    private final PaymentMapper paymentMapper;

    @Override
    public Order placeOrder(OrderContext orderContext) {
        log.info("Placing order for cart with id: {}", orderContext.getCartId());
        PaymentTransaction paymentTransaction = paymentService.processPayment(paymentMapper.toPayment(orderContext));
        if (FAIL.equals(paymentTransaction.getStatus())) {
            log.error("Payment failed");
            throw new PaymentTransactionFailed(paymentTransaction.getId(), orderContext.getCartId());
        }
        // TODO add mock for order service
        return createOrderMock(orderContext.getCartId(),
                orderContext.getOrderEntries(),
                orderContext.getTotalPrice(),
                orderContext.getCustomerId(),
                paymentTransaction.getId());
    }

    private Order createOrderMock(String cartId, List<OrderEntry> entries, Double total, String customerId, UUID transactionId) {
        return Order.builder()
                .orderId(UUID.randomUUID().toString())
                .transactionId(transactionId)
                .cartId(cartId)
                .entries(entries)
                .total(total)
                .customerId(customerId)
                .build();
    }
}
