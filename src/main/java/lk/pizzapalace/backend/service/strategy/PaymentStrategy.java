package lk.pizzapalace.backend.service.strategy;

import lk.pizzapalace.backend.entity.PaymentEntity;

public interface PaymentStrategy {
    void processPayment(PaymentEntity payment);
}
