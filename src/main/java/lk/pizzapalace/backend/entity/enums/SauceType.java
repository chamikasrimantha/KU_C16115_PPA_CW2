package lk.pizzapalace.backend.entity.enums;

public enum SauceType {
    TOMATO(200), GARLIC(250), PESTO(300);

    private final double price;

    SauceType(double price) {
        this.price = price;
    }

    public double getPrice() {
        return price;
    }
}
