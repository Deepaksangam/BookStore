package com.onilinebookstore;

import java.util.List;

public class Order {
    private int id;
    private Customer customer;
    private List<Book> items;
    private double totalcost;
	public Order(int id, Customer customer, List<Book> items, double totalcost) {
		super();
		this.id = id;
		this.customer = customer;
		this.items = items;
		this.totalcost = totalcost;
	}
	public int getId() {
		return id;
	}
	public Customer getCustomer() {
		return customer;
	}
	public List<Book> getItems() {
		return items;
	}
	public double getTotalCost() {
		return totalcost;
	}

    // Constructors, getters, setters
}
