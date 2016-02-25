/**
 * Production DAO Factory 
 * (uses Production Connection Factory) 
 */
package edu.depaul.se491.daos;

/**
 * @author Malik
 *
 */
public class ProductionDAOFactory extends DAOFactory {
	private static DAOFactory instance;
	
	
	/**
	 * return a Production DAOFactory instance
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
