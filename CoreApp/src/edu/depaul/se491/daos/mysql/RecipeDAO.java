/**
 * Recipe Data Access Object (DAO)
 */
package edu.depaul.se491.daos.mysql;

import edu.depaul.se491.daos.ConnectionFactory;
import edu.depaul.se491.daos.DAOFactory;

/**
 * @author Malik
 *
 */
public class RecipeDAO {
	private ConnectionFactory connFactory;
	
	public RecipeDAO(DAOFactory daoFactory, ConnectionFactory connFactory) {
		this.connFactory = connFactory;
	}

}
