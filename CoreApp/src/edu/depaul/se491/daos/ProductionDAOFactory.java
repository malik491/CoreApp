/**
 * A production DAOFactory (uses production connection factory) 
 */
package edu.depaul.se491.daos;

/**
 * @author Malik
 *
 */
public class ProductionDAOFactory extends DAOFactory {
	private static DAOFactory instance;
	
	
	/**
	 * return a production DAOFactory instance
	 * @return
	 */
	public static DAOFactory getInstance() {
		if (instance == null)
			instance = new ProductionDAOFactory();
		return instance;
	}
	
	private ProductionDAOFactory() {
		super(ProductionConnectionFactory.getInstance());
	}

}
