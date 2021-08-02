package com.ss.utopia.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.ss.utopia.domain.BookingUser;
import com.ss.utopia.domain.User;

public class UserDAO extends BaseDAO<User> {

	public UserDAO(Connection connection) {
		super(connection);
		// TODO Auto-generated constructor stub
	}

	public void addUser(User user) throws ClassNotFoundException, SQLException {
		save("INSERT INTO utopia.user (role_id, given_name, family_name, username, email, password, phone)\n"
				+ "VALUES(?, ?, ?, ?, ?, ?, ?)",
				new Object[] { user.getRoleId(), user.getGivenName(), user.getFamilyName(), user.getUsername(),
						user.getEmail(), user.getPassword(), user.getPhone() });

	}

	public void updateUser(User user) throws ClassNotFoundException, SQLException {
		save("UPDATE utopia.user SET given_name = ?, family_name = ?, username = ?, email= ?,  password = ?, phone =? where id = ?",
				new Object[] { user.getGivenName(), user.getFamilyName(), user.getUsername(), user.getEmail(),
						user.getPassword(), user.getPhone(), user.getId() });

	}

	public void deleteUser(User user) throws ClassNotFoundException, SQLException {
		save("DELETE FROM utopia.user where Id = ?", new Object[] { user.getId() });
	}

	public User readUserById(Integer memberNo) throws ClassNotFoundException, SQLException {
		List<User> users = read("SELECT * FROM utopia.user WHERE id =?", new Object[] { memberNo });

		return users.get(0);

	}

	public User readUserByBookingUser(BookingUser bookingUser) throws ClassNotFoundException, SQLException {
		List<User> users = read("SELECT * FROM utopia.user WHERE id =?", new Object[] { bookingUser.getUserId() });

		return users.get(0);

	}

	public List<User> readUsersByRoleId(Integer roleId) throws ClassNotFoundException, SQLException {
		return read("SELECT * FROM utopia.user WHERE role_id = ?", new Object[] { roleId });

	}

	@Override
	public List<User> extractData(ResultSet rs) throws SQLException, ClassNotFoundException {
		List<User> users = new ArrayList<>();
		while (rs.next()) {
			User user = new User();
			user.setId(rs.getInt("id"));
			user.setRoleId(rs.getInt("role_id"));
			user.setGivenName(rs.getString("given_name"));
			user.setFamilyName(rs.getString("family_name"));
			user.setUsername(rs.getString("username"));
			user.setEmail(rs.getString("email"));
			user.setPassword(rs.getString("password"));
			user.setPhone(rs.getString("phone"));

			users.add(user);
		}
		return users;
	}

}
