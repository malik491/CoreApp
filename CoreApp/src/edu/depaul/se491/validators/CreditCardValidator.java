/**
 * 
 */
package edu.depaul.se491.validators;

import edu.depaul.se491.beans.CreditCardBean;
import edu.depaul.se491.utils.ParamLengths;
import edu.depaul.se491.utils.ParamValues;

/**
 * @author usrm
 *
 */
public class CreditCardValidator extends BeanValidator {

	public boolean validatePayment(CreditCardBean bean) {
		boolean isValid = isValidObject(bean, "Invalid CreditCard (Null)");
		
		if (isValid) {
			isValid  = isValidCcNumber(bean);
			isValid &= isValidCcHolderName(bean);
			isValid &= isValidCcExpMonth(bean);
			isValid &= isValidCcExpYear(bean);
		}
		return isValid;
	}
	
	
	
	private boolean isValidCcNumber(CreditCardBean bean) {
		boolean isValid  = isValidString(bean.getCcNumber(), ParamLengths.CreditCard.MIN_CC_NUMBER, ParamLengths.CreditCard.MAX_CC_NUMBER, "Invalid credit card number (length)");
		isValid &= isValidNumbericString(bean.getCcNumber(), "Invalid credit card number (not digit)");
		
		return isValid;
	}
	
	private boolean isValidCcHolderName(CreditCardBean bean) {
		return isValidString(bean.getCcHolderName(), ParamLengths.CreditCard.MIN_CC_HOLDER_NAME, ParamLengths.CreditCard.MAX_CC_HOLDER_NAME, "Invalid credit card holder's name");
	}
	
	private boolean isValidCcExpMonth(CreditCardBean bean) {
		return isValidValue(bean.getExpMonth(), ParamValues.CreditCard.MIN_CC_EXP_MONTH, ParamValues.CreditCard.MAX_CC_EXP_MONTH, "Invalid credit card expiration month");
		
	}
	
	private boolean isValidCcExpYear(CreditCardBean bean) {
		return isValidValue(bean.getExpYear(), ParamValues.CreditCard.MIN_CC_EXP_YEAR, ParamValues.CreditCard.MAX_CC_EXP_YEAR, "Invalid credit card expiration year");

	}
}

