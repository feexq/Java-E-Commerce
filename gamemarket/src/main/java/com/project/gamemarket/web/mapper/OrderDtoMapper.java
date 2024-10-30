package com.project.gamemarket.web.mapper;

import com.project.gamemarket.common.GameType;
import com.project.gamemarket.domain.order.Order;
import com.project.gamemarket.domain.order.OrderContext;
import com.project.gamemarket.domain.order.OrderEntry;
import com.project.gamemarket.dto.order.OrderEntryDto;
import com.project.gamemarket.dto.order.PlaceOrderRequestDto;
import com.project.gamemarket.dto.order.PlaceOrderResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderDtoMapper {

    @Mapping(target = "cartId", source = "cartId")
    @Mapping(target = "totalPrice", source = "orderDto.totalPrice")
    @Mapping(target = "customerId", source = "customerId")
    @Mapping(target = "orderEntries", source = "orderDto.orderEntries")
    OrderContext toOrderContext(String cartId, String customerId, PlaceOrderRequestDto orderDto);

    @Mapping(target = "gameType", source = "gameType")
    @Mapping(target = "quantity", source = "quantity")
    OrderEntry toOrderEntry(OrderEntryDto orderEntryDto);

    default GameType toGameType(String gameType) {
        return GameType.fromName(gameType);
    }

    @Mapping(target = "orderId", source = "orderId")
    @Mapping(target = "transactionId", source = "transactionId")
    PlaceOrderResponseDto toPlaceOrderResponseDto(Order order);
}
