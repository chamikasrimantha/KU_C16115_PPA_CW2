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
import lk.pizzapalace.backend.service.builder.OrderBuilder;
import lk.pizzapalace.backend.service.chainofresponsibility.CrustCustomizationHandler;
import lk.pizzapalace.backend.service.chainofresponsibility.OrderCustomizationHandler;
import lk.pizzapalace.backend.service.chainofresponsibility.ToppingsCustomizationHandler;
import lk.pizzapalace.backend.service.command.Command;
import lk.pizzapalace.backend.service.command.PlaceOrderCommand;
import lk.pizzapalace.backend.service.command.ProvideFeedbackCommand;
import lk.pizzapalace.backend.service.decorator.BaseOrderDecorator;
import lk.pizzapalace.backend.service.decorator.ExtraToppingsDecorator;
import lk.pizzapalace.backend.service.decorator.OrderDecorator;
import lk.pizzapalace.backend.service.decorator.SpecialPackagingDecorator;
import lk.pizzapalace.backend.service.observer.OrderObserver;
import lk.pizzapalace.backend.service.strategy.PaymentStrategy;

public class OrderServiceImpl implements OrderService {
    // Collections
    private final List<FavouritePizzaEntity> favouritePizzas = new ArrayList<>();
    private final List<PromotionEntity> promotions = new ArrayList<>();
    private final List<OrderEntity> orders = new ArrayList<>();
    private final List<OrderObserver> observers = new ArrayList<>();

    // Chain of Responsibility for customization
    private final OrderCustomizationHandler crustHandler;
    private final OrderCustomizationHandler toppingsHandler;

    public OrderServiceImpl() {
        // Initialize handlers for Chain of Responsibility
        crustHandler = new CrustCustomizationHandler();
        toppingsHandler = new ToppingsCustomizationHandler();
        crustHandler.setNextHandler(toppingsHandler);
    }

    @Override
    public OrderEntity createOrder(OrderEntity order) {
        // Use Builder pattern to construct the order
        OrderBuilder builder = new OrderBuilder()
                .setId(order.getId())
                .setDeliveryType(order.getDeliveryType())
                .setStatus(OrderStatus.ORDER_RECEIVED)
                .setUser(order.getUserEntity())
                .setPizza(order.getPizzaEntity())
                .setPayment(order.getPaymentEntity());

        OrderEntity newOrder = builder.build();
        orders.add(newOrder);

        // Apply Chain of Responsibility for customization
        crustHandler.handleCustomization(newOrder.getPizzaEntity());

        // Notify observers using Observer pattern
        notifyObservers(newOrder);

        return newOrder;
    }

    @Override
    public void updateOrderStatus(OrderEntity orderEntity) {
        OrderEntity existingOrder = getOrderById(orderEntity.getId());
        if (existingOrder != null) {
            // Update the status directly
            existingOrder.setStatus(orderEntity.getStatus());
            notifyObservers(existingOrder);
        }
    }

    @Override
    public OrderEntity reviewOrder(OrderEntity order) {
        // Use Command pattern for review operation
        Command reviewCommand = new PlaceOrderCommand(order);
        reviewCommand.execute();
        return getOrderById(order.getId());
    }

    @Override
    public void processPayment(PaymentEntity payment, PaymentStrategy strategy) {
        // Strategy pattern for payment processing
        strategy.processPayment(payment);
    }

    @Override
    public void enhanceOrder(OrderEntity order, boolean addToppings, boolean specialPackaging) {
        // Start with base decorator
        OrderDecorator decorator = new BaseOrderDecorator(order);

        // Add enhancements using decorator pattern
        if (addToppings) {
            decorator = new ExtraToppingsDecorator(decorator);
            decorator.enhance();
        }

        if (specialPackaging) {
            decorator = new SpecialPackagingDecorator(decorator);
            decorator.enhance();
        }
    }

    @Override
    public void addOrderObserver(OrderObserver observer) {
        observers.add(observer);
    }

    @Override
    public void removeOrderObserver(OrderObserver observer) {
        observers.remove(observer);
    }

    private void notifyObservers(OrderEntity order) {
        for (OrderObserver observer : observers) {
            observer.update(order.getStatus());
        }
    }

    // Standard CRUD operations
    @Override
    public OrderEntity getOrderById(Long id) {
        return orders.stream()
                .filter(a -> a.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<OrderEntity> getAllOrders() {
        return new ArrayList<>(orders);
    }

    @Override
    public FavouritePizzaEntity saveFavouritePizza(FavouritePizzaEntity favouritePizzaEntity) {
        if (favouritePizzaEntity.getUserEntity() != null && favouritePizzaEntity.getPizzaEntity() != null) {
            favouritePizzas.add(favouritePizzaEntity);
        }
        return favouritePizzaEntity;
    }

    @Override
    public List<PizzaEntity> getFavoritePizzasByUser(Long id) {
        return favouritePizzas.stream()
                .filter(fp -> fp.getUserEntity().getId().equals(id))
                .map(FavouritePizzaEntity::getPizzaEntity)
                .collect(Collectors.toList());
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
    public void addLoyaltyPoints(Customer customer, double amount) {
        int pointsToAdd = (int) (amount / 100) * 10; // 10 points per 100 spent
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
        Command feedbackCommand = new ProvideFeedbackCommand(rate.getFeedback());
        feedbackCommand.execute();
        rate.getOrderEntity().getRateEntities().add(rate);
    }

    @Override
    public List<RateEntity> getFeedbackForOrder(OrderEntity order) {
        return order.getRateEntities();
    }

    @Override
    public List<RateEntity> getAllFeedbacks() {
        return orders.stream()
                .flatMap(order -> order.getRateEntities().stream())
                .collect(Collectors.toList());
    }
}