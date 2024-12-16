package lk.pizzapalace.backend.service.chainofresponsibility;

import lk.pizzapalace.backend.entity.PizzaEntity;

public abstract class OrderCustomizationHandler {
    protected OrderCustomizationHandler nextHandler;

    public void setNextHandler(OrderCustomizationHandler nextHandler) {
        this.nextHandler = nextHandler;
    }

    public void handleCustomization(PizzaEntity pizza) {
        if (nextHandler != null) {
            nextHandler.handleCustomization(pizza);
        }
    }
}
