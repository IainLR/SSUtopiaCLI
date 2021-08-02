package com.ss.utopia.CLI;

import java.sql.SQLException;
import java.time.YearMonth;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import com.ss.utopia.domain.Airplane;
import com.ss.utopia.domain.AirplaneType;
import com.ss.utopia.domain.Airport;
import com.ss.utopia.domain.Booking;
import com.ss.utopia.domain.BookingUser;
import com.ss.utopia.domain.Flight;
import com.ss.utopia.domain.FlightBooking;
import com.ss.utopia.domain.Route;
import com.ss.utopia.domain.User;
import com.ss.utopia.service.AdminService;

public class AdminCLI {
	private static final Integer TRAVELER = 1;
	private static final Integer EMPLOYEE = 3;
//	private static final Integer ADMIN = 2;
	private static Scanner scanner = new Scanner(System.in);
	private static AdminService adminService = new AdminService();

	public void run() throws SQLException {
		initialPromp();

	}

	private void initialPromp() throws SQLException {

		while (true) {
			String prompt = "1) Flights\n" + "2) Bookings\n" + "3) Airports\n" + "4) Travelers\n" + "5) Employees\n"
					+ "6) Over-ride Trip Cancellation for a ticket\n" + "7) Quit to previous\n";
			int userChoice = getNumber(prompt, 7);
			if (userChoice == 1) {
				crudMenu("Flights");
			} else if (userChoice == 2) {
				crudMenu("Bookings");
			} else if (userChoice == 3) {
				crudMenu("Airports");
			} else if (userChoice == 4) {
				crudMenu("Travelers");
			} else if (userChoice == 5) {
				crudMenu("Employees");
			} else if (userChoice == 6) {
				overRideCancellation();
			} else if (userChoice == 7) {
				return;
			}

		}
	}

	private void overRideCancellation() throws SQLException {
		List<Booking> cancelledBookings = adminService.readCancelledBookings();
		Booking bookingSelection = new Booking();
		int i = 1;
		StringBuilder promptStringBuilder = new StringBuilder();
		User user = new User();
		Flight flight = new Flight();
		Route route = new Route();
		String active;

		for (Booking booking : cancelledBookings) {
			flight = adminService.readFlightByBooking(booking);
			route = adminService.readRouteById(flight.getRouteId());
			user = adminService.readUserByBooking(booking);
			if (booking.getIsActive() == 1) {
				active = "YES";
			} else {
				active = "NO";
			}

			promptStringBuilder.append(i).append(") \n").append("Booking ID: ").append(booking.getId()).append("\n")
					.append("Confirmation code: ").append(booking.getConfirmationCode()).append("\n").append("Active: ")
					.append(active).append("\n").append("Traveler: ").append(user.getGivenName()).append(" ")
					.append(user.getFamilyName()).append("\n").append("Username: ").append(user.getUsername())
					.append(" ").append("Email: ").append(user.getEmail()).append("\n").append("Phone: ")
					.append(user.getPhone()).append("\n").append("Flight ID: ").append(flight.getId()).append(" ")
					.append("departure time: ").append(flight.getDepartureTime()).append("\n").append("Route: ")
					.append(route.getOriginId()).append("->").append(route.getDestinationId()).append("\n")
					.append("-----------------------------------").append("\n");

			i++;
		}
		promptStringBuilder.append(i).append(") \n").append("Quit to Previous").append("\n")
				.append("-----------------------------------").append("\n");

		int bookingChoice = getNumber(promptStringBuilder.toString(), cancelledBookings.size() + 1);
		if (bookingChoice == cancelledBookings.size() + 1) {
			return;
		}

		bookingSelection = cancelledBookings.get(bookingChoice - 1);

		boolean yesOrNo = getYesOrNo("Are you certain you wish to override this cancelled booking?");
		if (yesOrNo) {
			bookingSelection.setIsActive(1);
			adminService.updateBooking(bookingSelection);
		}

	}

