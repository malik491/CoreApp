package edu.depaul.se491.models;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

import javax.ws.rs.core.Response.Status;

import edu.depaul.se491.beans.OrderBean;
import edu.depaul.se491.beans.CredentialsBean;
import edu.depaul.se491.beans.OrderItemBean;
import edu.depaul.se491.beans.PaymentBean;
import edu.depaul.se491.daos.DAOFactory;
import edu.depaul.se491.enums.AccountRole;
import edu.depaul.se491.enums.MenuItemCategory;
import edu.depaul.se491.enums.OrderItemStatus;
import edu.depaul.se491.enums.OrderStatus;
import edu.depaul.se491.enums.OrderType;
import edu.depaul.se491.enums.PaymentType;
import edu.depaul.se491.utils.ConfirmationGenerator;
import edu.depaul.se491.validators.AddressValidator;
import edu.depaul.se491.validators.CreditCardValidator;
import edu.depaul.se491.validators.OrderItemValidator;
import edu.depaul.se491.validators.OrderValidator;
import edu.depaul.se491.validators.PaymentValidator;

/**
 * Order Model
 * class to manipulate orders (create, update, delete, etc)
 * 
 * @author Malik
 */
public class OrderModel extends BaseModel {
	
	/**
	 * construct OrderModel
	 * @param daoFactory
	 * @param credentials
	 */
	public OrderModel(DAOFactory daoFactory, CredentialsBean credentials) {
		super(daoFactory, credentials);
	}

	/**
	 * create new order
	 * @param bean
	 * @return newly created order or null
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
				OrderBean order = copyOrder(bean);
				order.setTimestamp(new Timestamp(System.currentTimeMillis()));
				order.setConfirmation(ConfirmationGenerator.getOrderConfirmation());
				
				PaymentBean payment = order.getPayment();
				if (payment.getType() == PaymentType.CREDIT_CARD) {
					payment.setTransactionConfirmation(ConfirmationGenerator.getPaymentConfirmation(payment.getCreditCard()));
				}
				
				createdOrder = getDAOFactory().getOrderDAO().add(order);
				
			} catch (SQLException e){
				setResponseAndMeessageForDBError(e);
			}
	}
		
		return createdOrder;
	}

	/**
	 * update order
	 * @param updatedOrder
	 * @return
	 */
	public Boolean update(OrderBean updatedOrder) {
		AccountRole[] allowedRoles = new AccountRole[] {MANAGER};
		boolean isValid = hasPermission(allowedRoles);
		
		isValid = isValid? isValidOrder(updatedOrder, false): false;

		OrderBean oldOrder = isValid? read(updatedOrder.getId()) : null;
		isValid = (oldOrder != null);

		Boolean updated = null;
		if (isValid) {
			boolean updateOrder = isValidUpdatedOrderItems(oldOrder.getOrderItems(), updatedOrder.getOrderItems());

			if (updateOrder) {
				// confirmation, timestamp, and payment don't get updated.
				updatedOrder.setTimestamp(oldOrder.getTimestamp());
				updatedOrder.setConfirmation(oldOrder.getConfirmation());
				updatedOrder.setPayment(oldOrder.getPayment());
			}
			
			if (updateOrder) {
				try {
					updated = getDAOFactory().getOrderDAO().update(updatedOrder);					
				} catch (SQLException e) {
					setResponseAndMeessageForDBError(e);
				}
			}
		}
		
		return updated;
	}
	
	/**
	 * update order items status for a given station
	 * @param updatedOrder
	 * @param currentStation
	 * @return
	 */
	public Boolean update(OrderBean updatedOrder, MenuItemCategory currentStation) {
		AccountRole[] allowedRoles = new AccountRole[] {EMPLOYEE};
		boolean isValid = hasPermission(allowedRoles);
		
		isValid = isValid? isValidOrder(updatedOrder, false): false;
		isValid = isValid? isValidStation(currentStation) : false;

		OrderBean oldOrder = isValid? read(updatedOrder.getId()) : null;
		isValid = (oldOrder != null);

		Boolean updated = null;
		if (isValid) {
			boolean updateOrder = isValidUpdatedOrderItems(oldOrder.getOrderItems(), updatedOrder.getOrderItems());

			if (updateOrder) {
				// employee can only update order items status
				// confirmation, timestamp, and payment don't get updated.
				
				updatedOrder.setTimestamp(oldOrder.getTimestamp());
				updatedOrder.setConfirmation(oldOrder.getConfirmation());
				updatedOrder.setPayment(oldOrder.getPayment());
				updatedOrder.setStatus(oldOrder.getStatus());
				updatedOrder.setType(oldOrder.getType());
				updatedOrder.setAddress(oldOrder.getAddress());
				
				// only update items for the current station
				for (OrderItemBean updatedItem : updatedOrder.getOrderItems()) {
					if (updatedItem.getMenuItem().getItemCategory() != currentStation) {
						for (OrderItemBean oldItem : oldOrder.getOrderItems()) {
							if (oldItem.getMenuItem().getId() == updatedItem.getMenuItem().getId()) {
								updatedItem.setStatus(oldItem.getStatus());
								break;
							}
						}
					}
				}
				
				// if the order is not canceled, auto update its status based on the status of all order items
				if (updatedOrder.getStatus() != OrderStatus.CANCELED) {
					boolean isAllOrderItemsReady = isAllOrderItemsReady(updatedOrder.getOrderItems());
					updatedOrder.setStatus(isAllOrderItemsReady? OrderStatus.PREPARED : OrderStatus.SUBMITTED);
				}
			}
			
			if (updateOrder) {
				try {
					updated = getDAOFactory().getOrderDAO().update(updatedOrder);					
				} catch (SQLException e) {
					setResponseAndMeessageForDBError(e);
				}
			}
		}
		
		return updated;
	}


