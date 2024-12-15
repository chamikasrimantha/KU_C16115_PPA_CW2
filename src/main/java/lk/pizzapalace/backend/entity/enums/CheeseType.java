package lk.pizzapalace.backend.entity.enums;

public enum CheeseType {
    MOZZARELLA(200), CHEDDAR(220), VEGAN(250);

    private final double price;

    CheeseType(double price) {
        this.price = price;
    }

    public double getPrice() {
        return price;
    }
}
