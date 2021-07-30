package com.ss.utopia.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.ss.utopia.domain.Airplane;

public class AirplaneDAO extends BaseDAO<Airplane> {

	public AirplaneDAO(Connection connection) {
		super(connection);
		// TODO Auto-generated constructor stub
	}

	public List<Airplane> readAllPlanes() throws SQLException, ClassNotFoundException {

		return read("SELECT * FROM utopia.airplane", null);

	}

	@Override
	public List<Airplane> extractData(ResultSet rs) throws SQLException, ClassNotFoundException {
		// TODO Auto-generated method stub
		List<Airplane> airplanes = new ArrayList<>();
		while (rs.next()) {
			Airplane airplane = new Airplane();
			airplane.setid(rs.getInt("id"));
			airplane.setAirplaneTypeId(rs.getInt("type_id"));

			airplanes.add(airplane);
		}

		return airplanes;
	}

}
