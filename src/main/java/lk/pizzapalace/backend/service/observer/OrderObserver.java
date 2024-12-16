package lk.pizzapalace.backend.service.observer;

import lk.pizzapalace.backend.entity.enums.OrderStatus;

public interface OrderObserver {
    void update(OrderStatus status);
}
