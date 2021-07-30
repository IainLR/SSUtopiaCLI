package com.ss.utopia.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.ss.utopia.domain.Airport;

public class AirportDAO extends BaseDAO<Airport> {

	public AirportDAO(Connection connection) {
		super(connection);
		// TODO Auto-generated constructor stub
	}

	public List<Airport> readAllAirports() throws SQLException, ClassNotFoundException {

		return read("SELECT * FROM utopia.airport", null);

	}

	@Override
	public List<Airport> extractData(ResultSet rs) throws SQLException, ClassNotFoundException {
		List<Airport> airports = new ArrayList<>();
		while (rs.next()) {
			Airport airport = new Airport();
			airport.setIataId(rs.getString("iata_id"));
			airport.setCity(rs.getString("city"));
			airports.add(airport);
		}
		return airports;
	}

}
