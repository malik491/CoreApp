package edu.depaul.se491.beans;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class UserBeanTest {
	private UserBean  bean;
	
	@Before
	public void setUp() throws Exception {
		bean = new UserBean();
	}

	@Test
	public void testUserBean() {
		assertNotNull(bean);
		assertNull(bean.getFirstName());
		assertNull(bean.getLastName());
		assertNull(bean.getEmail());
		assertNull(bean.getAddress());
		assertNull(bean.getPhone());
		assertEquals(0,bean.getId());
		
	}

	@Test
	public void testUserBeanLongStringStringStringStringAddressBean() {
		String firstname = "first";
		String lastname = "last";
		String email = "email";
		String phone = "1234567890";
		AddressBean address = new AddressBean();
		long id  = 1;
		
		UserBean user2 = new UserBean(id, firstname, lastname, email, phone, address);
		assertNotNull(user2);
		assertNotNull(user2.getFirstName());
		assertEquals(firstname,user2.getFirstName());
		assertNotNull(user2.getLastName());
		assertEquals(lastname,user2.getLastName());
		assertNotNull(user2.getEmail());
		assertEquals(email,user2.getEmail());
		assertNotNull(user2.getPhone());
		assertEquals(phone,user2.getPhone());
		assertNotNull(user2.getAddress());
		assertEquals(address, user2.getAddress());
		assertEquals(id, user2.getId());
		
	}

	@Test
	public void testGetId() {
		assertEquals(0,bean.getId());
		
		long id = 1;
		bean.setId(id);
		assertEquals(id ,bean.getId());
	}

	@Test
	public void testSetId() {
	
		long id = 1;
		bean.setId(id);
		assertEquals(id ,bean.getId());
		
		id = -1;
		bean.setId(id);
		assertEquals(id ,bean.getId());
		
		id = 0;
		bean.setId(id);
		assertEquals(id ,bean.getId());
		
	}

	@Test
	public void testGetFirstName() {
		assertNull(bean.getFirstName());
		
		String firstname = "first";
		bean.setFirstName(firstname);
		assertEquals(firstname, bean.getFirstName());
	}

	@Test
	public void testSetFirstName() {
		
		String firstname = "first";
		bean.setFirstName(firstname);
		assertEquals(firstname, bean.getFirstName());
		
		firstname = "";
		bean.setFirstName(firstname);
		assertEquals(firstname, bean.getFirstName());
		
		bean.setFirstName(null);
		assertNull(bean.getFirstName());
	}

	@Test
	public void testGetLastName() {
		assertNull(bean.getLastName());
		
		String lastname = "last";
		bean.setLastName(lastname);
		assertEquals(lastname, bean.getLastName());
	}

	@Test
	public void testSetLastName() {
		
		String lastname = "last";
		bean.setLastName(lastname);
		assertEquals(lastname, bean.getLastName());
		
		lastname = "";
		bean.setLastName(lastname);
		assertEquals(lastname, bean.getLastName());
		
		bean.setLastName(null);
		assertNull(bean.getLastName());
	}

	@Test
	public void testGetEmail() {
		assertNull(bean.getEmail());
		
		String email = "email";
		bean.setEmail(email);
		assertEquals(email, bean.getEmail());
		
	}

	@Test
	public void testSetEmail() {
		
		String email = "email";
		bean.setEmail(email);
		assertEquals(email, bean.getEmail());
		
		email = "";
		bean.setEmail(email);
		assertTrue(bean.getEmail().isEmpty());
		
		email = null;
		bean.setEmail(email);
		assertNull(bean.getEmail());
	}

	@Test
	public void testGetPhone() {
		assertNull(bean.getPhone());
		
		String phone = "phone";
		bean.setPhone(phone);
		assertEquals(phone, bean.getPhone());

	}

	@Test
	public void testSetPhone() {
	
		String phone = "phone";
		bean.setPhone(phone);
		assertEquals(phone, bean.getPhone());
		
		phone = "";
		bean.setPhone(phone);
		assertTrue(bean.getPhone().isEmpty());
		
		phone = null;
		bean.setPhone(phone);
		assertNull(bean.getPhone());
	}

	@Test
	public void testGetAddress() {
		assertNull(bean.getAddress());
		
		AddressBean address = new AddressBean();
		bean.setAddress(address);
		assertNotNull(bean.getAddress());
	}

	@Test
	public void testSetAddress() {
		AddressBean address = new AddressBean();
		bean.setAddress(address);
		assertNotNull(bean.getAddress());
		
		address = null;
		bean.setAddress(address);
		assertNull(bean.getAddress());
	}

}
