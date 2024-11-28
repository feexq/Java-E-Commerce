package com.project.gamemarket.service.mapper;

import com.project.gamemarket.domain.ProductDetails;
import com.project.gamemarket.domain.order.Order;
import com.project.gamemarket.domain.order.OrderEntry;
import com.project.gamemarket.dto.order.OrderDto;
import com.project.gamemarket.dto.order.OrderEntryDto;
import com.project.gamemarket.dto.order.OrderListDto;
import com.project.gamemarket.dto.order.OrderRequestDto;
import com.project.gamemarket.repository.entity.CustomerEntity;
import com.project.gamemarket.repository.entity.OrderEntity;
import com.project.gamemarket.repository.entity.OrderEntryEntity;
import org.aspectj.weaver.ast.Or;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.UUID;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    @Mapping(target = "cartId", source = "cartId")
    @Mapping(target = "total", source = "orderDto.total")
    @Mapping(target = "customerId", source = "customerReference")
    @Mapping(target = "entries", source = "orderDto.entries")
    Order toOrder(String cartId, String customerReference, OrderRequestDto orderDto);

    @Mapping(target = "cartId", source = "cart_id")
    @Mapping(target = "total", source = "total_price")
    @Mapping(target = "customerId", source = "customer")
    @Mapping(target = "entries", source = "order_entries")
    Order toOrder(OrderEntity orderEntity);

    @Mapping(source = "cartId", target = "cart_id")
    @Mapping(source = "total", target = "total_price")
    @Mapping(source = "entries", target = "order_entries")
    OrderEntity toOrderEntity(Order order);

    List<Order> toOrderList(List<OrderEntity> orderEntities);

    default OrderEntry toOrderEntry(OrderEntryEntity orderEntryEntity) {
        if (orderEntryEntity == null) {
            return null;
        }

        return OrderEntry.builder()
                .gameType(ProductDetails.builder()
                        .title(orderEntryEntity.getProductEntity().getTitle())
                        .build())
                .quantity(orderEntryEntity.getQuantity())
                .build();
    }

    List<OrderEntry> toOrderEntryList(List<OrderEntryEntity> orderEntryEntities);

    default OrderListDto toOrderListDto(List<Order> orders) {
        return OrderListDto.builder()
                .orders((toOrderDtoList(orders)))
                .build();
    }

    List<OrderDto> toOrderDtoList(List<Order> orders);

    List<OrderEntryDto> toOrderEntryDto(List<OrderEntry> orderEntries);

    default UUID mapCustomerId(CustomerEntity customerEntity) {
        return customerEntity != null ? customerEntity.getCustomerReference() : null;
    }

    OrderDto toOrderDto(Order order);

    default ProductDetails map(String gameType) {
        return ProductDetails.builder()
                .title(gameType)
                .build();
    }

    default String map(ProductDetails productDetails) {
        return productDetails.getTitle();
    }
}
