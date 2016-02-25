/**
 * Interface for database connection factory
 */
package edu.depaul.se491.daos;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author Malik
 */
public interface ConnectionFactory {
	/**
	 * return database connection
	 * @return
	 * @throws SQLException
	 */
	public Connection getConnection()throws SQLException;
	
	/**
	 * close the underlying data source (connection factory/connection pool)
	 * @throws SQLException
	 */
	public void close() throws SQLException;
}