	private void crudMenu(String selection) throws SQLException {
		System.out.println(selection + " CRUD menu");
		String prompt = "1) create " + selection + "\n" + "2) list " + selection + "\n" + "3) update " + selection
				+ "\n" + "4) Delete " + selection + "\n" + "5) Quit to previous \n";
		int userChoice = getNumber(prompt, 5);
		if (selection.equals("Flights")) {
			flightCrudOptions(userChoice);
		} else if (selection.equals("Bookings")) {
			bookingCrudOptions(userChoice);
		} else if (selection.equals("Airports")) {
			airportCrudOptions(userChoice);
		} else if (selection.equals("Travelers")) {
			travelersCrudOptions(userChoice);
		} else if (selection.equals("Employees")) {
			employeesCrudOptions(userChoice);
		} else if (userChoice == 5) {
			return;
		}

	}

	private void flightCrudOptions(Integer userChoice) throws SQLException {
		if (userChoice == 1) {
			createFlight();
		} else if (userChoice == 2) {
			listFlights();
		} else if (userChoice == 3) {
			System.out.println("Choosde a flight to Update: \n");
			Flight flightChoice = listFlights();
			updateFlight(flightChoice);
		} else if (userChoice == 4) {
			System.out.println("Select a flight you wish to DELETE \n");
			Flight flightChoice = listFlights();
			deleteFlight(flightChoice);
		}
	}

	private void airportCrudOptions(Integer userChoice) throws SQLException {
		if (userChoice == 1) {
			createAirport();
		} else if (userChoice == 2) {
			listAirports();
		} else if (userChoice == 3) {
			System.out.println("Choosde an Airport to Update: \n");
			Airport airportChoice = listAirports();
			updateAirport(airportChoice);
		} else if (userChoice == 4) {
			System.out.println("Select an Airport you wish to DELETE \n");
			Airport airportChoice = listAirports();
			deleteAirport(airportChoice);
		}

	}

	private void bookingCrudOptions(Integer userChoice) throws SQLException {
		if (userChoice == 1) {
			createBooking();
		} else if (userChoice == 2) {
			listBooking();
		} else if (userChoice == 3) {
			System.out.println("Choose a Booking to Update: \n");
			Booking booking = listBooking();
			updateBooking(booking);
		} else if (userChoice == 4) {
			System.out.println("Select a Booking you wish to DELETE \n");
			Booking booking = listBooking();
			deleteBooking(booking);
		}
	}

	private void travelersCrudOptions(Integer userChoice) throws SQLException {
		if (userChoice == 1) {
			createTraveler(TRAVELER);
		} else if (userChoice == 2) {
			listUserByRoleId(TRAVELER);
		} else if (userChoice == 3) {
			System.out.println("Choose a User to Update: \n");
			User user = listUserByRoleId(TRAVELER);
			updateUser(user);
		} else if (userChoice == 4) {
			System.out.println("Select a User you wish to DELETE \n");
			User user = listUserByRoleId(TRAVELER);
			deleteUser(user);
		}

	}

	private void employeesCrudOptions(int userChoice) throws SQLException {
		if (userChoice == 1) {
			createTraveler(EMPLOYEE);
		} else if (userChoice == 2) {
			listUserByRoleId(EMPLOYEE);
		} else if (userChoice == 3) {
			System.out.println("Choosde a flight to Update: \n");
			User user = listUserByRoleId(EMPLOYEE);
			updateUser(user);
		} else if (userChoice == 4) {
			System.out.println("Select a flight you wish to DELETE \n");
			User user = listUserByRoleId(EMPLOYEE);
			deleteUser(user);
		}

	}

	private void deleteUser(User user) throws SQLException {
		boolean yesOrNo = getYesOrNo("Are you certain you wish to delete user ID: " + user.getId() + "? Y/N \n");
		if (yesOrNo) {
			adminService.deleteUser(user);
			return;
		} else {
			return;
		}

	}

	private void updateUser(User user) throws SQLException {
		boolean loop = true;

		while (loop) {
			System.out.println("Please Enter a First Name: \n");
			String firstName = scanner.nextLine();
			user.setGivenName(firstName);

			System.out.println("Enter a Last Name: \n");
			String lastName = scanner.nextLine();
			user.setFamilyName(lastName);

			System.out.println("Enter a Username: \n");
			String userName = scanner.nextLine();
			user.setUsername(userName);

			System.out.println("Enter an email: \n");
			String email = scanner.nextLine();
			user.setEmail(email);

			System.out.println("Enter a password: \n");
			String password = scanner.nextLine();
			user.setPassword(password);

			System.out.println("Enter a phone number: \n");
			String phone = scanner.nextLine();
			user.setPhone(phone);

			boolean yesOrNo = getYesOrNo("Would you like to confirm this update? Y/N \n");
			if (yesOrNo) {
				adminService.updateUser(user);
				return;
			} else {
				return;
			}
		}

	}

