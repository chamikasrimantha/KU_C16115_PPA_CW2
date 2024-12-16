package lk.pizzapalace.backend.service.decorator;

import lk.pizzapalace.backend.entity.OrderEntity;

public class BaseOrderDecorator extends OrderDecorator {
    public BaseOrderDecorator(OrderEntity order) {
        super(order);
    }

    @Override
    public void enhance() {
        // Base implementation does nothing
        System.out.println("Base order, no additional enhancements.");
    }
}
