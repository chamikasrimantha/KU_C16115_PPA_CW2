package lk.pizzapalace.backend.entity.enums;

public enum CrustType {
    THIN_CRUST(600), THICK_CRUST(700), CHEESE_STUFFED(800);

    private final double price;

    CrustType(double price) {
        this.price = price;
    }

    public double getPrice() {
        return price;
    }
}
