package lk.pizzapalace.backend.entity.enums;

public enum CheeseType {
    MOZZARELLA(300), CHEDDAR(350), VEGAN(400);

    private final double price;

    CheeseType(double price) {
        this.price = price;
    }

    public double getPrice() {
        return price;
    }
}
