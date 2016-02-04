/**
 * Order Model
 * 
 * Class to manipulate orders (create, update, delete, etc)
 */
package edu.depaul.se491.models;

import java.sql.Timestamp;
import java.util.List;

import javax.ws.rs.core.Response.Status;

import edu.depaul.se491.beans.OrderBean;
import edu.depaul.se491.beans.CredentialsBean;
import edu.depaul.se491.beans.OrderItemBean;
import edu.depaul.se491.beans.PaymentBean;
import edu.depaul.se491.daos.DAOFactory;
import edu.depaul.se491.daos.ProductionDAOFactory;
import edu.depaul.se491.enums.AccountRole;
import edu.depaul.se491.enums.OrderItemStatus;
import edu.depaul.se491.enums.OrderStatus;
import edu.depaul.se491.enums.OrderType;
import edu.depaul.se491.enums.PaymentType;
import edu.depaul.se491.exceptions.DBException;
import edu.depaul.se491.utils.ConfirmationGenerator;
import edu.depaul.se491.validators.AddressValidator;
import edu.depaul.se491.validators.CreditCardValidator;
import edu.depaul.se491.validators.MenuItemValidator;
import edu.depaul.se491.validators.OrderItemValidator;
import edu.depaul.se491.validators.OrderValidator;
import edu.depaul.se491.validators.PaymentValidator;

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
	 */
	public OrderBean create(OrderBean bean) {
		boolean isValid = isValidOrder(bean, true);
		
		AccountRole[] allowedRoles = null;
		if (isValid) {
			OrderStatus status = bean.getStatus();
			if (status == OrderStatus.SUBMITTED) {
				allowedRoles = new AccountRole[] {MANAGER, EMPLOYEE, CUSTOMER_APP};
			} else {
				allowedRoles = new AccountRole[] {MANAGER};
			}
		}
		
		isValid = isValid? hasPermission(allowedRoles) : false;

		OrderBean createdOrder = null;
		if (isValid) {
			try {
				bean.setTimestamp(new Timestamp(System.currentTimeMillis()));
				bean.setConfirmation(ConfirmationGenerator.getOrderConfirmation());
				
				PaymentBean payment = bean.getPayment();
				if (payment.getType() == PaymentType.CREDIT_CARD)
					payment.setTransactionConfirmation(ConfirmationGenerator.getPaymentConfirmation(payment.getCreditCard()));
				
				createdOrder = getDAOFactory().getOrderDAO().add(bean);
				if (createdOrder == null)
					setResponseAndMeessageForDBError();
				
			} catch (DBException e){
				setResponseAndMeessageForDBError();
			}
	}
		
		return createdOrder;
	}

	/**
	 * update an existing order
	 * @param updatedOrder
	 * @return
	 */
	public Boolean update(OrderBean updatedOrder) {
		boolean isValid = isValidOrder(updatedOrder, false);
		
		AccountRole[] allowedRoles = null;
		if (isValid) {
			if (updatedOrder.getStatus() == OrderStatus.CANCELED)
				allowedRoles = new AccountRole[] {MANAGER};
			else
				allowedRoles = new AccountRole[] {MANAGER, EMPLOYEE};
		}
		isValid = isValid? hasPermission(allowedRoles) : false;

		OrderBean oldOrder = isValid? read(updatedOrder.getId()) : null;
		isValid = (oldOrder != null);

		Boolean updated = null;
		if (isValid) {
			boolean updateOrder = true;
			
			// confirmation, timestamp, and payment don't get updated.
			updatedOrder.setTimestamp(oldOrder.getTimestamp());
			updatedOrder.setConfirmation(oldOrder.getConfirmation());
			updatedOrder.setPayment(oldOrder.getPayment());

			if (getLoggedinAccount().getRole() != MANAGER) {
				// employee update (from kitchen terminal)
				
				if (oldOrder.getStatus() == OrderStatus.CANCELED) {
					// employee trying to updated a canceled order
					// the order got canceled while it was being prepared
					updateOrder = false;
					updated = false;	
				} else {
					// order type & address don't get updated by employee
					updatedOrder.setType(oldOrder.getType());
					updatedOrder.setAddress(oldOrder.getAddress());
					
					// only update orderItem status
					List<OrderItemBean> updatableItems = getUpdatableOrderItems(oldOrder, updatedOrder);
					updatedOrder.setItems(updatableItems);
					
					// auto update order status (based on the status of all order items)
					boolean isAllOrderItemsReady = isAllOrderItemsReady(updatableItems);
					updatedOrder.setStatus(isAllOrderItemsReady? OrderStatus.PREPARED : OrderStatus.SUBMITTED);
				}
			}
			
			if (updateOrder) {
				try {
					updated = getDAOFactory().getOrderDAO().update(updatedOrder);					
				} catch (DBException e) {
					setResponseAndMeessageForDBError();
				}
			}
		}
		
		return updated;
	}
	
	/**
	 * delete an existing order
	 * @param id
	 * @return
	 */
	public Boolean delete(Long id) throws DBException {
		AccountRole[] allowedRoles = new AccountRole[] {MANAGER};
		boolean isValid = hasPermission(allowedRoles);
		
		isValid = isValid? isValidId(id) : false;
		
		Boolean deleted = null;
		if (isValid) {
			try {
				deleted = getDAOFactory().getOrderDAO().delete(id);
			} catch (DBException e) {
				setResponseAndMeessageForDBError();
			}
		}
		
		return deleted;
	}
	
	/**
	 * return order with associated with the given id or null
	 * @param id
	 * @return
	 */
	public OrderBean read(Long id) {
		AccountRole[] allowedRoles = new AccountRole[] {MANAGER, EMPLOYEE};
		boolean isValid = hasPermission(allowedRoles);
		
		isValid = isValid? isValidId(id) : false;
		
		OrderBean orderBean = null;
		if (isValid) {
			try {
				orderBean = getDAOFactory().getOrderDAO().get(id);
				if (orderBean == null) {
					setResponseStatus(Status.NOT_FOUND);
					setResponseMessage(String.format("No order found (id = %d)", id));
				}
			} catch (DBException e) {
				setResponseAndMeessageForDBError();
			}
		}
		
		return orderBean;
	}

	/**
	 * return order with associated with the given confirmation number or null
	 * intended for customer to track their orders
	 * @param confirmation
	 * @return
	 */
	public OrderBean read(String confirmation) {
		AccountRole[] allowedRoles = new AccountRole[] {MANAGER, EMPLOYEE, CUSTOMER_APP};
		boolean isValid = hasPermission(allowedRoles);
		
		isValid = isValid? isValidConfirmation(confirmation) : false;
		
		OrderBean orderBean = null;
		if (isValid) {
			try {
				orderBean = getDAOFactory().getOrderDAO().get(confirmation);
				if (orderBean == null) {
					setResponseStatus(Status.NOT_FOUND);
					setResponseMessage(String.format("No order found (confirmation = %s)", confirmation));
				}
			} catch (DBException e)  {
				setResponseAndMeessageForDBError();
			}
		}
		
		return orderBean;
	}
	
	/**
	 * return all orders with the given status (empty list if there is none)
	 * @param status
	 * @return
	 */
	public List<OrderBean> readAll(OrderStatus status) {
		AccountRole[] allowedRoles = new AccountRole[] {MANAGER, EMPLOYEE};
		boolean isValid = hasPermission(allowedRoles);
		
		isValid = isValid? isValidStatus(status): false;
		
		List<OrderBean> orders = null;
		if (isValid) {
			try {
				orders = getDAOFactory().getOrderDAO().getAllWithStatus(status);
			} catch (DBException e) {
				setResponseAndMeessageForDBError();
			}
		}
		
		return orders;
	}
	/**
	 * return all orders with the given type (empty list if there is none)
	 * @return
	 */
	public List<OrderBean> readAll() {
		AccountRole[] allowedRoles = new AccountRole[] {MANAGER};
		boolean isValid = hasPermission(allowedRoles);
		
		List<OrderBean> orders = null;
		if (isValid) {
			try {
				orders = getDAOFactory().getOrderDAO().getAll();
			} catch (DBException e) {
				setResponseAndMeessageForDBError();
			}
		}
		return orders;
	}
	
	
	/**
	 * return all orders with the given type (empty list if there is none)
	 * @param type
	 * @return
	 */
	public List<OrderBean> readAll(OrderType type) {
		AccountRole[] allowedRoles = new AccountRole[] {MANAGER, EMPLOYEE};
		boolean isValid = hasPermission(allowedRoles);
		
		isValid = isValid? isValidType(type) : false;
		
		List<OrderBean> orders = null;
		if (isValid) {
			try {
				orders = getDAOFactory().getOrderDAO().getAllWithType(type);
			} catch (DBException e) {
				setResponseAndMeessageForDBError();
			}
		}
		
		return orders;
	}
	
	
	private boolean isValidId(Long id) {
		boolean isValid = new OrderValidator().validateId(id, false);
		
		if (!isValid) {
			setResponseStatus(Status.BAD_REQUEST);
			setResponseMessage("Invalid Order id");
		}		
		return isValid;
	}
	
	
	private boolean isValidConfirmation(String confirmation) {
		boolean isValid = new OrderValidator().validateConfirmation(confirmation);
		
		if (!isValid) {
			setResponseStatus(Status.BAD_REQUEST);
			setResponseMessage("Invalid Order confirmation");
		}
		
		return isValid;
	}
	
	private boolean isValidStatus(OrderStatus status) {
		boolean isValid = (status != null);
		
		if (!isValid) {
			setResponseStatus(Status.BAD_REQUEST);
			setResponseMessage("Invalid Order status");
		}
		
		return isValid;
	}
	
	private boolean isValidType(OrderType type) {
		boolean isValid = (type != null);
		
		if (!isValid) {
			setResponseStatus(Status.BAD_REQUEST);
			setResponseMessage("Invalid Order type ");
		}
		
		return isValid;
	}
	
	private boolean isValidOrder(OrderBean bean, boolean isNewOrder) {
		boolean isValid = new OrderValidator().validate(bean, isNewOrder);

		if (isValid) {
			// validate payment
			PaymentBean payment = bean.getPayment();
			isValid = new PaymentValidator().validate(payment, isNewOrder);
			
			if (isValid && isNewOrder) {
				// validate credit card
				PaymentType type = payment.getType();
				isValid = (type == PaymentType.CREDIT_CARD)? new CreditCardValidator().validate(payment.getCreditCard()) : true;
			}
		}
		
		if (isValid) {
			// validate address (if delivery)
			if (bean.getType() == OrderType.DELIVERY)
				isValid = new AddressValidator().validate(bean.getAddress(), isNewOrder);
		}
		
		if (isValid) {
			// validate orderItem & menuItem
			OrderItemValidator orderItemValidator = new OrderItemValidator();
			
			for (OrderItemBean orderItem: bean.getItems()) {
				isValid &= orderItemValidator.validate(orderItem);
				if (!isValid)
					break;
			}
		}
		
		if (!isValid) {
			setResponseStatus(Status.BAD_REQUEST);
			setResponseMessage("Invalid Order data");
		}
		
		
		return isValid;
	}
	
	
	private List<OrderItemBean> getUpdatableOrderItems(OrderBean oldOrder, OrderBean updatedOrder) {
		List<OrderItemBean> updatableOrderItems = oldOrder.getItems();
		
		for (OrderItemBean updatedOrderItem: updatedOrder.getItems()) {
			for (OrderItemBean oldOrderItem: updatableOrderItems) {
				long updatedMenuItemId = updatedOrderItem.getMenuItem().getId();
				long oldMenuItemId = oldOrderItem.getMenuItem().getId();
				
				if (updatedMenuItemId == oldMenuItemId) {
					// update the old order item status
					oldOrderItem.setStatus(updatedOrderItem.getStatus());
				}		
			}
		}
		return updatableOrderItems;
	}
	
	
	private boolean isAllOrderItemsReady(List<OrderItemBean> orderItems) {
		boolean isAllReady = true;
		for (OrderItemBean orderItem: orderItems)
			isAllReady &= (orderItem.getStatus() == OrderItemStatus.READY);
		return isAllReady;
	}

}
