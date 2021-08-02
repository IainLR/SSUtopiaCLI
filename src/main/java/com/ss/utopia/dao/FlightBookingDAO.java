package com.ss.utopia.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.ss.utopia.domain.Booking;
import com.ss.utopia.domain.FlightBooking;

public class FlightBookingDAO extends BaseDAO<FlightBooking> {

	public FlightBookingDAO(Connection connection) {
		super(connection);
		// TODO Auto-generated constructor stub
	}

	public void addFlightBooking(FlightBooking flightBooking) throws ClassNotFoundException, SQLException {
		save("INSERT INTO utopia.flight_bookings (flight_id, booking_id)\n" + "VALUES(?, ?)",
				new Object[] { flightBooking.getFlightId(), flightBooking.getBookingId() });
	}

	public FlightBooking readFlightBookingByBooking(Booking booking) throws ClassNotFoundException, SQLException {
		List<FlightBooking> fB = read("SELECT * FROM utopia.flight_bookings WHERE booking_id = ?",
				new Object[] { booking.getId() });
		return fB.get(0);
	}

	@Override
	public List<FlightBooking> extractData(ResultSet rs) throws SQLException, ClassNotFoundException {
		List<FlightBooking> flightBookings = new ArrayList<>();
		while (rs.next()) {
			FlightBooking flightBooking = new FlightBooking();
			flightBooking.setBookingId(rs.getInt("booking_id"));
			flightBooking.setFlightId(rs.getInt("flight_id"));
			flightBookings.add(flightBooking);
		}
		return flightBookings;
	}

}
