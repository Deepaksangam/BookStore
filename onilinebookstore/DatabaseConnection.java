package com.onilinebookstore;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
	public static void main(String[] args) {
		String jdbcUrl = "jdbc:mysql://localhost:3306/store";
		String username = "root";
		String password = "root";

		try {
			Class.forName("com.mysql.cj.jdbc.Driver"); // Load the MySQL JDBC driver
			Connection connection = DriverManager.getConnection(jdbcUrl, username, password);

			if (connection != null) {
				System.out.println("Connected to the database!");
				connection.close();
			} else {
				System.out.println("Failed to connect to the database.");
			}
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}
}
