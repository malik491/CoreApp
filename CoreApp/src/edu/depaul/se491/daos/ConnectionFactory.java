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
}
