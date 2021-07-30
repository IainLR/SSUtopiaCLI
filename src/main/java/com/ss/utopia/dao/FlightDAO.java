package com.ss.utopia.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.ss.utopia.domain.Flight;

public class FlightDAO extends BaseDAO<Flight> {

	public FlightDAO(Connection connection) {
		super(connection);
		// TODO Auto-generated constructor stub
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
			flight.setDepartureTime(rs.getTimestamp("departure_time"));
			flight.setReservedSeats(rs.getInt("reserved_seats"));
			flight.setSeatPrice(rs.getFloat("seat_price"));
			flights.add(flight);
		}
		return flights;
	}

}
