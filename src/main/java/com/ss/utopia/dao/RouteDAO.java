package com.ss.utopia.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.ss.utopia.domain.Route;

public class RouteDAO extends BaseDAO<Route> {

	public RouteDAO(Connection connection) {
		super(connection);
		// TODO Auto-generated constructor stub
	}

	public Integer addRouteReturnPrimaryKey(Route route) throws SQLException, ClassNotFoundException {
		return saveAndReturnPrimaryKey("INSERT INTO utopia.route (origin_id, destination_id)\n" + "VALUES(?, ?)",
				new Object[] { route.getOriginId(), route.getDestinationId() });
	}

	public Route readRoutById(Integer routeId) throws ClassNotFoundException, SQLException {
		List<Route> routes = read("SELECT * FROM utopia.route\n" + "WHERE id = ?", new Object[] { routeId });
		return routes.get(0);
	}

	public List<Route> readAllRoutes() throws SQLException, ClassNotFoundException {

		return read("SELECT * FROM utopia.route", null);

	}

	@Override
	public List<Route> extractData(ResultSet rs) throws SQLException, ClassNotFoundException {
		// TODO Auto-generated method stub
		List<Route> routes = new ArrayList<>();
		while (rs.next()) {
			Route route = new Route();
			route.setId(rs.getInt("id"));
			route.setDestinationId(rs.getString("destination_id"));
			route.setOriginId(rs.getString("origin_id"));

			routes.add(route);
		}

		return routes;
	}

}
