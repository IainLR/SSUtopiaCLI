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
		} finally {
			if (connection != null) {
				connection.close();
			}
		}

		return null;
	}

	public void updateAirport(Airport airport, String iata) throws SQLException {
		Connection connection = null;
		try {
			connection = connUtil.getConnection();

			AirportDAO airportDAO = new AirportDAO(connection);
			airportDAO.updateAiport(airport, iata);

			connection.commit();
		} catch (Exception e) {
			connection.rollback();
			System.out.println("Error: " + e.getMessage());
		} finally {
			if (connection != null) {
				connection.close();
			}
		}

	}

	public void addAirport(Airport airport) throws SQLException {
		Connection connection = null;
		try {
			connection = connUtil.getConnection();

			AirportDAO airportDAO = new AirportDAO(connection);
			airportDAO.addAiport(airport);

			connection.commit();
		} catch (Exception e) {
			connection.rollback();
			System.out.println("Error: " + e.getMessage());
		} finally {
			if (connection != null) {
				connection.close();
			}
		}

	}

	public void deleteAirport(Airport airport) throws SQLException {
		Connection connection = null;
		try {
			connection = connUtil.getConnection();

			AirportDAO airportDAO = new AirportDAO(connection);
			airportDAO.deleteAirport(airport);

			connection.commit();
		} catch (Exception e) {
			// TODO: handle exception
			connection.rollback();
			System.out.println("Error: " + e.getMessage());
		} finally {
			if (connection != null) {
				connection.close();
			}
		}

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
		} finally {
			if (connection != null) {
				connection.close();
			}
		}

		return null;
	}

	public Route readRouteById(Integer routeId) throws SQLException {
		Connection connection = null;
		try {
			connection = connUtil.getConnection();

			RouteDAO routeDAO = new RouteDAO(connection);
			Route route = routeDAO.readRoutById(routeId);

			return route;
		} catch (Exception e) {
			System.out.println("Error: " + e.getMessage());
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
		} finally {
			if (connection != null) {
				connection.close();
			}
		}

		return null;
	}

	public AirplaneType readAirplaneTypeById(Integer airplaneTypeId) throws SQLException {
		Connection connection = null;
		try {
			connection = connUtil.getConnection();

			AirplaneTypeDAO airplaneTypeDAO = new AirplaneTypeDAO(connection);
			AirplaneType airplaneType = airplaneTypeDAO.readAirplaneTypeById(airplaneTypeId);

			return airplaneType;
		} catch (Exception e) {
			System.out.println("Error: " + e.getMessage());
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
		} finally {
			if (connection != null) {
				connection.close();
			}
		}

		return null;
	}

	public Airplane readAirplaneById(Integer airplaneId) throws SQLException {
		Connection connection = null;
		try {
			connection = connUtil.getConnection();

			AirplaneDAO airplaneDAO = new AirplaneDAO(connection);
			Airplane airplane = airplaneDAO.readAirplaneById(airplaneId);

			return airplane;
		} catch (Exception e) {
			System.out.println("Error: " + e.getMessage());
		} finally {
			if (connection != null) {
				connection.close();
			}
		}

		return null;
	}

//	Flight CRUD--------------------------------------------

	public Integer addFlightReturnPrimaryKey(Flight flight, String departureString) throws SQLException {
		Connection connection = null;
		try {
			connection = connUtil.getConnection();

			FlightDAO flightDAO = new FlightDAO(connection);
			Integer flightPrimaryKey = flightDAO.addFlightReturnPrimaryKey(flight, departureString);

			connection.commit();
			return flightPrimaryKey;
		} catch (Exception e) {
			// TODO: handle exception
			connection.rollback();
			System.out.println("Error: " + e.getMessage());
		} finally {
			if (connection != null) {
				connection.close();
			}
		}
		return null;

	}

	public void addFlightNewRoute(Flight flight, String departureString, Route route) throws SQLException {
		Connection connection = null;
		try {
			connection = connUtil.getConnection();

			RouteDAO routeDAO = new RouteDAO(connection);
			int routeId = routeDAO.addRouteReturnPrimaryKey(route);
			flight.setRouteId(routeId);
			FlightDAO flightDAO = new FlightDAO(connection);
			flightDAO.addFlightReturnPrimaryKey(flight, departureString);

			connection.commit();
		} catch (Exception e) {
			// TODO: handle exception
			connection.rollback();
			System.out.println("Error: " + e.getMessage());
		} finally {
			if (connection != null) {
				connection.close();
			}
		}

	}

	public void deleteFlight(Flight flight) throws SQLException {
		Connection connection = null;
		try {
			connection = connUtil.getConnection();

			FlightDAO flightDAO = new FlightDAO(connection);
			flightDAO.deleteFlight(flight);

			connection.commit();
		} catch (Exception e) {
			// TODO: handle exception
			connection.rollback();
			System.out.println("Error: " + e.getMessage());
		} finally {
			if (connection != null) {
				connection.close();
			}
		}

	}

	public List<Flight> readFlights() throws SQLException {
		Connection connection = null;
		try {
			connection = connUtil.getConnection();

			FlightDAO flightDAO = new FlightDAO(connection);
			List<Flight> flights = flightDAO.readFlights();

			return flights;
		} catch (Exception e) {
			System.out.println("Error: " + e.getMessage());
		} finally {
			if (connection != null) {
				connection.close();
			}
		}

		return null;
	}

	public void updateFlight(Flight flight, String departureString) throws SQLException {
		Connection connection = null;
		try {
			connection = connUtil.getConnection();

			FlightDAO flightDAO = new FlightDAO(connection);
			flightDAO.updateFlight(flight, departureString);

			connection.commit();
		} catch (Exception e) {
			connection.rollback();
			System.out.println("Error: " + e.getMessage());
		} finally {
			if (connection != null) {
				connection.close();
			}
		}

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
		} finally {
			if (connection != null) {
				connection.close();
			}
		}

		return null;
	}

}
