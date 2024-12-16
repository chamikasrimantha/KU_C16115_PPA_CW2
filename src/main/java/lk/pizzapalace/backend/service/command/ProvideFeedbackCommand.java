package lk.pizzapalace.backend.service.command;

public class ProvideFeedbackCommand implements Command {
    private final String feedback;

    public ProvideFeedbackCommand(String feedback) {
        this.feedback = feedback;
    }

    @Override
    public void execute() {
        System.out.println("Providing feedback: " + feedback);
    }
}
