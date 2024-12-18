package lk.pizzapalace.backend.service.strategy;

import lk.pizzapalace.backend.entity.PaymentEntity;

public class DigitalWalletPayment implements PaymentStrategy {
    @Override
    public void processPayment(PaymentEntity payment) {
        System.out.println("Processing digital wallet payment of Rs. " + payment.getPrice());
    }
}
