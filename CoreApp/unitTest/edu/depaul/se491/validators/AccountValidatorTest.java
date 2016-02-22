package edu.depaul.se491.validators;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import edu.depaul.se491.beans.AccountBean;
import edu.depaul.se491.beans.CredentialsBean;
import edu.depaul.se491.beans.UserBean;
import edu.depaul.se491.enums.AccountRole;

public class AccountValidatorTest {
	private AccountValidator validator;

	@Before
	public void setUp() throws Exception {
		validator = new AccountValidator();
	}

	@Test
	public void testValidate() {
		// valid bean
		AccountBean account = new AccountBean(new CredentialsBean(), new UserBean(), AccountRole.EMPLOYEE);
		assertTrue(validator.validate(account));
		
		// invalid
		assertFalse(validator.validate(new AccountBean()));
		assertFalse(validator.validate(new AccountBean(null, new UserBean(), AccountRole.ADMIN)));
		assertFalse(validator.validate(new AccountBean(new CredentialsBean(), null, AccountRole.MANAGER)));
		assertFalse(validator.validate(new AccountBean(new CredentialsBean(), new UserBean(), null)));
		
	}

}
