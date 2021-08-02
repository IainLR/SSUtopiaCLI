package com.ss.utopia.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.ss.utopia.dao.AirplaneDAO;
import com.ss.utopia.dao.AirplaneTypeDAO;
import com.ss.utopia.dao.AirportDAO;
import com.ss.utopia.dao.BookingDAO;
import com.ss.utopia.dao.BookingUserDAO;
import com.ss.utopia.dao.FlightBookingDAO;
import com.ss.utopia.dao.FlightDAO;
import com.ss.utopia.dao.RouteDAO;
import com.ss.utopia.dao.UserDAO;
import com.ss.utopia.domain.Airplane;
import com.ss.utopia.domain.AirplaneType;
import com.ss.utopia.domain.Airport;
import com.ss.utopia.domain.Booking;
import com.ss.utopia.domain.BookingUser;
import com.ss.utopia.domain.Flight;
import com.ss.utopia.domain.FlightBooking;
import com.ss.utopia.domain.Route;
import com.ss.utopia.domain.User;

public class EmployeeService {

	ConnectionUtil connUtil = new ConnectionUtil();

	public User readUserById(Integer memberNo) throws SQLException {
		Connection connection = null;
		try {
			connection = connUtil.getConnection();
			UserDAO userDAO = new UserDAO(connection);

			return userDAO.readUserById(memberNo);
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

	public List<Airport> readAirportsByRoute(Route route) throws SQLException {
		Connection connection = null;
		try {
			connection = connUtil.getConnection();
			AirportDAO airportDAO = new AirportDAO(connection);
			List<Airport> airports = new ArrayList<>();
			airports.add(airportDAO.readOriginAirportByRoute(route));
			airports.add(airportDAO.readDestinationAirportByRoute(route));

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

	public List<Flight> readFlightsByUser(User user) throws SQLException {
		Connection connection = null;
		try {
			connection = connUtil.getConnection();

			FlightDAO flightDAO = new FlightDAO(connection);
			List<Flight> flights = flightDAO.readFlightsByUser(user);

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

	public List<Flight> readActiveFlightsByUser(User user) throws SQLException {
		Connection connection = null;
		try {
			connection = connUtil.getConnection();

			FlightDAO flightDAO = new FlightDAO(connection);
			List<Flight> flights = flightDAO.readActiveFlightsByUser(user);

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

	public void addBooking(Booking booking, Flight flight, FlightBooking flightBooking, BookingUser bookingUser)
			throws SQLException {
		Connection connection = null;
		try {
			connection = connUtil.getConnection();
			// save booking, return key
			BookingDAO bookingDAO = new BookingDAO(connection);
			int bookingInt = bookingDAO.addBookingReturnPrimaryKey(booking);
			// update flight occupancy
			FlightDAO flightDAO = new FlightDAO(connection);
			flightDAO.updateFlightOccupancy(flight);
			// set bookingId for flightBooking and bookingUser
			flightBooking.setBookingId(bookingInt);
			bookingUser.setBookingId(bookingInt);
			// add flightBooking
			FlightBookingDAO flightBookingDAO = new FlightBookingDAO(connection);
			flightBookingDAO.addFlightBooking(flightBooking);
			// add bookingUser
			BookingUserDAO bookingUserDAO = new BookingUserDAO(connection);
			bookingUserDAO.addBookingUser(bookingUser);

			connection.commit();
			System.out.println("Booking commit successful!");
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

	public List<String> readBookingConfCodes() throws SQLException {
		Connection connection = null;
		try {
			connection = connUtil.getConnection();

			BookingDAO bookingDAO = new BookingDAO(connection);
			List<String> codes = bookingDAO.readBookingConfirmations();

			return codes;
		} catch (Exception e) {
			System.out.println("Error: " + e.getMessage());
		} finally {
			if (connection != null) {
				connection.close();
			}
		}

		return null;
	}

	public Booking readActiveBookingByUser(User user, Flight flight) throws SQLException {
		Connection connection = null;
		try {
			connection = connUtil.getConnection();

			BookingDAO bookingDAO = new BookingDAO(connection);
			List<Booking> bookings = bookingDAO.readBookingsByUser(user, flight);

			return bookings.get(0);
		} catch (Exception e) {
			System.out.println("Error: " + e.getMessage());
		} finally {
			if (connection != null) {
				connection.close();
			}
		}

		return null;
	}

	public void updateBooking(Booking booking) throws SQLException {
		Connection connection = null;
		try {
			connection = connUtil.getConnection();

			BookingDAO bookingDAO = new BookingDAO(connection);
			bookingDAO.updateBooking(booking);

			connection.commit();
			System.out.println("Update Successful");
		} catch (Exception e) {
			connection.rollback();
			System.out.println("Error: " + e.getMessage());
		} finally {
			if (connection != null) {
				connection.close();
			}
		}

	}

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
}
