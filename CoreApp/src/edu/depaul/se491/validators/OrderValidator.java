package edu.depaul.se491.validators;

import edu.depaul.se491.beans.OrderBean;
import edu.depaul.se491.enums.OrderType;
import edu.depaul.se491.beans.OrderItemBean;
import edu.depaul.se491.utils.ParamLengths;
import edu.depaul.se491.utils.ParamValues;

/**
 * OrderBean Validator
 * 
 * @author Malik
 */
public class OrderValidator extends BeanValidator {
	
	/**
	 * validate OrderBean
	 * @param bean
	 * @param isNewOrder
	 * @return
	 */
	public boolean validate(OrderBean bean, boolean isNewOrder) {
		boolean isValid = isValidObject(bean);

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
			if (type == OrderType.DELIVERY)
				isValid &= isValidAddress(bean);
			else
				isValid &= bean.getAddress() == null;
		}
		
		return isValid;
	}

	/**
	 * validate order id
	 * @param orderId
	 * @param isNewOrder
	 * @return
	 */
	public boolean validateId(Long orderId, boolean isNewOrder) {
		boolean isValid = isValidObject(orderId);
		
		if (isValid)
			isValid = isValidId(orderId, isNewOrder);
		
		return isValid;
	}
	
	/**
	 * validate order confirmation
	 * @param confiramtion
	 * @return
	 */
	public boolean validateConfirmation(String confiramtion) {
		return isValidConfirmation(confiramtion, false);
	}
	
	private boolean isValidStatus(OrderBean bean) {
		return isValidObject(bean.getStatus());
	}
	
	private boolean isValidType(OrderBean bean) {
		return isValidObject(bean.getType());
	}

	private boolean isValidPayment(OrderBean bean, boolean isNewOrder) {
		return isValidObject(bean.getPayment());
	}
	
	private boolean isValidConfirmation(String confiramtion, boolean isNewOrder) {
		// new confiramtion is generated for new orders so don't check it here
		boolean isValid = true;
		if (!isNewOrder) {
			isValid = isValidString(confiramtion, ParamLengths.Order.MIN_CONFIRMATION, ParamLengths.Order.MAX_CONFIRMATION);
		}
		return isValid;
	}	

	private boolean isValidOrderItems(OrderBean bean) {
		OrderItemBean[] items = bean.getOrderItems();
		
		boolean isValid = isValidObject(items);
		
		if (isValid) {
			int itemsCount = items.length;
			isValid  = isValidValue(itemsCount, ParamValues.Order.MIN_ORDER_ITEMS, ParamValues.Order.MAX_ORDER_ITEMS);
		}
		
		return isValid;
	}
	
	private boolean isValidAddress(OrderBean bean) {
		return isValidObject(bean.getAddress());
	}

}
