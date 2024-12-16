package lk.pizzapalace.backend.service.strategy;

import lk.pizzapalace.backend.entity.PaymentEntity;

public interface PaymentStrategy {
    void processPayment(PaymentEntity payment);
}

class CreditCardPayment implements PaymentStrategy {
    @Override
    public void processPayment(PaymentEntity payment) {
        System.out.println("Processing credit card payment of $" + payment.getPrice());
    }
}

class DigitalWalletPayment implements PaymentStrategy {
    @Override
    public void processPayment(PaymentEntity payment) {
        System.out.println("Processing digital wallet payment of $" + payment.getPrice());
    }
}