package lk.pizzapalace.backend.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import lk.pizzapalace.backend.entity.Admin;
import lk.pizzapalace.backend.entity.Customer;
import lk.pizzapalace.backend.entity.OrderEntity;
import lk.pizzapalace.backend.entity.PaymentEntity;
import lk.pizzapalace.backend.entity.PizzaEntity;
import lk.pizzapalace.backend.entity.PromotionEntity;
import lk.pizzapalace.backend.entity.RateEntity;
import lk.pizzapalace.backend.entity.UserEntity;
import lk.pizzapalace.backend.entity.enums.OrderStatus;
import lk.pizzapalace.backend.entity.enums.ToppingsType;
import lk.pizzapalace.backend.service.observer.OrderObserver;
import lk.pizzapalace.backend.service.strategy.PaymentStrategy;

public class OrderServiceImpl implements OrderService {
    
    private final List<UserEntity> users = new ArrayList<>();
    private final List<PizzaEntity> pizzaMenu = new ArrayList<>();
    private final List<PromotionEntity> promotions = new ArrayList<>();
    private final List<OrderEntity> orders = new ArrayList<>();
    private final List<OrderObserver> observers = new ArrayList<>();

    @Override
    public Customer registerCustomer(Customer customer) {
        users.add(customer);
        return customer;
    }

    @Override
    public Admin registerAdmin(Admin admin) {
        users.add(admin);
        return admin;
    }

    @Override
    public UserEntity login(UserEntity user) {
        return users.stream()
                .filter(u -> u.getEmail().equals(user.getEmail()) && u.getPassword().equals(user.getPassword()))
                .findFirst()
                .orElse(null);
    }

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
    public OrderEntity createOrder(OrderEntity order) {
        order.setStatus(OrderStatus.ORDER_RECEIVED);
        orders.add(order);
        notifyObservers(order);
        return order;
    }

    @Override
    public void placeOrder(OrderEntity order) {
        order.setStatus(OrderStatus.ORDER_PREPARING_STARTED);
        notifyObservers(order);
    }

    @Override
    public OrderEntity reviewOrder(OrderEntity order) {
        return orders.stream().filter(o -> o.getId().equals(order.getId())).findFirst().orElse(null);
    }

    @Override
    public void saveFavoritePizza(Customer customer, PizzaEntity pizza) {
        customer.getFavourites().add(pizza);
    }

    @Override
    public List<PizzaEntity> getFavoritePizzas(Customer customer) {
        return customer.getFavourites();
    }

    @Override
    public OrderStatus trackOrderStatus(OrderEntity order) {
        return order.getStatus();
    }

    @Override
    public void addOrderObserver(OrderObserver observer) {
        observers.add(observer);
    }

    @Override
    public void removeOrderObserver(OrderObserver observer) {
        observers.remove(observer);
    }

    @Override
    public void processPayment(PaymentEntity payment, PaymentStrategy strategy) {
        strategy.processPayment(payment);
    }

    @Override
    public void addLoyaltyPoints(Customer customer, double amount) {
        int points = (int) amount / 10; // Example: 1 point for every 10 currency spent
        customer.setLoyaltyPoints(customer.getLoyaltyPoints() + points);
    }

    @Override
    public int getLoyaltyPoints(Customer customer) {
        return customer.getLoyaltyPoints();
    }

    @Override
    public PromotionEntity addPromotion(PromotionEntity promotion) {
        promotions.add(promotion);
        return promotion;
    }

    @Override
    public List<PromotionEntity> getActivePromotions() {
        Date currentDate = new Date();
        return promotions.stream()
                .filter(p -> p.getStartDate().before(currentDate) && p.getEndDate().after(currentDate))
                .collect(Collectors.toList());
    }

    @Override
    public void provideFeedback(RateEntity rate) {
        rate.getOrderEntity().getRateEntities().add(rate);
    }

    @Override
    public List<RateEntity> getFeedbackForOrder(OrderEntity order) {
        return order.getRateEntities();
    }

    @Override
    public void enhanceOrder(OrderEntity order, boolean addToppings, boolean specialPackaging) {
        if (addToppings) {
            order.getPizzaEntity().setToppingsType(ToppingsType.CHICKEN); // Example topping enhancement
        }
        if (specialPackaging) {
            // Add logic for special packaging
        }
    }

    public void notifyObservers(OrderEntity order) {
        for (OrderObserver observer : observers) {
            observer.update(order.getStatus());
        }
    }    

}
