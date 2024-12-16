package lk.pizzapalace.backend.service.chainofresponsibility;

import lk.pizzapalace.backend.entity.PizzaEntity;

public class ToppingsCustomizationHandler extends OrderCustomizationHandler {
    @Override
    public void handleCustomization(PizzaEntity pizza) {
        System.out.println("Customizing toppings: " + pizza.getToppingsType());
        super.handleCustomization(pizza);
    }
}