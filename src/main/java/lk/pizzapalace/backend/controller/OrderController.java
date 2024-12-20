package lk.pizzapalace.backend.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import lk.pizzapalace.backend.entity.Admin;
import lk.pizzapalace.backend.entity.Customer;
import lk.pizzapalace.backend.entity.FavouritePizzaEntity;
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
import lk.pizzapalace.backend.entity.enums.PaymentType;
import lk.pizzapalace.backend.entity.enums.SauceType;
import lk.pizzapalace.backend.entity.enums.ToppingsType;
import lk.pizzapalace.backend.service.OrderService;
import lk.pizzapalace.backend.service.OrderServiceImpl;
import lk.pizzapalace.backend.service.PizzaService;
import lk.pizzapalace.backend.service.PizzaServiceImpl;
import lk.pizzapalace.backend.service.UserService;
import lk.pizzapalace.backend.service.UserServiceImpl;
import lk.pizzapalace.backend.service.strategy.CreditCardPayment;
import lk.pizzapalace.backend.service.strategy.DigitalWalletPayment;
import lk.pizzapalace.backend.service.strategy.PaymentStrategy;

public class OrderController {
    private final OrderService orderService;
    private final UserService userService;
    private final PizzaService pizzaService;
    private static Long customerIdCounter = 1L;
    private static Long adminIdCounter = 1L;
    private static Long pizzaIdCounter = 1L;
    private static Long orderIdCounter = 1L;
    private static Long rateIdCounter = 1L;
    private static Long promotionIdCounter = 1L;
    private static Long favouritePizzaIdCounter = 1L;
    private final Scanner scanner = new Scanner(System.in);
    private UserEntity currentUser = null; // To keep track of logged-in user

    // Constructor with default service implementation
    public OrderController() {
        this.orderService = new OrderServiceImpl();
        this.userService = new UserServiceImpl();
        this.pizzaService = new PizzaServiceImpl();
    }

    public void startApp() {
        System.out.println("╔═══════════════════════════════════════════╗");
        System.out.println("║  Welcome to Pizza Palace Order Management ║");
        System.out.println("╚═══════════════════════════════════════════╝");
        start();
    }

    public void start() {
        boolean running = true;
        while (running) {
            System.out.println("\n╔══════════════════════════════════════╗");
            System.out.println("║           MAIN MENU                  ║");
            System.out.println("╠══════════════════════════════════════╣");
            System.out.println("║  1. Register as Customer             ║");
            System.out.println("║  2. Register as Admin                ║");
            System.out.println("║  3. Login                            ║");
            System.out.println("║  4. Exit                             ║");
            System.out.println("╚══════════════════════════════════════╝");
            System.out.print("Choose an option: ");

            try {
                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                switch (choice) {
                    case 1 -> registerCustomer();
                    case 2 -> registerAdmin();
                    case 3 -> login();
                    case 4 -> {
                        System.out.println("╔══════════════════════════════════════╗");
                        System.out.println("║        Exiting... Goodbye!           ║");
                        System.out.println("╚══════════════════════════════════════╝");
                        running = false;
                    }
                    default -> {
                        System.out.println("╔══════════════════════════════════════╗");
                        System.out.println("║     Invalid choice. Try again.       ║");
                        System.out.println("╚══════════════════════════════════════╝");
                    }
                }
            } catch (Exception e) {
                System.out.println("╔══════════════════════════════════════╗");
                System.out.println("║   Please enter a valid number.       ║");
                System.out.println("╚══════════════════════════════════════╝");
                scanner.nextLine(); // Clear invalid input
            }
        }
    }

    private void registerCustomer() {
        System.out.println("╔══════════════════════════════════════╗");
        System.out.println("║       Customer Registration          ║");
        System.out.println("╠══════════════════════════════════════╣");

        System.out.print("  Username: ");
        String username = scanner.nextLine();
        System.out.print("  Email: ");
        String email = scanner.nextLine();
        System.out.print("  Password: ");
        String password = scanner.nextLine();
        System.out.print("  Phone: ");
        String phone = scanner.nextLine();
        System.out.print("  Address: ");
        String address = scanner.nextLine();
        System.out.print("  Name: ");
        String name = scanner.nextLine();

        Customer customer = new Customer();
        customer.setId(customerIdCounter++);
        customer.setUsername(username);
        customer.setEmail(email);
        customer.setPassword(password);
        customer.setPhone(phone);
        customer.setAddress(address);
        customer.setName(name);
        customer.setLoyaltyPoints(0);
        customer.setFavourites(null);

        Customer registeredCustomer = userService.registerCustomer(customer);

        System.out.println("╠══════════════════════════════════════╣");
        System.out.println("║ Customer registered successfully!    ║");
        System.out.println("  ║ Your ID: " + String.format("%-25s", registeredCustomer.getId()) + "║");
        System.out.println("╚══════════════════════════════════════╝");
    }

