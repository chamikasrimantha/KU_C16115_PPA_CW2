package lk.pizzapalace.backend.service;

import java.util.ArrayList;
import java.util.List;

import lk.pizzapalace.backend.entity.PizzaEntity;

public class PizzaServiceImpl implements PizzaService {

    private final List<PizzaEntity> pizzaMenu = new ArrayList<>();

    @Override
    public PizzaEntity customizePizza(PizzaEntity pizza) {
        pizzaMenu.add(pizza);
        return pizza;
    }

    @Override
    public void processPizzaCustomization(PizzaEntity pizza) {
        pizzaMenu.add(pizza);
    }

    @Override
    public PizzaEntity getPizzaById(Long id) {
        return pizzaMenu.stream()
                .filter(a -> a.getId().equals(id))
                .findFirst()
                .orElse(null);
    }
}
