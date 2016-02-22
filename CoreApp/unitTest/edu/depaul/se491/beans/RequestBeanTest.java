/**
 * 
 */
package edu.depaul.se491.beans;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

/**
 * @author Malik
 *
 */
public class RequestBeanTest {
	private RequestBean<String> bean;

	@Before
	public void setUp() throws Exception {
		bean = new RequestBean<String>();
	}

	@Test
	public void testRequestBean() {
		assertNotNull(bean);
	}

	@Test
	public void testRequestBeanCredentialsBeanT() {
		bean = null;
		assertNull(bean);
		
		bean = new RequestBean<String>(new CredentialsBean(), "extra");
		assertNotNull(bean);
		assertNotNull(bean.getCredentials());
		assertNotNull(bean.getExtra());
	}

	@Test
	public void testGetCredentials() {
		assertNull(bean.getCredentials());
		
		CredentialsBean credentials = new CredentialsBean("username", "password");
		bean.setCredentials(credentials);
		assertNotNull(bean.getCredentials());
		assertEquals(credentials.getUsername(), bean.getCredentials().getUsername());
		assertEquals(credentials.getPassword(), bean.getCredentials().getPassword());

	}

	@Test
	public void testSetCredentials() {
		assertNull(bean.getCredentials());
		
		CredentialsBean credentials = new CredentialsBean("username", "password");
		bean.setCredentials(credentials);
		assertNotNull(bean.getCredentials());
		assertEquals(credentials.getUsername(), bean.getCredentials().getUsername());
		assertEquals(credentials.getPassword(), bean.getCredentials().getPassword());
	}

	@Test
	public void testGetExtra() {
		assertNull(bean.getExtra());
		
		String extra = "extra";
		bean.setExtra(extra);
		assertNotNull(bean.getExtra());
		assertEquals(extra, bean.getExtra());
	}

	@Test
	public void testSetExtra() {
		assertNull(bean.getExtra());
		
		String extra = "extra";
		bean.setExtra(extra);
		assertNotNull(bean.getExtra());
		assertEquals(extra, bean.getExtra());

	}

}
