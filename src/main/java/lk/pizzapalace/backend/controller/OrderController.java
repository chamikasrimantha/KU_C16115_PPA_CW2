package lk.pizzapalace.backend.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import lk.pizzapalace.backend.entity.Admin;
import lk.pizzapalace.backend.entity.Customer;
import lk.pizzapalace.backend.entity.UserEntity;
import lk.pizzapalace.backend.service.OrderService;
import lk.pizzapalace.backend.service.OrderServiceImpl;

public class OrderController {
    private final OrderService orderService;
    private final Scanner scanner = new Scanner(System.in);
    private UserEntity currentUser = null; // To keep track of logged-in user

    // Constructor with default service implementation
    public OrderController() {
        this.orderService = new OrderServiceImpl();
    }

    public void startApp() {
        System.out.println("Welcome to Pizza Palace Order Management System!");
        start();
    }


    public void start() {
        boolean running = true;
        while (running) {
            System.out.println("\n1. Register as Customer");
            System.out.println("2. Register as Admin");
            System.out.println("3. Login");
            System.out.println("4. Exit");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    registerCustomer();
                    break;
                case 2:
                    registerAdmin();
                    break;
                case 3:
                    login();
                    break;
                case 4:
                    System.out.println("Exiting... Goodbye!");
                    running = false;
                    break;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }

    private void registerCustomer() {
        System.out.println("Enter customer details:");
        System.out.print("Name: ");
        String name = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();
        Customer customer = new Customer();
        customer.setName(name);
        customer.setEmail(email);
        Customer registeredCustomer = orderService.registerCustomer(customer);
        System.out.println("Customer registered successfully with ID: " + registeredCustomer.getId());
    }

    private void registerAdmin() {
        System.out.println("Enter admin details:");
        System.out.print("Name: ");
        String name = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();
        Admin admin = new Admin();
        admin.setName(name);
        admin.setEmail(email);
        Admin registeredAdmin = orderService.registerAdmin(admin);
        System.out.println("Admin registered successfully with ID: " + registeredAdmin.getId());
    }

    private void login() {
        
    }

    private void customerMenu() {
        boolean loggedIn = true;
        while (loggedIn) {
            System.out.println("\n1. Customize Pizza");
            System.out.println("2. Place Order");
            System.out.println("3. Track Order");
            System.out.println("4. User Profile");
            System.out.println("5. Payment & Loyalty");
            System.out.println("6. Feedback and Ratings");
            System.out.println("7. Logout");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    customizePizza();
                    break;
                case 2:
                    placeOrder();
                    break;
                case 3:
                    trackOrder();
                    break;
                case 4:
                    userProfile();
                    break;
                case 5:
                    paymentAndLoyalty();
                    break;
                case 6:
                    provideFeedback();
                    break;
                case 7:
                    System.out.println("Logging out...");
                    currentUser = null;
                    loggedIn = false;
                    break;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }

    private void adminMenu() {
        boolean loggedIn = true;
        while (loggedIn) {
            System.out.println("\n1. Add Promotion");
            System.out.println("2. View Promotions");
            System.out.println("3. Update status of orders");
            System.out.println("4. Logout");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    addPromotion();
                    break;
                case 2:
                    viewPromotions();
                    break;
                case 3:
                    updateOrderStatus();
                    break;
                case 4:
                    System.out.println("Logging out...");
                    currentUser = null;
                    loggedIn = false;
                    break;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }

    private void customizePizza() {
        
    }

    private void placeOrder() {
        
    }

    private void trackOrder() {
        
    }

    private void userProfile() {
        
    }

    private void paymentAndLoyalty() {
        
    }

    private void provideFeedback() {
        
    }

    private void addPromotion() {
       
    }

    private void viewPromotions() {
        
    }

    private void updateOrderStatus() {
       
    }
}
