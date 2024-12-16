package lk.pizzapalace.backend.service.state;

import lk.pizzapalace.backend.entity.OrderEntity;
import lk.pizzapalace.backend.entity.enums.OrderStatus;

public interface OrderState {
    void next(OrderEntity order);
    void previous(OrderEntity order);
    void printStatus();
}

class OrderReceivedState implements OrderState {
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

class OrderPreparedState implements OrderState {
    @Override
    public void next(OrderEntity order) {
        order.setStatus(OrderStatus.READY_FOR_PICKUP);
        System.out.println("Order is ready for pickup.");
    }

    @Override
    public void previous(OrderEntity order) {
        order.setStatus(OrderStatus.ORDER_PREPARING_STARTED);
        System.out.println("Order is being prepared again.");
    }

    @Override
    public void printStatus() {
        System.out.println("Order is prepared.");
    }
}