	private void createTraveler(Integer role) throws SQLException {
		User user = new User();
		user.setRoleId(role);
		boolean loop = true;

		while (loop) {
			System.out.println("Please Enter a First Name: \n");
			String firstName = scanner.nextLine();
			user.setGivenName(firstName);

			System.out.println("Enter a Last Name: \n");
			String lastName = scanner.nextLine();
			user.setFamilyName(lastName);

			System.out.println("Enter a Username: \n");
			String userName = scanner.nextLine();
			user.setUsername(userName);

			System.out.println("Enter an email: \n");
			String email = scanner.nextLine();
			user.setEmail(email);

			System.out.println("Enter a password: \n");
			String password = scanner.nextLine();
			user.setPassword(password);

			System.out.println("Enter a phone number: \n");
			String phone = scanner.nextLine();
			user.setPhone(phone);

			boolean yesOrNo = getYesOrNo("Would you like to save this new User? Y/N \n");
			if (yesOrNo) {
				adminService.addUser(user);
				return;
			} else {
				return;
			}

		}

	}

	private void deleteBooking(Booking booking) throws SQLException {
		boolean yesOrNo = getYesOrNo("Do you wish to delete your Booking with an id of: " + booking.getId());
		if (yesOrNo) {
//			adminService.deleteBooking(booking);
		}

	}

	private void createBooking() throws SQLException {
		// booking, set is active to 1, generate confirmation code, check uniqueness of
		// code, get primary key
		Booking booking = new Booking();
		booking.setIsActive(1);
		List<String> confCodeStrings = adminService.readBookingConfCodes();
		boolean stringCheck = true;
		while (stringCheck) {
			String generatedString = generateConfirmationString();
			boolean codeCheck = confCodeStrings.contains(generatedString);
			if (!codeCheck) {
				booking.setConfirmationCode(generatedString);
				stringCheck = false;
			}
		}

		// select flight
		Flight flight = new Flight();
		boolean capacityCheck = true;
		while (capacityCheck) {
			System.out.println("Select a Flight: \n");
			flight = listFlights();
			int avaliableSeats = flight.getAirplane().getAirplaneType().getMaxCapacity() - flight.getReservedSeats();
			if (avaliableSeats > 0) {
				capacityCheck = false;
			} else {
				System.out.println("That flight is at capacity, please choose another");
			}
		}
		flight.setReservedSeats(flight.getReservedSeats() + 1);

		// create flightBooking NEED returned booking id, can get in adminServ
		FlightBooking flightBooking = new FlightBooking();
		flightBooking.setFlightId(flight.getId());
		// select user
		System.out.println("Select a Traveler: \n");
		User user = listUserByRoleId(TRAVELER);

		// create bookingUser NEED booking id
		BookingUser bookingUser = new BookingUser();
		bookingUser.setUserId(user.getId());

		// once saved, I will have to update the available seats on flight
		boolean yesOrNo = getYesOrNo("Are you ready to save this Booking? Y/N \n");
		if (yesOrNo) {
			adminService.addBooking(booking, flight, flightBooking, bookingUser);
		} else {
			return;
		}

	}

	private Booking listBooking() throws SQLException {
		// get bookings, get flight, get user
		List<Booking> bookings = adminService.readBookings();

		int i = 1;
		StringBuilder promptStringBuilder = new StringBuilder();
		User user = new User();
		Flight flight = new Flight();
		Route route = new Route();
		String active;

		for (Booking booking : bookings) {
			flight = adminService.readFlightByBooking(booking);
			route = adminService.readRouteById(flight.getRouteId());
			user = adminService.readUserByBooking(booking);
			if (booking.getIsActive() == 1) {
				active = "YES";
			} else {
				active = "NO";
			}

			promptStringBuilder.append(i).append(") \n").append("Booking ID: ").append(booking.getId()).append("\n")
					.append("Confirmation code: ").append(booking.getConfirmationCode()).append("\n").append("Active: ")
					.append(active).append("\n").append("Traveler: ").append(user.getGivenName()).append(" ")
					.append(user.getFamilyName()).append("\n").append("Username: ").append(user.getUsername())
					.append(" ").append("Email: ").append(user.getEmail()).append("\n").append("Phone: ")
					.append(user.getPhone()).append("\n").append("Flight ID: ").append(flight.getId()).append(" ")
					.append("departure time: ").append(flight.getDepartureTime()).append("\n").append("Route: ")
					.append(route.getOriginId()).append("->").append(route.getDestinationId()).append("\n")
					.append("-----------------------------------").append("\n");

			i++;
		}

		int bookingChoice = getNumber(promptStringBuilder.toString(), bookings.size() + 1);
		if (bookingChoice == bookings.size() + 1) {
			return null;
		}

		return bookings.get(bookingChoice - 1);

	}

