package lk.pizzapalace.backend.service.decorator;

public class SpecialPackagingDecorator extends OrderDecorator {
    public SpecialPackagingDecorator(OrderDecorator decorator) {
        super(decorator.order); // Pass the original order object
    }

    @Override
    public void enhance() {
        System.out.println("Adding special packaging to order.");
        // Call the next enhancement in the chain
    }
}
