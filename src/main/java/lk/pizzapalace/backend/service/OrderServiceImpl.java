package lk.pizzapalace.backend.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lk.pizzapalace.backend.entity.Admin;
import lk.pizzapalace.backend.entity.Customer;
import lk.pizzapalace.backend.entity.OrderEntity;
import lk.pizzapalace.backend.entity.PaymentEntity;
import lk.pizzapalace.backend.entity.PizzaEntity;
import lk.pizzapalace.backend.entity.PromotionEntity;
import lk.pizzapalace.backend.entity.RateEntity;
import lk.pizzapalace.backend.entity.UserEntity;
import lk.pizzapalace.backend.entity.enums.CheeseType;
import lk.pizzapalace.backend.entity.enums.CrustType;
import lk.pizzapalace.backend.entity.enums.DeliveryType;
import lk.pizzapalace.backend.entity.enums.OrderStatus;
import lk.pizzapalace.backend.entity.enums.SauceType;
import lk.pizzapalace.backend.entity.enums.ToppingsType;
import lk.pizzapalace.backend.entity.enums.UserRole;
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
import lk.pizzapalace.backend.service.observer.OrderStatusNotifier;
import lk.pizzapalace.backend.service.strategy.PaymentStrategy;

public class OrderServiceImpl implements OrderService {
    private final List<UserEntity> users = new ArrayList<>();
    private final List<PizzaEntity> pizzaMenu = new ArrayList<>();
    private final List<PromotionEntity> promotions = new ArrayList<>();
    private final OrderStatusNotifier orderStatusNotifier = new OrderStatusNotifier();

    // USER REGISTRATION
    @Override
    public Customer registerCustomer(String username, String email, String password, String phone, String address,
            String name) {
        Customer customer = new Customer();
        customer.setUsername(username);
        customer.setEmail(email);
        customer.setPassword(password);
        customer.setPhone(phone);
        customer.setAddress(address);
        customer.setName(name);
        customer.setRole(UserRole.CUSTOMER);
        customer.setLoyaltyPoints(0);
        users.add(customer);
        System.out.println("Customer registered: " + username);
        return customer;
    }

    @Override
    public Admin registerAdmin(String username, String email, String password, String name) {
        Admin admin = new Admin();
        admin.setUsername(username);
        admin.setEmail(email);
        admin.setPassword(password);
        admin.setName(name);
        admin.setRole(UserRole.ADMIN);
        users.add(admin);
        System.out.println("Admin registered: " + username);
        return admin;
    }

    // USER LOGIN
    @Override
    public UserEntity login(String email, String password) {
        for (UserEntity user : users) {
            if (user.getEmail().equals(email) && user.getPassword().equals(password)) {
                System.out.println("Login successful for user: " + user.getUsername());
                return user;
            }
        }
        System.out.println("Login failed. Invalid email or password.");
        return null;
    }

    // PIZZA CUSTOMIZATION
    @Override
    public PizzaEntity customizePizza(String name, CrustType crust, SauceType sauce, List<ToppingsType> toppings,
            CheeseType cheese, double price) {
        PizzaEntity pizza = new PizzaEntity();
        pizza.setCrustType(crust);
        pizza.setSauceType(sauce);
        pizza.setToppingsType(toppings.get(0)); // Assuming one topping for simplicity.
        pizza.setCheeseType(cheese);
        pizza.setPrice(price);
        pizzaMenu.add(pizza);
        System.out.println("Customized pizza: " + name);
        return pizza;
    }

    @Override
    public void processPizzaCustomization(PizzaEntity pizza) {
        OrderCustomizationHandler crustHandler = new CrustCustomizationHandler();
        OrderCustomizationHandler toppingsHandler = new ToppingsCustomizationHandler();

        crustHandler.setNextHandler(toppingsHandler);
        crustHandler.handleCustomization(pizza);
    }

    // ORDERING PROCESS
    @Override
    public OrderEntity createOrder(UserEntity user, PizzaEntity pizza, DeliveryType deliveryType, int quantity,
            PaymentEntity payment) {
        OrderBuilder builder = new OrderBuilder()
                .setUser(user)
                .setPizza(pizza)
                .setDeliveryType(deliveryType)
                .setPayment(payment)
                .setStatus(OrderStatus.ORDER_RECEIVED);

        OrderEntity order = builder.build();
        notifyOrderStatus(order);
        return order;
    }

