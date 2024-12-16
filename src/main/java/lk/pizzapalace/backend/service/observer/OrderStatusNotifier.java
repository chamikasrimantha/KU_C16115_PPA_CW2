package lk.pizzapalace.backend.service.observer;

import java.util.ArrayList;
import java.util.List;

import lk.pizzapalace.backend.entity.OrderEntity;
import lk.pizzapalace.backend.entity.enums.OrderStatus;

public class OrderStatusNotifier {
    private final List<OrderObserver> observers = new ArrayList<>();

    public void addObserver(OrderObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(OrderObserver observer) {
        observers.remove(observer);
    }

    public void notifyObservers(OrderEntity order) {
        for (OrderObserver observer : observers) {
            observer.update(order.getStatus());
        }
    }
}

class ConsoleLoggerObserver implements OrderObserver {
    @Override
    public void update(OrderStatus status) {
        System.out.println("Order status changed to: " + status);
    }
}

class AppNotifierObserver implements OrderObserver {
    @Override
    public void update(OrderStatus status) {
        System.out.println("Notifying app about order status: " + status);
    }
}