package lk.pizzapalace.backend.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import lk.pizzapalace.backend.entity.Customer;
import lk.pizzapalace.backend.entity.FavouritePizzaEntity;
import lk.pizzapalace.backend.entity.OrderEntity;
import lk.pizzapalace.backend.entity.PaymentEntity;
import lk.pizzapalace.backend.entity.PizzaEntity;
import lk.pizzapalace.backend.entity.PromotionEntity;
import lk.pizzapalace.backend.entity.RateEntity;
import lk.pizzapalace.backend.entity.enums.OrderStatus;
import lk.pizzapalace.backend.entity.enums.ToppingsType;
import lk.pizzapalace.backend.service.observer.OrderObserver;
import lk.pizzapalace.backend.service.strategy.PaymentStrategy;

public class OrderServiceImpl implements OrderService {

    // private final List<UserEntity> users = new ArrayList<>();
    // private final List<PizzaEntity> pizzaMenu = new ArrayList<>();
    private final List<FavouritePizzaEntity> favouritePizzas = new ArrayList<>();
    private final List<PromotionEntity> promotions = new ArrayList<>();
    private final List<OrderEntity> orders = new ArrayList<>();
    private final List<OrderObserver> observers = new ArrayList<>();

    // private UserService userService = new UserServiceImpl(); // Or inject via constructor

    @Override
    public OrderEntity createOrder(OrderEntity order) {
        order.setStatus(OrderStatus.ORDER_RECEIVED);
        orders.add(order);
        notifyObservers(order);
        return order;
    }

    @Override
    public void updateOrderStatus(OrderEntity updatedOrder) {
        OrderEntity existingOrder = getOrderById(updatedOrder.getId());
        if (existingOrder != null) {
            existingOrder.setStatus(updatedOrder.getStatus());
            // System.out.println("Order ID " + updatedOrder.getId() + " status updated to " + updatedOrder.getStatus());
        } else {
            System.out.println("Order not found!");
        }
    }

    @Override
    public OrderEntity reviewOrder(OrderEntity order) {
        return orders.stream().filter(o -> o.getId().equals(order.getId())).findFirst().orElse(null);
    }

    @Override
    public OrderEntity getOrderById(Long id) {
        return orders.stream()
                .filter(a -> a.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<OrderEntity> getAllOrders() {
        return orders;
    }

    @Override
    public FavouritePizzaEntity saveFavouritePizza(FavouritePizzaEntity favouritePizzaEntity) {
        // Ensure that the user and pizza are correctly set in the FavouritePizzaEntity
        if (favouritePizzaEntity.getUserEntity() != null && favouritePizzaEntity.getPizzaEntity() != null) {
            favouritePizzas.add(favouritePizzaEntity);
        }
        return favouritePizzaEntity;
    }

    @Override
    public List<PizzaEntity> getFavoritePizzasByUser(Long id) {
        return null;
    }

    @Override
    public FavouritePizzaEntity getFavouritePizzaById(Long id) {
        return favouritePizzas.stream()
                .filter(a -> a.getId().equals(id))
                .findFirst()
                .orElse(null);
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
        // Award 10 points for each order
        int pointsToAdd = 10;
        customer.setLoyaltyPoints(customer.getLoyaltyPoints() + pointsToAdd);
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
