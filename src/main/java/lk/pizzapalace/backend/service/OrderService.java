package lk.pizzapalace.backend.service;

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
import lk.pizzapalace.backend.service.observer.OrderObserver;
import lk.pizzapalace.backend.service.strategy.PaymentStrategy;

public interface OrderService {
    // User Registration
    Customer registerCustomer(String username, String email, String password, String phone, String address, String name);
    Admin registerAdmin(String username, String email, String password, String name);

    // User Login
    UserEntity login(String email, String password);

    // Pizza Customization
    PizzaEntity customizePizza(String name, CrustType crust, SauceType sauce, List<ToppingsType> toppings, CheeseType cheese, double price);
    void processPizzaCustomization(PizzaEntity pizza);

    // Ordering Process
    OrderEntity createOrder(UserEntity user, PizzaEntity pizza, DeliveryType deliveryType, int quantity, PaymentEntity payment);
    void placeOrder(OrderEntity order);
    OrderEntity reviewOrder(OrderEntity order);

    // User Profiles and Favorites
    void saveFavoritePizza(Customer customer, PizzaEntity pizza);
    List<PizzaEntity> getFavoritePizzas(Customer customer);

    // Real-Time Order Tracking
    OrderStatus trackOrderStatus(OrderEntity order);
    void addOrderObserver(OrderObserver observer);
    void removeOrderObserver(OrderObserver observer);

    // Payment and Loyalty Program
    void processPayment(PaymentEntity payment, PaymentStrategy strategy);
    void addLoyaltyPoints(Customer customer, double amount);
    int getLoyaltyPoints(Customer customer);

    // Seasonal Specials and Promotions
    PromotionEntity addPromotion(String description, double discount, java.util.Date startDate, java.util.Date endDate);
    List<PromotionEntity> getActivePromotions();

    // Feedback and Ratings
    void provideFeedback(OrderEntity order, double rating, String feedback);
    List<RateEntity> getFeedbackForOrder(OrderEntity order);

    // Enhance Order
    void enhanceOrder(OrderEntity order, boolean addToppings, boolean specialPackaging);
}
