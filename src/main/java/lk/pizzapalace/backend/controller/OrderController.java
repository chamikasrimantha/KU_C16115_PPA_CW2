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
        System.out.println("╔═══════════════════════════════════════════════╗");
        System.out.println("║  Welcome to Pizza Palace Food Ordering System ║");
        System.out.println("╚═══════════════════════════════════════════════╝");
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
        System.out.println("\n╔══════════════════════════════════════╗");
        System.out.println("║        Pizza Customization           ║");
        System.out.println("╚══════════════════════════════════════╝");

        // Get pizza name
        System.out.print("Enter pizza name: ");
        String name = scanner.nextLine();

        // Crust selection
        System.out.println("\n╔══════════════════════════════════════╗");
        System.out.println("║           Select Crust               ║");
        System.out.println("║  1. Thin Crust     [Rs. " + CrustType.THIN_CRUST.getPrice() + "]         ║");
        System.out.println("║  2. Thick Crust    [Rs. " + CrustType.THICK_CRUST.getPrice() + "]         ║");
        System.out.println("║  3. Cheese Stuffed [Rs. " + CrustType.CHEESE_STUFFED.getPrice() + "]         ║");
        System.out.println("╚══════════════════════════════════════╝");
        System.out.print("Choose crust (1-3): ");
        int crustChoice = Integer.parseInt(scanner.nextLine());
        CrustType crustType = switch (crustChoice) {
            case 1 -> CrustType.THIN_CRUST;
            case 2 -> CrustType.THICK_CRUST;
            case 3 -> CrustType.CHEESE_STUFFED;
            default -> throw new IllegalArgumentException("Invalid crust type");
        };

        // Sauce selection
        System.out.println("\n╔══════════════════════════════════════╗");
        System.out.println("║           Select Sauce               ║");
        System.out.println("║  1. Tomato  [Rs. " + SauceType.TOMATO.getPrice() + "]                 ║");
        System.out.println("║  2. Garlic  [Rs. " + SauceType.GARLIC.getPrice() + "]                 ║");
        System.out.println("║  3. Pesto   [Rs. " + SauceType.PESTO.getPrice() + "]                 ║");
        System.out.println("╚══════════════════════════════════════╝");
        System.out.print("Choose sauce (1-3): ");
        int sauceChoice = Integer.parseInt(scanner.nextLine());
        SauceType sauceType = switch (sauceChoice) {
            case 1 -> SauceType.TOMATO;
            case 2 -> SauceType.GARLIC;
            case 3 -> SauceType.PESTO;
            default -> throw new IllegalArgumentException("Invalid sauce type");
        };

        // Toppings selection
        System.out.println("\n╔══════════════════════════════════════╗");
        System.out.println("║          Select Toppings             ║");
        System.out.println("║  1. Pepperoni [Rs. " + ToppingsType.PEPPERONI.getPrice() + "]               ║");
        System.out.println("║  2. Mushrooms [Rs. " + ToppingsType.MUSHROOMS.getPrice() + "]               ║");
        System.out.println("║  3. Olives    [Rs. " + ToppingsType.OLIVES.getPrice() + "]               ║");
        System.out.println("║  4. Chicken   [Rs. " + ToppingsType.CHICKEN.getPrice() + "]               ║");
        System.out.println("╚══════════════════════════════════════╝");
        System.out.print("Choose toppings (1-4): ");
        int toppingsChoice = Integer.parseInt(scanner.nextLine());
        ToppingsType toppingsType = switch (toppingsChoice) {
            case 1 -> ToppingsType.PEPPERONI;
            case 2 -> ToppingsType.MUSHROOMS;
            case 3 -> ToppingsType.OLIVES;
            case 4 -> ToppingsType.CHICKEN;
            default -> throw new IllegalArgumentException("Invalid toppings type");
        };

        // Cheese selection
        System.out.println("\n╔══════════════════════════════════════╗");
        System.out.println("║           Select Cheese              ║");
        System.out.println("║  1. Mozzarella [Rs. " + CheeseType.MOZZARELLA.getPrice() + "]              ║");
        System.out.println("║  2. Cheddar    [Rs. " + CheeseType.CHEDDAR.getPrice() + "]              ║");
        System.out.println("║  3. Vegan      [Rs. " + CheeseType.VEGAN.getPrice() + "]              ║");
        System.out.println("╚══════════════════════════════════════╝");
        System.out.print("Choose cheese (1-3): ");
        int cheeseChoice = Integer.parseInt(scanner.nextLine());
        CheeseType cheeseType = switch (cheeseChoice) {
            case 1 -> CheeseType.MOZZARELLA;
            case 2 -> CheeseType.CHEDDAR;
            case 3 -> CheeseType.VEGAN;
            default -> throw new IllegalArgumentException("Invalid cheese type");
        };

        // Calculate and create pizza
        double price = crustType.getPrice() + sauceType.getPrice() + toppingsType.getPrice() + cheeseType.getPrice();
        PizzaEntity pizza = new PizzaEntity();
        pizza.setId(pizzaIdCounter++);
        pizza.setName(name);
        pizza.setCrustType(crustType);
        pizza.setSauceType(sauceType);
        pizza.setToppingsType(toppingsType);
        pizza.setCheeseType(cheeseType);
        pizza.setPrice(price);

        PizzaEntity customizedPizza = pizzaService.customizePizza(pizza);

        // Display order summary
        System.out.println("\n╔══════════════════════════════════════╗");
        System.out.println("║          Customized Pizza            ║");
        System.out.println("╠══════════════════════════════════════╣");
        System.out.println("  Name: " + customizedPizza.getName());
        System.out.println("  Crust: " + customizedPizza.getCrustType() + " - Rs. " + crustType.getPrice());
        System.out.println("  Sauce: " + customizedPizza.getSauceType() + " - Rs. " + sauceType.getPrice());
        System.out.println("  Toppings: " + customizedPizza.getToppingsType() + " - Rs. " + toppingsType.getPrice());
        System.out.println("  Cheese: " + customizedPizza.getCheeseType() + " - Rs. " + cheeseType.getPrice());
        System.out.println("  Total Price: Rs. " + customizedPizza.getPrice());
        System.out.println("╚══════════════════════════════════════╝");

        // Add to favorites prompt
        System.out.print("\nAdd to favorites? (yes/no): ");
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
            System.out.println("\n╔══════════════════════════════════════╗");
            System.out.println("║    Please login to place order!      ║");
            System.out.println("╚══════════════════════════════════════╝");
            return;
        }

        if (!(currentUser instanceof Customer)) {
            System.out.println("\n╔══════════════════════════════════════╗");
            System.out.println("║  Ordering is for customers only!     ║");
            System.out.println("╚══════════════════════════════════════╝");
            return;
        }

        Customer customer = (Customer) currentUser;

        System.out.println("\n╔══════════════════════════════════════╗");
        System.out.println("║         Place Your Order             ║");
        System.out.println("╚══════════════════════════════════════╝");

        // Delivery selection
        System.out.println("\n╔══════════════════════════════════════╗");
        System.out.println("║         Delivery Options             ║");
        System.out.println("║  1. Pickup                          ║");
        System.out.println("║  2. Home Delivery                   ║");
        System.out.println("╚══════════════════════════════════════╝");
        System.out.print("Select delivery type (1-2): ");
        int deliveryChoice = Integer.parseInt(scanner.nextLine());
        DeliveryType deliveryType = switch (deliveryChoice) {
            case 1 -> DeliveryType.PICKUP;
            case 2 -> DeliveryType.DELIVERY;
            default -> throw new IllegalArgumentException("Invalid delivery type");
        };

        // Payment selection
        System.out.println("\n╔══════════════════════════════════════╗");
        System.out.println("║         Payment Options              ║");
        System.out.println("║  1. Credit Card                     ║");
        System.out.println("║  2. Digital Wallet                  ║");
        System.out.println("╚══════════════════════════════════════╝");
        System.out.print("Select payment method (1-2): ");
        int paymentChoice = Integer.parseInt(scanner.nextLine());
        PaymentType paymentType = switch (paymentChoice) {
            case 1 -> PaymentType.CREDIT_CARD;
            case 2 -> PaymentType.DIGITAL_WALLET;
            default -> throw new IllegalArgumentException("Invalid payment type");
        };

        // Payment details collection
        if (paymentType == PaymentType.CREDIT_CARD) {
            System.out.println("\n╔══════════════════════════════════════╗");
            System.out.println("║       Credit Card Details            ║");
            System.out.println("╚══════════════════════════════════════╝");
            System.out.print("Card Number: ");
            scanner.nextLine();
            System.out.print("Cardholder Name: ");
            scanner.nextLine();
            System.out.print("Expiry (MM/YY): ");
            scanner.nextLine();
            System.out.print("CVV: ");
            scanner.nextLine();
        } else {
            System.out.println("\n╔══════════════════════════════════════╗");
            System.out.println("║      Digital Wallet Details          ║");
            System.out.println("╚══════════════════════════════════════╝");
            System.out.print("Wallet Name (PayPal/GPay): ");
            scanner.nextLine();
            System.out.print("Wallet ID/Email: ");
            scanner.nextLine();
        }

        // Loyalty points section
        int currentLoyaltyPoints = orderService.getLoyaltyPoints(customer);
        System.out.println("\n╔══════════════════════════════════════╗");
        System.out.println("║         Loyalty Points               ║");
        System.out.println("║  Current Points: " + String.format("%-15d", currentLoyaltyPoints) + "║");
        System.out.println("╚══════════════════════════════════════╝");

        boolean isLoyaltyUsed = false;
        double discountAmount = 0;

        if (currentLoyaltyPoints > 0) {
            System.out.print("Use loyalty points? (yes/no): ");
            if (scanner.nextLine().equalsIgnoreCase("yes")) {
                System.out.println("\n╔══════════════════════════════════════╗");
                System.out.println("║       Redemption Options             ║");
                System.out.println("║  1. 100 points  = Rs. 10 off        ║");
                System.out.println("║  2. 200 points  = Rs. 25 off        ║");
                System.out.println("║  3. 500 points  = Rs. 75 off        ║");
                System.out.println("║  4. 1000 points = Rs. 150 off       ║");
                System.out.println("║  5. Cancel                          ║");
                System.out.println("╚══════════════════════════════════════╝");
                System.out.print("Choose option (1-5): ");

                int redeemChoice = Integer.parseInt(scanner.nextLine());
                switch (redeemChoice) {
                    case 1 -> {
                        if (currentLoyaltyPoints >= 100) {
                            isLoyaltyUsed = true;
                            discountAmount = 10;
                            customer.setLoyaltyPoints(currentLoyaltyPoints - 100);
                        } else {
                            System.out.println("Insufficient points!");
                        }
                    }
                    case 2 -> {
                        if (currentLoyaltyPoints >= 200) {
                            isLoyaltyUsed = true;
                            discountAmount = 25;
                            customer.setLoyaltyPoints(currentLoyaltyPoints - 200);
                        } else {
                            System.out.println("Insufficient points!");
                        }
                    }
                    case 3 -> {
                        if (currentLoyaltyPoints >= 500) {
                            isLoyaltyUsed = true;
                            discountAmount = 75;
                            customer.setLoyaltyPoints(currentLoyaltyPoints - 500);
                        } else {
                            System.out.println("Insufficient points!");
                        }
                    }
                    case 4 -> {
                        if (currentLoyaltyPoints >= 1000) {
                            isLoyaltyUsed = true;
                            discountAmount = 150;
                            customer.setLoyaltyPoints(currentLoyaltyPoints - 1000);
                        } else {
                            System.out.println("Insufficient points!");
                        }
                    }
                }
            }
        }

        // Pizza selection
        System.out.println("\n╔══════════════════════════════════════╗");
        System.out.println("║          Pizza Selection             ║");
        System.out.println("╚══════════════════════════════════════╝");
        System.out.print("Enter pizza ID or 'new' to customize: ");
        String pizzaInput = scanner.nextLine();

        PizzaEntity pizzaEntity;
        if (pizzaInput.equalsIgnoreCase("new")) {
            customizePizza();
            System.out.print("Enter customized pizza ID: ");
            Long pizzaId = Long.parseLong(scanner.nextLine());
            pizzaEntity = pizzaService.getPizzaById(pizzaId);
        } else {
            Long pizzaId = Long.parseLong(pizzaInput);
            pizzaEntity = pizzaService.getPizzaById(pizzaId);
        }

        // Process order
        double price = pizzaEntity.getPrice() - (isLoyaltyUsed ? discountAmount : 0);

        PaymentEntity paymentEntity = new PaymentEntity();
        paymentEntity.setPaymentType(paymentType);
        paymentEntity.setPrice(price);
        paymentEntity.setLoyaltyUsed(isLoyaltyUsed);

        PaymentStrategy paymentStrategy = paymentType == PaymentType.CREDIT_CARD
                ? new CreditCardPayment()
                : new DigitalWalletPayment();

        orderService.processPayment(paymentEntity, paymentStrategy);

        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setId(orderIdCounter++);
        orderEntity.setDeliveryType(deliveryType);
        orderEntity.setStatus(OrderStatus.ORDER_RECEIVED);
        orderEntity.setUserEntity(customer);
        orderEntity.setPizzaEntity(pizzaEntity);
        orderEntity.setPaymentEntity(paymentEntity);

        OrderEntity createdOrder = orderService.createOrder(orderEntity);
        orderService.addLoyaltyPoints(customer, createdOrder.getPaymentEntity().getPrice());

        // Order summary
        System.out.println("\n╔══════════════════════════════════════╗");
        System.out.println("║         Order Summary                ║");
        System.out.println("╠══════════════════════════════════════╣");
        System.out.println("  Order ID: " + createdOrder.getId());
        System.out.println("  Pizza: " + createdOrder.getPizzaEntity().getName());
        System.out.println("  Delivery: " + createdOrder.getDeliveryType());
        System.out.println("  Payment: " + createdOrder.getPaymentEntity().getPaymentType());
        System.out.println("  Status: " + createdOrder.getStatus());
        if (isLoyaltyUsed) {
            System.out.println("  Discount Applied: Rs. " + discountAmount);
        }
        System.out.println("  Total Price: Rs. " + createdOrder.getPaymentEntity().getPrice());
        System.out.println("  Updated Points: " + orderService.getLoyaltyPoints(customer));
        System.out.println("╚══════════════════════════════════════╝");
    }

    private void trackOrder() {
        if (currentUser == null) {
            System.out.println("\n╔══════════════════════════════════════╗");
            System.out.println("║    Please login to track orders!     ║");
            System.out.println("╚══════════════════════════════════════╝");
            return;
        }

        try {
            List<OrderEntity> userOrders = orderService.getAllOrders().stream()
                    .filter(order -> order.getUserEntity().equals(currentUser))
                    .collect(Collectors.toList());

            if (userOrders.isEmpty()) {
                System.out.println("\n╔══════════════════════════════════════╗");
                System.out.println("║      No orders found to track!      ║");
                System.out.println("╚══════════════════════════════════════╝");
                return;
            }

            System.out.println("\n╔══════════════════════════════════════╗");
            System.out.println("║           Your Orders                ║");
            System.out.println("╠══════════════════════════════════════╣");
            for (OrderEntity order : userOrders) {
                System.out.printf("  Order #%-3d | Rs. %-7.2f | %-15s%n",
                        order.getId(),
                        order.getPizzaEntity().getPrice(),
                        order.getStatus());
            }
            System.out.println("╚══════════════════════════════════════╝");

            System.out.println("\n╔══════════════════════════════════════╗");
            System.out.println("║         Order Tracking               ║");
            System.out.println("╚══════════════════════════════════════╝");
            System.out.print("Enter Order ID (0 to exit): ");
            Long orderId = Long.parseLong(scanner.nextLine());

            if (orderId == 0) {
                return;
            }

            OrderEntity selectedOrder = orderService.getOrderById(orderId);

            if (selectedOrder == null) {
                System.out.println("\n╔══════════════════════════════════════╗");
                System.out.println("║      Invalid Order ID!              ║");
                System.out.println("╚══════════════════════════════════════╝");
                return;
            }

            if (!selectedOrder.getUserEntity().equals(currentUser)) {
                System.out.println("\n╔══════════════════════════════════════╗");
                System.out.println("║   Unauthorized to view this order!  ║");
                System.out.println("╚══════════════════════════════════════╝");
                return;
            }

            System.out.println("\n╔══════════════════════════════════════╗");
            System.out.println("║         Order Details                ║");
            System.out.println("╠══════════════════════════════════════╣");
            System.out.println("  Order ID: " + selectedOrder.getId());
            System.out.println("  Pizza: " + selectedOrder.getPizzaEntity().getName());
            System.out.println("  Price: Rs. " + selectedOrder.getPizzaEntity().getPrice());
            System.out.println("  Status: " + selectedOrder.getStatus());

            System.out.println("╠══════════════════════════════════════╣");
            System.out.println("║         Status Details               ║");
            System.out.println("╠══════════════════════════════════════╣");

            String statusMessage = switch (selectedOrder.getStatus()) {
                case ORDER_RECEIVED -> "  Your order has been received";
                case ORDER_PREPARING_STARTED -> "  Chef has started preparing your order";
                case ORDER_PREPARED -> "  Your delicious pizza is ready";
                case OUT_FOR_DELIVERY -> "  Your pizza is on its way to you";
                case DELIVERED -> "  Order delivered successfully";
                case READY_FOR_PICKUP -> "  Your order is ready for pickup";
                default -> "  Status currently undefined";
            };

            System.out.println(statusMessage);
            System.out.println("╚══════════════════════════════════════╝");

        } catch (NumberFormatException e) {
            System.out.println("\n╔══════════════════════════════════════╗");
            System.out.println("║    Please enter a valid Order ID!    ║");
            System.out.println("╚══════════════════════════════════════╝");
        } catch (Exception e) {
            System.out.println("\n╔══════════════════════════════════════╗");
            System.out.println("║    An error occurred! Try again.     ║");
            System.out.println("╚══════════════════════════════════════╝");
        }
    }

    private void userProfile() {
        if (currentUser == null) {
            System.out.println("\n╔══════════════════════════════════════╗");
            System.out.println("║    Please login to view profile!     ║");
            System.out.println("╚══════════════════════════════════════╝");
            return;
        }

        try {
            // Header
            System.out.println("\n╔══════════════════════════════════════╗");
            System.out.println("║           User Profile               ║");
            System.out.println("╠══════════════════════════════════════╣");
            System.out.println("  Username: " + currentUser.getUsername());
            System.out.println("╠══════════════════════════════════════╣");

            List<FavouritePizzaEntity> favouritePizzas = currentUser.getFavourites();

            if (favouritePizzas == null || favouritePizzas.isEmpty()) {
                System.out.println("║      No favorite pizzas found!      ║");
                System.out.println("╚══════════════════════════════════════╝");
                return;
            }

            System.out.println("║         Favorite Pizzas              ║");
            System.out.println("╠══════════════════════════════════════╣");

            for (FavouritePizzaEntity favouritePizzaEntity : favouritePizzas) {
                if (favouritePizzaEntity != null && favouritePizzaEntity.getPizzaEntity() != null) {
                    PizzaEntity pizza = favouritePizzaEntity.getPizzaEntity();
                    System.out.printf("  %-20s | Rs. %-7.2f%n",
                            pizza.getName(),
                            pizza.getPrice());
                } else {
                    System.out.println("  Invalid pizza entry");
                }
            }
            System.out.println("╚══════════════════════════════════════╝");

        } catch (Exception e) {
            System.out.println("\n╔══════════════════════════════════════╗");
            System.out.println("║    An error occurred! Try again.     ║");
            System.out.println("╚══════════════════════════════════════╝");
        }
    }

    private void paymentAndLoyalty() {
        if (currentUser == null) {
            System.out.println("\n╔══════════════════════════════════════╗");
            System.out.println("║    Please login to view points!      ║");
            System.out.println("╚══════════════════════════════════════╝");
            return;
        }

        if (!(currentUser instanceof Customer)) {
            System.out.println("\n╔══════════════════════════════════════╗");
            System.out.println("║    Customers only feature!           ║");
            System.out.println("╚══════════════════════════════════════╝");
            return;
        }

        Customer customer = (Customer) currentUser;
        int currentPoints = orderService.getLoyaltyPoints(customer);

        System.out.println("\n╔══════════════════════════════════════╗");
        System.out.println("║    Loyalty Points Information        ║");
        System.out.println("╠══════════════════════════════════════╣");
        System.out.println("  Your Current Loyalty Points: " + currentPoints + " points");

        System.out.println("╠══════════════════════════════════════╣");
        System.out.println("║       Loyalty Points Details         ║");
        System.out.println("╠══════════════════════════════════════╣");
        System.out.println("  - You earn 10 loyalty points per every Rs. 100 spent");
        System.out.println("  - Points accumulate with each purchase");

        System.out.println("╠══════════════════════════════════════╣");
        System.out.println("║       Points Earning Guide           ║");
        System.out.println("╠══════════════════════════════════════╣");
        System.out.println("  - In every pizza order you can earn 10 loyalty");
        System.out.println("    points per every Rs. 100 spent");
        System.out.println("  - Current balance: " + currentPoints + " points");
        System.out.println("╚══════════════════════════════════════╝");

        System.out.println("\nPress Enter to return...");
        scanner.nextLine();
    }

    private void provideFeedback() {
        // Ensure there is a logged-in user
        if (currentUser == null) {
            System.out.println("\n╔══════════════════════════════════════╗");
            System.out.println("║    No user is currently logged in.   ║");
            System.out.println("╚══════════════════════════════════════╝");
            return;
        }

        // Get all orders for the current user
        List<OrderEntity> userOrders = orderService.getAllOrders().stream()
                .filter(order -> order.getUserEntity().equals(currentUser))
                .collect(Collectors.toList());

        // Check if user has any orders
        if (userOrders.isEmpty()) {
            System.out.println("\n╔══════════════════════════════════════╗");
            System.out.println("║ You have no orders to provide        ║");
            System.out.println("║ feedback for.                        ║");
            System.out.println("╚══════════════════════════════════════╝");
            return;
        }

        // Show all feedbacks provided by the current user
        System.out.println("\n╔══════════════════════════════════════╗");
        System.out.println("║         Your Feedbacks               ║");
        System.out.println("╚══════════════════════════════════════╝");

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
            System.out.println("\n╔══════════════════════════════════════╗");
            System.out.println("║         Your Orders                 ║");
            System.out.println("╚══════════════════════════════════════╝");
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
                System.out.println("\n╔══════════════════════════════════════╗");
                System.out.println("║  Invalid input. Please enter a number.║");
                System.out.println("╚══════════════════════════════════════╝");
                continue;
            }

            // Exit option
            if (choice == 0) {
                return;
            }

            // Validate order selection
            if (choice < 1 || choice > userOrders.size()) {
                System.out.println("\n╔══════════════════════════════════════╗");
                System.out.println("║      Invalid order selection.        ║");
                System.out.println("╚══════════════════════════════════════╝");
                continue;
            }

            // Selected order
            OrderEntity selectedOrder = userOrders.get(choice - 1);

            // Check if order is eligible for feedback
            if (selectedOrder.getStatus() != OrderStatus.DELIVERED) {
                System.out.println("\n╔══════════════════════════════════════╗");
                System.out.println("║ You can only provide feedback for    ║");
                System.out.println("║ delivered orders.                    ║");
                System.out.println("╚══════════════════════════════════════╝");
                continue;
            }

            // Provide rating
            System.out.print("Rate your experience (1-5 stars): ");
            double rating;
            try {
                rating = Double.parseDouble(scanner.nextLine());
                if (rating < 1 || rating > 5) {
                    System.out.println("\n╔══════════════════════════════════════╗");
                    System.out.println("║   Rating must be between 1 and 5.    ║");
                    System.out.println("╚══════════════════════════════════════╝");
                    continue;
                }
            } catch (NumberFormatException e) {
                System.out.println("\n╔══════════════════════════════════════╗");
                System.out.println("║ Invalid rating. Please enter a number ║");
                System.out.println("║ between 1 and 5.                      ║");
                System.out.println("╚══════════════════════════════════════╝");
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

            System.out.println("\n╔══════════════════════════════════════╗");
            System.out.println("║     Thank you for your feedback!     ║");
            System.out.println("╚══════════════════════════════════════╝");

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
            System.out.println("\n╔══════════════════════════════════════╗");
            System.out.println("║       Enter Promotion Details        ║");
            System.out.println("╠══════════════════════════════════════╣");

            // Read the promotion details
            System.out.print("  Description: ");
            String description = scanner.nextLine();

            System.out.print("  Discount: ");
            double discount = Double.parseDouble(scanner.nextLine());

            System.out.print("  Start Date (yyyy-MM-dd): ");
            String startDateStr = scanner.nextLine();
            Date startDate = java.sql.Date.valueOf(startDateStr); // Assuming the date format is 'yyyy-MM-dd'

            System.out.print("  End Date (yyyy-MM-dd): ");
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

            System.out.println("\n╔══════════════════════════════════════╗");
            System.out.println("║       Promotion Added Success        ║");
            System.out.println("╠══════════════════════════════════════╣");
            System.out.println("  " + addedPromotion.getDescription());
            System.out.println("╚══════════════════════════════════════╝");

        } catch (Exception e) {
            System.out.println("\n╔══════════════════════════════════════╗");
            System.out.println("║          Error Adding Promotion      ║");
            System.out.println("╠══════════════════════════════════════╣");
            System.out.println("  " + e.getMessage());
            System.out.println("╚══════════════════════════════════════╝");
        }
    }

    // Method to view active promotions
    private void viewPromotions() {
        try {
            System.out.println("\n╔══════════════════════════════════════╗");
            System.out.println("║          Active Promotions           ║");
            System.out.println("╠══════════════════════════════════════╣");

            // Get active promotions
            List<PromotionEntity> activePromotions = orderService.getActivePromotions();

            if (activePromotions.isEmpty()) {
                System.out.println("║  No active promotions at the moment  ║");
            } else {
                for (PromotionEntity promotion : activePromotions) {
                    System.out.println("╠══════════════════════════════════════╣");
                    System.out.println("║ Description: " + promotion.getDescription());
                    System.out.println("║ Discount: " + promotion.getDiscount() + "%");
                    System.out.println("║ Start Date: " + promotion.getStartDate());
                    System.out.println("║ End Date: " + promotion.getEndDate());
                }
            }
            System.out.println("╚══════════════════════════════════════╝");
        } catch (Exception e) {
            System.out.println("\n╔══════════════════════════════════════╗");
            System.out.println("║         Error Viewing Promotions      ║");
            System.out.println("╠══════════════════════════════════════╣");
            System.out.println("  " + e.getMessage());
            System.out.println("╚══════════════════════════════════════╝");
        }
    }

    private void updateOrderStatus() {
        try {
            System.out.println("\n╔══════════════════════════════════════╗");
            System.out.println("║              All Orders              ║");
            System.out.println("╠══════════════════════════════════════╣");

            // Fetch all orders and sort them so DELIVERED comes last
            List<OrderEntity> ordersList = orderService.getAllOrders();
            ordersList.sort((o1, o2) -> {
                if (o1.getStatus() == OrderStatus.DELIVERED)
                    return 1;
                if (o2.getStatus() == OrderStatus.DELIVERED)
                    return -1;
                return o1.getStatus().compareTo(o2.getStatus());
            });

            if (ordersList.isEmpty()) {
                System.out.println("║         No orders available.         ║");
            } else {
                for (OrderEntity order : ordersList) {
                    System.out.println("╠══════════════════════════════════════╣");
                    System.out.println("║ Order ID: " + order.getId());
                    System.out.println("║ Status: " + order.getStatus());
                    System.out.println("║ Username: " + order.getUserEntity().getUsername());
                }
            }
            System.out.println("╚══════════════════════════════════════╝");

            // Ask user to select an order ID to update status
            System.out.print("\nEnter the order ID to update status: ");
            Long orderId = Long.parseLong(scanner.nextLine());

            // Fetch the order to update
            OrderEntity orderToUpdate = orderService.getOrderById(orderId);
            if (orderToUpdate == null) {
                System.out.println("\n╔══════════════════════════════════════╗");
                System.out.println("║          Order Not Found             ║");
                System.out.println("╚══════════════════════════════════════╝");
                return;
            }

            // Ask for new status
            System.out.println("\n╔══════════════════════════════════════╗");
            System.out.println("║       Select New Status              ║");
            System.out.println("╠══════════════════════════════════════╣");
            System.out.println("║ 1) ORDER_RECEIVED                    ║");
            System.out.println("║ 2) ORDER_PREPARING_STARTED           ║");
            System.out.println("║ 3) ORDER_PREPARED                    ║");
            System.out.println("║ 4) READY_FOR_PICKUP                  ║");
            System.out.println("║ 5) OUT_FOR_DELIVERY                  ║");
            System.out.println("║ 6) DELIVERED                         ║");
            System.out.println("╚══════════════════════════════════════╝");

            int statusChoice = Integer.parseInt(scanner.nextLine());
            OrderStatus newStatus = switch (statusChoice) {
                case 1 -> OrderStatus.ORDER_RECEIVED;
                case 2 -> OrderStatus.ORDER_PREPARING_STARTED;
                case 3 -> OrderStatus.ORDER_PREPARED;
                case 4 -> OrderStatus.READY_FOR_PICKUP;
                case 5 -> OrderStatus.OUT_FOR_DELIVERY;
                case 6 -> OrderStatus.DELIVERED;
                default -> null;
            };

            if (newStatus == null) {
                System.out.println("\n╔══════════════════════════════════════╗");
                System.out.println("║          Invalid Choice!             ║");
                System.out.println("╚══════════════════════════════════════╝");
            } else {
                // Update the order status
                orderToUpdate.setStatus(newStatus);
                orderService.updateOrderStatus(orderToUpdate);

                System.out.println("\n╔══════════════════════════════════════╗");
                System.out.println("║       Order Status Updated           ║");
                System.out.println("╠══════════════════════════════════════╣");
                System.out.println("║ Order ID: " + orderId);
                System.out.println("║ New Status: " + newStatus);
                System.out.println("╚══════════════════════════════════════╝");
            }

        } catch (Exception e) {
            System.out.println("\n╔══════════════════════════════════════╗");
            System.out.println("║         Error Updating Status        ║");
            System.out.println("╠══════════════════════════════════════╣");
            System.out.println("  " + e.getMessage());
            System.out.println("╚══════════════════════════════════════╝");
        }
    }

}
