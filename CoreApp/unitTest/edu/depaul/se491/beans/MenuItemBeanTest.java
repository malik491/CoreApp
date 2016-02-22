package edu.depaul.se491.beans;


import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import edu.depaul.se491.enums.MenuItemCategory;

public class MenuItemBeanTest {
	private MenuItemBean menuItem;
	
	@Before
	public void setUp() throws Exception {
		menuItem = new MenuItemBean();
	}

	@Test
	public void testMenuItemBean() {
		assertNotNull(menuItem);
		
		assertNotEquals(null, menuItem.getId());
		assertEquals(null, menuItem.getName());
		assertNull(menuItem.getDescription());	
		assertTrue(menuItem.getPrice() == 0);
		assertNull(menuItem.getItemCategory());
		
		long id = 1;
		String name = "soda";
		String description = "Decaf Soda";
		double price = 10;
		
		menuItem = new MenuItemBean(id, name, description, price, MenuItemCategory.MAIN);
		assertNotNull(menuItem);
		assertEquals(id, menuItem.getId());
		assertEquals(name, menuItem.getName());
		assertEquals(description, menuItem.getDescription());
		assertEquals(0, Double.compare(price, menuItem.getPrice()));
		assertEquals(MenuItemCategory.MAIN, menuItem.getItemCategory());

	}

	@Test
	public void testGetId() {

		long idValid1 = 1;
		long idValid2 = 2;
		
		menuItem.setId(idValid1);
		assertEquals(idValid1, menuItem.getId());
		assertNotEquals(idValid2, menuItem.getId());
		
		menuItem.setId(idValid2);
		assertEquals(idValid2, menuItem.getId());
		assertNotEquals(idValid1, menuItem.getId());
	}

	@Test
	public void testSetId() {
	
		long idValid1 = 1;
		long idValid2 = 2;
		long idinvalid3 = -2;
		
		menuItem.setId(idValid1);
		assertEquals(idValid1, menuItem.getId());
		assertNotEquals(idValid2, menuItem.getId());
		
		menuItem.setId(idValid2);
		assertEquals(idValid2, menuItem.getId());
		assertNotEquals(idValid1, menuItem.getId());
		
		menuItem.setId(idinvalid3);
		assertEquals(idinvalid3, menuItem.getId());

	}

	@Test
	public void testGetName() {
		assertEquals(null, menuItem.getName());

		String name1 = "soda";
		String name2 = "burger";
		
		menuItem.setName(name1);
		assertTrue(menuItem.getName().equals(name1));
		assertFalse(menuItem.getName().equals(name2));
		
		menuItem.setName(name2);
		assertTrue(menuItem.getName().equals(name2));
		assertFalse(menuItem.getName().equals(name1));
		
		menuItem.setName("");
		assertTrue(menuItem.getName().equals(""));

		menuItem.setName(null);
		assertEquals(null, menuItem.getName());
	}

	@Test
	public void testSetName() {
		assertEquals(null, menuItem.getName());

		String name1 = "soda";
		String name2 = "burger";
		
		menuItem.setName(name1);
		assertTrue(menuItem.getName().equals(name1));
		assertFalse(menuItem.getName().equals(name2));
		
		menuItem.setName(name2);
		assertTrue(menuItem.getName().equals(name2));
		assertFalse(menuItem.getName().equals(name1));

		menuItem.setName("");
		assertTrue(menuItem.getName().equals(""));

		menuItem.setName(null);
		assertEquals(null, menuItem.getName());
	}

	@Test
	public void testGetDescription() {
		assertNull(menuItem.getDescription());	
		
		String desc1 = "cold soda";
		String desc2 = "hot burger";
		
		menuItem.setDescription(desc1);
		assertTrue(menuItem.getDescription().equals(desc1));
		assertFalse(menuItem.getDescription().equals(desc2));
		
		menuItem.setDescription(desc2);
		assertTrue(menuItem.getDescription().equals(desc2));
		assertFalse(menuItem.getDescription().equals(desc1));

		menuItem.setDescription(null);
		assertEquals(null, menuItem.getDescription());		
	}

	@Test
	public void testSetDescription() {
		assertNull(menuItem.getDescription());	
		
		String desc1 = "cold soda";
		String desc2 = "hot burger";
		
		menuItem.setDescription(desc1);
		assertTrue(menuItem.getDescription().equals(desc1));
		assertFalse(menuItem.getDescription().equals(desc2));
		
		menuItem.setDescription(desc2);
		assertTrue(menuItem.getDescription().equals(desc2));
		assertFalse(menuItem.getDescription().equals(desc1));

		menuItem.setDescription(null);
		assertEquals(null, menuItem.getDescription());
	}

	@Test
	public void testGetPrice() {
		assertTrue(menuItem.getPrice() == 0);
		
		double  price1 = 10.99;
		double  price2 = 10.99998;
		
		menuItem.setPrice(price1);
		
		boolean equalPrice1 = Double.compare(price1, menuItem.getPrice()) == 0;
		assertTrue(equalPrice1);
		
		boolean equalPrice2 = Double.compare(price2, menuItem.getPrice()) == 0;
		assertFalse(equalPrice2);	
	}

	@Test
	public void testSetPrice() {
		assertTrue(menuItem.getPrice() == 0);
		
		double  price1 = 10.99;
		double  price2 = 10.99998;
		
		menuItem.setPrice(price1);
		
		boolean equalPrice1 = Double.compare(price1, menuItem.getPrice()) == 0;
		assertTrue(equalPrice1);
		
		boolean equalPrice2 = Double.compare(price2, menuItem.getPrice()) == 0;
		assertFalse(equalPrice2);
	}

	@Test
	public void testGetItemCategory() {
		
		assertTrue(menuItem.getItemCategory() == null);
		
		MenuItemCategory itCategory1 = MenuItemCategory.BEVERAGE;
		MenuItemCategory itCategory2 = MenuItemCategory.MAIN;
		MenuItemCategory itCategory3 = null;
		
		menuItem.setItemCategory(itCategory1);
		assertTrue(menuItem.getItemCategory().equals(itCategory1));
		assertFalse(menuItem.getItemCategory().equals(itCategory2));
		
		menuItem.setItemCategory(itCategory2);
		assertTrue(menuItem.getItemCategory().equals(itCategory2));
		assertFalse(menuItem.getItemCategory().equals(itCategory1));
		
		menuItem.setItemCategory(itCategory3);
		assertTrue(menuItem.getItemCategory() == null);
	}

	@Test
	public void testSetItemCategory() {
		assertTrue(menuItem.getItemCategory() == null);
		
		MenuItemCategory itCategory1 = MenuItemCategory.BEVERAGE;
		MenuItemCategory itCategory2 = MenuItemCategory.MAIN;
		MenuItemCategory itCategory3 = null;
		
		menuItem.setItemCategory(itCategory1);
		assertTrue(menuItem.getItemCategory().equals(itCategory1));
		assertFalse(menuItem.getItemCategory().equals(itCategory2));
		
		menuItem.setItemCategory(itCategory2);
		assertTrue(menuItem.getItemCategory().equals(itCategory2));
		assertFalse(menuItem.getItemCategory().equals(itCategory1));
		
		menuItem.setItemCategory(itCategory3);
		assertTrue(menuItem.getItemCategory() == null);
	}

}
