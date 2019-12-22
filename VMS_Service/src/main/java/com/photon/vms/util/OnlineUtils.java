package com.photon.vms.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.photon.vms.common.exception.VmsLogging;

public class OnlineUtils {
	public static void destroyObjects(ResultSet resultSet, PreparedStatement preparedStatement, Connection connection) throws SQLException{
		if(resultSet != null){
			resultSet.close();
		}
		if(preparedStatement != null){
			preparedStatement.close();
		}
		if(connection != null){
			connection.close();
		}
	}
	
}
