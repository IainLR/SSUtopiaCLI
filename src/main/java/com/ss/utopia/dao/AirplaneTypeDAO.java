package com.ss.utopia.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.ss.utopia.domain.AirplaneType;

public class AirplaneTypeDAO extends BaseDAO<AirplaneType> {

	public AirplaneTypeDAO(Connection connection) {
		super(connection);
		// TODO Auto-generated constructor stub
	}

	public AirplaneType readAirplaneTypeById(Integer airplaneTypeId) throws ClassNotFoundException, SQLException {
		List<AirplaneType> airplaneTypes = read("SELECT * FROM utopia.airplane_type WHERE id = ?",
				new Object[] { airplaneTypeId });
		return airplaneTypes.get(0);
	}

	public List<AirplaneType> readAllPlaneTypes() throws SQLException, ClassNotFoundException {

		return read("SELECT * FROM utopia.airplane_type", null);

	}

	@Override
	public List<AirplaneType> extractData(ResultSet rs) throws SQLException, ClassNotFoundException {
		// TODO Auto-generated method stub
		List<AirplaneType> airplaneTypes = new ArrayList<>();
		while (rs.next()) {
			AirplaneType airplaneType = new AirplaneType();
			airplaneType.setId(rs.getInt("id"));
			airplaneType.setMaxCapacity(rs.getInt("max_capacity"));

			airplaneTypes.add(airplaneType);
		}

		return airplaneTypes;
	}

}
