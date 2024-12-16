package lk.pizzapalace.backend.service.chainofresponsibility;

import lk.pizzapalace.backend.entity.PizzaEntity;

public class CrustCustomizationHandler extends OrderCustomizationHandler {
    @Override
    public void handleCustomization(PizzaEntity pizza) {
        System.out.println("Customizing crust: " + pizza.getCrustType());
        super.handleCustomization(pizza);
    }
}

