package com.ss.utopia.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.sql.SQLException;

import org.junit.Test;

import com.ss.utopia.domain.Route;
import com.ss.utopia.service.TravelerService;

public class TravelerServiceTest {
	TravelerService travelerService = new TravelerService();

	@Test
	public void readFlightsTest() throws SQLException {
		assertNotEquals(null, travelerService.readFlights());
		assertEquals(new Integer(1), travelerService.readFlights().get(0).getRouteId());
	}

	@Test
	public void readUserByIdTest() throws SQLException {
		assertNotEquals(null, travelerService.readUserById(2));
		assertEquals("Passenger", travelerService.readUserById(2).getFamilyName());
	}

	@Test
	public void readRouteByIdTest() throws SQLException {
		assertNotEquals(null, travelerService.readRouteById(2));
		assertEquals("DFW", travelerService.readRouteById(2).getDestinationId());
	}

	@Test
	public void readAirplaneByIdTest() throws SQLException {
		assertNotEquals(null, travelerService.readAirplaneById(2));
		assertEquals(new Integer(2), travelerService.readAirplaneById(2).getAirplaneTypeId());
	}

	@Test
	public void readAirplaneTypeByIdTest() throws SQLException {
		assertNotEquals(null, travelerService.readAirplaneTypeById(2));
		assertEquals(new Integer(68), travelerService.readAirplaneTypeById(2).getMaxCapacity());
	}

	@Test
	public void readAirportsByRouteTest() throws SQLException {
		Route route = travelerService.readRouteById(2);

		assertNotEquals(null, travelerService.readAirportsByRoute(route));
		assertEquals("Dallas", travelerService.readAirportsByRoute(route).get(1).getCity());
	}

}
