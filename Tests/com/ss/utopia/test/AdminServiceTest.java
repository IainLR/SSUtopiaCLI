package com.ss.utopia.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.sql.SQLException;

import org.junit.Test;

import com.ss.utopia.service.AdminService;

public class AdminServiceTest {
	AdminService adminService = new AdminService();

	@Test
	public void readRoutesTest() throws SQLException {
		assertNotEquals(null, adminService.readRoutes());
		assertEquals("DFW", adminService.readRoutes().get(0).getOriginId());
	}

	@Test
	public void readAirplanesTest() throws SQLException {
		assertNotEquals(null, adminService.readAirplanes());
		assertEquals(new Integer(1), adminService.readAirplanes().get(0).getAirplaneTypeId());
	}

	@Test
	public void readAirports() throws SQLException {
		assertNotEquals(null, adminService.readAiports());
		assertEquals("Acapulco", adminService.readAiports().get(0).getCity());
	}

	@Test
	public void readAirplaneTypes() throws SQLException {
		assertNotEquals(null, adminService.readAirplaneTypes());
		assertEquals(new Integer(50), adminService.readAirplaneTypes().get(0).getMaxCapacity());
	}

	@Test
	public void readBookingsTest() throws SQLException {
		assertNotEquals(null, adminService.readBookings());
		assertEquals("aaa", adminService.readBookings().get(0).getConfirmationCode());
	}

	@Test
	public void readFlightsTest() throws SQLException {
		assertNotEquals(null, adminService.readFlights());
		assertEquals(new Integer(1), adminService.readFlights().get(0).getRouteId());
	}

}
