package edu.depaul.se491.beans;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class MenuItemBeanTest {
	private MenuItemBean bean;
	
	@Before
	public void setUp() throws Exception {
		bean = new MenuItemBean();
	}

	@Test
	public void testMenuItemBean() {
		assertNotNull(bean);
	}

	@Test
	public void testGetId() {
		assertEquals(0, bean.getId());
		
		bean.setId(1);
		assertEquals(1, bean.getId());
	}

	@Test
	public void testSetId() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetName() {
		fail("Not yet implemented");
	}

	@Test
	public void testSetName() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetDescription() {
		fail("Not yet implemented");
	}

	@Test
	public void testSetDescription() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetPrice() {
		fail("Not yet implemented");
	}

	@Test
	public void testSetPrice() {
	}

	@Test
	public void testGetItemCategory() {
		fail("Not yet implemented");
	}

	@Test
	public void testSetItemCategory() {
		fail("Not yet implemented");
	}

}
