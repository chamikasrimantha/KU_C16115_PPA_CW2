package lk.pizzapalace.backend.service.command;

import lk.pizzapalace.backend.entity.OrderEntity;

public interface Command {
    void execute();
}

class PlaceOrderCommand implements Command {
    private final OrderEntity order;

    public PlaceOrderCommand(OrderEntity order) {
        this.order = order;
    }

    @Override
    public void execute() {
        System.out.println("Placing order: " + order.getId());
    }
}

class ProvideFeedbackCommand implements Command {
    private final String feedback;

    public ProvideFeedbackCommand(String feedback) {
        this.feedback = feedback;
    }

    @Override
    public void execute() {
        System.out.println("Providing feedback: " + feedback);
    }
}