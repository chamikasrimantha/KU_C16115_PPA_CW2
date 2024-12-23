package lk.pizzapalace.backend.service.state;

import lk.pizzapalace.backend.entity.OrderEntity;

public interface OrderState {
    void next(OrderEntity order);
    void previous(OrderEntity order);
    void printStatus();
}
