package com.ss.utopia.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public abstract class BaseDAO<T> {

	public static Connection connection = null;

	public BaseDAO(Connection connection) {
		this.connection = connection;
	}

	private String driver = "com.mysql.cj.jdbc.Driver";
	private String url = "jdbc:mysql://localhost:3306/utopia";
	private String username = "root";
	private String password = "Kira-1701-D";

	public Connection getConnection() throws SQLException, ClassNotFoundException {
		// 1. Register driver
		Class.forName(driver);
		// 2. create connection
		Connection connection = DriverManager.getConnection(url, username, password);
		return connection;
	}

	public void save(String sql, Object[] vals) throws SQLException, ClassNotFoundException {
		PreparedStatement preparedStmt = connection.prepareStatement(sql);
		if (vals != null) {
			int count = 1;
			for (Object o : vals) {
				preparedStmt.setObject(count, o);
				count++;
			}
		}
		preparedStmt.executeUpdate();
	}

	public Integer saveAndReturnPrimaryKey(String sql, Object[] vals) throws SQLException, ClassNotFoundException {
		PreparedStatement preparedStmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
		if (vals != null) {
			int count = 1;
			for (Object o : vals) {
				preparedStmt.setObject(count, o);
				count++;
			}
		}
		preparedStmt.executeUpdate();
		ResultSet rs = preparedStmt.getGeneratedKeys(); // this may replace below code
//        ResultSet rs = preparedStmt.executeQuery();
		while (rs.next()) {
			return rs.getInt(1); // try 0, indices
		}

		return null;
	}

	public List<T> read(String sql, Object[] vals) throws SQLException, ClassNotFoundException {
		PreparedStatement preparedStmt = connection.prepareStatement(sql);
		if (vals != null) {
			int count = 1;
			for (Object o : vals) {
				preparedStmt.setObject(count, o);
				count++;
			}
		}
		ResultSet rs = preparedStmt.executeQuery();
		return extractData(rs);
	}

	public abstract List<T> extractData(ResultSet rs) throws SQLException, ClassNotFoundException;

}
