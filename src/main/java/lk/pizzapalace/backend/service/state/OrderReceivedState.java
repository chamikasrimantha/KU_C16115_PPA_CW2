package lk.pizzapalace.backend.service.state;

import lk.pizzapalace.backend.entity.OrderEntity;
import lk.pizzapalace.backend.entity.enums.OrderStatus;

public class OrderReceivedState implements OrderState {
    @Override
    public void next(OrderEntity order) {
        order.setStatus(OrderStatus.ORDER_PREPARING_STARTED);
        System.out.println("Order is now being prepared.");
    }

    @Override
    public void previous(OrderEntity order) {
        System.out.println("Order is in the initial state. Cannot go back.");
    }

    @Override
    public void printStatus() {
        System.out.println("Order is received.");
    }
}
