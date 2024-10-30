package com.project.gamemarket.web;

import com.project.gamemarket.domain.order.OrderContext;
import com.project.gamemarket.dto.order.PlaceOrderRequestDto;
import com.project.gamemarket.dto.order.PlaceOrderResponseDto;
import com.project.gamemarket.service.OrderService;
import com.project.gamemarket.web.mapper.OrderDtoMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@Validated
@RequestMapping("/api/v1/{customerReference}/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final OrderDtoMapper orderDtoMapper;

    @PostMapping("/{cartId}")
    public ResponseEntity<PlaceOrderResponseDto> placeOrder(
            @PathVariable("customerReference") String customerReference,
            @PathVariable("cartId") String cartId,
            @RequestBody @Valid PlaceOrderRequestDto placeOrderDto) {
        log.info("Placing the order for cart with id : {}", cartId);
        OrderContext context = orderDtoMapper.toOrderContext(cartId, customerReference, placeOrderDto);
        return ResponseEntity.ok(orderDtoMapper.toPlaceOrderResponseDto(orderService.placeOrder(context)));
    }
}