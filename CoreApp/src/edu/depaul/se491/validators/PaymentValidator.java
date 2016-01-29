/**
 * 
 */
package edu.depaul.se491.validators;

import edu.depaul.se491.beans.PaymentBean;
import edu.depaul.se491.enums.PaymentType;
import edu.depaul.se491.utils.ParamLengths;
import edu.depaul.se491.utils.ParamValues;


/**
 * @author Malik
 *
 */
public class PaymentValidator extends BeanValidator {

	public boolean validatePayment(PaymentBean bean, boolean isNewPayment) {
		boolean isValid = isValidObject(bean, "Invalid Payment (Null)");
		
		if(isValid){
			isValid  = validateId(bean.getId(), isNewPayment);
			isValid &= isValidType(bean, isNewPayment);
			isValid &= isValidTotal(bean);
		}
		
		return isValid;
	}
	
	public boolean validateId(Long paymentId, boolean isNewPayment) {
		boolean isValid = isValidObject(paymentId, "Invalid payment id (Null Long object)");
		
		if (isValid) {
			String invalidMsg = String.format("Invalid payment Id %s", isNewPayment? " (for new payment)": "");
			isValid = isValidId(paymentId, isNewPayment, invalidMsg);	
		}
		return isValid;
	}
	
	private boolean isValidTotal(PaymentBean bean) {
		return isValidValue(bean.getTotal(), ParamValues.Payment.MIN_TOTAL, ParamValues.Payment.MAX_TOTAL, "Invalid Payment total");
	}
	
	private boolean isValidType(PaymentBean bean, boolean isNewPayment) {
		PaymentType type = bean.getType();
		boolean isValid = isValidObject(type, "Invalid Payment Type (NULL)");
		
		if (isValid) {
			if (type == PaymentType.CASH) {
				isValid  = (bean.getCreditCard() == null);
				isValid &= (bean.getTransactionConfirmation() == null);
				
				if (!isValid)
					addMessage("Invalid cash payment (cash payment can't have credit card info & transaction confirmation");
				
		    } else if (type == PaymentType.CREDIT_CARD) {
		    	if (isNewPayment) {
		    		isValid  = isValidObject(bean.getCreditCard(), "Invalid CreditCard (Null)");
		    		isValid &= bean.getTransactionConfirmation() == null; // auto generated for new payment
		    	} else {
		    		int min = ParamLengths.Payment.MIN_TRANSACTION_CONFORMATION;
		    		int max = ParamLengths.Payment.MAX_TRANSACTION_CONFORMATION;
		    		isValid = isValidString(bean.getTransactionConfirmation(), min, max, "Invalid credit card payment transaction confirmation");
		    	}
			}
		}
				
		return isValid;
	}
}
