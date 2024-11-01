//package com.project.gamemarket.service.mapper;
//
//import com.project.gamemarket.domain.order.OrderContext;
//import com.project.gamemarket.domain.payment.Payment;
//import org.mapstruct.Mapper;
//import org.mapstruct.Mapping;
//
//@Mapper(componentModel = "spring")
//public interface PaymentMapper {
//
//    @Mapping(target = "customerId", source = "customerId")
//    @Mapping(target = "cartId", source = "cartId")
//    @Mapping(target = "quantity", source = "totalPrice")
//    @Mapping(target = "paymentAssetId", ignore = true) // we can get it from somewhere else for example
//    Payment toPayment(OrderContext orderContext);
//}
