package lk.pizzapalace.backend.entity.enums;

public enum SauceType {
    TOMATO(100), GARLIC(120), PESTO(150);

    private final double price;

    SauceType(double price) {
        this.price = price;
    }

    public double getPrice() {
        return price;
    }
}
