package edu.depaul.se491.validators;

import edu.depaul.se491.beans.UserBean;
import edu.depaul.se491.utils.ParamLengths;

/**
 * UserBean Validator
 * 
 * @author Malik
 */
public class UserValidator extends BeanValidator {

	/**
	 * validate UserBean
	 * @param bean
	 * @param isNewUser
	 * @return
	 */
	public boolean validate(UserBean bean, boolean isNewUser) {
		boolean isValid = isValidObject(bean);

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

	/**
	 * validate user id
	 * @param userId
	 * @param isNewUser
	 * @return
	 */
	public boolean validateId(Long userId, boolean isNewUser) {
		boolean isValid = isValidObject(userId);
		
		if (isValid)
			isValid = isValidId(userId, isNewUser);			
		
		return isValid;
	}
	
	/**
	 * validate user email
	 * @param email
	 * @return
	 */
	public boolean isValidEmail(String email) {
		return isValidString(email, ParamLengths.User.MIN_EMAIL, ParamLengths.User.MAX_EMAIL);
	}

	private boolean isValidFirstName(UserBean bean) {
		return isValidString(bean.getFirstName(), ParamLengths.User.MIN_F_NAME, ParamLengths.User.MAX_F_NAME);
	}
	
	private boolean isValidLastName(UserBean bean) {
		return isValidString(bean.getLastName(), ParamLengths.User.MIN_L_NAME, ParamLengths.User.MAX_L_NAME);
	}
		
	private boolean isValidPhone(UserBean bean) {
		return isValidString(bean.getPhone(), ParamLengths.User.MIN_PHONE, ParamLengths.User.MAX_PHONE);
	}
	
	private boolean isValidAddress(UserBean bean) {
		return isValidObject(bean.getAddress());
	}
}
