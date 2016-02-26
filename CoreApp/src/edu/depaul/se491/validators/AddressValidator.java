package edu.depaul.se491.validators;

import edu.depaul.se491.beans.AddressBean;
import edu.depaul.se491.utils.ParamLengths;

/**
 * AddressBean Validator
 * 
 * @author Malik
 */
public class AddressValidator extends BeanValidator {

	/**
	 * validate address bean
	 * @param bean
	 * @param isNewAddress
	 * @return
	 */
	public boolean validate(AddressBean bean, boolean isNewAddress) {
		boolean isValid = isValidObject(bean);

		if(isValid){
			isValid  =  validateId(bean.getId(), isNewAddress);
			isValid &= isValidLine1(bean);
			isValid &= isValidLine2(bean);
			isValid &= isValidCity(bean);
			isValid &= isValidState(bean);
			isValid &= isValidZipcode(bean);
		}
		
		return isValid;
	}
	
	/**
	 * validate address id
	 * @param addressId
	 * @param isNewAddress
	 * @return
	 */
	public boolean validateId(Long addressId, boolean isNewAddress) {
		boolean isValid = isValidObject(addressId);
		
		if (isValid)
			isValid = isValidId(addressId, isNewAddress);			
		
		return isValid;
	}
	
	private boolean isValidLine1(AddressBean bean) {
		return isValidString(bean.getLine1(), ParamLengths.Address.MIN_LINE1, ParamLengths.Address.MAX_LINE1);
	}
		
	private boolean isValidCity(AddressBean bean) {
		return isValidString(bean.getCity(), ParamLengths.Address.MIN_CITY, ParamLengths.Address.MAX_CITY);
	}	
	
	private boolean isValidState(AddressBean bean) {
		return isValidObject(bean.getState());
	}
	
	private boolean isValidZipcode(AddressBean bean) {
		return isValidString(bean.getZipcode(), ParamLengths.Address.MIN_ZIPCODE, ParamLengths.Address.MAX_ZIPCODE);
	}
	
	private boolean isValidLine2(AddressBean bean) {
		boolean isValid = false;
		
		String line2 = bean.getLine2();

		if (line2 == null || line2.isEmpty()) {
			// null or empty string (zero length) are allowed for line2
			isValid = true;
		} else {
			isValid = isValidString(line2, ParamLengths.Address.MIN_LINE2, ParamLengths.Address.MAX_LINE2);
		}
		
		return isValid;
	}
} 
