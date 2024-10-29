package com.project.gamemarket.service;

import com.project.gamemarket.domain.payment.Payment;
import com.project.gamemarket.domain.payment.PaymentTransaction;

public interface PaymentService {

    PaymentTransaction processPayment(Payment payment);

}
