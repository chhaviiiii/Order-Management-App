package ui;

import model.Order;
import model.OrderID;
import model.OrderStatus;
import model.Product;
import model.ProductType;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class OrderTracker {
    private final List<Order> orders;

    public OrderTracker() {
        orders = new ArrayList<>();
    }

    public int displayMenu(Scanner input) {
        System.out.println("\n Please select an option: ");
        System.out.println("1. Create an order");
        System.out.println("2. Update an order status");
        System.out.println("3. View all active orders");
        System.out.println("4. Exit");

        if (!input.hasNextInt()) {
            System.out.println("Invalid input, Please enter a valid number!");
            input.next();
            return -1;
        }
        return input.nextInt();
    }

    public void createOrder(Scanner input) {
        List<Product> productsToSell = new ArrayList<>();

        boolean moreProducts = true;
        while (moreProducts) {
            Product product = getProduct(input);
            if (product != null) {
                productsToSell.add(product);
            }

            moreProducts = askForMoreProducts(input);
        }

        String customerDetails = getCustomerDetails(input);

        String orderID = OrderID.generateOrderID(productsToSell);
        Order newOrder = new Order(orderID, "Products in Order: "
                + productsToSell.size(),
                customerDetails,
                OrderStatus.PLACED,
                productsToSell);
        orders.add(newOrder);
        System.out.println("Order created successfully with Order ID: " + orderID);
    }

    private Product getProduct(Scanner input) {
        Product product = null;

        input.nextLine();
        String productName = input.nextLine();
        String productDescription = input.nextLine();

        double productPrice = getDouble(input, "Invalid input, Please enter a valid number!");
        if (productPrice == -1) {
            return null;
        }

        int productTypeIndex = getInt(input, "Invalid input, Please enter a valid number!", "Invalid option selected!");
        if (productTypeIndex == -1 || productTypeIndex < 1 || productTypeIndex > 4) {
            return product;
        }

        ProductType productType = ProductType.values()[productTypeIndex - 1];
        product = new Product(productName, productDescription, productPrice, productType);

        return product;
    }

    private double getDouble(Scanner input, String errorMessage) {
        if (!input.hasNextDouble()) {
            System.out.println(errorMessage);
            input.next();
            return -1;
        }
        return input.nextDouble();
    }

    private int getInt(Scanner input, String invalidInputMessage, String invalidOptionMessage) {
        if (!input.hasNextInt()) {
            System.out.println(invalidInputMessage);
            input.next();
            return -1;
        }
        int number = input.nextInt();
        if (!(number >= 1 && number <= 4)) {
            System.out.println(invalidOptionMessage);
            return -1;
        }
        return number;
    }

    private boolean askForMoreProducts(Scanner input) {
        System.out.println("Do you want to add more product? enter (1 for Yes / 2 for No)");

        int addMoreProductsNum = getInt(input,
                "Invalid input, Please enter a valid number!",
                "Invalid option selected!");
        return addMoreProductsNum == 1;
    }

    private String getCustomerDetails(Scanner input) {
        input.nextLine();
        return input.nextLine();
    }


    public void viewAllActiveOrders() {
        System.out.println("Active orders:");
        for (Order order : orders) {
            System.out.println("Order ID: " + order.getOrderID()
                    + ", Product Details: " + order.getProductDetails()
                    + ", Customer Details: " + order.getCustomerDetails()
                    + ", Status: " + order.getOrderStatus());
        }
    }

    public void updateOrderStatus(Scanner input) {
        System.out.println("Enter order ID:");
        String orderId = input.next();
        Order orderToUpdate = null;
        for (Order order : orders) {
            if (order.getOrderID().equals(orderId)) {
                orderToUpdate = order;
                break;
            }
        }
        if (orderToUpdate == null) {
            System.out.println("No order found with ID: " + orderId);
            return;
        }

        System.out.println("Enter new status # (1.PLACED, 2.PROCESSED, 3.SHIPPED, 4.DELIVERED, 5.COMPLETE):");
        int statusNum = input.nextInt();
        OrderStatus newStatus = OrderStatus.values()[statusNum - 1];
        orderToUpdate.updateOrderStatus(newStatus);

        if (newStatus.equals(OrderStatus.COMPLETE)) {
            orders.remove(orderToUpdate);
            System.out.println("Order status updated to COMPLETE and it's deactivated now.");
        } else {
            System.out.println("Order updated successfully.");
        }
    }


    public void exit(Scanner input) {
        System.out.println("Exiting the program...");
        input.close();
    }


    public void start() {
        boolean runTracker = true;

        Scanner input = new Scanner(System.in);

        while (runTracker) {
            System.out.println("Welcome to the Order Tracker.");
            int operation = displayMenu(input);

            if (operation == 1) {
                createOrder(input);
            } else if (operation == 2) {
                updateOrderStatus(input);
            } else if (operation == 3) {
                viewAllActiveOrders();
            } else if (operation == 4) {
                runTracker = false;
            } else {
                System.out.println("Invalid Option Selected!");
            }
        }


        if (input != null) {
            input.close();
        }
    }
}




