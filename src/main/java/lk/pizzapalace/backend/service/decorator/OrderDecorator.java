package lk.pizzapalace.backend.service.decorator;

import lk.pizzapalace.backend.entity.OrderEntity;

public abstract class OrderDecorator {
    protected OrderEntity order;

    public OrderDecorator(OrderEntity order) {
        this.order = order;
    }

    public abstract void enhance();
}
