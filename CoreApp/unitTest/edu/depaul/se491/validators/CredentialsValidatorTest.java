package edu.depaul.se491.validators;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import edu.depaul.se491.beans.CredentialsBean;
import edu.depaul.se491.utils.ParamLengths;

public class CredentialsValidatorTest {
	private CredentialsValidator validator;

	@Before
	public void setUp() throws Exception {
		validator = new CredentialsValidator();
	}

	@Test
	public void testValidate() {
		// null
		assertFalse(validator.validate(null));
		
		CredentialsBean bean = new CredentialsBean();
		//not null
		assertFalse(validator.validate(bean));
		
		bean.setUsername("username");
		bean.setPassword("password");
		
		//Valid bean
		assertTrue(validator.validate(bean));
		
		
		//username < MIN_USERNAME
		bean.setUsername("us");
		bean.setPassword("password");
		assertFalse(validator.validate(bean));
		
		//username > MAX_USERNAME
		String moreThanMaxLen = getLongString(ParamLengths.Credentials.MAX_USERNAME + 1);
		bean.setUsername(moreThanMaxLen);
		bean.setPassword("password");
		assertFalse(validator.validate(bean));
		
		//password < MIN_PASSWORD
		bean.setUsername("username");
		bean.setPassword("pa");
		assertFalse(validator.validate(bean));
		
		//password > MAX_PASSWORD
		moreThanMaxLen = getLongString(ParamLengths.Credentials.MAX_PASSWORD + 1);
		bean.setUsername("username");
		bean.setPassword(moreThanMaxLen);
		assertFalse(validator.validate(bean));
	
	}

	@Test
	public void testIsValidUsername() {
		
		//valid username
		assertTrue(validator.isValidUsername("username"));
		
		//username < MIN_USERNAME
		assertFalse(validator.isValidUsername("us"));
		
		//username > MAX_USERNAME
		String moreThanMaxLen = getLongString(ParamLengths.Credentials.MAX_USERNAME + 1);
		assertFalse(validator.isValidUsername(moreThanMaxLen));
		
		
	}
	
	private String getLongString(int maxLength) {
		StringBuilder sb = new StringBuilder();
		for (int i=0; i < maxLength; i++)
			sb.append("x");
		
		return sb.toString();
	}

}
