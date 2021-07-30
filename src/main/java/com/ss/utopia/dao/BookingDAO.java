package com.ss.utopia.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.ss.utopia.domain.Booking;

public class BookingDAO extends BaseDAO<Booking> {

	public BookingDAO(Connection connection) {
		super(connection);
		// TODO Auto-generated constructor stub
	}

	public List<Booking> readBookings() throws ClassNotFoundException, SQLException {
		return read("SELECT * FROM utopia.booking", null);

	}

	@Override
	public List<Booking> extractData(ResultSet rs) throws SQLException, ClassNotFoundException {
		List<Booking> bookings = new ArrayList<>();
		while (rs.next()) {
			Booking booking = new Booking();
			booking.setId(rs.getInt("id"));
			booking.setIsActive(rs.getInt("is_active"));
			booking.setConfirmationCode(rs.getString("confirmation_code"));
			bookings.add(booking);
		}
		return bookings;
	}

}
