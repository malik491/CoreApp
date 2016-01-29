/**
 * Test DAOFactory
 */
package edu.depaul.se491.daos;

import edu.depaul.se491.daos.DAOFactory;

/**
 * @author Malik
 *
 */
public class TestDAOFactory extends DAOFactory {
	private static DAOFactory testInstance;
	
	/**
	 * return a test DAOFactory instance
	 * @return
	 */
	public static DAOFactory getInstance() {
		if (testInstance == null)
			testInstance = new TestDAOFactory();
		return testInstance;
	}
	
	
	private TestDAOFactory() {
		super(TestConnectionFactory.getInstance());
	}	
}
