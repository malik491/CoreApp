/**
 * Interface for database connection drivers
 */
package edu.depaul.se491.daos;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author Malik
 */
public interface ConnectionFactory {
	public Connection getConnection()throws SQLException;
	
	/**
	 * close the underlying data source (connection factory/connection pool)
	 * THIS SHOULD BE USED WHEN USING CONNECTION FACTORIES THAT RUN OUTSIDE
	 * A CONTAINER (WHICH MANAGES CONNECTION POOL)
	 * (in other word, when using a connection factory in test environment)
	 * @throws SQLException
	 */
	public void close() throws SQLException;
}
