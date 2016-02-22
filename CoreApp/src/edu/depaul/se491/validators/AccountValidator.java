/**
 * Validator class for Account Bean
 */
package edu.depaul.se491.validators;

import edu.depaul.se491.beans.AccountBean;

/**
 * @author Malik
 *
 */
public class AccountValidator extends BeanValidator {

	public boolean validate(AccountBean bean) {
		boolean isValid = isValidObject(bean);

		if(isValid){
			isValid  = isValidCredentials(bean);
			isValid &= isValidRole(bean);
			isValid &= isValidUser(bean);
		}
		
		return isValid;
	}
	
	private boolean isValidCredentials(AccountBean bean)
	{
		return isValidObject(bean.getCredentials());
	}
	
	private boolean isValidUser(AccountBean bean)
	{
		return isValidObject(bean.getUser());
	}
	
	private boolean isValidRole(AccountBean bean)
	{
		return isValidObject(bean.getRole());
	}
}
