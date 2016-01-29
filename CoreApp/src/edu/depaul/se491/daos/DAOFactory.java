/**
 * Factory class for all Data Access Objects (DAOs)
 */
package edu.depaul.se491.daos;

import edu.depaul.se491.daos.mysql.*;

/**
 * @author Malik
 *
 */
public abstract class DAOFactory {
	private ConnectionFactory connFactory;	
	
	
	/**
	 * construct with a production driver
	 */
	protected DAOFactory(ConnectionFactory connFactory) {
		this.connFactory = connFactory;
	}
	
	public OrderDAO getOrderDAO(){
		return new OrderDAO(this, connFactory);
	}
	
	public AccountDAO getAccountDAO(){
		return new AccountDAO(this, connFactory);
	}
	
	public AddressDAO getAddressDAO(){
		return new AddressDAO(this, connFactory);
	}
	
	public MenuItemDAO getMenuItemDAO(){
		return new MenuItemDAO(this, connFactory);
	}

	public OrderItemDAO getOrderItemDAO() {
		return new OrderItemDAO(this, connFactory);
	}
	
	public UserDAO getUserDAO() {
		return new UserDAO(this, connFactory);
	}
	
	public RecipeDAO getRecipeDAO() {
		return new RecipeDAO(this, connFactory);
	}
	
	public RecipeItemDAO getRecipeItemDAO() {
		return new RecipeItemDAO(this, connFactory);
	}
	
	public InventoryItemDAO getInventoryItemDAO() {
		return new InventoryItemDAO(this, connFactory);
	}

	public EmailDAO getEmailDAO() {
		return new EmailDAO(this, connFactory);
	}
	
}