    private void registerAdmin() {
        System.out.println("╔══════════════════════════════════════╗");
        System.out.println("║         Admin Registration           ║");
        System.out.println("╠══════════════════════════════════════╣");

        System.out.print("  Username: ");
        String username = scanner.nextLine();
        System.out.print("  Email: ");
        String email = scanner.nextLine();
        System.out.print("  Password: ");
        String password = scanner.nextLine();
        System.out.print("  Name: ");
        String name = scanner.nextLine();

        Admin admin = new Admin();
        admin.setId(adminIdCounter++);
        admin.setUsername(username);
        admin.setEmail(email);
        admin.setPassword(password);
        admin.setName(name);

        Admin registeredAdmin = userService.registerAdmin(admin);

        System.out.println("╠══════════════════════════════════════╣");
        System.out.println("║ Admin registered successfully!       ║");
        System.out.println("  ║ Your ID: " + String.format("%-25s", registeredAdmin.getId()) + "║");
        System.out.println("╚══════════════════════════════════════╝");
    }

    private void login() {
        System.out.println("╔══════════════════════════════════════╗");
        System.out.println("║           Login Details              ║");
        System.out.println("╠══════════════════════════════════════╣");

        System.out.print("  Email: ");
        String email = scanner.nextLine();
        System.out.print("  Password: ");
        String password = scanner.nextLine();

        UserEntity user = new UserEntity();
        user.setEmail(email);
        user.setPassword(password);

        UserEntity loggedInUser = userService.login(user);
        if (loggedInUser != null) {
            currentUser = loggedInUser;
            System.out.println("╠══════════════════════════════════════╣");
            System.out.println("║ Login successful!                   ║");
            System.out.println("  ║ Welcome, " + String.format("%-25s", loggedInUser.getUsername()) + "║");
            System.out.println("╚══════════════════════════════════════╝");

            if (loggedInUser instanceof Customer) {
                customerMenu();
                currentUser = loggedInUser;
            } else if (loggedInUser instanceof Admin) {
                adminMenu();
                currentUser = loggedInUser;
            }
        } else {
            System.out.println("╠══════════════════════════════════════╣");
            System.out.println("║ Invalid email. Login failed.         ║");
            System.out.println("╚══════════════════════════════════════╝");
        }
    }

    private void customerMenu() {
        boolean loggedIn = true;
        while (loggedIn) {
            System.out.println("\n╔══════════════════════════════════════╗");
            System.out.println("║          Customer Menu               ║");
            System.out.println("╠══════════════════════════════════════╣");
            System.out.println("║  1. Customize Pizza                  ║");
            System.out.println("║  2. Place Order                      ║");
            System.out.println("║  3. Track Order                      ║");
            System.out.println("║  4. User Profile                     ║");
            System.out.println("║  5. Payment & Loyalty                ║");
            System.out.println("║  6. Feedback and Ratings             ║");
            System.out.println("║  7. View Active Promotions           ║");
            System.out.println("║  8. Logout                           ║");
            System.out.println("╚══════════════════════════════════════╝");
            System.out.print("Choose an option: ");

            try {
                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                switch (choice) {
                    case 1 -> customizePizza();
                    case 2 -> placeOrder();
                    case 3 -> trackOrder();
                    case 4 -> userProfile();
                    case 5 -> paymentAndLoyalty();
                    case 6 -> provideFeedback();
                    case 7 -> viewPromotions();
                    case 8 -> {
                        System.out.println("╔══════════════════════════════════════╗");
                        System.out.println("║           Logging out...             ║");
                        System.out.println("╚══════════════════════════════════════╝");
                        currentUser = null;
                        loggedIn = false;
                    }
                    default -> {
                        System.out.println("╔══════════════════════════════════════╗");
                        System.out.println("║     Invalid choice. Try again.       ║");
                        System.out.println("╚══════════════════════════════════════╝");
                    }
                }
            } catch (Exception e) {
                System.out.println("╔══════════════════════════════════════╗");
                System.out.println("║   Please enter a valid number.       ║");
                System.out.println("╚══════════════════════════════════════╝");
                scanner.nextLine(); // Clear invalid input
            }
        }
    }

