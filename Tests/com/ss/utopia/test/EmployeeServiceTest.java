package com.ss.utopia.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.sql.SQLException;

import org.junit.Test;

import com.ss.utopia.domain.Route;
import com.ss.utopia.service.EmployeeService;

public class EmployeeServiceTest {

	EmployeeService employeeService = new EmployeeService();

	@Test
	public void readFlightsTest() throws SQLException {
		assertNotEquals(null, employeeService.readFlights());
		assertEquals(new Integer(1), employeeService.readFlights().get(0).getRouteId());
	}

	@Test
	public void readUserByIdTest() throws SQLException {
		assertNotEquals(null, employeeService.readUserById(2));
		assertEquals("Passenger", employeeService.readUserById(2).getFamilyName());
	}

	@Test
	public void readRouteByIdTest() throws SQLException {
		assertNotEquals(null, employeeService.readRouteById(2));
		assertEquals("DFW", employeeService.readRouteById(2).getDestinationId());
	}

	@Test
	public void readAirplaneByIdTest() throws SQLException {
		assertNotEquals(null, employeeService.readAirplaneById(2));
		assertEquals(new Integer(2), employeeService.readAirplaneById(2).getAirplaneTypeId());
	}

	@Test
	public void readAirplaneTypeByIdTest() throws SQLException {
		assertNotEquals(null, employeeService.readAirplaneTypeById(2));
		assertEquals(new Integer(68), employeeService.readAirplaneTypeById(2).getMaxCapacity());
	}

	@Test
	public void readAirportsByRouteTest() throws SQLException {
		Route route = employeeService.readRouteById(2);

		assertNotEquals(null, employeeService.readAirportsByRoute(route));
		assertEquals("Dallas", employeeService.readAirportsByRoute(route).get(1).getCity());
	}

}
