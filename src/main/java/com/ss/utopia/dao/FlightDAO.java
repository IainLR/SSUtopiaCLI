package com.ss.utopia.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.ss.utopia.domain.Flight;
import com.ss.utopia.domain.FlightBooking;
import com.ss.utopia.domain.User;

public class FlightDAO extends BaseDAO<Flight> {

	public FlightDAO(Connection connection) {
		super(connection);
		// TODO Auto-generated constructor stub
	}

	public Integer addFlightReturnPrimaryKey(Flight flight, String departureString)
			throws SQLException, ClassNotFoundException {
		return saveAndReturnPrimaryKey(
				"INSERT INTO utopia.flight (route_id, airplane_id, departure_time, reserved_seats, seat_price)\n"
						+ "VALUES(?, ?, ?, ?, ?)",
				new Object[] { flight.getRouteId(), flight.getAirplaneId(), departureString, flight.getReservedSeats(),
						flight.getSeatPrice() });
	}

	public void updateFlight(Flight flight, String departureString) throws ClassNotFoundException, SQLException {
		save("UPDATE utopia.flight SET route_id = ?, airplane_id = ?, departure_time = ?, reserved_seats = ?, seat_price = ? "
				+ "where id = ?",
				new Object[] { flight.getRouteId(), flight.getAirplaneId(), departureString, flight.getReservedSeats(),
						flight.getSeatPrice(), flight.getId() });

	}

	public void updateFlightOccupancy(Flight flight) throws ClassNotFoundException, SQLException {
		save("UPDATE utopia.flight SET reserved_seats = ? where id = ?",
				new Object[] { flight.getReservedSeats(), flight.getId() });

	}

	public void deleteFlight(Flight flight) throws ClassNotFoundException, SQLException {
		save("DELETE FROM utopia.flight where id = ?", new Object[] { flight.getId() });
	}

	public Flight readFlightByFlightBooking(FlightBooking flightBooking) throws ClassNotFoundException, SQLException {
		List<Flight> flights = read("SELECT * FROM utopia.flight WHERE id = ?",
				new Object[] { flightBooking.getFlightId() });

		return flights.get(0);

	}

	public List<Flight> readFlightsByUser(User user) throws ClassNotFoundException, SQLException {
		return read(
				"SELECT \n" + "user.given_name,\n" + "flight.id,\n" + "flight.route_id,\n" + "flight.airplane_id,\n"
						+ "flight.departure_time,\n" + "flight.reserved_seats,\n" + "flight.seat_price\n"
						+ "FROM utopia.user\n" + "INNER JOIN utopia.booking_user ON user.id = booking_user.user_id\n"
						+ "INNER JOIN utopia.booking ON booking.id = booking_user.booking_id\n"
						+ "INNER JOIN utopia.flight_bookings ON flight_bookings.booking_id = booking.id\n"
						+ "INNER JOIN utopia.flight ON flight_bookings.flight_id = flight.id\n" + "WHERE user.id != ?",
				new Object[] { user.getId() });
	}

	public List<Flight> readActiveFlightsByUser(User user) throws ClassNotFoundException, SQLException {
		return read("SELECT \n" + "user.given_name,\n" + "booking.is_active,\n" + "flight.id,\n" + "flight.route_id,\n"
				+ "flight.airplane_id,\n" + "flight.departure_time,\n" + "flight.reserved_seats,\n"
				+ "flight.seat_price\n" + "FROM utopia.user\n"
				+ "INNER JOIN utopia.booking_user ON user.id = booking_user.user_id\n"
				+ "INNER JOIN utopia.booking ON booking.id = booking_user.booking_id\n"
				+ "INNER JOIN utopia.flight_bookings ON flight_bookings.booking_id = booking.id\n"
				+ "INNER JOIN utopia.flight ON flight_bookings.flight_id = flight.id\n"
				+ "WHERE user.id = ? AND booking.is_active = 1", new Object[] { user.getId() });
	}

	public List<Flight> readFlights() throws ClassNotFoundException, SQLException {
		return read("SELECT * FROM utopia.flight", null);

	}

	@Override
	public List<Flight> extractData(ResultSet rs) throws SQLException, ClassNotFoundException {
		List<Flight> flights = new ArrayList<>();
		while (rs.next()) {
			Flight flight = new Flight();
			flight.setId(rs.getInt("id"));
			flight.setRouteId(rs.getInt("route_id"));
			flight.setAirplaneId(rs.getInt("airplane_id"));
//			flight.setDepartureTime(rs.getDate("departure_time").toLocalDate());
//			flight.setDepartureTime(rs.getTimestamp("departure_time"));
			flight.setDepartureTime(rs.getTimestamp("departure_time").toLocalDateTime());
			flight.setReservedSeats(rs.getInt("reserved_seats"));
			flight.setSeatPrice(rs.getFloat("seat_price"));
			flights.add(flight);
		}
		return flights;
	}

}
