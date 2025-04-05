package in.abc.main;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;

import in.abc.jdbcUtil.JdbcUtil;

/*
DELIMITER $$

CREATE
    [DEFINER = { user | CURRENT_USER }]
    PROCEDURE `enterprisejavabatch1`.`getStudentsById`(IN id INT,OUT stdName VARCHAR(20),OUT stdAddr VARCHAR(20))
    
	BEGIN
		SELECT sname,saddr INTO stdName,stdAddr
		FROM student
		WHERE sid = id;
	END$$

DELIMITER ;*/

public class RetrieveRecordsApp {

	public static void main(String[] args) {
		Connection connection = null;

		CallableStatement cstmt = null;
		Integer id = 7;

		try {
			// Getting the database connection using utility code
			connection = JdbcUtil.getJdbcConnection();

			// Syntax for stored procedure => {call procedure_name(?,?,?...)}
			String storedProcedure = "{call getStudentsById(?,?,?)}";

			if (connection != null)
				cstmt = connection.prepareCall(storedProcedure);

			if (cstmt != null) {
				// before calling set the input value to StoredProcedure
				cstmt.setInt(1, id);

				//register the OutputParameter with the specific data for conversion
				//use JDBCTypes to map all java DataTypes to DBspecific DataTypes
				cstmt.registerOutParameter(2, Types.VARCHAR);
				cstmt.registerOutParameter(3, Types.VARCHAR);
				
				//execute the query
				cstmt.execute();
				
				//Retrieving the value
				System.out.println("Name of student is    :: "+cstmt.getString(2));
				System.out.println("Address of student is :: "+cstmt.getString(3));
				
			}

		} catch (SQLException e) {
			// handling logic of exception related to SQLException
			e.printStackTrace();

		} catch (Exception e) {
			// handling logic of exception related to common problem
			e.printStackTrace();
		} finally {
			// closing the resource
			try {
				JdbcUtil.closeConnection(null, cstmt, connection);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

	}

}
