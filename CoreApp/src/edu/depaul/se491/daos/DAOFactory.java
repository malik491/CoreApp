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
	
	
	public MenuItemDAO getMenuItemDAO(){
		return new MenuItemDAO(this, connFactory);
	}

	public AddressDAO getAddressDAO(){
		return new AddressDAO(this, connFactory);
	}
	
	public UserDAO getUserDAO() {
		return new UserDAO(this, connFactory);
	}
	
	public AccountDAO getAccountDAO(){
		return new AccountDAO(this, connFactory);
	}
	
	public PaymentDAO getPaymentDAO() {
		return new PaymentDAO(this, connFactory);
	}
	
	public OrderDAO getOrderDAO(){
		return new OrderDAO(this, connFactory);
	}

	public OrderItemDAO getOrderItemDAO() {
		return new OrderItemDAO(this, connFactory);
	}
}
