package lk.pizzapalace.backend.service.decorator;

public class ExtraToppingsDecorator extends OrderDecorator {
    public ExtraToppingsDecorator(OrderDecorator decorator) {
        super(decorator.order); // Pass the original order object
    }

    @Override
    public void enhance() {
        System.out.println("Adding extra toppings to order.");
        // Call the next enhancement in the chain
    }
}