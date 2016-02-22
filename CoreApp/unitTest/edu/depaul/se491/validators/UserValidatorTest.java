package edu.depaul.se491.validators;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import edu.depaul.se491.beans.AddressBean;
import edu.depaul.se491.beans.UserBean;
import edu.depaul.se491.utils.ParamLengths;

public class UserValidatorTest {
	private UserValidator validator;
	@Before
	public void setUp() throws Exception {
		validator = new UserValidator();
	}

	@Test
	public void testValidate() {
		UserBean bean = new UserBean();
		AddressBean abean = new AddressBean();
		
		// null
		assertFalse(validator.validate(null, true));
		
		// not null
		assertFalse(validator.validate(bean, true));
		
		bean.setEmail("email");
		bean.setFirstName("firstName");
		bean.setLastName("lastName");
		bean.setPhone("01234567890");
		bean.setAddress(abean);
		
		//valid bean
		assertTrue(validator.validate(bean, true));
		assertFalse(validator.validate(bean, false));
		
		//address == null
		bean.setEmail("email");
		bean.setFirstName("firstName");
		bean.setLastName("lastName");
		bean.setPhone("0123456789");
		bean.setAddress(null);
		assertFalse(validator.validate(bean, true));
		assertFalse(validator.validate(bean, false));
		

		
		//email < MIN_EMAIL
		bean.setEmail("emai");	
		assertFalse(validator.validate(bean, true));
		assertFalse(validator.validate(bean, false));
		
		//email > MAX_EMAIL
		String moreThanMaxLen = getLongString(ParamLengths.User.MAX_EMAIL + 1);
		bean.setEmail(moreThanMaxLen);
		assertFalse(validator.validate(bean, true));
		assertFalse(validator.validate(bean, false));
		
		//firstName < MIN_F_NAME
		bean.setEmail("email");
		bean.setFirstName("");
		bean.setLastName("lastName");
		bean.setPhone("01234567890");
		bean.setAddress(abean);
		assertFalse(validator.validate(bean, true));
		assertFalse(validator.validate(bean, false));
		
		//firstName > MAX_F_NAME
		moreThanMaxLen = getLongString(ParamLengths.User.MAX_F_NAME + 1);
		bean.setEmail("email");
		bean.setFirstName(moreThanMaxLen);
		bean.setLastName("lastName");
		bean.setPhone("01234567890");
		bean.setAddress(abean);
		assertFalse(validator.validate(bean, true));
		assertFalse(validator.validate(bean, false));
		
		//lastName < MIN_L_NAME
		bean.setEmail("email");
		bean.setFirstName("firstName");
		bean.setLastName("");
		bean.setPhone("01234567890");
		bean.setAddress(abean);
		assertFalse(validator.validate(bean, true));
		assertFalse(validator.validate(bean, false));
		
		//lastName > MAX_L_NAME
		moreThanMaxLen = getLongString(ParamLengths.User.MAX_L_NAME + 1);
		bean.setEmail("email");
		bean.setFirstName("firstName");
		bean.setLastName(moreThanMaxLen);
		bean.setPhone("01234567890");
		bean.setAddress(abean);
		assertFalse(validator.validate(bean, true));
		assertFalse(validator.validate(bean, false));
		
		//phone < MIN_PHONE
		bean.setEmail("email");
		bean.setFirstName("firstName");
		bean.setLastName("lastName");
		bean.setPhone("012345678");
		bean.setAddress(abean);
		assertFalse(validator.validate(bean, true));
		assertFalse(validator.validate(bean, false));
		
		//phone > MAX_PHONE
		bean.setEmail("email");
		bean.setFirstName("firstName");
		bean.setLastName("lastName");
		bean.setPhone("0123456789012345");
		bean.setAddress(abean);
		assertFalse(validator.validate(bean, true));
		assertFalse(validator.validate(bean, false));
		
		//user is not new
		bean.setId(1L);
		
		//Valid existing user
		bean.setEmail("email");
		bean.setFirstName("firstName");
		bean.setLastName("lastName");
		bean.setPhone("0123456789");
		bean.setAddress(abean);		
		assertFalse(validator.validate(bean, true));
		assertTrue(validator.validate(bean, false));
		
		//address == null
		bean.setEmail("email");
		bean.setFirstName("firstName");
		bean.setLastName("lastName");
		bean.setPhone("0123456789");
		bean.setAddress(null);
		assertFalse(validator.validate(bean, true));
		assertFalse(validator.validate(bean, false));
		
		
		//email < MIN_EMAIL
		bean.setEmail("emai");	
		assertFalse(validator.validate(bean, true));
		assertFalse(validator.validate(bean, false));
		
		//email > MAX_EMAIL
		moreThanMaxLen = getLongString(ParamLengths.User.MAX_EMAIL + 1);
		bean.setEmail(moreThanMaxLen);
		assertFalse(validator.validate(bean, true));
		assertFalse(validator.validate(bean, false));
		
		//firstName < MIN_F_NAME
		bean.setEmail("email");
		bean.setFirstName("");
		bean.setLastName("lastName");
		bean.setPhone("01234567890");
		bean.setAddress(abean);
		assertFalse(validator.validate(bean, true));
		assertFalse(validator.validate(bean, false));
		
		//firstName > MAX_F_NAME
		moreThanMaxLen = getLongString(ParamLengths.User.MAX_F_NAME + 1);
		bean.setEmail("email");
		bean.setFirstName(moreThanMaxLen);
		bean.setLastName("lastName");
		bean.setPhone("01234567890");
		bean.setAddress(abean);
		assertFalse(validator.validate(bean, true));
		assertFalse(validator.validate(bean, false));
		
		//lastName < MIN_L_NAME
		bean.setEmail("email");
		bean.setFirstName("firstName");
		bean.setLastName("");
		bean.setPhone("01234567890");
		bean.setAddress(abean);
		assertFalse(validator.validate(bean, true));
		assertFalse(validator.validate(bean, false));
		
		//lastName > MAX_L_NAME
		moreThanMaxLen = getLongString(ParamLengths.User.MAX_L_NAME + 1);
		bean.setEmail("email");
		bean.setFirstName("firstName");
		bean.setLastName(moreThanMaxLen);
		bean.setPhone("01234567890");
		bean.setAddress(abean);
		assertFalse(validator.validate(bean, true));
		assertFalse(validator.validate(bean, false));
		
		//phone < MIN_PHONE
		bean.setEmail("email");
		bean.setFirstName("firstName");
		bean.setLastName("lastName");
		bean.setPhone("012345678");
		bean.setAddress(abean);
		assertFalse(validator.validate(bean, true));
		assertFalse(validator.validate(bean, false));
		
		//phone > MAX_PHONE
		bean.setEmail("email");
		bean.setFirstName("firstName");
		bean.setLastName("lastName");
		bean.setPhone("0123456789012345");
		bean.setAddress(abean);
		assertFalse(validator.validate(bean, true));
		assertFalse(validator.validate(bean, false));
		
	}

	@Test
	public void testValidateId() {
		boolean isNewUser;
		
		// null
		Long id = null;
		isNewUser = false;
		assertFalse(validator.validateId(id, isNewUser));
		
		isNewUser = true;
		assertFalse(validator.validateId(id, isNewUser));
		
		// zero (the L to make 0 long instead of int)
		id = 0L; 
		isNewUser = true;
		assertTrue(validator.validateId(id, isNewUser));
		
		isNewUser = false;
		assertFalse(validator.validateId(id, isNewUser));
		
		// > 0
		id = Long.MAX_VALUE;
		isNewUser = false;
		assertTrue(validator.validateId(id, isNewUser));
		
		isNewUser = true;
		assertFalse(validator.validateId(id, isNewUser));
		
		// < 0
		id = Long.MIN_VALUE;
		isNewUser = true;
		assertFalse(validator.validateId(id, isNewUser));
		
		isNewUser = false;
		assertFalse(validator.validateId(id, isNewUser));
		
	}


	
	private String getLongString(int maxLength) {
		StringBuilder sb = new StringBuilder();
		for (int i=0; i < maxLength; i++)
			sb.append("x");
		
		return sb.toString();
	}
}
