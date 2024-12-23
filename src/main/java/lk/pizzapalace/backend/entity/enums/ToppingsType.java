package lk.pizzapalace.backend.entity.enums;

public enum ToppingsType {
    PEPPERONI(200), MUSHROOMS(250), OLIVES(300), CHICKEN(350);

    private final double price;

    ToppingsType(double price) {
        this.price = price;
    }

    public double getPrice() {
        return price;
    }
}
