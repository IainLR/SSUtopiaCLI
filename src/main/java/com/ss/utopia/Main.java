package com.ss.utopia;

import java.sql.SQLException;
import java.util.List;

import com.ss.utopia.CLI.MainMenuCLI;
import com.ss.utopia.domain.Airplane;
import com.ss.utopia.domain.AirplaneType;
import com.ss.utopia.domain.Airport;
import com.ss.utopia.domain.Booking;
import com.ss.utopia.domain.Flight;
import com.ss.utopia.domain.Route;
import com.ss.utopia.service.AdminService;

public class Main {

	public static void main(String[] args) throws SQLException {
		AdminService adminService = new AdminService();

		// JDBC connected!
		List<Airport> airports = adminService.readAiports();

//		for (Airport airport : airports) {
//			System.out.println(airport.getIataId() + ", " + airport.getCity());
//
//		}

		List<Route> routes = adminService.readRoutes();

//		for (Route route : routes) {
//			System.out.println("Origin: " + route.getOriginId() + ", Destination: " + route.getDestinationId());
//		}

		List<AirplaneType> airplaneTypes = adminService.readAirplaneTypes();

//		for (AirplaneType apType : airplaneTypes) {
//			System.out.println(apType.getMaxCapacity());
//		}

		List<Airplane> airplanes = adminService.readAirplanes();

//		for (Airplane airplane : airplanes) {
//			System.out.println(airplane.getid());
//		}

		List<Flight> flights = adminService.readFlights();
//		for (Flight flight : flights) {
//			System.out.println(flight.getDepartureTime().toLocalDateTime().getHour());
//			System.out.println(flight.getDepartureTime().toLocalDateTime().getMinute());
//			System.out.println(flight.getDepartureTime().toLocalDateTime());
////			System.out.println(flight.getReservedSeats());
//			System.out.println(flight.getSeatPrice());
//			System.out.println(flight.getId());
//			System.out.println(flight.getRouteId());
//			System.out.println("-------------------");
//
//		}

		List<Booking> bookings = adminService.readBookings();
//		for (Booking booking : bookings) {
//			System.out.println(booking.getConfirmationCode());
//		}

//		new AdminCLI().run();
		new MainMenuCLI().run();
	}

}