    private void adminMenu() {
        boolean loggedIn = true;
        while (loggedIn) {
            System.out.println("\n╔══════════════════════════════════════╗");
            System.out.println("║              Admin Menu              ║");
            System.out.println("╠══════════════════════════════════════╣");
            System.out.println("║  1. Add Promotion                    ║");
            System.out.println("║  2. View Promotions                  ║");
            System.out.println("║  3. Update status of orders          ║");
            System.out.println("║  4. Logout                           ║");
            System.out.println("╚══════════════════════════════════════╝");
            System.out.print("Choose an option: ");

            try {
                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                switch (choice) {
                    case 1 -> addPromotion();
                    case 2 -> viewPromotions();
                    case 3 -> updateOrderStatus();
                    case 4 -> {
                        System.out.println("╔══════════════════════════════════════╗");
                        System.out.println("║           Logging out...             ║");
                        System.out.println("╚══════════════════════════════════════╝");
                        currentUser = null;
                        loggedIn = false;
                    }
                    default -> {
                        System.out.println("╔══════════════════════════════════════╗");
                        System.out.println("║     Invalid choice. Try again.       ║");
                        System.out.println("╚══════════════════════════════════════╝");
                    }
                }
            } catch (Exception e) {
                System.out.println("╔══════════════════════════════════════╗");
                System.out.println("║   Please enter a valid number.       ║");
                System.out.println("╚══════════════════════════════════════╝");
                scanner.nextLine(); // Clear invalid input
            }
        }
    }

    private void customizePizza() {
        System.out.println("Customizing your pizza...");

        // Prompt the user for pizza details
        System.out.print("Enter pizza name: ");
        String name = scanner.nextLine();

        // Select crust type
        System.out.println("Choose crust type: (1) THIN_CRUST, (2) THICK_CRUST, (3) CHEESE_STUFFED");
        int crustChoice = Integer.parseInt(scanner.nextLine());
        CrustType crustType = switch (crustChoice) {
            case 1 -> CrustType.THIN_CRUST;
            case 2 -> CrustType.THICK_CRUST;
            case 3 -> CrustType.CHEESE_STUFFED;
            default -> throw new IllegalArgumentException("Invalid crust type");
        };

        // Select sauce type
        System.out.println("Choose sauce type: (1) TOMATO, (2) GARLIC, (3) PESTO");
        int sauceChoice = Integer.parseInt(scanner.nextLine());
        SauceType sauceType = switch (sauceChoice) {
            case 1 -> SauceType.TOMATO;
            case 2 -> SauceType.GARLIC;
            case 3 -> SauceType.PESTO;
            default -> throw new IllegalArgumentException("Invalid sauce type");
        };

        // Select toppings type
        System.out.println("Choose toppings type: (1) PEPPERONI, (2) MUSHROOMS, (3) OLIVES, (4) CHICKEN");
        int toppingsChoice = Integer.parseInt(scanner.nextLine());
        ToppingsType toppingsType = switch (toppingsChoice) {
            case 1 -> ToppingsType.PEPPERONI;
            case 2 -> ToppingsType.MUSHROOMS;
            case 3 -> ToppingsType.OLIVES;
            case 4 -> ToppingsType.CHICKEN;
            default -> throw new IllegalArgumentException("Invalid toppings type");
        };

        // Select cheese type
        System.out.println("Choose cheese type: (1) MOZZARELLA, (2) CHEDDAR, (3) VEGAN");
        int cheeseChoice = Integer.parseInt(scanner.nextLine());
        CheeseType cheeseType = switch (cheeseChoice) {
            case 1 -> CheeseType.MOZZARELLA;
            case 2 -> CheeseType.CHEDDAR;
            case 3 -> CheeseType.VEGAN;
            default -> throw new IllegalArgumentException("Invalid cheese type");
        };

        // Calculate the price using the selected enum prices
        double price = crustType.getPrice() + sauceType.getPrice() + toppingsType.getPrice() + cheeseType.getPrice();

        // Create the pizza entity
        PizzaEntity pizza = new PizzaEntity();
        pizza.setId(pizzaIdCounter++);
        pizza.setName(name);
        pizza.setCrustType(crustType);
        pizza.setSauceType(sauceType);
        pizza.setToppingsType(toppingsType);
        pizza.setCheeseType(cheeseType);
        pizza.setPrice(price);

        // Call the service to save the customized pizza
        PizzaEntity customizedPizza = pizzaService.customizePizza(pizza);

        // Display the details of the customized pizza
        System.out.println("Pizza customized successfully:");
        System.out.println("Name: " + customizedPizza.getName());
        System.out.println("Crust: " + customizedPizza.getCrustType() + " - Rs. " + crustType.getPrice());
        System.out.println("Sauce: " + customizedPizza.getSauceType() + " - Rs. " + sauceType.getPrice());
        System.out.println("Toppings: " + customizedPizza.getToppingsType() + " - Rs. " + toppingsType.getPrice());
        System.out.println("Cheese: " + customizedPizza.getCheeseType() + " - Rs. " + cheeseType.getPrice());
        System.out.println("Total Price: Rs. " + customizedPizza.getPrice());

        // Ask if the user wants to add the pizza to their favorites
        System.out.println("Do you want to add this pizza to your favorites? (yes/no)");
        String addToFavorites = scanner.nextLine().toLowerCase();

        if (addToFavorites.equals("yes")) {
            addPizzaToFavorites(customizedPizza);
        }
    }