    @Override
    public void placeOrder(OrderEntity order) {
        Command placeOrderCommand = new PlaceOrderCommand(order);
        placeOrderCommand.execute();
    }

    @Override
    public OrderEntity reviewOrder(OrderEntity order) {
        System.out.println("Reviewing order: " + order.getId());
        return order;
    }

    // USER PROFILES AND FAVORITES
    @Override
    public void saveFavoritePizza(Customer customer, PizzaEntity pizza) {
        if (customer.getFavourites() == null) {
            customer.setFavourites(new ArrayList<>());
        }
        customer.getFavourites().add(pizza);
        System.out.println("Saved pizza to favorites for: " + customer.getUsername());
    }

    @Override
    public List<PizzaEntity> getFavoritePizzas(Customer customer) {
        return customer.getFavourites() != null ? customer.getFavourites() : new ArrayList<>();
    }

    // REAL-TIME ORDER TRACKING
    @Override
    public OrderStatus trackOrderStatus(OrderEntity order) {
        return order.getStatus();
    }

    @Override
    public void addOrderObserver(OrderObserver observer) {
        orderStatusNotifier.addObserver(observer);
    }

    @Override
    public void removeOrderObserver(OrderObserver observer) {
        orderStatusNotifier.removeObserver(observer);
    }

    // PAYMENT AND LOYALTY PROGRAM
    @Override
    public void processPayment(PaymentEntity payment, PaymentStrategy strategy) {
        strategy.processPayment(payment);
    }

    @Override
    public void addLoyaltyPoints(Customer customer, double amount) {
        int points = (int) (amount / 10); // Earn 1 point per $10 spent.
        customer.setLoyaltyPoints(customer.getLoyaltyPoints() + points);
        System.out.println("Added loyalty points to: " + customer.getUsername());
    }

    @Override
    public int getLoyaltyPoints(Customer customer) {
        return customer.getLoyaltyPoints();
    }

    // SEASONAL SPECIALS AND PROMOTIONS
    @Override
    public PromotionEntity addPromotion(String description, double discount, java.util.Date startDate,
            java.util.Date endDate) {
        PromotionEntity promotion = new PromotionEntity();
        promotion.setDescription(description);
        promotion.setDiscount(discount);
        promotion.setStartDate(startDate);
        promotion.setEndDate(endDate);
        promotions.add(promotion);
        System.out.println("Added promotion: " + description);
        return promotion;
    }

    @Override
    public List<PromotionEntity> getActivePromotions() {
        Date now = new Date();
        List<PromotionEntity> activePromotions = new ArrayList<>();
        for (PromotionEntity promo : promotions) {
            if (promo.getStartDate().before(now) && promo.getEndDate().after(now)) {
                activePromotions.add(promo);
            }
        }
        return activePromotions;
    }

    // FEEDBACK AND RATINGS
    @Override
    public void provideFeedback(OrderEntity order, double rating, String feedback) {
        Command feedbackCommand = new ProvideFeedbackCommand(feedback);
        feedbackCommand.execute();
        RateEntity rate = new RateEntity();
        rate.setOrderEntity(order);
        rate.setRating(rating);
        rate.setFeedback(feedback);
        if (order.getRateEntities() == null) {
            order.setRateEntities(new ArrayList<>());
        }
        order.getRateEntities().add(rate);
    }

    @Override
    public List<RateEntity> getFeedbackForOrder(OrderEntity order) {
        return order.getRateEntities() != null ? order.getRateEntities() : new ArrayList<>();
    }

    // ENHANCE ORDER
    @Override
    public void enhanceOrder(OrderEntity order, boolean addToppings, boolean specialPackaging) {
        OrderDecorator decorator = new BaseOrderDecorator(order); // Start with the base decorator

        if (addToppings) {
            decorator = new ExtraToppingsDecorator(decorator); // Wrap with toppings decorator
        }

        if (specialPackaging) {
            decorator = new SpecialPackagingDecorator(decorator); // Wrap with packaging decorator
        }

        decorator.enhance(); // Apply all enhancements
    }

    // Notify Order Status (Observer Pattern)
    private void notifyOrderStatus(OrderEntity order) {
        orderStatusNotifier.notifyObservers(order);
    }
}
