package lk.pizzapalace.backend.entity.enums;

public enum ToppingsType {
    PEPPERONI(150), MUSHROOMS(100), OLIVES(100), CHICKEN(200);

    private final double price;

    ToppingsType(double price) {
        this.price = price;
    }

    public double getPrice() {
        return price;
    }
}
