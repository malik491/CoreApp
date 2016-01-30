package edu.depaul.se491.validators;

import edu.depaul.se491.beans.CredentialsBean;
import edu.depaul.se491.utils.ParamLengths;

public class CredentialsValidator extends BeanValidator {

	
	public boolean validate(CredentialsBean bean) {
		boolean isValid = isValidObject(bean, "Invalid CredentialsBean (Null)");
	
		if(isValid){
			isValid  = isValidUsername(bean.getUsername());
			isValid &= isValidPassword(bean);
		}
		
		return isValid;
	}
	
	public boolean isValidUsername(String username)
	{
		return isValidString(username, ParamLengths.Credentials.MIN_USERNAME, ParamLengths.Credentials.MAX_USERNAME, "Invalid Credentials username");
	}
	
	private boolean isValidPassword(CredentialsBean bean)
	{
		return isValidString(bean.getPassword(), ParamLengths.Credentials.MIN_PASSWORD, ParamLengths.Credentials.MAX_PASSWORD, "Invalid Credentials password");
	}
}
