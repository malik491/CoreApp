/**
 * Validator class for Order bean
 */
package edu.depaul.se491.validators;

import java.util.List;

import edu.depaul.se491.beans.OrderBean;
import edu.depaul.se491.enums.OrderType;
import edu.depaul.se491.beans.OrderItemBean;
import edu.depaul.se491.utils.ParamLengths;
import edu.depaul.se491.utils.ParamValues;

/**
 * @author Malik
 *
 */
public class OrderValidator extends BeanValidator {
	
	
	public boolean validate(OrderBean bean, boolean isNewOrder) {
		boolean isValid = isValidObject(bean, "Invalid Order (Null)");

		if(isValid){
			isValid  = validateId(bean.getId(), isNewOrder);
			isValid &= isValidType(bean);
			isValid &= isValidStatus(bean);
			isValid &= isValidConfirmation(bean.getConfirmation(), isNewOrder); // conf can be null for new orders
			isValid &= isValidPayment(bean, isNewOrder);
			isValid &= isValidOrderItems(bean);
		}
		
		if (isValid) {
			OrderType type = bean.getType();
			if (type == OrderType.DELIVERY) {
				isValid &= isValidAddress(bean);
			} else {
				isValid &= bean.getAddress() == null;
				if (!isValid)
					addMessage("Invalid order (only delivery orders can have address and (optional) notification email");
			}
		}
		
		return isValid;
	}

	public boolean validateId(Long orderId, boolean isNewOrder) {
		boolean isValid = isValidObject(orderId, "Invalid order id (Null Long object)");
		
		if (isValid) {
			String invalidMsg = String.format("Invalid order id %s", isNewOrder? " (for new order)": "");
			isValid = isValidId(orderId, isNewOrder, invalidMsg);			
		}
		
		return isValid;
	}
	
	public boolean validateConfirmation(String confiramtion) {
		return isValidConfirmation(confiramtion, false);
	}
	
	private boolean isValidStatus(OrderBean bean) {
		return isValidObject(bean.getStatus(), "Invalid Order Status (Null)");
	}
	
	private boolean isValidType(OrderBean bean) {
		return isValidObject(bean.getType(), "Invalid Order Type (Null)");
	}

	private boolean isValidPayment(OrderBean bean, boolean isNewOrder) {
		return isValidObject(bean.getPayment(), "Invalid Order Payment (Null)");
	}
	
	private boolean isValidConfirmation(String confiramtion, boolean isNewOrder) {
		// new confiramtion is generated for new orders so don't check it here
		boolean isValid = true;
		if (!isNewOrder) {
			isValid = isValidString(confiramtion, ParamLengths.Order.MIN_CONFIRMATION, ParamLengths.Order.MIN_CONFIRMATION, "Invalid Order Confirmation");
		}
		return isValid;
	}	

	private boolean isValidOrderItems(OrderBean bean) {
		List<OrderItemBean> items = bean.getItems();
		
		boolean isValid = isValidObject(items, "Invalid OrderItems for Order (Null)");
		
		if (isValid) {
			int itemsCount = items.size();
			isValid  = isValidValue(itemsCount, ParamValues.Order.MIN_ORDER_ITEMS, ParamValues.Order.MAX_ORDER_ITEMS, "Invalid OrderItems for Order (0 or too many items)");
		}
		
		return isValid;
	}
	
	private boolean isValidAddress(OrderBean bean) {
		return isValidObject(bean.getAddress(), "Invalid order address (missing delivery address)");
	}

}
