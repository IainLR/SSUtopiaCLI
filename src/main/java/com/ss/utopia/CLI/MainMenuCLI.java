package com.ss.utopia.CLI;

import java.sql.SQLException;
import java.util.Scanner;

public class MainMenuCLI {

	private static Scanner scanner = new Scanner(System.in);

	public void run() throws SQLException {
		boolean running = true;
		while (running) {
			System.out.println("Welcome to the Utopia Airlines Management System. Which category of a user are you\n");
			String message = "1) Employee/Agent \n" + "2) Administrator\n" + "3) Traveler\n" + "4) Quit\n";
			int userSelection = getNumber(message, 4);

			if (userSelection == 1) {
				new EmployeeCLI().run();
			} else if (userSelection == 2) {
				new AdminCLI().run();
			} else if (userSelection == 3) {
				new TravelerCLI().run();
			} else if (userSelection == 4) {
				running = false;
				System.out.println("Goodbye");
			}
		}
		scanner.close();

	}

	private int getNumber(String message, int upperLimit) {
		while (true) {
			System.out.print(message);
			String userInput = scanner.nextLine();
			try {
				if (Integer.parseInt(userInput) >= 0 && Integer.parseInt(userInput) <= upperLimit) {
					return Integer.parseInt(userInput);
				} else {
					System.out.println("Please enter a valid number");
				}
			} catch (Exception ignored) {
				System.out.printf("%s is not a valid option. Please enter reference number %n", userInput);
			}
		}
	}

}
