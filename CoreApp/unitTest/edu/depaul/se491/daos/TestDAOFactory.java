package edu.depaul.se491.daos;

import edu.depaul.se491.daos.DAOFactory;

/**
 * DAOFactory for testing
 * (use TestConnectionFactory instead of Tomcat's connection pool)
 * 
 * @author Malik
 */
public class TestDAOFactory extends DAOFactory {
	
	/**
	 * construct TestDAOFactory
	 * @param connFactory
	 */
	public TestDAOFactory(ConnectionFactory connFactory) {
		super(connFactory);
	}
}
