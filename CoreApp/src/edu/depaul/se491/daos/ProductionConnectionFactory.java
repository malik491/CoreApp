/**
 * 
 */
package edu.depaul.se491.daos;

import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

/**
 * @author Malik
 *
 */
public class ProductionConnectionFactory implements ConnectionFactory {
	private static ConnectionFactory instance;
	private static DataSource dataSource;
	private boolean isInitialized;
	
	/**
	 * return a production connection factory instance 
	 * @return
	 */
	public static ConnectionFactory getInstance() {
		if (instance == null)
			instance = new ProductionConnectionFactory();
		return instance;
	} 
	
	private ProductionConnectionFactory() {
		isInitialized = false;
	}

	@Override
	public Connection getConnection() throws SQLException {
		if (!isInitialized)
			lazyInitialize();
		return dataSource.getConnection();
	}

	private void lazyInitialize() throws SQLException {
		try {
			InitialContext initialContext = new InitialContext();
			Context context = (Context) initialContext.lookup("java:comp/env");
			dataSource = (DataSource) context.lookup("jdbc/se491"); // actual connection factory
			isInitialized = true;
		} catch (NamingException e) {
			throw new SQLException(("Context Lookup Naming Exception: " + e.getMessage()));
		}		
	}

}
