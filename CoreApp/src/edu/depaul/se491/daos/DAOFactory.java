/**
 * Factory for all Data Access Objects (DAOs)
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
	 * construct a DAO factory with the given connection factory
	 * @param connFactory database connection factory
	 */
	protected DAOFactory(ConnectionFactory connFactory) {
		this.connFactory = connFactory;
	}
	
	/**
	 * return MenuItem DAO
	 * @return
	 */
	public MenuItemDAO getMenuItemDAO(){
		return new MenuItemDAO(this, connFactory);
	}

	/**
	 * return Address DAO
	 * @return
	 */
	public AddressDAO getAddressDAO(){
		return new AddressDAO(this, connFactory);
	}
	
	/**
	 * return User DAO
	 * @return
	 */
	public UserDAO getUserDAO() {
		return new UserDAO(this, connFactory);
	}
	
	/**
	 * return Account DAO
	 * @return
	 */
	public AccountDAO getAccountDAO(){
		return new AccountDAO(this, connFactory);
	}
	
	/**
	 * return Payment DAO
	 * @return
	 */
	public PaymentDAO getPaymentDAO() {
		return new PaymentDAO(this, connFactory);
	}
	
	/**
	 * return Order DAO
	 * @return
	 */
	public OrderDAO getOrderDAO(){
		return new OrderDAO(this, connFactory);
	}

	/**
	 * return OrderItem DAO
	 * @return
	 */
	public OrderItemDAO getOrderItemDAO() {
		return new OrderItemDAO(this, connFactory);
	}
}
