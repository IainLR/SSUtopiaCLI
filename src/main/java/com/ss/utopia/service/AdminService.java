package com.ss.utopia.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.ss.utopia.dao.AirplaneDAO;
import com.ss.utopia.dao.AirplaneTypeDAO;
import com.ss.utopia.dao.AirportDAO;
import com.ss.utopia.dao.BookingDAO;
import com.ss.utopia.dao.FlightDAO;
import com.ss.utopia.dao.RouteDAO;
import com.ss.utopia.domain.Airplane;
import com.ss.utopia.domain.AirplaneType;
import com.ss.utopia.domain.Airport;
import com.ss.utopia.domain.Booking;
import com.ss.utopia.domain.Flight;
import com.ss.utopia.domain.Route;

public class AdminService {

	ConnectionUtil connUtil = new ConnectionUtil();

//	Airport CRUD--------------------------------------------
	public List<Airport> readAiports() throws SQLException {
		Connection connection = null;
		try {
			connection = connUtil.getConnection();

			AirportDAO airportDAO = new AirportDAO(connection);
			List<Airport> airports = airportDAO.readAllAirports();

			return airports;
		} catch (Exception e) {
			System.out.println("Error: " + e.getMessage());
//			connection.rollback();
		} finally {
			if (connection != null) {
				connection.close();
			}
		}

		return null;
	}

//	Route CRUD--------------------------------------------
	public List<Route> readRoutes() throws SQLException {
		Connection connection = null;
		try {
			connection = connUtil.getConnection();

			RouteDAO routeDAO = new RouteDAO(connection);
			List<Route> routes = routeDAO.readAllRoutes();

			return routes;
		} catch (Exception e) {
			System.out.println("Error: " + e.getMessage());
//			connection.rollback();
		} finally {
			if (connection != null) {
				connection.close();
			}
		}

		return null;
	}

//	AirplaneType CRUD--------------------------------------------
	public List<AirplaneType> readAirplaneTypes() throws SQLException {
		Connection connection = null;
		try {
			connection = connUtil.getConnection();

			AirplaneTypeDAO airplaneTypeDAO = new AirplaneTypeDAO(connection);
			List<AirplaneType> airplaneTypes = airplaneTypeDAO.readAllPlaneTypes();

			return airplaneTypes;
		} catch (Exception e) {
			System.out.println("Error: " + e.getMessage());
//			connection.rollback();
		} finally {
			if (connection != null) {
				connection.close();
			}
		}

		return null;
	}

//	Airplane CRUD--------------------------------------------
	public List<Airplane> readAirplanes() throws SQLException {
		Connection connection = null;
		try {
			connection = connUtil.getConnection();

			AirplaneDAO airplaneDAO = new AirplaneDAO(connection);
			List<Airplane> airplanes = airplaneDAO.readAllPlanes();

			return airplanes;
		} catch (Exception e) {
			System.out.println("Error: " + e.getMessage());
//			connection.rollback();
		} finally {
			if (connection != null) {
				connection.close();
			}
		}

		return null;
	}

//	Flight CRUD--------------------------------------------
	public List<Flight> readFlights() throws SQLException {
		Connection connection = null;
		try {
			connection = connUtil.getConnection();

			FlightDAO flightDAO = new FlightDAO(connection);
			List<Flight> flights = flightDAO.readFlights();

			return flights;
		} catch (Exception e) {
			System.out.println("Error: " + e.getMessage());
//			connection.rollback();
		} finally {
			if (connection != null) {
				connection.close();
			}
		}

		return null;
	}

//	Booking CRUD--------------------------------------------
	public List<Booking> readBookings() throws SQLException {
		Connection connection = null;
		try {
			connection = connUtil.getConnection();

			BookingDAO bookingDAO = new BookingDAO(connection);
			List<Booking> bookings = bookingDAO.readBookings();

			return bookings;
		} catch (Exception e) {
			System.out.println("Error: " + e.getMessage());
//			connection.rollback();
		} finally {
			if (connection != null) {
				connection.close();
			}
		}

		return null;
	}

}
