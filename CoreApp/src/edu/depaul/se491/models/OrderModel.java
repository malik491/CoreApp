/**
 * Order Model
 * 
 * Class to manipulate orders (create, update, delete, etc)
 */
package edu.depaul.se491.models;

import java.util.List;

import edu.depaul.se491.beans.OrderBean;
import edu.depaul.se491.beans.CredentialsBean;
import edu.depaul.se491.beans.OrderItemBean;
import edu.depaul.se491.daos.DAOFactory;
import edu.depaul.se491.daos.ProductionDAOFactory;
import edu.depaul.se491.enums.AccountRole;
import edu.depaul.se491.enums.OrderStatus;
import edu.depaul.se491.enums.OrderType;
import edu.depaul.se491.exceptions.DBException;
import edu.depaul.se491.validators.MenuItemValidator;
import edu.depaul.se491.validators.OrderItemValidator;
import edu.depaul.se491.validators.OrderValidator;

/**
 * @author Malik
 *
 */
public class OrderModel extends BaseModel {
	
	public OrderModel(CredentialsBean credentials) {
		super(ProductionDAOFactory.getInstance(), credentials);
	}
	
	public OrderModel(DAOFactory daoFactory, CredentialsBean credentials) {
		super(daoFactory, credentials);
	}

	/**
	 * create new order and return the newly created order (with id) or null
	 * @param bean
	 * @return
	 * @throws DBException
	 */
	public OrderBean create(OrderBean bean) throws DBException {
		boolean isValid = isValidOrder(bean, true);
		
		AccountRole[] allowedRoles = null;
		if (isValid) {
			OrderStatus status = bean.getStatus();
			if (status == OrderStatus.SUBMITTED)
				allowedRoles = new AccountRole[] {AccountRole.MANAGER, AccountRole.EMPLOYEE, AccountRole.CUSTOMER_APP};
			else
				allowedRoles = new AccountRole[] {AccountRole.MANAGER};
		}
		
		isValid = isValid? hasPermission(allowedRoles) : false;

		OrderBean createdOrder = null;
		if (isValid) {
			createdOrder = getDAOFactory().getOrderDAO().add(bean);
			if (createdOrder == null)
				setResponseMessage("New order could not be added (check order details)");
		}
		
		return createdOrder;
	}

	/**
	 * update an existing order
	 * @param bean
	 * @return
	 * @throws DBException
	 */
	public Boolean update(OrderBean bean) throws DBException {
		boolean isValid = isValidOrder(bean, false);
		
		AccountRole[] allowedRoles = null;
		if (isValid) {
			OrderStatus status = bean.getStatus();
			if (status == OrderStatus.SUBMITTED || status == OrderStatus.PREPARED)
				allowedRoles = new AccountRole[] {AccountRole.MANAGER, AccountRole.EMPLOYEE};
			else
				allowedRoles = new AccountRole[] {AccountRole.MANAGER};
		}
		
		isValid = isValid? hasPermission(allowedRoles) : false;

		Boolean updated = null;
		if (isValid)
			updated = getDAOFactory().getOrderDAO().update(bean);
		
		return updated;
	}
	
	/**
	 * delete an existing order
	 * @param id
	 * @return
	 * @throws DBException
	 */
	public Boolean delete(Long id) throws DBException {
		AccountRole[] allowedRoles = new AccountRole[] {AccountRole.MANAGER};
		boolean isValid = hasPermission(allowedRoles);
		
		isValid = isValid? isValidId(id) : false;
		
		Boolean deleted = null;
		if (isValid) {
			deleted = getDAOFactory().getOrderDAO().delete(id);
		}
		
		return deleted;
	}
	
	/**
	 * return order with associated with the given id or null
	 * @param id
	 * @return
	 * @throws DBException
	 */
	public OrderBean read(Long id) throws DBException {
		AccountRole[] allowedRoles = new AccountRole[] {AccountRole.MANAGER, AccountRole.EMPLOYEE};
		boolean isValid = hasPermission(allowedRoles);
		
		isValid = isValid? isValidId(id) : false;
		
		OrderBean orderBean = null;
		if (isValid) {
			orderBean = getDAOFactory().getOrderDAO().get(id);
			if (orderBean == null)
				setResponseMessage(String.format("No order found (id = %d)", id));
		}
		
		return orderBean;
	}

