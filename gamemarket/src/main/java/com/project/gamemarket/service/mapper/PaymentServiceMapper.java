//package com.project.gamemarket.service.mapper;
//
//import com.project.gamemarket.domain.payment.Payment;
//import com.project.gamemarket.domain.payment.PaymentTransaction;
//import com.project.gamemarket.dto.payment.PaymentClientRequestDto;
//import com.project.gamemarket.dto.payment.PaymentClientResponseDto;
//import org.mapstruct.Mapper;
//import org.mapstruct.Mapping;
//
//@Mapper(componentModel = "spring")
//public interface PaymentServiceMapper {
//
//    @Mapping(target = "customerId", source = "customerId")
//    @Mapping(target = "paymentAssetId", source = "paymentAssetId")
//    @Mapping(target = "quantity", source = "quantity")
//    PaymentClientRequestDto toPaymentClientRequestDto(Payment payment);
//
//
//    @Mapping(target = "id", source = "paymentClientResponseDto.uuid")
//    @Mapping(target = "status", source = "paymentClientResponseDto.status")
//    @Mapping(target = "cartId", source = "cartId")
//    PaymentTransaction toPaymentTransaction(String cartId, PaymentClientResponseDto paymentClientResponseDto);
//}
//
