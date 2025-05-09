/**
 * DbConfig is a configuration class for managing database connections. It handles the connection to a MySQL
 * database using JDBC.
 */

package com.AuroraStore.config;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbConfig{
	//Database configuration information
	private static final String DB_Name="aurorastore";
	private static final String URL="jdbc:mysql://localhost:3306/"+DB_Name;
	private static final String USERNAME="root";
	private static final String PASSWORD="";
	
	/**
	 * Establishes a connection to the database.
	 * @return Connection object for the database.
	 * @throws SQLException if a database access errors occur.
	 * @throws ClassNotFoundException if the JDBC class is not found.
	 */
	public static Connection getDbConnection()
		throws SQLException, ClassNotFoundException{
	Class.forName("com.mysql.cj.jdbc.Driver");
	return DriverManager.getConnection(URL,USERNAME,PASSWORD);
		
	}
	
			
}