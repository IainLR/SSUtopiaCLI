package com.ss.utopia.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.ss.utopia.domain.Booking;
import com.ss.utopia.domain.BookingUser;

public class BookingUserDAO extends BaseDAO<BookingUser> {

	public BookingUserDAO(Connection connection) {
		super(connection);
		// TODO Auto-generated constructor stub
	}

	public void addBookingUser(BookingUser bookingUser) throws ClassNotFoundException, SQLException {
		save("INSERT INTO utopia.booking_user (booking_id, user_id) VALUES(?, ?)",
				new Object[] { bookingUser.getBookingId(), bookingUser.getUserId() });
	}

	public BookingUser readBookingUserByBooking(Booking booking) throws ClassNotFoundException, SQLException {
		List<BookingUser> bU = read("SELECT * FROM utopia.booking_user WHERE booking_id = ?",
				new Object[] { booking.getId() });
		return bU.get(0);
	}

	@Override
	public List<BookingUser> extractData(ResultSet rs) throws SQLException, ClassNotFoundException {
		List<BookingUser> bookingUsers = new ArrayList<>();
		while (rs.next()) {
			BookingUser bookingUser = new BookingUser();
			bookingUser.setBookingId(rs.getInt("booking_id"));
			bookingUser.setUserId(rs.getInt("user_id"));
			bookingUsers.add(bookingUser);
		}
		return bookingUsers;
	}

}