	/**
	 * return order with associated with the given confirmation number or null
	 * intended for customer to track their orders
	 * @param confirmation
	 * @return
	 * @throws DBException
	 */
	public OrderBean read(String confirmation) throws DBException {
		AccountRole[] allowedRoles = new AccountRole[] {AccountRole.MANAGER, AccountRole.CUSTOMER_APP};
		boolean isValid = hasPermission(allowedRoles);
		
		isValid = isValid? isValidConfirmation(confirmation) : false;
		
		OrderBean orderBean = null;
		if (isValid) {
			orderBean = getDAOFactory().getOrderDAO().get(confirmation);
			if (orderBean == null)
				setResponseMessage(String.format("No order found (confirmation = %s)", confirmation));
		}
		
		return orderBean;
	}
	
	/**
	 * return all orders with the given status (empty list if there is none)
	 * @param status
	 * @return
	 * @throws DBException
	 */
	public List<OrderBean> readAll(OrderStatus status) throws DBException {
		AccountRole[] allowedRoles = new AccountRole[] {AccountRole.MANAGER, AccountRole.EMPLOYEE};
		boolean isValid = hasPermission(allowedRoles);
		
		isValid = isValid? isValidStatus(status): false;
		
		List<OrderBean> orders = null;
		if (isValid)
			orders = getDAOFactory().getOrderDAO().getAllWithStatus(status);
		
		return orders;
	}
	
	/**
	 * return all orders with the given type (empty list if there is none)
	 * @param type
	 * @return
	 * @throws DBException
	 */
	public List<OrderBean> readAll(OrderType type) throws DBException {
		AccountRole[] allowedRoles = new AccountRole[] {AccountRole.MANAGER, AccountRole.EMPLOYEE};
		boolean isValid = hasPermission(allowedRoles);
		
		isValid = isValid? isValidType(type) : false;
		
		List<OrderBean> orders = null;
		if (isValid)
			orders = getDAOFactory().getOrderDAO().getAllWithType(type);
		
		return orders;
	}
	
	
	private boolean isValidId(Long id) {
		OrderValidator orderValidtor = new OrderValidator();
		boolean isValid = orderValidtor.validateId(id, false);
		if (!isValid)
			setResponseMessage(orderValidtor.getValidationMessages().toString());
		
		return isValid;
	}
	
	
	private boolean isValidConfirmation(String confirmation) {
		OrderValidator orderValidtor = new OrderValidator();
		boolean isValid = orderValidtor.validateConfirmation(confirmation);
		
		if (!isValid)
			setResponseMessage(orderValidtor.getValidationMessages().toString());
		
		return isValid;
	}
	
	private boolean isValidStatus(OrderStatus status) {
		boolean isValid = status != null;
		
		if (!isValid)
			setResponseMessage("Invalid order status (NULL)");
		
		return isValid;
	}
	
	private boolean isValidType(OrderType type) {
		boolean isValid = type != null;
		
		if (!isValid)
			setResponseMessage("Invalid order type (NULL)");
		
		return isValid;
	}
	
	private boolean isValidOrder(OrderBean bean, boolean isNewOrder) {
		OrderValidator orderValidtor = new OrderValidator();
		boolean isValid = orderValidtor.validate(bean, isNewOrder);

		if (!isValid) {
			setResponseMessage(orderValidtor.getValidationMessages().toString());
		} else {
			// validate orderItem & menuItem
			OrderItemValidator oiValidator = new OrderItemValidator();
			MenuItemValidator miValidator = new MenuItemValidator();
			
			for (OrderItemBean oi: bean.getItems()) {
				isValid &= oiValidator.validate(oi);
				if (!isValid) {
					setResponseMessage(oiValidator.getValidationMessages().toString());
					break;
				}
				
				isValid &= miValidator.validate(oi.getMenuItem(), false);
				if (!isValid) {
					setResponseMessage(miValidator.getValidationMessages().toString());
					break;
				}
			}
		}
		
		
		return isValid;
	}
	

}
