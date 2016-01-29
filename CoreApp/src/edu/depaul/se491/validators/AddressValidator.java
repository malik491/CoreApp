/**
 * Validator class for Address bean
 */
package edu.depaul.se491.validators;

import edu.depaul.se491.beans.AddressBean;
import edu.depaul.se491.utils.ParamLengths;

/**
 * @author Malik
 *
 */
public class AddressValidator extends BeanValidator {

	public boolean validate(AddressBean bean, boolean isNewAddress) {
		boolean isValid = isValidObject(bean, "Invalid Address (Null)");

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
	
	public boolean validateId(Long addressId, boolean isNewAddress) {
		boolean isValid = isValidObject(addressId, "Invalid address id (Null Long object)");
		
		if (isValid) {
			String invalidMsg = String.format("Invalid Address Id %s", isNewAddress? " (for new Address)": "");
			isValid = isValidId(addressId, isNewAddress, invalidMsg);			
		}
		
		return isValid;
	}
	
	private boolean isValidLine1(AddressBean bean)
	{
		return isValidString(bean.getLine1(), ParamLengths.Address.MIN_LINE1, ParamLengths.Address.MAX_LINE1, "Invalid Address Line1");
	}
		
	private boolean isValidCity(AddressBean bean)
	{
		return isValidString(bean.getCity(), ParamLengths.Address.MIN_CITY, ParamLengths.Address.MAX_CITY, "Invalid Address City");
	}	
	
	private boolean isValidState(AddressBean bean)
	{
		return isValidObject(bean.getState(), "Invalid Address State (Null)");
	}
	
	private boolean isValidZipcode(AddressBean bean)
	{
		return isValidString(bean.getZipcode(), ParamLengths.Address.MIN_ZIPCODE, ParamLengths.Address.MIN_ZIPCODE, "Invalid Address Zipcode");
	}
	
	private boolean isValidLine2(AddressBean bean)
	{
		boolean isValid = false;
		
		String line2 = bean.getLine2();

		if (line2 == null || line2.isEmpty()) {
			// null or empty string (zero length) are allowed for line2
			isValid = true;
		} else {
			isValid = isValidString(line2, ParamLengths.Address.MIN_LINE2, ParamLengths.Address.MAX_LINE2, "Invalid Address Line2");
		}
		
		return isValid;
	}
} 
