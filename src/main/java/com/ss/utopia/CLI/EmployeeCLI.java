package com.ss.utopia.CLI;

import java.sql.SQLException;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.ss.utopia.domain.Airplane;
import com.ss.utopia.domain.AirplaneType;
import com.ss.utopia.domain.Airport;
import com.ss.utopia.domain.Flight;
import com.ss.utopia.domain.Route;
import com.ss.utopia.service.EmployeeService;

public class EmployeeCLI {

	private static Scanner scanner = new Scanner(System.in);
	private static EmployeeService employeeService = new EmployeeService();

	public void run() throws SQLException {
		emp1();
	}

	public void emp1() throws SQLException {
		String menuOptions = "1) Enter Flights You Manage\n" + "2) Quit to Previous\n ";
		boolean loop = true;

		while (loop) {
			int choice = getNumber(menuOptions, 2);

			if (choice == 1) {
				Flight flightChoiceFlight = listFlights();
				if (flightChoiceFlight != null) {
					flightDetail(flightChoiceFlight);
				}

			} else if (choice == 2) {

				loop = false;
			}

		}
	}

	private Flight listFlights() throws SQLException {
		List<Flight> flights = new ArrayList<>();

		flights = employeeService.readFlights();

		int i = 1;
		StringBuilder promptStringBuilder = new StringBuilder();
		List<Airport> airports = new ArrayList<>();

		for (Flight flight : flights) {
			Route route = employeeService.readRouteById(flight.getRouteId());
			flight.setRoute(route);
			airports = employeeService.readAirportsByRoute(route);
			Airplane airplane = employeeService.readAirplaneById(flight.getAirplaneId());
			flight.setAirplane(airplane);
			AirplaneType airplaneType = employeeService.readAirplaneTypeById(airplane.getAirplaneTypeId());
			flight.getAirplane().setAirplaneType(airplaneType);
			int avaliableSeats = airplaneType.getMaxCapacity() - flight.getReservedSeats();

			promptStringBuilder.append(i).append(") ").append(airports.get(0).getIataId()).append(", ")
					.append(airports.get(0).getCity()).append(" -> ").append(airports.get(1).getIataId()).append(", ")
					.append(airports.get(1).getCity()).append("\n");

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

	private void flightDetail(Flight flight) throws SQLException {
		System.out.println();
		String menuOptions = "1) View Flight Details\n" + "2) Update flight\n" + "3) Quit to Previous\n ";

		int choice = getNumber(menuOptions, 3);
		if (choice == 1) {
			StringBuilder promptStringBuilder = new StringBuilder();

			Route route = employeeService.readRouteById(flight.getRouteId());
			flight.setRoute(route);
			Airplane airplane = employeeService.readAirplaneById(flight.getAirplaneId());
			flight.setAirplane(airplane);
			AirplaneType airplaneType = employeeService.readAirplaneTypeById(airplane.getAirplaneTypeId());
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

			boolean yesOrNo = getYesOrNo("Would you like to UPDATE this flight? Y/N \n");
			if (yesOrNo) {
				updateFlight(flight);
			}
		} else if (choice == 2) {
			updateFlight(flight);
		} else {
			return;
		}

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
			employeeService.updateFlight(flightChoice, flightDateTime);
		}

		return;

	}

	private Route listRoutes() throws SQLException {
		List<Route> routes = employeeService.readRoutes();

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
		List<Airplane> airplanes = employeeService.readAirplanes();

		int i = 1;
		StringBuilder promptStringBuilder = new StringBuilder();

		for (Airplane ap : airplanes) {
			AirplaneType airplaneType = employeeService.readAirplaneTypeById(ap.getAirplaneTypeId());
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

	private Float getFloat(String message) {
		while (true) {
			System.out.print(message);
			String userInput = scanner.nextLine();

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
