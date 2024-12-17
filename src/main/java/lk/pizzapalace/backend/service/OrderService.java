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
import lk.pizzapalace.backend.entity.enums.OrderStatus;
import lk.pizzapalace.backend.service.observer.OrderObserver;
import lk.pizzapalace.backend.service.strategy.PaymentStrategy;

public interface OrderService {
     // User Registration
     Customer registerCustomer(Customer customer);
     Admin registerAdmin(Admin admin);
 
     // User Login
     UserEntity login(UserEntity user);
 
     // Pizza Customization
     PizzaEntity customizePizza(PizzaEntity pizza);
     void processPizzaCustomization(PizzaEntity pizza);
 
     // Ordering Process
     OrderEntity createOrder(OrderEntity order);
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
     PromotionEntity addPromotion(PromotionEntity promotion);
     List<PromotionEntity> getActivePromotions();
 
     // Feedback and Ratings
     void provideFeedback(RateEntity rate);
     List<RateEntity> getFeedbackForOrder(OrderEntity order);
 
     // Enhance Order
     void enhanceOrder(OrderEntity order, boolean addToppings, boolean specialPackaging);
}
