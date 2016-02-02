/**
 * Validator class for OrderItem bean
 */
package edu.depaul.se491.validators;

import edu.depaul.se491.beans.OrderItemBean;
import edu.depaul.se491.utils.ParamValues;

/**
 * @author Malik
 *
 */
public class OrderItemValidator extends BeanValidator {

	public boolean validate(OrderItemBean bean) {
		boolean isValid = isValidObject(bean, "Invalid OrderItem (Null)");

		if(isValid){
			isValid  = isValidMenuItem(bean);
			isValid &= isValidQuantity(bean);
			isValid &= isValidStatus(bean);
		}
		
		return isValid;
	}

	private boolean isValidQuantity(OrderItemBean bean) {
		return isValidValue(bean.getQuantity(), ParamValues.OrderItem.MIN_QTY, ParamValues.OrderItem.MAX_QTY, "Invalid OrderItem Quantity");
	}

	private boolean isValidMenuItem(OrderItemBean bean) {
		return isValidObject(bean.getMenuItem(), "Invalid MenuItem for OrderItem (Null)");
	}
	
	private boolean isValidStatus(OrderItemBean bean) {
		return isValidObject(bean.getStatus(), "Invalid status for OrderItem (Null)");
	}
}
