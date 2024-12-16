package lk.pizzapalace.backend.service.builder;

import lk.pizzapalace.backend.entity.OrderEntity;
import lk.pizzapalace.backend.entity.PaymentEntity;
import lk.pizzapalace.backend.entity.PizzaEntity;
import lk.pizzapalace.backend.entity.UserEntity;
import lk.pizzapalace.backend.entity.enums.DeliveryType;
import lk.pizzapalace.backend.entity.enums.OrderStatus;

public class OrderBuilder {
    private DeliveryType deliveryType;
    private OrderStatus status;
    private UserEntity user;
    private PizzaEntity pizza;
    private PaymentEntity payment;

    public OrderBuilder setDeliveryType(DeliveryType deliveryType) {
        this.deliveryType = deliveryType;
        return this;
    }

    public OrderBuilder setStatus(OrderStatus status) {
        this.status = status;
        return this;
    }

    public OrderBuilder setUser(UserEntity user) {
        this.user = user;
        return this;
    }

    public OrderBuilder setPizza(PizzaEntity pizza) {
        this.pizza = pizza;
        return this;
    }

    public OrderBuilder setPayment(PaymentEntity payment) {
        this.payment = payment;
        return this;
    }

    public OrderEntity build() {
        OrderEntity order = new OrderEntity();
        order.setDeliveryType(deliveryType);
        order.setStatus(status);
        order.setUserEntity(user);
        order.setPizzaEntity(pizza);
        order.setPaymentEntity(payment);
        return order;
    }
}
