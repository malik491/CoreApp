/**
 * Inventory Model
 * 
 * Class to manipulate the inventory (create inventory item, update inventory item, etc)
 */
package edu.depaul.se491.models;

import edu.depaul.se491.beans.CredentialsBean;
import edu.depaul.se491.beans.InventoryItemBean;
import edu.depaul.se491.daos.DAOFactory;
import edu.depaul.se491.daos.ProductionDAOFactory;
import edu.depaul.se491.exceptions.DBException;

/**
 * @author Malik
 *
 */
public class InventoryModel extends BaseModel {
	
	public InventoryModel(CredentialsBean credentials) {
		super(ProductionDAOFactory.getInstance(), credentials);
	}
	
	public InventoryModel(DAOFactory daoFactory, CredentialsBean credentials) {
		super(daoFactory, credentials);
	}
	
	public InventoryItemBean create(InventoryItemBean bean) throws DBException {
		
		throw new UnsupportedOperationException("No implementation yet");
	}

	public InventoryItemBean read(Long id) throws DBException {
		throw new UnsupportedOperationException("No implementation yet");
	}

	public Boolean delete(Long id) throws DBException {
		throw new UnsupportedOperationException("No implementation yet");
	}

	public Boolean update(InventoryItemBean bean) throws DBException {
		throw new UnsupportedOperationException("No implementation yet");
	}

}
