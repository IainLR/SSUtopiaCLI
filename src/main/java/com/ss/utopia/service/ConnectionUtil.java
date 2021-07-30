package com.ss.utopia.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionUtil {
	private String driver = "com.mysql.cj.jdbc.Driver";
	private String url = "jdbc:mysql://localhost:3306/utopia";
	private String username = "root";
	private String password = "Kira-1701-D";

	public Connection getConnection() throws SQLException, ClassNotFoundException {
		// 1. Register driver
		Class.forName(driver);
		// 2. create connection
		Connection connection = DriverManager.getConnection(url, username, password);
		//
		connection.setAutoCommit(Boolean.FALSE);
		return connection;
	}
}
