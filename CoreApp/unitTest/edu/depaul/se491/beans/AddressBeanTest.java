package edu.depaul.se491.beans;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import edu.depaul.se491.enums.AddressState;

public class AddressBeanTest {
	AddressBean bean;
	
	@Before
	public void setUp() throws Exception {
		bean = new AddressBean();
	}

	@Test
	public void testAddressBean() {
		assertNotNull(bean);
		assertNull(bean.getLine1());
		assertNull(bean.getLine2());
		assertNull(bean.getZipcode());
		assertNull(bean.getCity());
		assertEquals(0, bean.getId());
		assertNull(bean.getState());
	}

	@Test
	public void testAddressBeanLongStringStringStringAddressStateString() {
		String line1 = "line1";
		String line2 = "line2";
		String zipcode = "zipcode";
		String city = "chicago";
		AddressState state = AddressState.IL;
		long id = 1;
		
		bean = new AddressBean(id,line1, line2, city, state, zipcode); 
		assertNotNull(bean);
		assertNotNull(bean.getLine1());
		assertNotNull(bean.getLine2());
		assertNotNull(bean.getZipcode());
		assertNotNull(bean.getCity());
		assertEquals(id, bean.getId());
		assertNotNull(bean.getState());
		
	}

	@Test
	public void testGetId() {
		assertEquals(0, bean.getId());
		
		long id = 1;
		bean.setId(id);
		assertEquals(id, bean.getId());
			
	}

	@Test
	public void testSetId() {
		long id = 1;
		bean.setId(id);
		assertEquals(id, bean.getId());
		
		id = 0;
		bean.setId(id);
		assertEquals(id, bean.getId());
		
		id = -1;
		bean.setId(id);
		assertEquals(id, bean.getId());
	}

	@Test
	public void testGetLine1() {
		assertNull(bean.getLine1());
		
		String line1 = "line1";
		bean.setLine1(line1);
		assertNotNull(bean.getLine1());
		
	}

	@Test
	public void testSetLine1() {
		String line1 = "line1";
		bean.setLine1(line1);
		assertNotNull(bean.getLine1());
		
		line1 = "";
		bean.setLine1(line1);
		assertTrue(bean.getLine1().isEmpty());
		
		line1 = null;
		bean.setLine1(line1);
		assertNull(bean.getLine1());
	}

	@Test
	public void testGetLine2() {
		assertNull(bean.getLine2());
		
		String line2 = "line2";
		bean.setLine2(line2);
		assertNotNull(bean.getLine2());
	}

	@Test
	public void testSetLine2() {
		String line2 = "line2";
		bean.setLine2(line2);
		assertNotNull(bean.getLine2());
		
		line2 = "";
		bean.setLine2(line2);
		assertTrue(bean.getLine2().isEmpty());
		
		line2 = null;
		bean.setLine2(line2);
		assertNull(bean.getLine2());
	}

	@Test
	public void testGetCity() {
		assertNull(bean.getCity());
		
		String city = "city";
		bean.setCity(city);
		assertNotNull(bean.getCity());
	}

	@Test
	public void testSetCity() {
		String city = "city";
		bean.setCity(city);
		assertNotNull(bean.getCity());
		
		city = "";
		bean.setCity(city);
		assertTrue(bean.getCity().isEmpty());
		
		city = null;
		bean.setCity(city);
		assertNull(bean.getCity());
	}

	@Test
	public void testGetState() {
		assertNull(bean.getState());
		
		AddressState state = AddressState.AR;
		bean.setState(state);
		assertNotNull(bean.getState());		
		
	}

	@Test
	public void testSetState() {
		assertNull(bean.getState());
		
		AddressState state = AddressState.AR;
		bean.setState(state);
		assertNotNull(bean.getState());
		bean.setState(AddressState.IL);
		assertNotNull(bean.getState());
		bean.setState(null);
		assertNull(bean.getState());
		
	}

	@Test
	public void testGetZipcode() {
		assertNull(bean.getZipcode());
		
		String zipcode = "zipcode";
		bean.setZipcode(zipcode);
		assertNotNull(bean.getZipcode());
	}

	@Test
	public void testSetZipcode() {
		String zipcode = "zipcode";
		bean.setZipcode(zipcode);
		assertNotNull(bean.getZipcode());
		
		zipcode = "";
		bean.setZipcode(zipcode);
		assertTrue(bean.getZipcode().isEmpty());
		
		zipcode = null;
		bean.setZipcode(zipcode);
		assertNull(bean.getZipcode());
	}

	@Test
	public void testEqualsObject() {
		AddressBean bean2  = new AddressBean();
		assertTrue(bean.equals(bean2));
		
		String line1 = "line1";
		String line2 = "line2";
		String zipcode = "zipcode";
		String city = "chicago";
		AddressState state = AddressState.IL;
		long id = 1;
		
		bean = new AddressBean(id,line1, line2, city, state, zipcode);
		assertFalse(bean.equals(bean2));
		bean2 = new AddressBean(id,line1, line2, city, state, zipcode);
		assertTrue(bean.equals(bean2));
		
	}

}