    // Method to add pizza to favorites
    private void addPizzaToFavorites(PizzaEntity customizedPizza) {
        if (currentUser == null) {
            System.out.println("No user is currently logged in.");
            return;
        }

        try {
            // Create a FavouritePizzaEntity and set its attributes
            FavouritePizzaEntity favouritePizzaEntity = new FavouritePizzaEntity();
            favouritePizzaEntity.setId(favouritePizzaIdCounter++);
            favouritePizzaEntity.setPizzaEntity(customizedPizza);
            favouritePizzaEntity.setUserEntity(currentUser);

            // Call the service to save the pizza to favorites
            FavouritePizzaEntity savedFavourite = orderService.saveFavouritePizza(favouritePizzaEntity);

            // Explicitly update the current user's favorites list
            if (currentUser.getFavourites() == null) {
                currentUser.setFavourites(new ArrayList<>());
            }
            currentUser.getFavourites().add(savedFavourite);

            System.out.println("Pizza added to your favorites!");

            // Debug: Print the number of favorites after adding
            System.out.println("Total favorite pizzas: " + currentUser.getFavourites().size());
        } catch (Exception e) {
            System.out.println("Error adding pizza to favorites: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void placeOrder() {
        if (currentUser == null) {
            System.out.println("You must log in to place an order.");
            return;
        }

        // Ensure the current user is a Customer
        if (!(currentUser instanceof Customer)) {
            System.out.println("Ordering is only available for registered customers.");
            return;
        }

        Customer customer = (Customer) currentUser;

        System.out.println("Placing an order for your pizza...");

        // Choose delivery type
        System.out.println("Choose delivery type: (1) PICKUP, (2) DELIVERY");
        int deliveryChoice = Integer.parseInt(scanner.nextLine());
        DeliveryType deliveryType = switch (deliveryChoice) {
            case 1 -> DeliveryType.PICKUP;
            case 2 -> DeliveryType.DELIVERY;
            default -> throw new IllegalArgumentException("Invalid delivery type");
        };

        // Choose payment type
        System.out.println("Choose payment type: (1) CREDIT_CARD, (2) DIGITAL_WALLET");
        int paymentChoice = Integer.parseInt(scanner.nextLine());
        PaymentType paymentType = switch (paymentChoice) {
            case 1 -> PaymentType.CREDIT_CARD;
            case 2 -> PaymentType.DIGITAL_WALLET;
            default -> throw new IllegalArgumentException("Invalid payment type");
        };

        // Ask for payment details based on payment type
        if (paymentType == PaymentType.CREDIT_CARD) {
            System.out.println("Enter Credit Card Details:");
            System.out.print("Card Number: ");
            scanner.nextLine();
            System.out.print("Cardholder Name: ");
            scanner.nextLine();
            System.out.print("Expiry Date (MM/YY): ");
            scanner.nextLine();
            System.out.print("CVV: ");
            scanner.nextLine();

            System.out.println("Credit Card details captured. Proceeding with payment...");
        } else if (paymentType == PaymentType.DIGITAL_WALLET) {
            System.out.println("Enter Digital Wallet Details:");
            System.out.print("Wallet Name (e.g., PayPal, Google Pay): ");
            scanner.nextLine();
            System.out.print("Wallet ID/Email: ");
            scanner.nextLine();

            System.out.println("Digital Wallet details captured. Proceeding with payment...");
        }

        // Check current loyalty points
        int currentLoyaltyPoints = orderService.getLoyaltyPoints(customer);
        System.out.println("Your current loyalty points: " + currentLoyaltyPoints);

        // Check if customer wants to use loyalty points
        boolean isLoyaltyUsed = false;
        double discountAmount = 0;

        if (currentLoyaltyPoints > 0) {
            System.out.println("Do you want to use loyalty points? (yes/no)");
            if (scanner.nextLine().equalsIgnoreCase("yes")) {
                // Loyalty points redemption options
                System.out.println("Redemption Options:");
                System.out.println("1. 100 points = Rs. 10 discount");
                System.out.println("2. 200 points = Rs. 25 discount");
                System.out.println("3. 500 points = Rs. 75 discount");
                System.out.println("4. 1000 points= Rs. 150 discount");
                System.out.println("5. Cancel");
                System.out.print("Choose a redemption option: ");

                int redeemChoice = Integer.parseInt(scanner.nextLine());

                switch (redeemChoice) {
                    case 1:
                        if (currentLoyaltyPoints >= 100) {
                            isLoyaltyUsed = true;
                            discountAmount = 10;
                            customer.setLoyaltyPoints(currentLoyaltyPoints - 100);
                        } else {
                            System.out.println("Insufficient points for this redemption.");
                        }
                        break;
                    case 2:
                        if (currentLoyaltyPoints >= 200) {
                            isLoyaltyUsed = true;
                            discountAmount = 25;
                            customer.setLoyaltyPoints(currentLoyaltyPoints - 200);
                        } else {
                            System.out.println("Insufficient points for this redemption.");
                        }
                        break;
                    case 3:
                        if (currentLoyaltyPoints >= 500) {
                            isLoyaltyUsed = true;
                            discountAmount = 75;
                            customer.setLoyaltyPoints(currentLoyaltyPoints - 500);
                        } else {
                            System.out.println("Insufficient points for this redemption.");
                        }
                        break;
                    case 4:
                        if (currentLoyaltyPoints >= 1000) {
                            isLoyaltyUsed = true;
                            discountAmount = 150;
                            customer.setLoyaltyPoints(currentLoyaltyPoints - 1000);
                        } else {
                            System.out.println("Insufficient points for this redemption.");
                        }
                        break;
                    case 5:
                        // User cancelled loyalty points usage
                        break;
                    default:
                        System.out.println("Invalid option.");
                }
            }
        }

        // Retrieve or create a pizza entity
        System.out.println("Enter the pizza ID you want to order or customize a new pizza (enter 'new'):");
        String pizzaInput = scanner.nextLine();

        PizzaEntity pizzaEntity;
        if (pizzaInput.equalsIgnoreCase("new")) {
            // Customize and create a new pizza
            customizePizza();
            System.out.println("Enter the pizza ID of the customized pizza:");
            Long pizzaId = Long.parseLong(scanner.nextLine());
            pizzaEntity = pizzaService.getPizzaById(pizzaId);
        } else {
            Long pizzaId = Long.parseLong(pizzaInput);
            pizzaEntity = pizzaService.getPizzaById(pizzaId);
        }

        // Calculate price with loyalty discount
        double price = pizzaEntity.getPrice();
        if (isLoyaltyUsed) {
            price -= discountAmount;
        }

        // Create payment entity
        PaymentEntity paymentEntity = new PaymentEntity();
        paymentEntity.setPaymentType(paymentType);
        paymentEntity.setPrice(price);
        paymentEntity.setLoyaltyUsed(isLoyaltyUsed);

        // Process payment
        PaymentStrategy paymentStrategy = paymentType == PaymentType.CREDIT_CARD
                ? new CreditCardPayment()
                : new DigitalWalletPayment();

        orderService.processPayment(paymentEntity, paymentStrategy);

        // Create order entity
        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setId(orderIdCounter++);
        orderEntity.setDeliveryType(deliveryType);
        orderEntity.setStatus(OrderStatus.ORDER_RECEIVED);
        orderEntity.setUserEntity(customer);
        orderEntity.setPizzaEntity(pizzaEntity);
        orderEntity.setPaymentEntity(paymentEntity);

        // Save and notify observers of the new order
        OrderEntity createdOrder = orderService.createOrder(orderEntity);

        // Add loyalty points for this purchase
        orderService.addLoyaltyPoints(customer, createdOrder.getPaymentEntity().getPrice());

        // Display order details
        System.out.println("Order placed successfully!");
        System.out.println("Order ID: " + createdOrder.getId());
        System.out.println("Delivery Type: " + createdOrder.getDeliveryType());
        System.out.println("Status: " + createdOrder.getStatus());
        System.out.println("Pizza: " + createdOrder.getPizzaEntity().getName());
        System.out.println("Payment: " + createdOrder.getPaymentEntity().getPaymentType());
        System.out.println("Total Price: Rs. " + createdOrder.getPaymentEntity().getPrice());

        if (isLoyaltyUsed) {
            System.out.println("Loyalty points used: Discount of Rs. " + discountAmount);
        }

        // Show updated loyalty points
        System.out.println("Updated Loyalty Points: " + orderService.getLoyaltyPoints(customer));
    }

    private void trackOrder() {
        // Ensure there is a logged-in user
        if (currentUser == null) {
            System.out.println("No user is currently logged in.");
            return;
        }

        try {
            // Get all orders
            List<OrderEntity> allOrders = orderService.getAllOrders();

            // Filter orders for the current user
            List<OrderEntity> userOrders = allOrders.stream()
                    .filter(order -> order.getUserEntity().equals(currentUser))
                    .collect(Collectors.toList());

            // Check if user has any orders
            if (userOrders.isEmpty()) {
                System.out.println("You have no orders to track.");
                return;
            }

            // Display user's orders
            System.out.println("Your Orders:");
            for (OrderEntity order : userOrders) {
                System.out.println("Order ID: " + order.getId() +
                        " | Total Price: Rs. " + order.getPizzaEntity().getPrice() +
                        " | Current Status: " + order.getStatus());
            }

            // Prompt user to track a specific order
            System.out.println("\nEnter the Order ID to get detailed status (or 0 to exit):");
            Long orderId = Long.parseLong(scanner.nextLine());

            // Exit if user chooses to
            if (orderId == 0) {
                return;
            }

            // Find and track the specific order
            OrderEntity selectedOrder = orderService.getOrderById(orderId);

            // Validate the order
            if (selectedOrder == null) {
                System.out.println("Invalid Order ID. No order found.");
                return;
            }

            // Verify the order belongs to the current user
            if (!selectedOrder.getUserEntity().equals(currentUser)) {
                System.out.println("You do not have permission to view this order.");
                return;
            }

            // Display detailed order status
            System.out.println("\nOrder Details:");
            System.out.println("Order ID: " + selectedOrder.getId());
            System.out.println("Total Price: Rs. " + selectedOrder.getPizzaEntity().getPrice());
            System.out.println("Current Status: " + selectedOrder.getStatus());

            // Optional: Add more detailed status tracking logic if needed
            switch (selectedOrder.getStatus()) {
                case ORDER_RECEIVED:
                    System.out.println("Your order has been placed.");
                    break;
                case ORDER_PREPARING_STARTED:
                    System.out.println("Your order is being started to preparing in the kitchen.");
                    break;
                case ORDER_PREPARED:
                    System.out.println("Your order is being prepared.");
                    break;
                case OUT_FOR_DELIVERY:
                    System.out.println("Your order is out for delivery.");
                    break;
                case DELIVERED:
                    System.out.println("Your order has delivered.");
                    break;
                case READY_FOR_PICKUP:
                    System.out.println("Your order is ready for pickup.");
                    break;
                default:
                    System.out.println("Order status is currently undefined.");
            }

        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a valid Order ID.");
        } catch (Exception e) {
            System.out.println("An error occurred while tracking the order: " + e.getMessage());
        }
    }

    private void userProfile() {
        // Ensure there is a logged-in user
        if (currentUser == null) {
            System.out.println("No user is currently logged in.");
            return;
        }

        try {
            // Display user profile information
            System.out.println("User Profile:");
            System.out.println("Username: " + currentUser.getUsername());

            // Refresh the user data to ensure we have the latest information
            // You might need to implement this method in your UserService
            // currentUser = userService.refreshUser(currentUser.getId());

            // Get the user's favorite pizzas (FavouritePizzaEntity list)
            List<FavouritePizzaEntity> favouritePizzas = currentUser.getFavourites();

            // Debug: Print the total number of favorite pizzas
            System.out.println(
                    "Total favorite pizzas: " + (favouritePizzas != null ? favouritePizzas.size() : "null"));

            // Check if the user has any favorite pizzas
            if (favouritePizzas == null || favouritePizzas.isEmpty()) {
                System.out.println("No favorite pizzas found.");
            } else {
                System.out.println("Favorite Pizzas:");
                // Iterate over the list of FavouritePizzaEntities and print each pizza's
                // details
                for (FavouritePizzaEntity favouritePizzaEntity : favouritePizzas) {
                    if (favouritePizzaEntity != null && favouritePizzaEntity.getPizzaEntity() != null) {
                        PizzaEntity pizza = favouritePizzaEntity.getPizzaEntity(); // Get the actual PizzaEntity
                        System.out.println("- " + pizza.getName() + " | Price: Rs. " + pizza.getPrice());
                    } else {
                        System.out.println("- Incomplete favorite pizza entry");
                    }
                }
            }
        } catch (Exception e) {
            // Handle exceptions gracefully
            System.out.println("An error occurred while fetching the user profile: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void paymentAndLoyalty() {
        // Ensure there is a logged-in user
        if (currentUser == null) {
            System.out.println("No user is currently logged in.");
            return;
        }

        // Ensure the current user is a Customer
        if (!(currentUser instanceof Customer)) {
            System.out.println("Loyalty program is only available for registered customers.");
            return;
        }

        Customer customer = (Customer) currentUser;

        // Get current loyalty points
        int currentPoints = orderService.getLoyaltyPoints(customer);

        // Display loyalty points information
        System.out.println("\n--- Loyalty Points Information ---");
        System.out.println("Your Current Loyalty Points: " + currentPoints + " points");

        // Explain loyalty points system
        System.out.println("\nLoyalty Points Details:");
        System.out.println("- You earn 10 loyalty points per every Rs. 100 spent");
        System.out.println("- Points accumulate with each purchase");

        // Show points earning potential
        System.out.println("\nPoints Earning Guide:");
        System.out.println("- In every pizza order you can earn 10 loyalty points per every Rs. 100 spent");
        System.out.println("- Current balance: " + currentPoints + " points");

        // Press any key to continue
        System.out.println("\nPress Enter to return...");
        scanner.nextLine();
    }

    private void provideFeedback() {
        // Ensure there is a logged-in user
        if (currentUser == null) {
            System.out.println("No user is currently logged in.");
            return;
        }

        // Get all orders for the current user
        List<OrderEntity> userOrders = orderService.getAllOrders().stream()
                .filter(order -> order.getUserEntity().equals(currentUser))
                .collect(Collectors.toList());

        // Check if user has any orders
        if (userOrders.isEmpty()) {
            System.out.println("You have no orders to provide feedback for.");
            return;
        }

        // Show all feedbacks provided by the current user
        System.out.println("\n--- Your Feedbacks ---");
        List<RateEntity> userFeedbacks = orderService.getAllFeedbacks().stream()
                .filter(feedback -> feedback.getOrderEntity().getUserEntity().equals(currentUser))
                .collect(Collectors.toList());

        if (userFeedbacks.isEmpty()) {
            System.out.println("You haven't provided any feedback yet.");
        } else {
            for (RateEntity feedback : userFeedbacks) {
                System.out.println("Order ID: " + feedback.getOrderEntity().getId());
                System.out.println("Rating: " + feedback.getRating() + " stars");
                System.out.println("Feedback: " + feedback.getFeedback());
                System.out.println("---");
            }
        }

        while (true) {
            // Display user's orders
            System.out.println("\n--- Your Orders ---");
            for (int i = 0; i < userOrders.size(); i++) {
                OrderEntity order = userOrders.get(i);
                System.out.println((i + 1) + ". Order ID: " + order.getId()
                        + " | Pizza: " + order.getPizzaEntity().getName()
                        + " | Status: " + order.getStatus());
            }
            System.out.println("0. Exit");

            // Choose an order to provide feedback
            System.out.print("Select an order to provide feedback (enter number): ");
            int choice;
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
                continue;
            }

            // Exit option
            if (choice == 0) {
                return;
            }

            // Validate order selection
            if (choice < 1 || choice > userOrders.size()) {
                System.out.println("Invalid order selection.");
                continue;
            }

            // Selected order
            OrderEntity selectedOrder = userOrders.get(choice - 1);

            // Check if order is eligible for feedback
            if (selectedOrder.getStatus() != OrderStatus.DELIVERED) {
                System.out.println("You can only provide feedback for delivered orders.");
                continue;
            }

            // Provide rating
            System.out.print("Rate your experience (1-5 stars): ");
            double rating;
            try {
                rating = Double.parseDouble(scanner.nextLine());
                if (rating < 1 || rating > 5) {
                    System.out.println("Rating must be between 1 and 5.");
                    continue;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid rating. Please enter a number between 1 and 5.");
                continue;
            }

            // Provide feedback text
            System.out.println("Please provide your detailed feedback (optional):");
            String feedbackText = scanner.nextLine();

            // Create feedback entity
            RateEntity rateEntity = new RateEntity();
            rateEntity.setId(rateIdCounter++);
            rateEntity.setRating(rating);
            rateEntity.setFeedback(feedbackText.isEmpty() ? "No additional comments" : feedbackText);
            rateEntity.setOrderEntity(selectedOrder);

            // Save feedback
            orderService.provideFeedback(rateEntity);

            System.out.println("\nThank you for your feedback!");

            // Option to provide feedback for another order
            System.out.println("Do you want to provide feedback for another order? (yes/no)");
            if (!scanner.nextLine().equalsIgnoreCase("yes")) {
                break;
            }
        }
    }

    // Method to add a promotion
    private void addPromotion() {
        try {
            System.out.println("Enter Promotion Details:");

            // Read the promotion details
            System.out.print("Description: ");
            String description = scanner.nextLine();

            System.out.print("Discount: ");
            double discount = Double.parseDouble(scanner.nextLine());

            System.out.print("Start Date (yyyy-MM-dd): ");
            String startDateStr = scanner.nextLine();
            Date startDate = java.sql.Date.valueOf(startDateStr); // Assuming the date format is 'yyyy-MM-dd'

            System.out.print("End Date (yyyy-MM-dd): ");
            String endDateStr = scanner.nextLine();
            Date endDate = java.sql.Date.valueOf(endDateStr);

            // Create a new promotion entity
            PromotionEntity promotion = new PromotionEntity();
            promotion.setId(promotionIdCounter++);
            promotion.setDescription(description);
            promotion.setDiscount(discount);
            promotion.setStartDate(startDate);
            promotion.setEndDate(endDate);

            // Add promotion to the list
            PromotionEntity addedPromotion = orderService.addPromotion(promotion);

            System.out.println("Promotion added successfully: " + addedPromotion.getDescription());
        } catch (Exception e) {
            System.out.println("Error adding promotion: " + e.getMessage());
        }
    }

    // Method to view active promotions
    private void viewPromotions() {
        try {
            System.out.println("Active Promotions:");

            // Get active promotions
            List<PromotionEntity> activePromotions = orderService.getActivePromotions();

            if (activePromotions.isEmpty()) {
                System.out.println("No active promotions at the moment.");
            } else {
                // Display active promotions
                for (PromotionEntity promotion : activePromotions) {
                    System.out.println("Description: " + promotion.getDescription());
                    System.out.println("Discount: " + promotion.getDiscount() + "%");
                    System.out.println("Start Date: " + promotion.getStartDate());
                    System.out.println("End Date: " + promotion.getEndDate());
                    System.out.println("-----------------------------------");
                }
            }
        } catch (Exception e) {
            System.out.println("Error viewing promotions: " + e.getMessage());
        }
    }

    private void updateOrderStatus() {
        try {
            // Display all orders
            System.out.println("All Orders:");

            // Fetch all orders and sort them so DELIVERED comes last
            List<OrderEntity> ordersList = orderService.getAllOrders();
            ordersList.sort((o1, o2) -> {
                if (o1.getStatus() == OrderStatus.DELIVERED)
                    return 1;
                if (o2.getStatus() == OrderStatus.DELIVERED)
                    return -1;
                return o1.getStatus().compareTo(o2.getStatus());
            });

            // Display orders
            for (OrderEntity order : ordersList) {
                System.out.println("Order ID: " + order.getId() + "  | Status: " + order.getStatus() + "  | Username: "
                        + order.getUserEntity().getUsername());
            }

            // Ask user to select an order ID to update status
            System.out.print("Enter the order ID to update status: ");
            Long orderId = Long.parseLong(scanner.nextLine());

            // Fetch the order to update
            OrderEntity orderToUpdate = orderService.getOrderById(orderId);
            if (orderToUpdate == null) {
                System.out.println("Order not found.");
                return;
            }

            // Ask for new status
            System.out.println("Select new status for Order ID " + orderId + ":");
            System.out.println("1) ORDER_RECEIVED");
            System.out.println("2) ORDER_PREPARING_STARTED");
            System.out.println("3) ORDER_PREPARED");
            System.out.println("4) READY_FOR_PICKUP");
            System.out.println("5) OUT_FOR_DELIVERY");
            System.out.println("6) DELIVERED");

            int statusChoice = Integer.parseInt(scanner.nextLine());
            OrderStatus newStatus = null;

            switch (statusChoice) {
                case 1 -> newStatus = OrderStatus.ORDER_RECEIVED;
                case 2 -> newStatus = OrderStatus.ORDER_PREPARING_STARTED;
                case 3 -> newStatus = OrderStatus.ORDER_PREPARED;
                case 4 -> newStatus = OrderStatus.READY_FOR_PICKUP;
                case 5 -> newStatus = OrderStatus.OUT_FOR_DELIVERY;
                case 6 -> newStatus = OrderStatus.DELIVERED;
                default -> System.out.println("Invalid choice!");
            }

            // Update the order status
            if (newStatus != null) {
                orderToUpdate.setStatus(newStatus);
                orderService.updateOrderStatus(orderToUpdate); // Call service method to update the status
                System.out.println("Order ID " + orderId + " status updated to " + newStatus);
            }

        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
    }

}
