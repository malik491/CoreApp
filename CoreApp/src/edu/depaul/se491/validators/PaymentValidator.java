package edu.depaul.se491.validators;

import edu.depaul.se491.beans.PaymentBean;
import edu.depaul.se491.enums.PaymentType;
import edu.depaul.se491.utils.ParamLengths;
import edu.depaul.se491.utils.ParamValues;

/**
 * PaymentBean Validator
 * 
 * @author Malik
 */
public class PaymentValidator extends BeanValidator {

	/**
	 * validate PaymentBean
	 * @param bean
	 * @param isNewPayment
	 * @return
	 */
	public boolean validate(PaymentBean bean, boolean isNewPayment) {
		boolean isValid = isValidObject(bean);
		
		if(isValid){
			isValid  = validateId(bean.getId(), isNewPayment);
			isValid &= isValidType(bean, isNewPayment);
			isValid &= isValidTotal(bean);
		}
		
		return isValid;
	}
	
	/**
	 * validate payment id
	 * @param paymentId
	 * @param isNewPayment
	 * @return
	 */
	public boolean validateId(Long paymentId, boolean isNewPayment) {
		boolean isValid = isValidObject(paymentId);
		
		if (isValid)
			isValid = isValidId(paymentId, isNewPayment);
		
		return isValid;
	}
	
	private boolean isValidTotal(PaymentBean bean) {
		return isValidValue(bean.getTotal(), ParamValues.Payment.MIN_TOTAL, ParamValues.Payment.MAX_TOTAL);
	}
	
	private boolean isValidType(PaymentBean bean, boolean isNewPayment) {
		PaymentType type = bean.getType();
		boolean isValid = isValidObject(type);
		
		if (isValid) {
			if (type == PaymentType.CASH) {
				isValid  = (bean.getCreditCard() == null);
				isValid &= (bean.getTransactionConfirmation() == null);
				
		    } else if (type == PaymentType.CREDIT_CARD) {
		    	if (isNewPayment) {
		    		isValid  = isValidObject(bean.getCreditCard());
		    		isValid &= bean.getTransactionConfirmation() == null; // auto generated for new payment
		    	} else {
		    		int min = ParamLengths.Payment.MIN_TRANSACTION_CONFORMATION;
		    		int max = ParamLengths.Payment.MAX_TRANSACTION_CONFORMATION;
		    		isValid = isValidString(bean.getTransactionConfirmation(), min, max);
		    	}
			}
		}
	
		return isValid;
	}
}
