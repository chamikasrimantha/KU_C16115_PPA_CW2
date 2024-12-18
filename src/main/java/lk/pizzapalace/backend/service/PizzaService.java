package lk.pizzapalace.backend.service;

import lk.pizzapalace.backend.entity.PizzaEntity;

public interface PizzaService {
    // Pizza Customization
    PizzaEntity customizePizza(PizzaEntity pizza);
    void processPizzaCustomization(PizzaEntity pizza);
    PizzaEntity getPizzaById(Long id);
}
