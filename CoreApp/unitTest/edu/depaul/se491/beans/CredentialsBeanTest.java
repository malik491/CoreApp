package edu.depaul.se491.beans;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class CredentialsBeanTest {
	private CredentialsBean credentials;
	
	@Before
	public void setUp() throws Exception {
		credentials = new CredentialsBean();
	}

	@Test
	public void testCredentialsBean() {
		assertNotNull(credentials);
		assertEquals(null, credentials.getUsername());
		assertEquals(null, credentials.getPassword());
		
		String userName = "JDoe";
		String password = "1234";
		
		credentials.setUsername(userName);
		credentials.setPassword(password);
		assertEquals(userName, credentials.getUsername());
		assertEquals(password, credentials.getPassword());
		
		userName = "";
		password = "";
		
		credentials.setUsername(userName);
		credentials.setPassword(password);
		assertEquals(userName, credentials.getUsername());
		assertEquals(password, credentials.getPassword());

		userName = null;
		password = null;
		
		credentials.setUsername(userName);
		credentials.setPassword(password);
		assertNull(credentials.getUsername());
		assertNull(credentials.getPassword());
	}

	@Test
	public void testCredentialsBeanStringString() {
		assertNotNull(credentials);
		assertEquals(null, credentials.getUsername());
		assertEquals(null, credentials.getPassword());
		
		String userName = "JDoe";
		String password = "1234";
		
		credentials.setUsername(userName);
		credentials.setPassword(password);
		assertEquals(userName, credentials.getUsername());
		assertEquals(password, credentials.getPassword());
		
		userName = "";
		password = "";
		
		credentials.setUsername(userName);
		credentials.setPassword(password);
		assertEquals(userName, credentials.getUsername());
		assertEquals(password, credentials.getPassword());

		userName = null;
		password = null;
		
		credentials.setUsername(userName);
		credentials.setPassword(password);
		assertNull(credentials.getUsername());
		assertNull(credentials.getPassword());
	}

	@Test
	public void testGetUsername() {
	assertNull(credentials.getUsername());
		
		String userName = "JDoe";
		
		credentials.setUsername(userName);
		assertEquals(userName, credentials.getUsername());
		
		userName = "";
				
		credentials.setUsername(userName);
		assertEquals(userName, credentials.getUsername());
		
		userName = null;
		
		credentials.setUsername(userName);
		assertNull(credentials.getUsername());
	}

	@Test
	public void testSetUsername() {
		String userName = "JDoe";
		
		credentials.setUsername(userName);
		assertEquals(userName, credentials.getUsername());
		
		userName = "";
				
		credentials.setUsername(userName);
		assertEquals(userName, credentials.getUsername());
		
		userName = null;
		
		credentials.setUsername(userName);
		assertNull(credentials.getUsername());
	}

	@Test
	public void testGetPassword() {
	assertEquals(null, credentials.getPassword());
		
		String password = "1234";
		credentials.setPassword(password);
		assertEquals(password, credentials.getPassword());

		password = "";
		credentials.setPassword(password);
		assertEquals(password, credentials.getPassword());

		password = null;
		credentials.setPassword(password);
		assertNull(credentials.getPassword());
	}

	@Test
	public void testSetPassword() {
		String password = "1234";
		credentials.setPassword(password);
		assertEquals(password, credentials.getPassword());

		password = "";
		credentials.setPassword(password);
		assertEquals(password, credentials.getPassword());

		password = null;
		credentials.setPassword(password);
		assertNull(credentials.getPassword());
	}

}
