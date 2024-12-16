package lk.pizzapalace.backend;

// import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

import lk.pizzapalace.backend.controller.OrderController;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class BackendApplication {

	public static void main(String[] args) {
		// SpringApplication.run(BackendApplication.class, args);
		// Run the console-based order system
        OrderController orderController = new OrderController();
        orderController.startApp(); // Start the application
	}

}