	private void updateBooking(Booking booking) throws SQLException {
		boolean loop = true;
		String codeString;
		String active;
		if (booking.getIsActive() == 1) {
			active = "YES";
		} else {
			active = "NO";
		}

		while (loop) {
			System.out.println("You have selected Booking ID: " + booking.getId() + "\n" + "Confirmation Code: "
					+ booking.getConfirmationCode() + " Is Active: " + active);
			boolean getCodeYesOrNo = getYesOrNo("Would you like to edit the confirmation code? Y/N \n");
			if (getCodeYesOrNo) {
				System.out.println("Enter new 5 letter confirmation code: \n");
				codeString = scanner.nextLine();
			} else {
				codeString = booking.getConfirmationCode();
			}
			booking.setConfirmationCode(codeString);

			boolean getActiveYesOrNo = getYesOrNo("Is Booking active(Y) or inactive(N)? Y/N \n");
			if (getActiveYesOrNo) {
				booking.setIsActive(1);
				active = "YES";
			} else {
				booking.setIsActive(0);
				active = "NO";
			}

			boolean confirmation = getYesOrNo("Confirmation code: " + booking.getConfirmationCode() + "\n"
					+ "Is Active: " + active + "\n" + "Are these values correct? Y/N \n");
			if (confirmation) {
				loop = false;
				adminService.updateBooking(booking);
			}

		}

	}

	private void createAirport() throws SQLException {
		Airport newAirport = new Airport();
		System.out.println("Please enter the three letter airport code: \n");
		String iata = scanner.nextLine();
		newAirport.setIataId(iata);

		System.out.println("Please enter the airport city: \n");
		String cityString = scanner.nextLine();
		newAirport.setCity(cityString);

		System.out.println("Airport code: " + iata + " and city: " + cityString);
		boolean confirmation = getYesOrNo("Would you like to create this airport? Y/N");
		if (confirmation) {
			adminService.addAirport(newAirport);
		}

	}

	private void updateAirport(Airport airport) throws SQLException {
		boolean loop = true;
		while (loop) {
			System.out.println("You have selected Airport: " + airport.getIataId() + " in " + airport.getCity() + "\n");
			boolean getIataYesOrNo = getYesOrNo("Would you like to edit the airport code? Y/N \n");
			String iataString;
			if (getIataYesOrNo) {
				System.out.println("Enter new three letter airport code: \n");
				iataString = scanner.nextLine();
			} else {
				iataString = airport.getIataId();
			}

			boolean getCityYesOrNo = getYesOrNo("Would you like to edit the airport city? Y/N \n");
			if (getCityYesOrNo) {
				System.out.println("Enter new city: \n");
				String cityString = scanner.nextLine();
				airport.setCity(cityString);
			}

			boolean confirmation = getYesOrNo("Airport code: " + iataString + "\n" + "Airport City: "
					+ airport.getCity() + "\n" + "Are these values correct? Y/N \n");
			if (confirmation) {
				loop = false;
				adminService.updateAirport(airport, iataString);
			}
		}

	}

	private void deleteAirport(Airport airport) throws SQLException {
		System.out.println("You have selected " + airport.getIataId() + ", " + airport.getCity());
		boolean confirmation = getYesOrNo("Are you certain you want to delete this Airport? Y/N \n");
		if (confirmation) {
			adminService.deleteAirport(airport);
		} else {
			return;
		}

	}

