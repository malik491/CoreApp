/**
 * 
 */
package edu.depaul.se491.validators;

import edu.depaul.se491.beans.CreditCardBean;
import edu.depaul.se491.utils.ParamLengths;
import edu.depaul.se491.utils.ParamValues;

/**
 * @author Malik
 *
 */
public class CreditCardValidator extends BeanValidator {

	public boolean validate(CreditCardBean bean) {
		boolean isValid = isValidObject(bean);
		
		if (isValid) {
			isValid  = isValidCcNumber(bean);
			isValid &= isValidCcHolderName(bean);
			isValid &= isValidCcExpMonth(bean);
			isValid &= isValidCcExpYear(bean);
		}
		return isValid;
	}
	
	
	
	private boolean isValidCcNumber(CreditCardBean bean) {
		boolean isValid  = isValidString(bean.getCcNumber(), ParamLengths.CreditCard.MIN_NUMBER, ParamLengths.CreditCard.MAX_NUMBER);
		isValid &= isValidNumbericString(bean.getCcNumber());
		
		return isValid;
	}
	
	private boolean isValidCcHolderName(CreditCardBean bean) {
		return isValidString(bean.getCcHolderName(), ParamLengths.CreditCard.MIN_HOLDER_NAME, ParamLengths.CreditCard.MAX_HOLDER_NAME);
	}
	
	private boolean isValidCcExpMonth(CreditCardBean bean) {
		return isValidValue(bean.getExpMonth(), ParamValues.CreditCard.MIN_EXP_MONTH, ParamValues.CreditCard.MAX_EXP_MONTH);
		
	}
	
	private boolean isValidCcExpYear(CreditCardBean bean) {
		return isValidValue(bean.getExpYear(), ParamValues.CreditCard.MIN_EXP_YEAR, ParamValues.CreditCard.MAX_EXP_YEAR);

	}
}

