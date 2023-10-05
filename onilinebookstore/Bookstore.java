package com.onilinebookstore;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;


public class Bookstore {
	private int orderCounter;
    private List<Customer> customers;
    private List<Order> orders;
    private Map<Customer, List<Book>> shoppingCarts;

    public Bookstore() {
    	orderCounter = 1;
        this.customers = new ArrayList<>();
        this.orders = new ArrayList<>();
        this.shoppingCarts = new HashMap<>();
    }
    
    public boolean isEmailRegistered(String email) {
        for (Customer customer : customers) {
            if (customer.getEmail().equalsIgnoreCase(email)) {
                return true; // Email is already registered
            }
        }
        return false; // Email is not registered
    }
    
    // Method to sign up
    public Customer signup(String name, String email, String password) {
        // Check if the email is already registered
        if (isEmailRegistered(email)) {
            System.out.println("Email is already registered. Please log in.");
            return null;
        }

        // Create a new Customer object
        Customer newCustomer = new Customer(generateCustomerId(), name, email, password);

        // Add the new customer to the list (you can also add it to the database)
        customers.add(newCustomer);

        // Store the new customer data in the MySQL database
        if (storeCustomerInDatabase(newCustomer)) {
            System.out.println("Signup successful. Please log in.");
            return newCustomer;
        } else {
            System.out.println("Failed to store customer data in the database.");
            return null;
        }
    }

    // Method to authenticate and log in a customer
    public Customer login(String email, String password) {
        // Query to check if the email and password match an existing customer
        String query = "SELECT * FROM customers WHERE email = ? AND password = ?";

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/store", "root", "root");
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            // Set parameters for the SQL statement
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, password);

            // Execute the SQL statement to check for a matching customer
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                // Customer found, return the customer object
                int customerId = resultSet.getInt("customer_id");
                String name = resultSet.getString("name");
                System.out.println("Login Successfull");
                return new Customer(customerId, name, email, password);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // No matching customer found, return null
        System.out.println("----------------- Sign Up First -----------------");
        return null;
    }



    // Fetch data from the database and initialize the books list
	public List<Book> getAllBooks() {
	    List<Book> books = new ArrayList<>();
	
	    String query = "SELECT * FROM books";
	
	    try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/store", "root", "root");
	         PreparedStatement preparedStatement = connection.prepareStatement(query);
	         ResultSet resultSet = preparedStatement.executeQuery()) {
	
	        while (resultSet.next()) {
	            int id = resultSet.getInt("book_id");
	            String title = resultSet.getString("title");
	            String author = resultSet.getString("author");
	            double price = resultSet.getDouble("price");
	            int quantity = resultSet.getInt("quantity");
	
	            Book book = new Book(id, title, author, price, quantity);
	            books.add(book);
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	
	    return books;
	}

	public void addCustomer(Customer customer) {
	    customers.add(customer);
	}

	// Method to add a book to a customer's shopping cart
    public void addToCart(Customer customer, Book book) {
        if (!shoppingCarts.containsKey(customer)) {
            shoppingCarts.put(customer, new ArrayList<>());
        }

        List<Book> cart = shoppingCarts.get(customer);
        cart.add(book);
    }

    // Method to view the contents of a customer's shopping cart
    public List<Book> viewCart(Customer customer) {
        if (shoppingCarts.containsKey(customer)) {
            return shoppingCarts.get(customer);
        } else {
            return new ArrayList<>();
        }
    }
    
    // to generate unique customer ID
	public int generateCustomerId() {
	    // You can start customer IDs from 20231000 and increment from there
	    int startingCustomerId = 20231000;
	
	    // Find the maximum customer ID in the list of customers
	    int maxId = startingCustomerId;
	    for (Customer customer : customers) {
	        if (customer.getId() > maxId) {
	            maxId = customer.getId();
	        }
	    }
	
	    // Increment the max ID to create a new unique ID
	    return maxId + 1;
	}

	// Method to retrieve customer information by email
    public Customer getCustomerByEmail(String email) {
        for (Customer customer : customers) {
            if (customer.getEmail().equalsIgnoreCase(email)) {
                return customer;
            }
        }
        return null;

    } 
        
    // Method to place order
    public void placeOrder(Customer customer) {
        List<Book> cart = shoppingCarts.get(customer);

        if (cart != null && !cart.isEmpty()) {
            // Calculate the total cost of the books in the cart
            double totalCost = calculateTotalCost(cart);

            // Generate a new order ID
            int orderId = generateOrderId();

            // Create a new order
            Order order = new Order(orderId, customer, cart, totalCost);

            // Add the order to the list of orders
            orders.add(order);

            // Clear the customer's cart
         // Display order confirmation
            System.out.println("Order placed successfully!");
            System.out.println("Total Cost: " + totalCost);
            storeOrder(customer);
            cart.clear();

            
        } else {
            System.out.println("Your cart is empty. Add books to your cart before placing an order.");
        }
    }
    
    public void storeOrder(Customer customer) {
    	List<Book> cart = shoppingCarts.get(customer);
        if (customer == null) {
            System.out.println("Please log in or sign up first.");
            return;
        }

        // Generate an order ID
        int orderId = generateOrderId()-1;

        // Calculate the total order amount (you may need to modify this logic)
        double totalCost = calculateTotalCost(cart);

        // Get the current date and time
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String orderDate = dateFormat.format(new Date());

        // Prepare the SQL statement to insert the order
        String insertOrderSQL = "INSERT INTO orders (order_id, customer_id, order_date, total_cost) VALUES (?, ?, ?, ?)";

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/store", "root", "root");
                PreparedStatement preparedStatement = connection.prepareStatement(insertOrderSQL)) {
            preparedStatement.setInt(1, orderId);
            preparedStatement.setInt(2, customer.getId());
            preparedStatement.setString(3, orderDate);
            preparedStatement.setDouble(4, totalCost);

            // Execute the SQL statement to insert the order
            int rowsInserted = preparedStatement.executeUpdate();

            if (rowsInserted > 0) {
                System.out.println("Order ID: " + orderId);
                
                
                //storeOrderItems(orderId, customer.getCart());
                //updateBookQuantities(customer.getCart());
            } else {
                System.out.println("Failed to place the order.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Helper method to store customer data in the MySQL database
	private boolean storeCustomerInDatabase(Customer customer) {
	    String insertQuery = "INSERT INTO customers (name, email, password) VALUES (?, ?, ?)";
	
	    try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/store", "root", "root");
	         PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
	
	        // Set parameters for the SQL statement
	        preparedStatement.setString(1, customer.getName());
	        preparedStatement.setString(2, customer.getEmail());
	        preparedStatement.setString(3, customer.getPassword());
	
	        // Execute the SQL statement to insert the customer data
	        int rowsAffected = preparedStatement.executeUpdate();
	
	        // Check if the insertion was successful
	        return rowsAffected > 0;
	    } catch (SQLException e) {
	        e.printStackTrace();
	        return false;
	    }
	}

	// Helper method to calculate the total cost of the books in the cart
    private double calculateTotalCost(List<Book> books) {
        double totalCost = 0.0;
        for (Book book : books) {
            totalCost += book.getPrice();
        }
        return totalCost;
    }

    // Helper method to generate a unique order ID based on time stamp
    private int generateOrderId() {
    	int startingCustomerId = 1;
    	
	    // Find the maximum customer ID in the list of customers
	    int maxId = startingCustomerId;
	    for (Order order : orders) {
	        if (order.getId() > maxId) {
	            maxId = order.getId();
	        }
	    }
	
	    // Increment the max ID to create a new unique ID
	    return maxId + 1;

    }


    
    

}
