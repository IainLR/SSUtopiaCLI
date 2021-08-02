package com.ss.utopia.CLI;

import java.sql.SQLException;
import java.util.ArrayList;
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
import com.ss.utopia.service.TravelerService;

public class TravelerCLI {

	private static Scanner scanner = new Scanner(System.in);
	private static TravelerService travelerService = new TravelerService();
	private static final Integer BOOK = 1;
	private static final Integer CANCEL = 2;
	private static User user;

	public void run() throws SQLException {
		membershipNumberCheck();
	}

	private void membershipNumberCheck() throws SQLException {
		String prompt = "Enter your Membership Number: \n";
		Integer memberNo = getNumber(prompt, 9999);

		User userAttemptUser = travelerService.readUserById(memberNo);
		if (userAttemptUser == null) {
			System.out.println("Sorry, that does not appear to be a valid membership number \n" + "Please try again");
			membershipNumberCheck();
		} else {
			user = userAttemptUser;
			System.out.println("Welcome " + user.getGivenName() + " " + userAttemptUser.getFamilyName());
			trav1();
		}

	}

	private void trav1() throws SQLException {
		String menuOptions = "1) Book a Ticket\n" + "2) Cancel an Upcoming Trip\n" + "3) Quit to Previous\n ";
		boolean loop = true;

		while (loop) {
			int choice = getNumber(menuOptions, 3);

			if (choice == 1) {
				Flight flightChoiceFlight = listFlights(BOOK);
				if (flightChoiceFlight != null) {
					flightDetail(flightChoiceFlight);
				}

			} else if (choice == 2) {
				Flight flightChoiceFlight = listFlights(CANCEL);
				if (flightChoiceFlight != null) {
					cancelFlight(flightChoiceFlight);
				}

			} else if (choice == 3) {
				System.out.println("Choice 3, quit");
				loop = false;
			}
		}

	}

	private void flightDetail(Flight flight) throws SQLException {
		System.out.println();
		String menuOptions = "1) View Flight Details\n" + "2) Book flight\n" + "3) Quit to Previous\n ";

		int choice = getNumber(menuOptions, 3);
		if (choice == 1) {
			StringBuilder promptStringBuilder = new StringBuilder();

			Route route = travelerService.readRouteById(flight.getRouteId());
			flight.setRoute(route);
			Airplane airplane = travelerService.readAirplaneById(flight.getAirplaneId());
			flight.setAirplane(airplane);
			AirplaneType airplaneType = travelerService.readAirplaneTypeById(airplane.getAirplaneTypeId());
			flight.getAirplane().setAirplaneType(airplaneType);
			int avaliableSeats = airplaneType.getMaxCapacity() - flight.getReservedSeats();

			promptStringBuilder.append("FLight ID: ").append(flight.getId()).append("\n").append("Departure Airport: ")
					.append(route.getOriginId()).append("->").append("\n").append("Destination Airport: ")
					.append(route.getDestinationId()).append("\n").append("Departure Date: ")
					.append(flight.getDepartureTime().getMonth() + ", " + flight.getDepartureTime().getDayOfMonth())
					.append("\n").append("Departure Time: ")
					.append(flight.getDepartureTime().getHour() + ":" + flight.getDepartureTime().getMinute())
					.append("\n").append("Airplane ID: ").append(airplane.getid()).append("\n")
					.append("Airplane Max Capacity: ").append(airplaneType.getMaxCapacity()).append("\n")
					.append("Avaliable seats: ").append(avaliableSeats).append("\n")
					.append("-----------------------------------").append("\n");

			System.out.println(promptStringBuilder.toString());

			boolean yesOrNo = getYesOrNo("Would you like to book this flight? Y/N \n");
			if (yesOrNo) {
				bookFlight(flight);
			}
		} else if (choice == 2) {
			bookFlight(flight);
		} else {
			return;
		}

	}

	private void bookFlight(Flight flight) throws SQLException {
		Booking booking = new Booking();
		booking.setIsActive(1);
		List<String> confCodeStrings = travelerService.readBookingConfCodes();
		boolean stringCheck = true;
		while (stringCheck) {
			String generatedString = generateConfirmationString();
			boolean codeCheck = confCodeStrings.contains(generatedString);
			if (!codeCheck) {
				booking.setConfirmationCode(generatedString);
				stringCheck = false;
			}
		}

		flight.setReservedSeats(flight.getReservedSeats() + 1);

		// create flightBooking NEED returned booking id, can get in adminServ
		FlightBooking flightBooking = new FlightBooking();
		flightBooking.setFlightId(flight.getId());

		BookingUser bookingUser = new BookingUser();
		bookingUser.setUserId(user.getId());

		boolean yesOrNo = getYesOrNo("Are you ready to save this Booking? Y/N \n");
		if (yesOrNo) {
			travelerService.addBooking(booking, flight, flightBooking, bookingUser);
		} else {
			return;
		}

	}

	private void cancelFlight(Flight flight) throws SQLException {
		Booking booking = travelerService.readActiveBookingByUser(user, flight);
		System.out.println(booking.getConfirmationCode());
		boolean confirmation = getYesOrNo(
				"Is above confirmation code correct? Are you ready to proceed with cancellation? Y/N ");
		if (confirmation) {
			booking.setIsActive(0);
			travelerService.updateBooking(booking);
		} else {
			return;
		}
	}

	private Flight listFlights(Integer bookOrCancel) throws SQLException {
		List<Flight> flights = new ArrayList<>();

		if (bookOrCancel == CANCEL) {
			flights = travelerService.readActiveFlightsByUser(user);
		} else if (bookOrCancel == BOOK) {
//			flights = travelerService.readFlightsByUser(user);
			flights = travelerService.readFlights();
		}

		int i = 1;
		StringBuilder promptStringBuilder = new StringBuilder();
		List<Airport> airports = new ArrayList<>();

		for (Flight flight : flights) {
			Route route = travelerService.readRouteById(flight.getRouteId());
			flight.setRoute(route);
			airports = travelerService.readAirportsByRoute(route);
			Airplane airplane = travelerService.readAirplaneById(flight.getAirplaneId());
			flight.setAirplane(airplane);
			AirplaneType airplaneType = travelerService.readAirplaneTypeById(airplane.getAirplaneTypeId());
			flight.getAirplane().setAirplaneType(airplaneType);
			int avaliableSeats = airplaneType.getMaxCapacity() - flight.getReservedSeats();

			promptStringBuilder.append(i).append(") ").append(airports.get(0).getIataId()).append(", ")
					.append(airports.get(0).getCity()).append(" -> ").append(airports.get(1).getIataId()).append(", ")
					.append(airports.get(1).getCity()).append("\n");

//			promptStringBuilder.append(i).append(") \n").append("FLight ID: ").append(flight.getId()).append("\n")
//					.append("Departure Airport: ").append(route.getOriginId()).append("->").append("\n")
//					.append("Destination Airport: ").append(route.getDestinationId()).append("\n")
//					.append("Departure Date: ")
//					.append(flight.getDepartureTime().getMonth() + ", " + flight.getDepartureTime().getDayOfMonth())
//					.append("\n").append("Departure Time: ")
//					.append(flight.getDepartureTime().getHour() + ":" + flight.getDepartureTime().getMinute())
//					.append("\n").append("Airplane ID: ").append(airplane.getid()).append("\n")
//					.append("Airplane Max Capacity: ").append(airplaneType.getMaxCapacity()).append("\n")
//					.append("Available seats: ").append(avaliableSeats).append("\n")
//					.append("-----------------------------------").append("\n");

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
