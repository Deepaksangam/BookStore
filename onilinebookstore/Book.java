package com.onilinebookstore;

public class Book {
    private int id;
    private String title;
    private String author;
    private double price;
    private int quantity;
    
    
	public Book(int id, String title, String author, double price, int quantity) {
		super();
		this.id = id;
		this.title = title;
		this.author = author;
		this.price = price;
		this.quantity = quantity;
	}


	public int getId() {
		return id;
	}


	public String getTitle() {
		return title;
	}


	public String getAuthor() {
		return author;
	}


	public double getPrice() {
		return price;
	}


	public int getQuantity() {
		return quantity;
	}
	

    
}