	private void createFlight() throws SQLException {
		System.out.println("create a flight!");
		Flight newFlight = new Flight();
		Route route = new Route();
		boolean airportCheck = true;

		// do I create a new route?
		boolean routeCheck = getYesOrNo("Use Existing Route? Y/N \n");

		if (!routeCheck) {
			while (airportCheck) {
				System.out.println("Create a new route \n");
				System.out.println("Select a departure Airport: \n");
				Airport depAirport = listAirports();

				System.out.println(depAirport.getIataId());

				System.out.println("Select a destination Airport: \n");
				Airport destAirport = listAirports();

				System.out.println(destAirport.getIataId());

				if (depAirport.getIataId().equals(destAirport.getIataId())) {
					System.out.println("Departure and Destination airports cannot be the same \n");
				} else {
					route.setOriginId(depAirport.getIataId());
					route.setDestinationId(destAirport.getIataId());
					airportCheck = false;
				}
			}
		} else {
			route = listRoutes();
		}

		// date/time
		System.out.println("Set a departure Date/Time: ");
		String flightDateTime = setDateTime();

		// select airplane
		System.out.println("Select an Airplane: \n");
		Airplane airplane = listPlanes();
		newFlight.setAirplane(airplane);
		newFlight.setAirplaneId(airplane.getid());

		// set seat price
		float seatPrice = getFloat("Enter a ticket price \n");
		newFlight.setSeatPrice(seatPrice);

		// set reserved seats
		newFlight.setReservedSeats(0);

		if (routeCheck) {
			newFlight.setRouteId(route.getId());
			adminService.addFlightReturnPrimaryKey(newFlight, flightDateTime);
		} else {
			adminService.addFlightNewRoute(newFlight, flightDateTime, route);
		}

	}

	private Flight listFlights() throws SQLException {
		List<Flight> flights = adminService.readFlights();

		int i = 1;
		StringBuilder promptStringBuilder = new StringBuilder();

		for (Flight flight : flights) {
			Route route = adminService.readRouteById(flight.getRouteId());
			flight.setRoute(route);
			Airplane airplane = adminService.readAirplaneById(flight.getAirplaneId());
			flight.setAirplane(airplane);
			AirplaneType airplaneType = adminService.readAirplaneTypeById(airplane.getAirplaneTypeId());
			flight.getAirplane().setAirplaneType(airplaneType);
			int avaliableSeats = airplaneType.getMaxCapacity() - flight.getReservedSeats();
//			LocalDateTime localDateTime = flight.getDepartureTime().toLocalDateTime();

			promptStringBuilder.append(i).append(") \n").append("FLight ID: ").append(flight.getId()).append("\n")
					.append("Departure Airport: ").append(route.getOriginId()).append("->").append("\n")
					.append("Destination Airport: ").append(route.getDestinationId()).append("\n")
					.append("Departure Date: ")
					.append(flight.getDepartureTime().getMonth() + ", " + flight.getDepartureTime().getDayOfMonth())
					.append("\n").append("Departure Time: ")
					.append(flight.getDepartureTime().getHour() + ":" + flight.getDepartureTime().getMinute())
					.append("\n").append("Airplane ID: ").append(airplane.getid()).append("\n")
					.append("Airplane Max Capacity: ").append(airplaneType.getMaxCapacity()).append("\n")
					.append("Avaliable seats: ").append(avaliableSeats).append("\n")
					.append("-----------------------------------").append("\n");

			i++;
		}
		promptStringBuilder.append(i).append(") \n").append("Quit to Previous").append("\n")
				.append("-----------------------------------").append("\n");
		int flightChoice = getNumber(promptStringBuilder.toString(), flights.size() + 1);
		if (flightChoice == flights.size() + 1) {
			return null;
		}
		System.out.println(flights.get(flightChoice - 1).getId());

		return flights.get(flightChoice - 1);

	}

