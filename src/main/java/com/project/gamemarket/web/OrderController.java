package com.project.gamemarket.web;

import com.project.gamemarket.domain.order.Order;
import com.project.gamemarket.dto.order.OrderDto;
import com.project.gamemarket.dto.order.OrderListDto;
import com.project.gamemarket.dto.order.OrderRequestDto;
import com.project.gamemarket.service.OrderService;
import com.project.gamemarket.service.mapper.OrderMapper;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Validated
@RequestMapping("/api/v1/orders")
@Slf4j
public class OrderController {

    private final OrderService orderService;
    private final OrderMapper orderMapper;

    public OrderController(OrderService orderService, OrderMapper orderMapper) {
        this.orderService = orderService;
        this.orderMapper = orderMapper;
    }

    @PostMapping("/{customerReference}/{cartId}")
    public ResponseEntity<OrderDto> createOrder(
            @PathVariable("customerReference") String customerReference,
            @PathVariable("cartId") String cartId,
            @Valid @RequestBody OrderRequestDto order) {
        Order placeOrder = orderMapper.toOrder(cartId,customerReference,order);
        log.info("Order created: " + placeOrder);
        return ResponseEntity.ok(orderMapper.toOrderDto(orderService.placeOrder(placeOrder)));

    }

    @GetMapping("/{cartId}")
    public ResponseEntity<OrderDto> getOrderById(
            @PathVariable("cartId") String cartId) {
        return ResponseEntity.ok(orderMapper.toOrderDto(orderService.getOrderByCartId(cartId)));
    }

    @GetMapping
    public ResponseEntity<OrderListDto> getAllOrders() {
        return ResponseEntity.ok(orderMapper.toOrderListDto(orderService.getAllOrders()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<OrderDto> deleteOrder(
            @PathVariable("id") String cartId) {
        orderService.deleteOrder(cartId);
        return ResponseEntity.noContent().build();
    }
}
