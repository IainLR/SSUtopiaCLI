package com.ss.utopia.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.ss.utopia.domain.Booking;
import com.ss.utopia.domain.Flight;
import com.ss.utopia.domain.User;

public class BookingDAO extends BaseDAO<Booking> {

	public BookingDAO(Connection connection) {
		super(connection);
		// TODO Auto-generated constructor stub
	}

	public void addBooking(Booking booking) throws ClassNotFoundException, SQLException {
		save("INSERT INTO utopia.booking (is_active, confirmation_code) VALUES(?, ?)",
				new Object[] { booking.getIsActive(), booking.getConfirmationCode() });
	}

	public void updateBooking(Booking booking) throws ClassNotFoundException, SQLException {
		save("UPDATE utopia.booking SET is_active = ?, confirmation_code = ? where id = ?",
				new Object[] { booking.getIsActive(), booking.getConfirmationCode(), booking.getId() });

	}

	public Integer addBookingReturnPrimaryKey(Booking booking) throws ClassNotFoundException, SQLException {
		return saveAndReturnPrimaryKey("INSERT INTO utopia.booking (is_active, confirmation_code) VALUES(?, ?)",
				new Object[] { booking.getIsActive(), booking.getConfirmationCode() });
	}

	public void deleteBooking(Booking booking) throws ClassNotFoundException, SQLException {
		save("DELETE FROM utopia.airport where id = ?", new Object[] { booking.getId() });
	}

	public List<String> readBookingConfirmations() throws ClassNotFoundException, SQLException {
		List<Booking> bookings = read("SELECT * FROM utopia.booking", null);
		List<String> codeStrings = new ArrayList<>();
		for (Booking booking : bookings) {
			codeStrings.add(booking.getConfirmationCode());
		}

		return codeStrings;

	}

	public List<Booking> readBookingsByUser(User user, Flight flight) throws ClassNotFoundException, SQLException {
		return read(
				"SELECT\n" + "user.given_name,\n" + "flight_bookings.flight_id,\n" + "booking.id,\n"
						+ "booking.is_active,\n" + "booking.confirmation_code,\n" + "user.given_name\n"
						+ "FROM utopia.user\n" + "INNER JOIN utopia.booking_user ON user.id = booking_user.user_id\n"
						+ "INNER JOIN utopia.booking ON booking.id = booking_user.booking_id\n"
						+ "INNER JOIN utopia.flight_bookings ON flight_bookings.booking_id = booking.id\n"
						+ "INNER JOIN utopia.flight ON flight_bookings.flight_id = flight.id\n"
						+ "WHERE user.id = ? AND flight_id = ? AND is_active = 1",
				new Object[] { user.getId(), flight.getId() });

	}

	public List<Booking> readCancelledBookings() throws ClassNotFoundException, SQLException {
		return read("SELECT * FROM utopia.booking WHERE  is_active = 0", null);

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
