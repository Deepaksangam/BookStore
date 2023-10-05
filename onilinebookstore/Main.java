package com.onilinebookstore;

import java.util.List;
import java.util.Scanner;





public class Main {

	public static void main(String[] args) {
	    Scanner scanner = new Scanner(System.in); 
		Bookstore bookstore = new Bookstore();
		Customer currentCustomer = null; // To keep track of the current customer
	
		while (true) {
		    System.out.println("1. Sign Up");
		    System.out.println("2. Log In");
		    System.out.println("3. List Books");
		    System.out.println("4. Add Book to Cart");
		    System.out.println("5. View Cart");
		    System.out.println("6. Place Order");
		    System.out.println("7. Exit");
		    System.out.print("Enter your choice: ");
	
		    String choiceStr = scanner.nextLine(); // Read input as a string
	

		        int choice = Integer.parseInt(choiceStr); // Parse the string to an integer
	
		        switch (choice) {
		            case 1:
			                // Sign up
		                System.out.print("Enter your name: ");
		                String name = scanner.nextLine();
		                System.out.print("Enter your email: ");
		                String email = scanner.nextLine();
		                System.out.print("Enter your password: ");
		                String password = scanner.nextLine();
		                currentCustomer = bookstore.signup(name, email, password);
		                break;
		            case 2:
		                // Login
		                System.out.print("Enter your email: ");
		                String loginEmail = scanner.nextLine();
		                System.out.print("Enter your password: ");
		                String loginPassword = scanner.nextLine();
		                currentCustomer = bookstore.login(loginEmail, loginPassword);
		                break;
		            case 3:
		                // List Books
		                if (currentCustomer != null) {
		                    List<Book> books = bookstore.getAllBooks();
		                    for (Book book : books) {
		                        System.out.println("ID: " + book.getId() + ", Title: " + book.getTitle() + ", Author: " + book.getAuthor() + ", Price: " + book.getPrice());
		                    }
		                } else {
		                    System.out.println("Please log in or sign up first.");
		                }
		                break;
		            case 4:
		                if (currentCustomer != null) {
		                    // Assuming you have a method to display a list of books and allow the user to choose one
		                	Book selectedBook = displayAndSelectBook(bookstore.getAllBooks(), scanner);
			                    if (selectedBook != null) {
		                        bookstore.addToCart(currentCustomer, selectedBook);
		                        System.out.println(selectedBook.getTitle() + " added to your cart.");
		                    } else {
		                        System.out.println("Invalid book selection.");
		                    }
		                    break;
		                } else {
		                    System.out.println("Please log in or sign up first.");
		                }
		                break;
		            case 5:
		                // View Cart
		                if (currentCustomer != null) {
		                    List<Book> cart = bookstore.viewCart(currentCustomer);
		                    for (Book book : cart) {
		                        System.out.println("Title: " + book.getTitle() + ", Author: " + book.getAuthor() + ", Price: " + book.getPrice());
		                    }
		                } else {
		                    System.out.println("Please log in or sign up first.");
		                }
		                break;
		            case 6:
		                // Place Order
		                if (currentCustomer != null) {
		                    bookstore.placeOrder(currentCustomer);
		                } else {
		                    System.out.println("Please log in or sign up first.");
		                }
		                break;
		            case 7:
		                System.out.println("Closed...");
			                
		                System.exit(0);
		                break;
		            default:
		                System.out.println("Invalid choice.");
		        }


		}
	}

	// Method to display and select a book from the list
	private static Book displayAndSelectBook(List<Book> books, Scanner scanner) {
	    	Scanner scan = new Scanner(System.in); 
			System.out.println("Available Books:");
				
			// Display books with IDs
			for (int i = 0; i < books.size(); i++) {
			    Book book = books.get(i);
			    System.out.println(" ID: " + book.getId() + ", Title: " + book.getTitle() + ", Author: " + book.getAuthor() + ", Price: " + book.getPrice());
			}

			System.out.print("Enter the ID of the book you want to add to the cart (0 to cancel): ");
			int choice = scan.nextInt();
//			scanner.nextLine();

			if (choice >= 1 && choice <= books.size()) {
			    return books.get(choice - 1); // Subtract 1 to get the correct index
			} else if (choice == 0) {
			    return null; // User canceled the selection
			} else {
			    return null; // Invalid choice
			}
}
}