	private void updateFlight(Flight flightChoice) throws SQLException {
		if (flightChoice == null) {
			return;
		}
		System.out.println("Update Flight");
		System.out.println("You have chosen to update the FLight with Id: " + flightChoice.getId() + " and Route ID: "
				+ flightChoice.getRouteId() + "\n" + "Departure Date/Time: " + flightChoice.getDepartureTime() + "\n"
				+ "Airplane ID: " + flightChoice.getAirplaneId() + "\n" + "Max Capacity: "
				+ flightChoice.getAirplane().getAirplaneType().getMaxCapacity() + "\n" + "Seats Booked: "
				+ flightChoice.getReservedSeats() + "\n" + "Seat Price: " + flightChoice.getSeatPrice() + "\n"
				+ "Enter ‘quit’ at any prompt to cancel operation.\n");

		System.out.println("Please Select a Route:\n");
		Route route = listRoutes();
		flightChoice.setRouteId(route.getId());
		flightChoice.setRoute(route);

		System.out.println("Set a departure Date/Time: ");
		String flightDateTime = setDateTime();
		System.out.println(flightDateTime);
//		flightChoice.setDepartureTime();

		boolean capacityCheck = true;
		int reservedSeats = flightChoice.getReservedSeats();

		while (capacityCheck) {
			System.out.println("Please Select a plane\n");
			Airplane planeChoice = listPlanes();

			if (planeChoice.getAirplaneType().getMaxCapacity() >= reservedSeats) {
				flightChoice.setAirplaneId(planeChoice.getid());
				flightChoice.setAirplane(planeChoice);
				System.out.println("Capacity test: " + flightChoice.getAirplane().getAirplaneType().getMaxCapacity());
				capacityCheck = false;
			} else {
				System.out.println("Plane selected has insuficient passenger capacity. \n");
				System.out.println("Minimum passenger capacity: " + reservedSeats + "\n");
			}

		}

		Float priceFloat = getFloat("Please Specify a ticket price \n");
		System.out.println(priceFloat);
		flightChoice.setSeatPrice(priceFloat);

		StringBuilder promptStringBuilder = new StringBuilder();
		promptStringBuilder.append("");

		System.out.println("FLight Id: " + flightChoice.getId() + " and Route ID: " + flightChoice.getRouteId() + "\n"
				+ "Departure Date/Time: " + flightDateTime + "\n" + "Airplane ID: " + flightChoice.getAirplaneId()
				+ "\n" + "Max Capacity: " + flightChoice.getAirplane().getAirplaneType().getMaxCapacity() + "\n"
				+ "Seats Booked: " + flightChoice.getReservedSeats() + "\n" + "Seat Price: "
				+ flightChoice.getSeatPrice() + "\n");

		boolean getYesOrNo = getYesOrNo("Double check the above details. Are you ready to UPDATE the flight? Y/N");

		if (getYesOrNo) {
			adminService.updateFlight(flightChoice, flightDateTime);
		}

		return;

	}

	private void deleteFlight(Flight flight) throws SQLException {
		System.out.println("Selected Flight ID: " + flight.getId());

		boolean getYesOrNo = getYesOrNo("Are you certain you want to delete this flight? Y/N \n");

		if (getYesOrNo) {
			adminService.deleteFlight(flight);
		} else {
			return;
		}

	}

	private Route listRoutes() throws SQLException {
		List<Route> routes = adminService.readRoutes();

		int i = 1;
		StringBuilder promptStringBuilder = new StringBuilder();

		for (Route route : routes) {
			promptStringBuilder.append(i).append(") ").append("ID: ").append(route.getId()).append("\n")
					.append("Departure ID: ").append(route.getOriginId()).append("->").append("\n")
					.append("Destination ID: ").append(route.getDestinationId()).append("\n")
					.append("-----------------------------------").append("\n");
			i++;
		}

		promptStringBuilder.append(i).append(") \n").append("Quit to Previous").append("\n")
				.append("-----------------------------------").append("\n");
		int routeChoice = getNumber(promptStringBuilder.toString(), routes.size() + 1);
		if (routeChoice == routes.size() + 1) {
			return null;
		}
		return routes.get(routeChoice - 1);

	}

	private Airplane listPlanes() throws SQLException {
		List<Airplane> airplanes = adminService.readAirplanes();

		int i = 1;
		StringBuilder promptStringBuilder = new StringBuilder();

		for (Airplane ap : airplanes) {
			AirplaneType airplaneType = adminService.readAirplaneTypeById(ap.getAirplaneTypeId());
			ap.setAirplaneType(airplaneType);

			promptStringBuilder.append(i).append(") ").append("ID: ").append(ap.getId()).append("\n")
					.append("Type ID: ").append(ap.getAirplaneTypeId()).append("\n").append("Max Capacity: ")
					.append(ap.getAirplaneType().getMaxCapacity()).append("\n")
					.append("-----------------------------------").append("\n");
			i++;
		}
		promptStringBuilder.append(i).append(") \n").append("Quit to Previous").append("\n")
				.append("-----------------------------------").append("\n");

		int airplaneChoice = getNumber(promptStringBuilder.toString(), airplanes.size() + 1);
		if (airplaneChoice == airplanes.size() + 1) {
			return null;
		}
		return airplanes.get(airplaneChoice - 1);

	}

