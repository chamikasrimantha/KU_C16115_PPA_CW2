package lk.pizzapalace.backend.service.decorator;

import lk.pizzapalace.backend.entity.OrderEntity;

public abstract class OrderDecorator {
    protected OrderEntity order;

    public OrderDecorator(OrderEntity order) {
        this.order = order;
    }

    public abstract void enhance();
}

class ExtraToppingsDecorator extends OrderDecorator {
    public ExtraToppingsDecorator(OrderEntity order) {
        super(order);
    }

    @Override
    public void enhance() {
        System.out.println("Adding extra toppings to order.");
    }
}

class SpecialPackagingDecorator extends OrderDecorator {
    public SpecialPackagingDecorator(OrderEntity order) {
        super(order);
    }

    @Override
    public void enhance() {
        System.out.println("Adding special packaging to order.");
    }
}