	/**
	 * delete order with the given id
	 * @param id
	 * @return
	 */
	public Boolean delete(Long id) {
		AccountRole[] allowedRoles = new AccountRole[] {MANAGER};
		boolean isValid = hasPermission(allowedRoles);
		
		isValid = isValid? isValidId(id) : false;
		
		Boolean deleted = null;
		if (isValid) {
			try {
				deleted = getDAOFactory().getOrderDAO().delete(id);
			} catch (SQLException e) {
				setResponseAndMeessageForDBError(e);
			}
		}
		
		return deleted;
	}
	
	/**
	 * return order with the given id
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
			} catch (SQLException e) {
				setResponseAndMeessageForDBError(e);
			}
		}
		
		return orderBean;
	}

	/**
	 * return order with given confirmation
	 * @param confirmation
	 * @return
	 */
	public OrderBean read(String confirmation) {
		AccountRole[] allowedRoles = new AccountRole[] {MANAGER, CUSTOMER_APP};
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
			} catch (SQLException e)  {
				setResponseAndMeessageForDBError(e);
			}
		}
		
		return orderBean;
	}
	
	/**
	 * return all orders or empty list
	 * @return
	 */
	public List<OrderBean> readAll() {
		AccountRole[] allowedRoles = new AccountRole[] {MANAGER};
		boolean isValid = hasPermission(allowedRoles);
		
		List<OrderBean> orders = null;
		if (isValid) {
			try {
				orders = getDAOFactory().getOrderDAO().getAll();
			} catch (SQLException e) {
				setResponseAndMeessageForDBError(e);
			}
		}
		return orders;
	}
	
	/**
	 * return all orders with the given status or empty list
	 * @param status
	 * @return
	 */
	public List<OrderBean> readAllByStatus(OrderStatus status) {
		AccountRole[] allowedRoles = new AccountRole[] {MANAGER, EMPLOYEE};
		boolean isValid = hasPermission(allowedRoles);
		
		isValid = isValid? isValidStatus(status): false;
		
		if (isValid && getLoggedinAccount().getRole() == EMPLOYEE) {
			// can only read all submitted orders
			isValid = (status == OrderStatus.SUBMITTED);
			if (!isValid) {
				setResponseStatus(Status.UNAUTHORIZED);
				setResponseMessage("Access Denied (unauthorized)");
			}
		}
		
		List<OrderBean> orders = null;
		if (isValid) {
			try {
				orders = getDAOFactory().getOrderDAO().getAllWithStatus(status);
			} catch (SQLException e) {
				setResponseAndMeessageForDBError(e);
			}
		}
		
		return orders;
	}
	
	/**
	 * return all orders with the given type or empty list
	 * @param type
	 * @return
	 */
	public List<OrderBean> readAllByType(OrderType type) {
		AccountRole[] allowedRoles = new AccountRole[] {MANAGER};
		boolean isValid = hasPermission(allowedRoles);
		
		isValid = isValid? isValidType(type) : false;
		
		List<OrderBean> orders = null;
		if (isValid) {
			try {
				orders = getDAOFactory().getOrderDAO().getAllWithType(type);
			} catch (SQLException e) {
				setResponseAndMeessageForDBError(e);
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
			if (bean.getType() == OrderType.DELIVERY) {
				boolean isNewAddress = bean.getAddress().getId() == 0;
				isValid = new AddressValidator().validate(bean.getAddress(), isNewAddress);
			}
		}
		
		if (isValid) {
			// validate orderItem & menuItem
			OrderItemValidator orderItemValidator = new OrderItemValidator();
			
			for (OrderItemBean orderItem: bean.getOrderItems()) {
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
	
	private boolean isValidStation(MenuItemCategory currentStation) {
		boolean isValid = (currentStation != null);
		if (!isValid) {
			setResponseStatus(Status.BAD_REQUEST);
			setResponseMessage("Invalid station data");
		}
		return isValid;
	}
	
	private boolean isValidUpdatedOrderItems(final OrderItemBean[] oldOrderItems, final OrderItemBean[] updatedOrderItems) {
		boolean allWithZeroQty = true;
		
		for (OrderItemBean updatedItem: updatedOrderItems) {
			for (OrderItemBean oldItem: oldOrderItems) {
				if (oldItem.getMenuItem().getId() == updatedItem.getMenuItem().getId()) {
					allWithZeroQty &= (updatedItem.getQuantity() == 0);
					break;
				}
			}
		}
		
		if (allWithZeroQty) {
			setResponseStatus(Status.BAD_REQUEST);
			setResponseMessage("At least one order item must have quantity > 0 (Otherwise, cancel the order instead)");
		}
		
		return !allWithZeroQty;		
	}
	
	
	private boolean isAllOrderItemsReady(OrderItemBean[] orderItems) {
		boolean isAllReady = true;
		for (OrderItemBean orderItem: orderItems)
			isAllReady &= (orderItem.getStatus() == OrderItemStatus.READY);
		return isAllReady;
	}

	
	private OrderBean copyOrder(OrderBean bean) {
		OrderBean order = new OrderBean();
		order.setId(bean.getId());
		order.setType(bean.getType());
		order.setStatus(bean.getStatus());
		order.setConfirmation(bean.getConfirmation());
		order.setTimestamp(bean.getTimestamp());
		order.setOrderItems(bean.getOrderItems());
		
		PaymentBean payment = bean.getPayment();
		order.setPayment(new PaymentBean(payment.getId(), payment.getTotal(), payment.getType(), payment.getCreditCard(), payment.getTransactionConfirmation()));
		order.setAddress(bean.getAddress());
		return order;
	}
}
