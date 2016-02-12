package edu.depaul.se491.beans;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import edu.depaul.se491.enums.AccountRole;

public class AccountBeanTest {
	private AccountBean account;
	
	@Before
	public void setUp() throws Exception {
		account = new AccountBean();
	}

	@Test
	public void testAccountBean() {
		assertNotNull(account);
		
		assertEquals(null, account.getCredentials());
		assertEquals(null, account.getRole());
		assertEquals(null, account.getUser());
	}

	@Test
	public void testAccountBeanCredentialsBeanUserBeanAccountRole() {
		
		CredentialsBean validCred = new CredentialsBean();
		UserBean validUser = new UserBean();
		AccountRole validRole = AccountRole.ADMIN;
		
		account.setCredentials(validCred);
		account.setUser(validUser);
		account.setRole(validRole);

		assertEquals(validCred, account.getCredentials());
		assertEquals(validUser, account.getUser());
		assertTrue(validRole == account.getRole());
	
		
		CredentialsBean invalidCred = null;
		UserBean invalidUser = null;
		AccountRole invalidRole = null;

		account.setCredentials(invalidCred);
		account.setUser(invalidUser);
		account.setRole(invalidRole);

		assertEquals(invalidCred, account.getCredentials());
		assertEquals(invalidUser, account.getUser());
		assertTrue(invalidRole == account.getRole());
	
	}

	@Test
	public void testGetCredentials() {

		CredentialsBean validCred1 = new CredentialsBean();
		CredentialsBean validCred2 = new CredentialsBean();
		
		account.setCredentials(validCred1);
		assertEquals(validCred1, account.getCredentials());
		assertNotEquals(validCred2, account.getCredentials());

		account.setCredentials(validCred2);
		assertEquals(validCred2, account.getCredentials());
		assertNotEquals(validCred1, account.getCredentials());

		CredentialsBean invalidCred = null;
		account.setCredentials(invalidCred);
		assertEquals(invalidCred, account.getCredentials());
	}

	@Test
	public void testSetCredentials() {
		
		CredentialsBean validCred1 = new CredentialsBean();
		CredentialsBean validCred2 = new CredentialsBean();
		
		account.setCredentials(validCred1);
		assertEquals(validCred1, account.getCredentials());
		assertNotEquals(validCred2, account.getCredentials());

		account.setCredentials(validCred2);
		assertEquals(validCred2, account.getCredentials());
		assertNotEquals(validCred1, account.getCredentials());

		CredentialsBean invalidCred = null;
		account.setCredentials(invalidCred);
		assertEquals(invalidCred, account.getCredentials());
	}

	@Test
	public void testGetUser() {
	
		UserBean validUser1 = new UserBean();
		UserBean validUser2 = new UserBean();

		account.setUser(validUser1);
		assertEquals(validUser1, account.getUser());
		assertNotEquals(validUser2, account.getUser());

		account.setUser(validUser2);
		assertEquals(validUser2, account.getUser());
		assertNotEquals(validUser1, account.getUser());
		
		UserBean invalidUser = null;

		account.setUser(invalidUser);
		assertEquals(invalidUser, account.getUser());
		
	}

	@Test
	public void testSetUser() {
		UserBean validUser1 = new UserBean();
		UserBean validUser2 = new UserBean();

		account.setUser(validUser1);
		assertEquals(validUser1, account.getUser());
		assertNotEquals(validUser2, account.getUser());

		account.setUser(validUser2);
		assertEquals(validUser2, account.getUser());
		assertNotEquals(validUser1, account.getUser());
		
		UserBean invalidUser = null;

		account.setUser(invalidUser);
		assertEquals(invalidUser, account.getUser());
	}

	@Test
	public void testGetRole() {
		
		AccountRole validRole1 = AccountRole.ADMIN;
		AccountRole validRole2 = AccountRole.CUSTOMER_APP;
		
		account.setRole(validRole1);
		assertTrue(validRole1 == account.getRole());
		assertFalse(validRole2 == account.getRole());

		account.setRole(validRole2);
		assertTrue(validRole2 == account.getRole());
		assertFalse(validRole1 == account.getRole());
		
		
		AccountRole invalidRole = null;

		account.setRole(invalidRole);
		assertTrue(invalidRole == account.getRole());
	}

	@Test
	public void testSetRole() {
		AccountRole validRole1 = AccountRole.ADMIN;
		AccountRole validRole2 = AccountRole.CUSTOMER_APP;
		
		account.setRole(validRole1);
		assertTrue(validRole1 == account.getRole());
		assertFalse(validRole2 == account.getRole());

		account.setRole(validRole2);
		assertTrue(validRole2 == account.getRole());
		assertFalse(validRole1 == account.getRole());
		
		
		AccountRole invalidRole = null;

		account.setRole(invalidRole);
		assertTrue(invalidRole == account.getRole());
	}

}