	private Airport listAirports() throws SQLException {
		List<Airport> airports = adminService.readAiports();

		int i = 1;
		StringBuilder promptStringBuilder = new StringBuilder();

		for (Airport airport : airports) {
			promptStringBuilder.append(i).append(") ").append(airport.getIataId()).append(", ")
					.append(airport.getCity()).append("\n").append("-----------------------------------").append("\n");
			i++;
		}

		int airportChoice = getNumber(promptStringBuilder.toString(), airports.size() + 1);
		if (airportChoice == airports.size() + 1) {
			return null;
		}

		return airports.get(airportChoice - 1);

	}

	private User listUserByRoleId(Integer roleId) throws SQLException {
		List<User> users = adminService.readUsersByRole(roleId);

		int i = 1;
		StringBuilder promptStringBuilder = new StringBuilder();

		for (User user : users) {
			promptStringBuilder.append(i).append(") ").append(user.getGivenName()).append(" ")
					.append(user.getFamilyName()).append("\n").append("Username: ").append(user.getUsername())
					.append("\n").append("Email: ").append(user.getEmail()).append(" Phone: ").append(user.getPhone())
					.append("\n").append("User ID: ").append(user.getId()).append("\n")
					.append("-----------------------------------").append("\n");
			i++;
		}

		int userChoice = getNumber(promptStringBuilder.toString(), users.size() + 1);
		if (userChoice == users.size() + 1) {
			return null;
		}
		return users.get(userChoice - 1);

	}

	private String setDateTime() {
		int month = getNumber("Please Enter a Month: \n", 12);
		YearMonth yearMonthObject = YearMonth.of(2021, month);
		int daysInMonth = yearMonthObject.lengthOfMonth();

		int date = getNumber("Please Enter a Date: \n", daysInMonth);
		int hour = getNumber("Enter Hour: \n", 23);
		int minute = getNumber("Enter minute: \n", 60);

		StringBuilder dateTimeStringBuilder = new StringBuilder();
		// "2021-10-11 09:45:00"
		dateTimeStringBuilder.append("2021-").append(month).append("-").append(date).append(" ").append(hour)
				.append(":").append(minute);

		System.out.println(dateTimeStringBuilder.toString());
		return dateTimeStringBuilder.toString();

	}

	private String generateConfirmationString() {
		int leftLimit = 97; // letter 'a'
		int rightLimit = 122; // letter 'z'
		int targetStringLength = 5;
		Random random = new Random();

		String generatedString = random.ints(leftLimit, rightLimit + 1).limit(targetStringLength)
				.collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append).toString();

		return generatedString;

	}

	private int getNumber(String message, int upperLimit) {
		while (true) {
			System.out.print(message);
			String userInput = scanner.nextLine();
			try {
				if (Integer.parseInt(userInput) > 0 && Integer.parseInt(userInput) <= upperLimit) {
					return Integer.parseInt(userInput);
				} else {
					System.out.println("Please enter a valid number");
				}
			} catch (Exception ignored) {
				System.out.printf("%s is not a valid option. Please enter reference number %n", userInput);
			}
		}
	}

	private Float getFloat(String message) {
		while (true) {
			System.out.print(message);
			String userInput = scanner.nextLine();
//			DecimalFormat df = new DecimalFormat();
//			df.setMaximumFractionDigits(2);

			try {
				if (Float.parseFloat(userInput) > 0 && Float.parseFloat(userInput) <= 9999.00) {
					return Float.parseFloat(userInput);
				} else {
					System.out.println("Please enter a valid number");
				}
			} catch (Exception ignored) {
				System.out.printf("%s is not a valid option. Please enter a float value %n", userInput);
			}
		}
	}

	public static boolean getYesOrNo(String question) {
		String answer;

		while (true) {
			System.out.printf("%s%n", question);
			answer = scanner.nextLine();
			answer = answer.toLowerCase();

			if (answer.equals("y")) {
				return true;
			}

			if (answer.equals("n")) {
				return false;
			}
		}
	}

}
