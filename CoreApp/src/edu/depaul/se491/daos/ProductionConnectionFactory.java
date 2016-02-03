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
	private static final ConnectionFactory instance;
	private static DataSource dataSource;

	static {
		instance = new ProductionConnectionFactory();
		try {
			InitialContext initialContext = new InitialContext();
			Context context = (Context) initialContext.lookup("java:comp/env");
			dataSource = (DataSource) context.lookup("jdbc/se491"); // actual connection factory
		} catch (NamingException e) {
			dataSource = null;
			synchronized (System.err) {
				System.err.println("Context Lookup Naming Exception: " + e.getMessage());				
			}
		}
	}
	
	
	
	/**
	 * return a production connection factory instance 
	 * @return
	 */
	public static ConnectionFactory getInstance() {
		return instance;
	} 
	
	private ProductionConnectionFactory() {
	}

	@Override
	public Connection getConnection() throws SQLException {
		return dataSource != null? dataSource.getConnection() : null;
	}

	@Override
	public void close() throws SQLException {
		throw new UnsupportedOperationException();
	}


}
