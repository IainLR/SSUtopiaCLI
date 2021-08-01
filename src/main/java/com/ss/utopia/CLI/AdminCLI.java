package com.ss.utopia.CLI;

import java.sql.SQLException;
import java.time.YearMonth;
import java.util.List;
import java.util.Scanner;

import com.ss.utopia.domain.Airplane;
import com.ss.utopia.domain.AirplaneType;
import com.ss.utopia.domain.Airport;
import com.ss.utopia.domain.Flight;
import com.ss.utopia.domain.Route;
import com.ss.utopia.service.AdminService;

public class AdminCLI {
	private static Scanner scanner = new Scanner(System.in);
	private static AdminService adminService = new AdminService();

	public void run() throws SQLException {
		initialPromp();
//		listAirports();

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
				// new method?
			} else if (userChoice == 7) {
				return;
			}

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
//            bookingCrudOptions(userChoice);
		} else if (selection.equals("Airports")) {
			airportCrudOptions(userChoice);
		} else if (selection.equals("Travelers")) {
//            publisherCrudOptions(userChoice);
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
