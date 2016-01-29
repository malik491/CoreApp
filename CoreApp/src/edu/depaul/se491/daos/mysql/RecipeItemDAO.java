/**
 * RecipeItem Data Access Object (DAO)
 */
package edu.depaul.se491.daos.mysql;

import edu.depaul.se491.daos.ConnectionFactory;
import edu.depaul.se491.daos.DAOFactory;

/**
 * @author Malik
 *
 */
public class RecipeItemDAO {
	private ConnectionFactory connFactory;
	
	public RecipeItemDAO(DAOFactory daoFactory, ConnectionFactory connFactory) {
		this.connFactory = connFactory;
	}
}
