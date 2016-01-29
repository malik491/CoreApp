/**
 * Validator class for User bean
 */
package edu.depaul.se491.validators;

import edu.depaul.se491.beans.UserBean;
import edu.depaul.se491.utils.ParamLengths;

/**
 * @author Malik
 *
 */
public class UserValidator extends BeanValidator {

	public boolean validate(UserBean bean, boolean isNewUser) {
		boolean isValid = isValidObject(bean, "Invalid User (Null)");

		if(isValid){
			isValid  = validateId(bean.getId(), isNewUser);
			isValid &= isValidEmail(bean.getEmail());
			isValid &= isValidFirstName(bean);
			isValid &= isValidLastName(bean);
			isValid &= isValidPhone(bean);
			isValid &= isValidAddress(bean);
		}
		
		return isValid;
	}

	public boolean validateId(Long userId, boolean isNewUser) {
		boolean isValid = isValidObject(userId, "Invalid user id (Null Long object)");
		
		if (isValid) {
			String invalidMsg = String.format("Invalid User Id %s", isNewUser? " (for new User)": "");
			isValid = isValidId(userId, isNewUser, invalidMsg);			
		}
		
		return isValid;
	}
	
	public boolean isValidEmail(String email) {
		return isValidString(email, ParamLengths.User.MIN_EMAIL, ParamLengths.User.MAX_EMAIL, "Invalid User Email Name");
	}

	
	private boolean isValidFirstName(UserBean bean) {
		return isValidString(bean.getFirstName(), ParamLengths.User.MIN_F_NAME, ParamLengths.User.MAX_F_NAME, "Invalid User First Name");
	}
	
	private boolean isValidLastName(UserBean bean) {
		return isValidString(bean.getLastName(), ParamLengths.User.MIN_L_NAME, ParamLengths.User.MAX_L_NAME, "Invalid User Last Name");
	}
		
	private boolean isValidPhone(UserBean bean) {
		return isValidString(bean.getPhone(), ParamLengths.User.MIN_PHONE, ParamLengths.User.MAX_PHONE, "Invalid User Phone Name");
	}
	
	private boolean isValidAddress(UserBean bean) {
		return isValidObject(bean.getAddress(), "Invalid User Address (Null)");
	}
}
